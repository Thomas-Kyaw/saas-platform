package com.thomaskyaw.subscriptionservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class MockPaymentGateway implements PaymentGateway {

    private static final Logger logger = LoggerFactory.getLogger(MockPaymentGateway.class);

    @Override
    public boolean charge(UUID tenantId, BigDecimal amount, String currency) {
        logger.info("Mock charging tenant {} amount {} {}", tenantId, amount, currency);
        // Simulate success
        return true;
    }
}
