package com.thomaskyaw.identityservice.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateTenantRequest(
        @NotBlank(message = "Name is required")
        String name
) {
}
