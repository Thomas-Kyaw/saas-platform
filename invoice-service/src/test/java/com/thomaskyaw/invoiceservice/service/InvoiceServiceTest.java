package com.thomaskyaw.invoiceservice.service;

import com.thomaskyaw.invoiceservice.model.Invoice;
import com.thomaskyaw.invoiceservice.repository.InvoiceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InvoiceServiceTest {

    @Mock
    private InvoiceRepository invoiceRepository;

    @InjectMocks
    private InvoiceService invoiceService;

    private UUID tenantId;

    @BeforeEach
    void setUp() {
        tenantId = UUID.randomUUID();
    }

    @Test
    void createInvoice_Success() {
        when(invoiceRepository.save(any(Invoice.class))).thenAnswer(invocation -> {
            Invoice invoice = invocation.getArgument(0);
            invoice.setId(UUID.randomUUID());
            return invoice;
        });

        Invoice result = invoiceService.createInvoice(tenantId, BigDecimal.valueOf(100), "USD", "PENDING");

        assertNotNull(result);
        assertEquals(tenantId, result.getTenantId());
        assertEquals(BigDecimal.valueOf(100), result.getAmount());
        assertEquals("USD", result.getCurrency());
        assertEquals("PENDING", result.getStatus());
        assertNotNull(result.getId());
        assertNotNull(result.getDueDate());

        verify(invoiceRepository).save(any(Invoice.class));
    }

    @Test
    void getInvoices_Success() {
        when(invoiceRepository.findByTenantId(tenantId)).thenReturn(List.of(new Invoice()));

        List<Invoice> result = invoiceService.getInvoices(tenantId);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(invoiceRepository).findByTenantId(tenantId);
    }
}
