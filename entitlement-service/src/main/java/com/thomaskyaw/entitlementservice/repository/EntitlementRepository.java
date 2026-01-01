package com.thomaskyaw.entitlementservice.repository;

import com.thomaskyaw.entitlementservice.model.Entitlement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EntitlementRepository extends JpaRepository<Entitlement, UUID> {
    List<Entitlement> findByPlanId(UUID planId);
    Optional<Entitlement> findByPlanIdAndFeatureKey(UUID planId, String featureKey);
}
