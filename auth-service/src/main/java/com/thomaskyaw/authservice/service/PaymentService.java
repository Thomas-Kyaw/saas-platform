package com.thomaskyaw.authservice.service;

import org.springframework.stereotype.Service;

/**
 * Payment service for handling payment method validation and Stripe integration.
 *
 * <p>This is a stub implementation. To integrate with Stripe:
 *
 * <h3>1. Add Stripe Dependency</h3>
 * <pre>
 * // In build.gradle
 * implementation 'com.stripe:stripe-java:24.4.0'
 * </pre>
 *
 * <h3>2. Configure Stripe API Key</h3>
 * <pre>
 * # In application.yml
 * stripe:
 *   api-key: ${STRIPE_SECRET_KEY}  # Set in environment variables
 * </pre>
 *
 * <h3>3. Implement Payment Methods</h3>
 * <ul>
 * <li>validatePaymentMethod(String paymentMethodId) - Verify payment method exists</li>
 * <li>createCustomer(UUID tenantId, String email, String paymentMethodId) - Create Stripe customer</li>
 * <li>chargeSubscription(UUID tenantId, BigDecimal amount) - Charge for subscription</li>
 * </ul>
 *
 * <h3>Example Implementation:</h3>
 * <pre>
 * {@code
 * @Value("${stripe.api-key}")
 * private String stripeApiKey;
 *
 * public void validatePaymentMethod(String paymentMethodId) {
 *     Stripe.apiKey = stripeApiKey;
 *     try {
 *         PaymentMethod.retrieve(paymentMethodId);
 *     } catch (StripeException e) {
 *         throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
 *             "Invalid payment method: " + e.getMessage());
 *     }
 * }
 *
 * public String createCustomer(UUID tenantId, String email, String paymentMethodId) {
 *     Stripe.apiKey = stripeApiKey;
 *     try {
 *         CustomerCreateParams params = CustomerCreateParams.builder()
 *             .setEmail(email)
 *             .setPaymentMethod(paymentMethodId)
 *             .setInvoiceSettings(
 *                 CustomerCreateParams.InvoiceSettings.builder()
 *                     .setDefaultPaymentMethod(paymentMethodId)
 *                     .build()
 *             )
 *             .putMetadata("tenant_id", tenantId.toString())
 *             .build();
 *
 *         Customer customer = Customer.create(params);
 *         return customer.getId();
 *     } catch (StripeException e) {
 *         throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,
 *             "Failed to create Stripe customer: " + e.getMessage());
 *     }
 * }
 * }
 * </pre>
 *
 * <h3>4. Store Stripe Customer ID</h3>
 * <p>Add a `stripe_customer_id` column to your tenant or subscription table to track the Stripe customer.
 *
 * <h3>5. Handle Webhooks</h3>
 * <p>Set up webhook endpoints to handle:
 * <ul>
 * <li>payment_intent.succeeded - Mark trial as converted</li>
 * <li>invoice.payment_failed - Suspend subscription</li>
 * <li>customer.subscription.deleted - Cancel subscription</li>
 * </ul>
 */
@Service
public class PaymentService {

    /**
     * Validates that a payment method is required for paid tiers.
     *
     * @param paymentMethodId The payment method ID from the request
     * @param tierName The plan tier name
     * @throws org.springframework.web.server.ResponseStatusException if payment method is missing for paid tier
     */
    public void validatePaymentMethodForTier(String paymentMethodId, String tierName) {
        if (requiresPayment(tierName) && (paymentMethodId == null || paymentMethodId.isBlank())) {
            throw new org.springframework.web.server.ResponseStatusException(
                org.springframework.http.HttpStatus.BAD_REQUEST,
                "Payment method is required for " + tierName + " tier"
            );
        }
    }

    /**
     * Checks if a tier requires payment.
     */
    private boolean requiresPayment(String tierName) {
        return "BASIC".equals(tierName) || "PRO".equals(tierName);
    }

    // TODO: Implement Stripe integration methods as documented above
    // - validatePaymentMethod(String paymentMethodId)
    // - createCustomer(UUID tenantId, String email, String paymentMethodId)
    // - chargeSubscription(UUID tenantId, BigDecimal amount)
}
