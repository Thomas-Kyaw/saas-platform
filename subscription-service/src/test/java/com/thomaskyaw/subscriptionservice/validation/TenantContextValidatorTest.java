package com.thomaskyaw.subscriptionservice.validation;

import com.thomaskyaw.common.context.UserContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TenantContextValidatorTest {

    private final TenantContextValidator validator = new TenantContextValidator();

    @AfterEach
    void cleanup() {
        UserContext.clear();
    }

    @Test
    void requireTenantId_ShouldReturnTenantId_WhenPresent() {
        UUID expectedTenantId = UUID.randomUUID();
        UserContext.setTenantId(expectedTenantId.toString());

        UUID result = validator.requireTenantId();

        assertEquals(expectedTenantId, result);
    }

    @Test
    void requireTenantId_ShouldThrowException_WhenMissing() {
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            validator.requireTenantId();
        });

        assertEquals("X-Tenant-Id header is required for this operation", exception.getMessage());
    }

    @Test
    void validateTenantId_ShouldPass_WhenMatches() {
        UUID tenantId = UUID.randomUUID();
        UserContext.setTenantId(tenantId.toString());

        assertDoesNotThrow(() -> validator.validateTenantId(tenantId));
    }

    @Test
    void validateTenantId_ShouldThrow_WhenMismatch() {
        UUID contextTenantId = UUID.randomUUID();
        UUID requestTenantId = UUID.randomUUID();
        UserContext.setTenantId(contextTenantId.toString());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            validator.validateTenantId(requestTenantId);
        });

        assertTrue(exception.getMessage().contains("Tenant ID mismatch"));
    }
}
