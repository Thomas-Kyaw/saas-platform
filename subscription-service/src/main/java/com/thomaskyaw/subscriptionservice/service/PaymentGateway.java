package com.thomaskyaw.subscriptionservice.service;

import java.math.BigDecimal;
import java.util.UUID;

public interface PaymentGateway {
    boolean charge(UUID tenantId, BigDecimal amount, String currency);
}
