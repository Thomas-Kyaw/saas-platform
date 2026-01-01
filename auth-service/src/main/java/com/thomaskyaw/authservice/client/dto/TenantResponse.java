package com.thomaskyaw.authservice.client.dto;

import java.util.UUID;

public record TenantResponse(
        UUID id,
        String name,
        String status
) {
}
