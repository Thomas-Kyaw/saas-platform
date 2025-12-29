package com.thomaskyaw.authservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BlacklistServiceTest {

    @Mock
    private StringRedisTemplate redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @InjectMocks
    private BlacklistService blacklistService;

    @Test
    void blacklistToken_ShouldStoreTokenInRedis_WithCorrectTTL() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        String token = "test-token";
        long ttlMillis = 3600000L; // milliseconds

        blacklistService.blacklistToken(token, ttlMillis);

        verify(redisTemplate).opsForValue();
        verify(valueOperations).set(
            "blacklist:" + token,
            "true",
            Duration.ofMillis(ttlMillis)
        );
    }

    @Test
    void isBlacklisted_ShouldReturnTrue_WhenTokenExists() {
        String token = "blacklisted-token";
        when(redisTemplate.hasKey("blacklist:" + token)).thenReturn(Boolean.TRUE);

        boolean result = blacklistService.isBlacklisted(token);

        assertTrue(result);
        verify(redisTemplate).hasKey("blacklist:" + token);
    }

    @Test
    void isBlacklisted_ShouldReturnFalse_WhenTokenDoesNotExist() {
        String token = "valid-token";
        when(redisTemplate.hasKey("blacklist:" + token)).thenReturn(Boolean.FALSE);

        boolean result = blacklistService.isBlacklisted(token);

        assertFalse(result);
        verify(redisTemplate).hasKey("blacklist:" + token);
    }

    @Test
    void isBlacklisted_ShouldReturnFalse_WhenRedisReturnsNull() {
        String token = "test-token";
        when(redisTemplate.hasKey("blacklist:" + token)).thenReturn(null);

        boolean result = blacklistService.isBlacklisted(token);

        assertFalse(result);
    }
}
