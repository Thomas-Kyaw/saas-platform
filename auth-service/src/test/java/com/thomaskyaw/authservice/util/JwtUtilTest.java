package com.thomaskyaw.authservice.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;
    private final String testSecret = "dGVzdC1zZWNyZXQta2V5LWZvci1qd3QtdGVzdGluZy0xMjM0NTY3ODkwMTIzNDU2Nzg5MDEyMzQ1Njc4OQ=="; // Base64 encoded

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(testSecret);
    }

    @Test
    void generateToken_ShouldCreateValidToken() {
        String userId = "user-123";
        List<Map<String, String>> roles = List.of(
            Map.of("tenantId", "tenant-1", "role", "ADMIN")
        );

        String token = jwtUtil.generateToken(userId, roles);

        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.split("\\.").length == 3); // JWT has 3 parts separated by dots
    }

    @Test
    void generateToken_ShouldWorkWithEmptyRoles() {
        String userId = "user-456";
        List<Map<String, String>> roles = List.of();

        String token = jwtUtil.generateToken(userId, roles);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void getExpirationDateFromToken_ShouldReturnFutureDate() {
        String userId = "user-999";
        List<Map<String, String>> roles = List.of();
        String token = jwtUtil.generateToken(userId, roles);

        Date expirationDate = jwtUtil.getExpirationDateFromToken(token);

        assertNotNull(expirationDate);
        assertTrue(expirationDate.after(new Date()));
    }

    @Test
    void getExpirationDateFromToken_ShouldThrowException_ForInvalidToken() {
        assertThrows(Exception.class, () -> {
            jwtUtil.getExpirationDateFromToken("invalid.token.here");
        });
    }
}

