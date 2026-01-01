package com.thomaskyaw.authservice.service;

import com.thomaskyaw.authservice.client.IdentityHttpClient;
import com.thomaskyaw.authservice.client.SubscriptionHttpClient;
import com.thomaskyaw.authservice.client.dto.CreateTenantRequest;
import com.thomaskyaw.authservice.client.dto.CreateUserRequest;
import com.thomaskyaw.authservice.client.dto.PlanResponse;
import com.thomaskyaw.authservice.client.dto.TenantResponse;
import com.thomaskyaw.authservice.client.dto.UserResponse;
import com.thomaskyaw.authservice.dto.PlanTier;
import com.thomaskyaw.authservice.dto.RegisterRequest;
import com.thomaskyaw.authservice.dto.RegisterResponse;
import com.thomaskyaw.authservice.grpc.IdentityServiceGrpcClient;
import com.thomaskyaw.authservice.model.Credential;
import com.thomaskyaw.authservice.repository.CredentialRepository;
import com.thomaskyaw.authservice.util.JwtUtil;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
public class RegistrationService {

    private final IdentityServiceGrpcClient identityGrpcClient;
    private final IdentityHttpClient identityHttpClient;
    private final SubscriptionHttpClient subscriptionHttpClient;
    private final CredentialRepository credentialRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final PaymentService paymentService;

    public RegistrationService(IdentityServiceGrpcClient identityGrpcClient,
                               IdentityHttpClient identityHttpClient,
                               SubscriptionHttpClient subscriptionHttpClient,
                               CredentialRepository credentialRepository,
                               PasswordEncoder passwordEncoder,
                               JwtUtil jwtUtil,
                               PaymentService paymentService) {
        this.identityGrpcClient = identityGrpcClient;
        this.identityHttpClient = identityHttpClient;
        this.subscriptionHttpClient = subscriptionHttpClient;
        this.credentialRepository = credentialRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.paymentService = paymentService;
    }

    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        if (request.planTier() == PlanTier.ENTERPRISE) {
            return new RegisterResponse(
                    null,
                    null,
                    null,
                    request.planTier().name(),
                    "Thanks for your interest. Our sales team will contact you shortly."
            );
        }

        // Validate payment method for paid tiers
        paymentService.validatePaymentMethodForTier(request.paymentMethodId(), request.planTier().name());

        ensureEmailAvailable(request.email());

        TenantResponse tenant = identityHttpClient.createTenant(new CreateTenantRequest(request.companyName()));
        if (tenant == null || tenant.id() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Failed to create tenant");
        }

        // Create user WITHOUT password - auth-service owns credentials
        UserResponse user = identityHttpClient.createUserForTenant(
                tenant.id(),
                new CreateUserRequest(request.email(), request.displayName())
        );
        if (user == null || user.id() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Failed to create user");
        }

        // Store credentials in auth-service
        Credential credential = new Credential();
        credential.setUserId(user.id());
        credential.setPasswordHash(passwordEncoder.encode(request.password()));
        credentialRepository.save(credential);

        PlanResponse plan = subscriptionHttpClient.getPlanByTier(request.planTier());
        if (plan == null || plan.id() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Failed to resolve plan tier");
        }

        // Start trial for BASIC/PRO, immediate activation for FREE
        boolean startTrial = request.planTier() == PlanTier.BASIC || request.planTier() == PlanTier.PRO;
        subscriptionHttpClient.createSubscription(tenant.id(), plan.id(), startTrial);

        String token = jwtUtil.generateToken(
                user.id().toString(),
                List.of(Map.of("tenantId", tenant.id().toString(), "role", "owner"))
        );

        return new RegisterResponse(
                token,
                tenant.id(),
                user.id(),
                request.planTier().name(),
                "Registration complete"
        );
    }

    private void ensureEmailAvailable(String email) {
        try {
            identityGrpcClient.getUserByEmail(email);
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email is already registered");
        } catch (StatusRuntimeException ex) {
            if (Objects.equals(ex.getStatus().getCode(), Status.Code.NOT_FOUND)) {
                return;
            }
            throw ex;
        }
    }
}
