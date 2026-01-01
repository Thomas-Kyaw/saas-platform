package com.thomaskyaw.authservice.client.dto;

import java.util.UUID;

public record CreateSubscriptionRequest(UUID planId, boolean startTrial) {
}
