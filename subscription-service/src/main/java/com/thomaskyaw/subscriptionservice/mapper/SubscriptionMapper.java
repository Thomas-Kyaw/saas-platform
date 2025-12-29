package com.thomaskyaw.subscriptionservice.mapper;

import com.thomaskyaw.subscriptionservice.dto.SubscriptionResponse;
import com.thomaskyaw.subscriptionservice.model.TenantSubscription;

public class SubscriptionMapper {

    public static SubscriptionResponse toSubscriptionResponse(TenantSubscription subscription) {
        return new SubscriptionResponse(
            subscription.getId(),
            subscription.getTenantId(),
            PlanMapper.toPlanResponse(subscription.getPlan()),
            subscription.getStatus().name(),
            subscription.getStartDate(),
            subscription.getEndDate(),
            subscription.getAutoRenew()
        );
    }
}
