package com.thomaskyaw.authservice.service;

import com.thomaskyaw.authservice.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private BlacklistService blacklistService;

    @InjectMocks
    private AuthService authService;

    @Test
    void logout_ShouldBlacklistToken_WithCorrectTTL() {
        String token = "test-token";
        long currentTime = System.currentTimeMillis();
        long expirationTime = currentTime + 3600000L; // 1 hour in future
        Date expirationDate = new Date(expirationTime);

        when(jwtUtil.getExpirationDateFromToken(token)).thenReturn(expirationDate);

        authService.logout(token);

        // Service uses milliseconds, not seconds
        verify(blacklistService).blacklistToken(eq(token), longThat(ttl -> ttl >= 3599000 && ttl <= 3600000));
    }

    @Test
    void logout_ShouldNotBlacklist_WhenTokenExpired() {
        String token = "expired-token";
        Date pastDate = new Date(System.currentTimeMillis() - 1000); // Already expired

        when(jwtUtil.getExpirationDateFromToken(token)).thenReturn(pastDate);

        authService.logout(token);

        // Should NOT blacklist if already expired
        verify(blacklistService, never()).blacklistToken(anyString(), anyLong());
    }

    @Test
    void logout_ShouldStripBearerPrefix() {
        String tokenWithPrefix = "Bearer test-token";
        String token = "test-token";
        Date expirationDate = new Date(System.currentTimeMillis() + 3600000L);

        when(jwtUtil.getExpirationDateFromToken(token)).thenReturn(expirationDate);

        authService.logout(tokenWithPrefix);

        verify(jwtUtil).getExpirationDateFromToken(token);
    }
}

