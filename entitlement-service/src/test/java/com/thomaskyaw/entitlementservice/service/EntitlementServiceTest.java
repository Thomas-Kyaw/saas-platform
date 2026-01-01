package com.thomaskyaw.entitlementservice.service;

import com.thomaskyaw.entitlementservice.model.Entitlement;
import com.thomaskyaw.entitlementservice.repository.EntitlementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EntitlementServiceTest {

    @Mock
    private EntitlementRepository entitlementRepository;

    @InjectMocks
    private EntitlementService entitlementService;

    private UUID planId;

    @BeforeEach
    void setUp() {
        planId = UUID.randomUUID();
    }

    @Test
    void checkEntitlement_Enabled() {
        String featureKey = "advanced_analytics";
        Entitlement entitlement = new Entitlement();
        entitlement.setEnabled(true);

        when(entitlementRepository.findByPlanIdAndFeatureKey(planId, featureKey)).thenReturn(Optional.of(entitlement));

        boolean result = entitlementService.checkEntitlement(planId, featureKey);

        assertTrue(result);
    }

    @Test
    void checkEntitlement_Disabled() {
        String featureKey = "advanced_analytics";
        Entitlement entitlement = new Entitlement();
        entitlement.setEnabled(false);

        when(entitlementRepository.findByPlanIdAndFeatureKey(planId, featureKey)).thenReturn(Optional.of(entitlement));

        boolean result = entitlementService.checkEntitlement(planId, featureKey);

        assertFalse(result);
    }

    @Test
    void checkEntitlement_NotFound() {
        String featureKey = "unknown_feature";

        when(entitlementRepository.findByPlanIdAndFeatureKey(planId, featureKey)).thenReturn(Optional.empty());

        boolean result = entitlementService.checkEntitlement(planId, featureKey);

        assertFalse(result);
    }
}
