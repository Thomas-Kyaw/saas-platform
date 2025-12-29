package com.thomaskyaw.authservice.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class BlacklistService {

    private final StringRedisTemplate redisTemplate;

    public BlacklistService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void blacklistToken(String token, long expirationMillis) {
        // Prefix with 'blacklist:' to avoid collisions
        redisTemplate.opsForValue().set("blacklist:" + token, "true", Duration.ofMillis(expirationMillis));
    }

    public boolean isBlacklisted(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey("blacklist:" + token));
    }
}
