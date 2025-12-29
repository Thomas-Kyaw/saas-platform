package com.thomaskyaw.subscriptionservice.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UpgradeSubscriptionRequest(
    @NotNull(message = "New plan ID is required")
    UUID newPlanId
) {
}
