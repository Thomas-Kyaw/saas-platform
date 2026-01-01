package com.thomaskyaw.usageservice.controller;

import com.thomaskyaw.usageservice.dto.ReportUsageRequest;
import com.thomaskyaw.usageservice.model.UsageRecord;
import com.thomaskyaw.usageservice.service.UsageService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/usage")
public class UsageController {

    private final UsageService usageService;

    public UsageController(UsageService usageService) {
        this.usageService = usageService;
    }

    @PostMapping
    public ResponseEntity<UsageRecord> reportUsage(@Valid @RequestBody ReportUsageRequest request,
                                                   @RequestHeader("X-Tenant-Id") UUID tenantId) {
        UsageRecord record = usageService.reportUsage(request, tenantId);
        return ResponseEntity.ok(record);
    }
}
