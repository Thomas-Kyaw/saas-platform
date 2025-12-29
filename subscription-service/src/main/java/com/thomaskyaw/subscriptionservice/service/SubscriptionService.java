package com.thomaskyaw.subscriptionservice.service;

import com.thomaskyaw.subscriptionservice.model.SubscriptionPlan;
import com.thomaskyaw.subscriptionservice.model.SubscriptionStatus;
import com.thomaskyaw.subscriptionservice.model.TenantSubscription;
import com.thomaskyaw.subscriptionservice.repository.SubscriptionPlanRepository;
import com.thomaskyaw.subscriptionservice.repository.TenantSubscriptionRepository;
import com.thomaskyaw.subscriptionservice.validation.TenantContextValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@Transactional
public class SubscriptionService {

    private final TenantSubscriptionRepository subscriptionRepository;
    private final SubscriptionPlanRepository planRepository;
    private final TenantContextValidator tenantContextValidator;

    public SubscriptionService(
            TenantSubscriptionRepository subscriptionRepository,
            SubscriptionPlanRepository planRepository,
            TenantContextValidator tenantContextValidator) {
        this.subscriptionRepository = subscriptionRepository;
        this.planRepository = planRepository;
        this.tenantContextValidator = tenantContextValidator;
    }

    /**
     * Create a subscription for the tenant in the current context.
     * @param planId The plan to subscribe to
     * @return The created subscription
     */
    public TenantSubscription createSubscription(UUID planId) {
        UUID tenantId = tenantContextValidator.requireTenantId();

        // Check if tenant already has a subscription
        if (subscriptionRepository.findByTenantId(tenantId).isPresent()) {
            throw new IllegalStateException("Tenant already has a subscription");
        }

        SubscriptionPlan plan = planRepository.findById(planId)
            .orElseThrow(() -> new IllegalArgumentException("Plan not found: " + planId));

        TenantSubscription subscription = new TenantSubscription();
        subscription.setTenantId(tenantId);
        subscription.setPlan(plan);
        subscription.setStatus(SubscriptionStatus.ACTIVE);
        subscription.setStartDate(OffsetDateTime.now());
        subscription.setAutoRenew(true);

        return subscriptionRepository.save(subscription);
    }

    /**
     * Get the subscription for the tenant in the current context.
     * @return The tenant's subscription
     */
    @Transactional(readOnly = true)
    public TenantSubscription getSubscription() {
        UUID tenantId = tenantContextValidator.requireTenantId();
        return subscriptionRepository.findByTenantId(tenantId)
            .orElseThrow(() -> new IllegalStateException("No subscription found for tenant"));
    }

    /**
     * Upgrade the current tenant's subscription to a new plan.
     * @param newPlanId The new plan ID
     * @return The updated subscription
     */
    public TenantSubscription upgradePlan(UUID newPlanId) {
        UUID tenantId = tenantContextValidator.requireTenantId();

        TenantSubscription subscription = subscriptionRepository.findByTenantId(tenantId)
            .orElseThrow(() -> new IllegalStateException("No subscription found for tenant"));

        SubscriptionPlan newPlan = planRepository.findById(newPlanId)
            .orElseThrow(() -> new IllegalArgumentException("Plan not found: " + newPlanId));

        subscription.setPlan(newPlan);
        return subscriptionRepository.save(subscription);
    }

    /**
     * Cancel the current tenant's subscription.
     */
    public void cancelSubscription() {
        UUID tenantId = tenantContextValidator.requireTenantId();

        TenantSubscription subscription = subscriptionRepository.findByTenantId(tenantId)
            .orElseThrow(() -> new IllegalStateException("No subscription found for tenant"));

        subscription.setStatus(SubscriptionStatus.CANCELLED);
        subscription.setEndDate(OffsetDateTime.now());
        subscription.setAutoRenew(false);
        subscriptionRepository.save(subscription);
    }
}
