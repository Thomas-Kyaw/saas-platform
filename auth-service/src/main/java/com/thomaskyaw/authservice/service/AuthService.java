package com.thomaskyaw.authservice.service;

import com.thomaskyaw.authservice.mapper.AuthMapper;
import com.thomaskyaw.authservice.model.Credential;
import com.thomaskyaw.authservice.repository.CredentialRepository;
import com.thomaskyaw.authservice.util.JwtUtil;
import com.thomaskyaw.saas.identity.v1.GetUserByEmailRequest;
import com.thomaskyaw.saas.identity.v1.GetUserResponse;
import com.thomaskyaw.saas.identity.v1.IdentityServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class AuthService {

    private final CredentialRepository credentialRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final AuthMapper authMapper;
    private final com.thomaskyaw.authservice.grpc.IdentityServiceGrpcClient identityServiceGrpcClient;
    private final BlacklistService blacklistService;

    public AuthService(CredentialRepository credentialRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder, AuthMapper authMapper, com.thomaskyaw.authservice.grpc.IdentityServiceGrpcClient identityServiceGrpcClient, BlacklistService blacklistService) {
        this.credentialRepository = credentialRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.authMapper = authMapper;
        this.identityServiceGrpcClient = identityServiceGrpcClient;
        this.blacklistService = blacklistService;
    }

    public String login(String email, String password) {
        // 1. Get user by email from Identity Service
        GetUserResponse userResponse;
        try {
            userResponse = identityServiceGrpcClient.getUserByEmail(email);
        } catch (Exception e) {
            throw new BadCredentialsException("Invalid credentials");
        }

        if (!userResponse.getActive()) {
            throw new org.springframework.security.authentication.DisabledException("User is disabled");
        }

        // 2. Validate password
        UUID userId = UUID.fromString(userResponse.getUserId());
        Credential credential = credentialRepository.findById(userId)
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));

        if (!passwordEncoder.matches(password, credential.getPasswordHash())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        // 3. Generate Token
        List<Map<String, String>> tenantRoles = authMapper.toTenantRoleMaps(userResponse.getRolesList());

        return jwtUtil.generateToken(userResponse.getUserId(), tenantRoles);
    }

    public void logout(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        java.util.Date expirationDate = jwtUtil.getExpirationDateFromToken(token);
        long expirationMillis = expirationDate.getTime() - System.currentTimeMillis();
        if (expirationMillis > 0) {
            blacklistService.blacklistToken(token, expirationMillis);
        }
    }
}
