package com.thomaskyaw.subscriptionservice.repository;

import com.thomaskyaw.subscriptionservice.model.PlanTier;
import com.thomaskyaw.subscriptionservice.model.SubscriptionPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan, UUID> {
    Optional<SubscriptionPlan> findByTier(PlanTier tier);
}
