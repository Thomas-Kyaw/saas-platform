package com.thomaskyaw.subscriptionservice.service;

import com.thomaskyaw.subscriptionservice.model.SubscriptionPlan;
import com.thomaskyaw.subscriptionservice.model.PlanTier;
import com.thomaskyaw.subscriptionservice.repository.SubscriptionPlanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class PlanService {

    private final SubscriptionPlanRepository planRepository;

    public PlanService(SubscriptionPlanRepository planRepository) {
        this.planRepository = planRepository;
    }

    public List<SubscriptionPlan> getAllPlans() {
        return planRepository.findAll();
    }

    public SubscriptionPlan getPlanById(UUID id) {
        return planRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Plan not found: " + id));
    }

    public SubscriptionPlan getPlanByTier(PlanTier tier) {
        return planRepository.findByTier(tier)
            .orElseThrow(() -> new IllegalArgumentException("Plan not found for tier: " + tier));
    }
}
