package com.thomaskyaw.invoiceservice.service;

import com.thomaskyaw.invoiceservice.model.Invoice;
import com.thomaskyaw.invoiceservice.repository.InvoiceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

    public InvoiceService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    @Transactional(readOnly = true)
    public List<Invoice> getInvoices(UUID tenantId) {
        return invoiceRepository.findByTenantId(tenantId);
    }

    @Transactional
    public Invoice createInvoice(UUID tenantId, BigDecimal amount, String currency, String status) {
        Invoice invoice = new Invoice();
        invoice.setTenantId(tenantId);
        invoice.setAmount(amount);
        invoice.setCurrency(currency);
        invoice.setStatus(status);
        invoice.setDueDate(OffsetDateTime.now().plusDays(30)); // Default due date
        return invoiceRepository.save(invoice);
    }
}
