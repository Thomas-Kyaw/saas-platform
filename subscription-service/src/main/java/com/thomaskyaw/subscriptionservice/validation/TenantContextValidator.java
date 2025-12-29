package com.thomaskyaw.subscriptionservice.validation;

import com.thomaskyaw.common.context.UserContext;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TenantContextValidator {

    /**
     * Validates that a tenant ID is present in the UserContext.
     * @return The tenant ID from UserContext
     * @throws IllegalStateException if tenant ID is missing
     */
    public UUID requireTenantId() {
        String tenantIdStr = UserContext.getTenantId();
        if (tenantIdStr == null || tenantIdStr.isBlank()) {
            throw new IllegalStateException("X-Tenant-Id header is required for this operation");
        }
        try {
            return UUID.fromString(tenantIdStr);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("Invalid tenant ID format: " + tenantIdStr);
        }
    }

    /**
     * Validates that the provided tenant ID matches the one in UserContext.
     * @param tenantId The tenant ID to validate
     * @throws IllegalArgumentException if tenant ID doesn't match context
     */
    public void validateTenantId(UUID tenantId) {
        UUID contextTenantId = requireTenantId();
        if (!contextTenantId.equals(tenantId)) {
            throw new IllegalArgumentException(
                "Tenant ID mismatch: context has " + contextTenantId + ", but request has " + tenantId
            );
        }
    }
}
