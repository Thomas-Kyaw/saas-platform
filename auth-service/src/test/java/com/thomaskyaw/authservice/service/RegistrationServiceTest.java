package com.thomaskyaw.authservice.service;

import com.thomaskyaw.authservice.client.IdentityHttpClient;
import com.thomaskyaw.authservice.client.SubscriptionHttpClient;
import com.thomaskyaw.authservice.client.dto.PlanResponse;
import com.thomaskyaw.authservice.client.dto.TenantResponse;
import com.thomaskyaw.authservice.client.dto.UserResponse;
import com.thomaskyaw.authservice.dto.PlanTier;
import com.thomaskyaw.authservice.dto.RegisterRequest;
import com.thomaskyaw.authservice.dto.RegisterResponse;
import com.thomaskyaw.authservice.grpc.IdentityServiceGrpcClient;
import com.thomaskyaw.authservice.repository.CredentialRepository;
import com.thomaskyaw.authservice.util.JwtUtil;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceTest {

    @Mock
    private IdentityServiceGrpcClient identityGrpcClient;

    @Mock
    private IdentityHttpClient identityHttpClient;

    @Mock
    private SubscriptionHttpClient subscriptionHttpClient;

    @Mock
    private CredentialRepository credentialRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private RegistrationService registrationService;

    @Test
    void register_ShouldReturnAccepted_ForEnterpriseTier() {
        RegisterRequest request = new RegisterRequest(
                "enterprise@test.com",
                "password123",
                "Enterprise User",
                "Enterprise Co",
                PlanTier.ENTERPRISE,
                null  // No payment method needed for ENTERPRISE
        );

        RegisterResponse response = registrationService.register(request);

        assertEquals("ENTERPRISE", response.planTier());
        assertNull(response.token());
        assertNotNull(response.message());
        verifyNoInteractions(identityGrpcClient, identityHttpClient, subscriptionHttpClient, credentialRepository, passwordEncoder, jwtUtil, paymentService);
    }

    @Test
    void register_ShouldCreateTenantUserSubscription_ForFreeTier() {
        RegisterRequest request = new RegisterRequest(
                "free@test.com",
                "password123",
                "Free User",
                "Free Co",
                PlanTier.FREE,
                null  // No payment method needed for FREE
        );

        UUID tenantId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID planId = UUID.randomUUID();

        when(identityGrpcClient.getUserByEmail(request.email()))
                .thenThrow(Status.NOT_FOUND.asRuntimeException());
        when(identityHttpClient.createTenant(any()))
                .thenReturn(new TenantResponse(tenantId, "Free Co", "ACTIVE"));
        when(identityHttpClient.createUserForTenant(eq(tenantId), any()))
                .thenReturn(new UserResponse(userId, request.email(), request.displayName(), "ACTIVE"));
        when(subscriptionHttpClient.getPlanByTier(PlanTier.FREE))
                .thenReturn(new PlanResponse(planId, "Free Plan", "FREE", BigDecimal.ZERO, BigDecimal.ZERO, 1, 1000, 1, "[]"));
        when(passwordEncoder.encode(request.password())).thenReturn("hashed");
        when(jwtUtil.generateToken(eq(userId.toString()), any())).thenReturn("token");

        RegisterResponse response = registrationService.register(request);

        assertEquals("token", response.token());
        assertEquals(tenantId, response.tenantId());
        assertEquals(userId, response.userId());
        verify(subscriptionHttpClient).createSubscription(tenantId, planId, false);  // FREE tier - no trial
        verify(credentialRepository).save(any());
    }

    @Test
    void register_ShouldRejectDuplicateEmail() {
        RegisterRequest request = new RegisterRequest(
                "taken@test.com",
                "password123",
                "Taken User",
                "Taken Co",
                PlanTier.BASIC,
                "pm_test_card"  // Payment method provided
        );

        when(identityGrpcClient.getUserByEmail(request.email())).thenReturn(null);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> registrationService.register(request));
        assertEquals(409, exception.getStatusCode().value());
        verifyNoInteractions(identityHttpClient, subscriptionHttpClient, credentialRepository);
    }
}
