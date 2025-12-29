package com.thomaskyaw.authservice.service;

import com.thomaskyaw.authservice.grpc.IdentityServiceGrpcClient;
import com.thomaskyaw.authservice.mapper.AuthMapper;
import com.thomaskyaw.authservice.model.Credential;
import com.thomaskyaw.authservice.repository.CredentialRepository;
import com.thomaskyaw.authservice.util.JwtUtil;
import com.thomaskyaw.saas.identity.v1.GetUserResponse;
import com.thomaskyaw.saas.identity.v1.TenantRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private CredentialRepository credentialRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthMapper authMapper;

    @Mock
    private IdentityServiceGrpcClient identityServiceGrpcClient;

    @InjectMocks
    private AuthService authService;

    private final String email = "test@example.com";
    private final String password = "password";
    private final String userId = UUID.randomUUID().toString();
    private final String token = "generated-jwt-token";

    @BeforeEach
    void setUp() {
    }

    @Test
    void login_Success() {
        // Mock Identity Service response
        GetUserResponse userResponse = GetUserResponse.newBuilder()
                .setUserId(userId)
                .setEmail(email)
                .setActive(true)
                .addRoles(TenantRole.newBuilder().setTenantId("tenant1").setRole("ADMIN").build())
                .build();
        when(identityServiceGrpcClient.getUserByEmail(email)).thenReturn(userResponse);

        // Mock Credential Repository
        Credential credential = new Credential();
        credential.setUserId(UUID.fromString(userId));
        credential.setPasswordHash("hashedPassword");
        when(credentialRepository.findById(UUID.fromString(userId))).thenReturn(Optional.of(credential));

        // Mock Password Encoder
        when(passwordEncoder.matches(password, "hashedPassword")).thenReturn(true);

        // Mock AuthMapper
        List<Map<String, String>> mappedRoles = List.of(Map.of("tenantId", "tenant1", "role", "ADMIN"));
        when(authMapper.toTenantRoleMaps(any())).thenReturn(mappedRoles);

        // Mock JwtUtil
        when(jwtUtil.generateToken(anyString(), any())).thenReturn(token);

        // Execute
        String result = authService.login(email, password);

        // Verify
        assertEquals(token, result);
    }

    @Test
    void login_InvalidCredentials_UserNotFound() {
        when(identityServiceGrpcClient.getUserByEmail(email)).thenThrow(new RuntimeException("User not found"));

        assertThrows(BadCredentialsException.class, () -> authService.login(email, password));
    }

    @Test
    void login_InvalidCredentials_PasswordMismatch() {
        // Mock Identity Service
        GetUserResponse userResponse = GetUserResponse.newBuilder()
                .setUserId(userId)
                .setActive(true)
                .build();
        when(identityServiceGrpcClient.getUserByEmail(email)).thenReturn(userResponse);

        // Mock Credential
        Credential credential = new Credential();
        credential.setPasswordHash("hashedPassword");
        when(credentialRepository.findById(UUID.fromString(userId))).thenReturn(Optional.of(credential));

        // Mock Password Mismatch
        when(passwordEncoder.matches(password, "hashedPassword")).thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> authService.login(email, password));
    }

    @Test
    void login_InactiveUser() {
        // Mock Identity Service
        GetUserResponse userResponse = GetUserResponse.newBuilder()
                .setUserId(userId)
                .setEmail(email)
                .setActive(false)
                .build();
        when(identityServiceGrpcClient.getUserByEmail(email)).thenReturn(userResponse);

        assertThrows(org.springframework.security.authentication.DisabledException.class, () -> authService.login(email, password));
    }
}
