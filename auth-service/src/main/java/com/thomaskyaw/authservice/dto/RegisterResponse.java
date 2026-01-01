package com.thomaskyaw.authservice.dto;

import java.util.UUID;

public record RegisterResponse(
        String token,
        UUID tenantId,
        UUID userId,
        String planTier,
        String message
) {
}
