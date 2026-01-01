package com.thomaskyaw.usageservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ReportUsageRequest(
    @NotNull(message = "Metric name is required")
    String metricName,

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    BigDecimal quantity
) {
}
