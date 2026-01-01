package com.thomaskyaw.usageservice.service;

import com.thomaskyaw.usageservice.dto.ReportUsageRequest;
import com.thomaskyaw.usageservice.model.UsageRecord;
import com.thomaskyaw.usageservice.repository.UsageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsageServiceTest {

    @Mock
    private UsageRepository usageRepository;

    @InjectMocks
    private UsageService usageService;

    private UUID tenantId;

    @BeforeEach
    void setUp() {
        tenantId = UUID.randomUUID();
    }

    @Test
    void reportUsage_Success() {
        ReportUsageRequest request = new ReportUsageRequest("api_calls", BigDecimal.valueOf(100));

        when(usageRepository.save(any(UsageRecord.class))).thenAnswer(invocation -> {
            UsageRecord record = invocation.getArgument(0);
            record.setId(UUID.randomUUID());
            return record;
        });

        UsageRecord result = usageService.reportUsage(request, tenantId);

        assertNotNull(result);
        assertEquals(tenantId, result.getTenantId());
        assertEquals("api_calls", result.getMetricName());
        assertEquals(BigDecimal.valueOf(100), result.getQuantity());
        assertNotNull(result.getId());

        verify(usageRepository).save(any(UsageRecord.class));
    }
}
