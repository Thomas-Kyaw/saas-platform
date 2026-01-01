package com.thomaskyaw.subscriptionservice.repository;

import com.thomaskyaw.subscriptionservice.model.SubscriptionStatus;
import com.thomaskyaw.subscriptionservice.model.TenantSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TenantSubscriptionRepository extends JpaRepository<TenantSubscription, UUID> {
    Optional<TenantSubscription> findByTenantId(UUID tenantId);

    List<TenantSubscription> findByTrialEndsAtBeforeAndStatus(OffsetDateTime date, SubscriptionStatus status);
}
