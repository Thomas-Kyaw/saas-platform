package com.thomaskyaw.entitlementservice.controller;

import com.thomaskyaw.entitlementservice.model.Entitlement;
import com.thomaskyaw.entitlementservice.service.EntitlementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/entitlements")
public class EntitlementController {

    private final EntitlementService entitlementService;

    public EntitlementController(EntitlementService entitlementService) {
        this.entitlementService = entitlementService;
    }

    @GetMapping("/{planId}")
    public ResponseEntity<List<Entitlement>> getEntitlements(@PathVariable UUID planId) {
        List<Entitlement> entitlements = entitlementService.getEntitlements(planId);
        return ResponseEntity.ok(entitlements);
    }

    @GetMapping("/{planId}/{featureKey}")
    public ResponseEntity<Boolean> checkEntitlement(@PathVariable UUID planId, @PathVariable String featureKey) {
        boolean hasAccess = entitlementService.checkEntitlement(planId, featureKey);
        return ResponseEntity.ok(hasAccess);
    }
}
