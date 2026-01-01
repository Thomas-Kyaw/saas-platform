package com.thomaskyaw.subscriptionservice.controller;

import com.thomaskyaw.subscriptionservice.dto.CreateSubscriptionRequest;
import com.thomaskyaw.subscriptionservice.dto.SubscriptionResponse;
import com.thomaskyaw.subscriptionservice.dto.UpgradeSubscriptionRequest;
import com.thomaskyaw.subscriptionservice.mapper.SubscriptionMapper;
import com.thomaskyaw.subscriptionservice.model.TenantSubscription;
import com.thomaskyaw.subscriptionservice.service.SubscriptionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @GetMapping
    public ResponseEntity<SubscriptionResponse> getSubscription() {
        TenantSubscription subscription = subscriptionService.getSubscription();
        return ResponseEntity.ok(SubscriptionMapper.toSubscriptionResponse(subscription));
    }

    @PostMapping
    public ResponseEntity<SubscriptionResponse> createSubscription(
            @Valid @RequestBody CreateSubscriptionRequest request) {
        TenantSubscription subscription = subscriptionService.createSubscription(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(SubscriptionMapper.toSubscriptionResponse(subscription));
    }

    @PutMapping("/change")
    public ResponseEntity<SubscriptionResponse> changeSubscription(
            @Valid @RequestBody UpgradeSubscriptionRequest request) {
        TenantSubscription subscription = subscriptionService.changeSubscription(request.newPlanId());
        return ResponseEntity.ok(SubscriptionMapper.toSubscriptionResponse(subscription));
    }

    @DeleteMapping
    public ResponseEntity<Void> cancelSubscription() {
        subscriptionService.cancelSubscription();
        return ResponseEntity.noContent().build();
    }
}
