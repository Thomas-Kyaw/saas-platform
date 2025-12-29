package com.thomaskyaw.subscriptionservice.mapper;

import com.thomaskyaw.subscriptionservice.dto.PlanResponse;
import com.thomaskyaw.subscriptionservice.model.SubscriptionPlan;

public class PlanMapper {

    public static PlanResponse toPlanResponse(SubscriptionPlan plan) {
        return new PlanResponse(
            plan.getId(),
            plan.getName(),
            plan.getTier().name(),
            plan.getMonthlyPrice(),
            plan.getAnnualPrice(),
            plan.getMaxUsers(),
            plan.getMaxApiCallsPerMonth(),
            plan.getMaxStorageGb(),
            plan.getFeatures()
        );
    }
}
