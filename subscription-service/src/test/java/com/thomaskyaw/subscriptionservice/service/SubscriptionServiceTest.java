package com.thomaskyaw.subscriptionservice.service;

import com.thomaskyaw.subscriptionservice.model.SubscriptionPlan;
import com.thomaskyaw.subscriptionservice.model.SubscriptionStatus;
import com.thomaskyaw.subscriptionservice.model.TenantSubscription;
import com.thomaskyaw.subscriptionservice.repository.SubscriptionPlanRepository;
import com.thomaskyaw.subscriptionservice.repository.TenantSubscriptionRepository;
import com.thomaskyaw.subscriptionservice.validation.TenantContextValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubscriptionServiceTest {

    @Mock
    private TenantSubscriptionRepository subscriptionRepository;
    @Mock
    private SubscriptionPlanRepository planRepository;
    @Mock
    private TenantContextValidator tenantContextValidator;
    @Mock
    private PaymentGateway paymentGateway;

    private SubscriptionService subscriptionService;

    @BeforeEach
    void setUp() {
        subscriptionService = new SubscriptionService(
            subscriptionRepository,
            planRepository,
            tenantContextValidator,
            paymentGateway
        );
    }

    @Test
    void processExpiredTrial_Success() {
        UUID tenantId = UUID.randomUUID();
        TenantSubscription subscription = new TenantSubscription();
        subscription.setTenantId(tenantId);
        subscription.setStatus(SubscriptionStatus.TRIAL);

        SubscriptionPlan plan = new SubscriptionPlan();
        plan.setMonthlyPrice(BigDecimal.TEN);
        subscription.setPlan(plan);

        when(paymentGateway.charge(eq(tenantId), eq(BigDecimal.TEN), eq("USD"))).thenReturn(true);

        subscriptionService.processExpiredTrial(subscription);

        verify(subscriptionRepository).save(argThat(s ->
            s.getStatus() == SubscriptionStatus.ACTIVE &&
            s.getTrialEndsAt() == null &&
            s.getEndDate().isAfter(OffsetDateTime.now())
        ));
    }

    @Test
    void processExpiredTrial_Failure() {
        UUID tenantId = UUID.randomUUID();
        TenantSubscription subscription = new TenantSubscription();
        subscription.setTenantId(tenantId);
        subscription.setStatus(SubscriptionStatus.TRIAL);

        SubscriptionPlan plan = new SubscriptionPlan();
        plan.setMonthlyPrice(BigDecimal.TEN);
        subscription.setPlan(plan);

        when(paymentGateway.charge(eq(tenantId), eq(BigDecimal.TEN), eq("USD"))).thenReturn(false);

        subscriptionService.processExpiredTrial(subscription);

        verify(subscriptionRepository).save(argThat(s ->
            s.getStatus() == SubscriptionStatus.SUSPENDED &&
            !s.getAutoRenew()
        ));
    }

    @Test
    void changeSubscription_Upgrade_Success() {
        UUID tenantId = UUID.randomUUID();
        UUID oldPlanId = UUID.randomUUID();
        UUID newPlanId = UUID.randomUUID();

        SubscriptionPlan oldPlan = new SubscriptionPlan();
        oldPlan.setId(oldPlanId);
        oldPlan.setMonthlyPrice(BigDecimal.valueOf(10));

        SubscriptionPlan newPlan = new SubscriptionPlan();
        newPlan.setId(newPlanId);
        newPlan.setMonthlyPrice(BigDecimal.valueOf(20));

        TenantSubscription subscription = new TenantSubscription();
        subscription.setTenantId(tenantId);
        subscription.setPlan(oldPlan);
        subscription.setStatus(SubscriptionStatus.ACTIVE);
        subscription.setStartDate(OffsetDateTime.now().minusDays(15));
        subscription.setEndDate(OffsetDateTime.now().plusDays(15));

        when(tenantContextValidator.requireTenantId()).thenReturn(tenantId);
        when(subscriptionRepository.findByTenantId(tenantId)).thenReturn(java.util.Optional.of(subscription));
        when(planRepository.findById(newPlanId)).thenReturn(java.util.Optional.of(newPlan));
        when(paymentGateway.charge(eq(tenantId), any(BigDecimal.class), eq("USD"))).thenReturn(true);

        subscriptionService.changeSubscription(newPlanId);

        // Verify charge is roughly 15 (20 - 5)
        verify(paymentGateway).charge(eq(tenantId), argThat(amount ->
            amount.compareTo(BigDecimal.valueOf(14)) > 0 && amount.compareTo(BigDecimal.valueOf(16)) < 0
        ), eq("USD"));

        verify(subscriptionRepository).save(argThat(s ->
            s.getPlan().getId().equals(newPlanId) &&
            s.getStatus() == SubscriptionStatus.ACTIVE
        ));
    }
}
