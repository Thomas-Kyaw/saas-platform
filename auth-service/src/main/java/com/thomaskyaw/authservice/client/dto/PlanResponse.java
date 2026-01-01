package com.thomaskyaw.authservice.client.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record PlanResponse(
        UUID id,
        String name,
        String tier,
        BigDecimal monthlyPrice,
        BigDecimal annualPrice,
        Integer maxUsers,
        Integer maxApiCallsPerMonth,
        Integer maxStorageGb,
        String features
) {
}
