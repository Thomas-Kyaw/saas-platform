package com.thomaskyaw.entitlementservice.service;

import com.thomaskyaw.entitlementservice.model.Entitlement;
import com.thomaskyaw.entitlementservice.repository.EntitlementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EntitlementService {

    private final EntitlementRepository entitlementRepository;

    public EntitlementService(EntitlementRepository entitlementRepository) {
        this.entitlementRepository = entitlementRepository;
    }

    @Transactional(readOnly = true)
    public List<Entitlement> getEntitlements(UUID planId) {
        return entitlementRepository.findByPlanId(planId);
    }

    @Transactional(readOnly = true)
    public boolean checkEntitlement(UUID planId, String featureKey) {
        Optional<Entitlement> entitlement = entitlementRepository.findByPlanIdAndFeatureKey(planId, featureKey);
        return entitlement.map(Entitlement::getEnabled).orElse(false);
    }
}
