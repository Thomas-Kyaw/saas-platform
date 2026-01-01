package com.thomaskyaw.usageservice.service;

import com.thomaskyaw.usageservice.dto.ReportUsageRequest;
import com.thomaskyaw.usageservice.model.UsageRecord;
import com.thomaskyaw.usageservice.repository.UsageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UsageService {

    private final UsageRepository usageRepository;

    public UsageService(UsageRepository usageRepository) {
        this.usageRepository = usageRepository;
    }

    @Transactional
    public UsageRecord reportUsage(ReportUsageRequest request, UUID tenantId) {
        UsageRecord record = new UsageRecord();
        record.setTenantId(tenantId);
        record.setMetricName(request.metricName());
        record.setQuantity(request.quantity());
        // Timestamp is set by @CreationTimestamp

        return usageRepository.save(record);
    }
}
