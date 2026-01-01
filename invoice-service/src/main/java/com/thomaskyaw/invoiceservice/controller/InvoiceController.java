package com.thomaskyaw.invoiceservice.controller;

import com.thomaskyaw.invoiceservice.model.Invoice;
import com.thomaskyaw.invoiceservice.service.InvoiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping
    public ResponseEntity<List<Invoice>> getInvoices(@RequestHeader("X-Tenant-Id") UUID tenantId) {
        List<Invoice> invoices = invoiceService.getInvoices(tenantId);
        return ResponseEntity.ok(invoices);
    }
}
