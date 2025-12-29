package com.thomaskyaw.subscriptionservice.controller;

import com.thomaskyaw.subscriptionservice.dto.PlanResponse;
import com.thomaskyaw.subscriptionservice.mapper.PlanMapper;
import com.thomaskyaw.subscriptionservice.model.SubscriptionPlan;
import com.thomaskyaw.subscriptionservice.service.PlanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/plans")
public class PlanController {

    private final PlanService planService;

    public PlanController(PlanService planService) {
        this.planService = planService;
    }

    @GetMapping
    public ResponseEntity<List<PlanResponse>> getAllPlans() {
        List<SubscriptionPlan> plans = planService.getAllPlans();
        return ResponseEntity.ok(plans.stream().map(PlanMapper::toPlanResponse).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlanResponse> getPlanById(@PathVariable UUID id) {
        SubscriptionPlan plan = planService.getPlanById(id);
        return ResponseEntity.ok(PlanMapper.toPlanResponse(plan));
    }
}
