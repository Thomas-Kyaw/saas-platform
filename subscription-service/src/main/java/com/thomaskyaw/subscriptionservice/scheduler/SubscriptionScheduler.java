package com.thomaskyaw.subscriptionservice.scheduler;

import com.thomaskyaw.subscriptionservice.model.SubscriptionStatus;
import com.thomaskyaw.subscriptionservice.model.TenantSubscription;
import com.thomaskyaw.subscriptionservice.repository.TenantSubscriptionRepository;
import com.thomaskyaw.subscriptionservice.service.SubscriptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;

@Component
public class SubscriptionScheduler {

    private static final Logger logger = LoggerFactory.getLogger(SubscriptionScheduler.class);

    private final TenantSubscriptionRepository subscriptionRepository;
    private final SubscriptionService subscriptionService;

    public SubscriptionScheduler(TenantSubscriptionRepository subscriptionRepository, SubscriptionService subscriptionService) {
        this.subscriptionRepository = subscriptionRepository;
        this.subscriptionService = subscriptionService;
    }

    @Scheduled(cron = "0 0 0 * * *") // Run daily at midnight
    public void checkExpiredTrials() {
        logger.info("Checking for expired trials...");
        OffsetDateTime now = OffsetDateTime.now();
        List<TenantSubscription> expiredTrials = subscriptionRepository.findByTrialEndsAtBeforeAndStatus(now, SubscriptionStatus.TRIAL);

        logger.info("Found {} expired trials", expiredTrials.size());

        for (TenantSubscription subscription : expiredTrials) {
            try {
                subscriptionService.processExpiredTrial(subscription);
            } catch (Exception e) {
                logger.error("Error processing expired trial for tenant {}", subscription.getTenantId(), e);
            }
        }
    }
}
