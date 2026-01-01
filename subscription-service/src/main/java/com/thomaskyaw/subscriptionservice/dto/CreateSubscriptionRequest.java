package com.thomaskyaw.subscriptionservice.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateSubscriptionRequest(
    @NotNull(message = "Plan ID is required")
    UUID planId,

    boolean startTrial
) {
}
