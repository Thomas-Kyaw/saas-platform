package com.thomaskyaw.authservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterRequest(
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email,

        @NotBlank(message = "Password is required")
        String password,

        @NotBlank(message = "Display name is required")
        String displayName,

        @NotBlank(message = "Company name is required")
        String companyName,

        @NotNull(message = "Plan tier is required")
        PlanTier planTier,

        String paymentMethodId  // Optional - required for BASIC/PRO, not for FREE/ENTERPRISE
) {
}
