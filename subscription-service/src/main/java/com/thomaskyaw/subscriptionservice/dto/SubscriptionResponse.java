package com.thomaskyaw.subscriptionservice.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record SubscriptionResponse(
    UUID id,
    UUID tenantId,
    PlanResponse plan,
    String status,
    OffsetDateTime startDate,
    OffsetDateTime endDate,
    Boolean autoRenew
) {
}
