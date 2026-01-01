package com.thomaskyaw.subscriptionservice.service;

import com.thomaskyaw.subscriptionservice.dto.CreateSubscriptionRequest;
import com.thomaskyaw.subscriptionservice.model.SubscriptionPlan;
import com.thomaskyaw.subscriptionservice.model.SubscriptionStatus;
import com.thomaskyaw.subscriptionservice.model.TenantSubscription;
import com.thomaskyaw.subscriptionservice.repository.SubscriptionPlanRepository;
import com.thomaskyaw.subscriptionservice.repository.TenantSubscriptionRepository;
import com.thomaskyaw.subscriptionservice.validation.TenantContextValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@Transactional
public class SubscriptionService {

    private final TenantSubscriptionRepository subscriptionRepository;
    private final SubscriptionPlanRepository planRepository;
    private final TenantContextValidator tenantContextValidator;

    /**
     * Create a subscription for the tenant in the current context.
     * @param request The subscription creation request
     * @return The created subscription
     */
    public TenantSubscription createSubscription(CreateSubscriptionRequest request) {
        UUID tenantId = tenantContextValidator.requireTenantId();

        // Check if tenant already has a subscription
        if (subscriptionRepository.findByTenantId(tenantId).isPresent()) {
            throw new IllegalStateException("Tenant already has a subscription");
        }

        SubscriptionPlan plan = planRepository.findById(request.planId())
            .orElseThrow(() -> new IllegalArgumentException("Plan not found: " + request.planId()));

        TenantSubscription subscription = new TenantSubscription();
        subscription.setTenantId(tenantId);
        subscription.setPlan(plan);
        subscription.setStartDate(OffsetDateTime.now());
        subscription.setAutoRenew(true);

        // Set trial or active status
        if (request.startTrial()) {
            subscription.setStatus(SubscriptionStatus.TRIAL);
            subscription.setTrialEndsAt(OffsetDateTime.now().plusDays(14));
        } else {
            subscription.setStatus(SubscriptionStatus.ACTIVE);
        }

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
     * Change the current tenant's subscription to a new plan (Upgrade/Downgrade).
     * Handles proration by crediting unused time and charging for the new plan.
     * Resets the billing cycle to today.
     *
     * @param newPlanId The new plan ID
     * @return The updated subscription
     */
    public TenantSubscription changeSubscription(UUID newPlanId) {
        UUID tenantId = tenantContextValidator.requireTenantId();

        TenantSubscription subscription = subscriptionRepository.findByTenantId(tenantId)
            .orElseThrow(() -> new IllegalStateException("No subscription found for tenant"));

        SubscriptionPlan newPlan = planRepository.findById(newPlanId)
            .orElseThrow(() -> new IllegalArgumentException("Plan not found: " + newPlanId));

        if (subscription.getPlan().getId().equals(newPlanId)) {
            throw new IllegalArgumentException("Already subscribed to this plan");
        }

        // Calculate unused value of current plan
        BigDecimal unusedValue = BigDecimal.ZERO;
        if (subscription.getStatus() == SubscriptionStatus.ACTIVE && subscription.getEndDate() != null) {
            long totalDays = java.time.temporal.ChronoUnit.DAYS.between(subscription.getStartDate(), subscription.getEndDate());
            long remainingDays = java.time.temporal.ChronoUnit.DAYS.between(OffsetDateTime.now(), subscription.getEndDate());

            if (totalDays > 0 && remainingDays > 0) {
                BigDecimal currentPrice = subscription.getPlan().getMonthlyPrice(); // Assuming monthly
                unusedValue = currentPrice.multiply(BigDecimal.valueOf(remainingDays))
                        .divide(BigDecimal.valueOf(totalDays), 2, java.math.RoundingMode.HALF_UP);
            }
        }

        // Calculate new charge
        BigDecimal newPrice = newPlan.getMonthlyPrice();
        BigDecimal amountToCharge = newPrice.subtract(unusedValue);

        if (amountToCharge.compareTo(BigDecimal.ZERO) > 0) {
            boolean paymentSuccess = paymentGateway.charge(tenantId, amountToCharge, "USD");
            if (!paymentSuccess) {
                throw new IllegalStateException("Payment failed for subscription change");
            }
        } else {
            // If downgrade results in credit, we might store it. For now, we just don't charge.
            // TODO: Implement credit balance
        }

        subscription.setPlan(newPlan);
        subscription.setStartDate(OffsetDateTime.now());
        subscription.setEndDate(OffsetDateTime.now().plusMonths(1)); // Reset cycle
        subscription.setStatus(SubscriptionStatus.ACTIVE);

        return subscriptionRepository.save(subscription);
    }

    public void cancelSubscription() {
        UUID tenantId = tenantContextValidator.requireTenantId();

        TenantSubscription subscription = subscriptionRepository.findByTenantId(tenantId)
            .orElseThrow(() -> new IllegalStateException("No subscription found for tenant"));

        subscription.setStatus(SubscriptionStatus.CANCELLED);
        subscription.setEndDate(OffsetDateTime.now());
        subscription.setAutoRenew(false);
        subscriptionRepository.save(subscription);
    }

    /**
     * Process an expired trial subscription.
     * @param subscription The expired subscription
     */
    public void processExpiredTrial(TenantSubscription subscription) {
        if (subscription.getStatus() != SubscriptionStatus.TRIAL) {
            return;
        }

        // Attempt to charge the payment method
        // For now, we use a mock amount and currency
        boolean paymentSuccess = paymentGateway.charge(
            subscription.getTenantId(),
            subscription.getPlan().getMonthlyPrice(),
            "USD"
        );

        if (paymentSuccess) {
            subscription.setStatus(SubscriptionStatus.ACTIVE);
            subscription.setStartDate(OffsetDateTime.now());
            // Assuming monthly billing for now
            subscription.setEndDate(OffsetDateTime.now().plusMonths(1));
            subscription.setTrialEndsAt(null);
        } else {
            // Payment failed, suspend subscription
            subscription.setStatus(SubscriptionStatus.SUSPENDED);
            subscription.setAutoRenew(false);
        }
        subscriptionRepository.save(subscription);
    }

    private final PaymentGateway paymentGateway;

    public SubscriptionService(
            TenantSubscriptionRepository subscriptionRepository,
            SubscriptionPlanRepository planRepository,
            TenantContextValidator tenantContextValidator,
            PaymentGateway paymentGateway) {
        this.subscriptionRepository = subscriptionRepository;
        this.planRepository = planRepository;
        this.tenantContextValidator = tenantContextValidator;
        this.paymentGateway = paymentGateway;
    }
}
