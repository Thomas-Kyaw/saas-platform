package com.thomaskyaw.authservice.client;

import com.thomaskyaw.authservice.client.dto.CreateSubscriptionRequest;
import com.thomaskyaw.authservice.client.dto.PlanResponse;
import com.thomaskyaw.authservice.dto.PlanTier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Component
public class SubscriptionHttpClient {

    private final RestTemplate restTemplate;
    private final String subscriptionBaseUrl;

    public SubscriptionHttpClient(RestTemplate restTemplate,
                                  @Value("${subscription.service.url:http://localhost:4003}") String subscriptionBaseUrl) {
        this.restTemplate = restTemplate;
        this.subscriptionBaseUrl = subscriptionBaseUrl;
    }

    public PlanResponse getPlanByTier(PlanTier tier) {
        return restTemplate.getForObject(subscriptionBaseUrl + "/plans/tier/" + tier.name(), PlanResponse.class);
    }

    public void createSubscription(UUID tenantId, UUID planId, boolean startTrial) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("X-Tenant-Id", tenantId.toString());

        CreateSubscriptionRequest request = new CreateSubscriptionRequest(planId, startTrial);
        restTemplate.postForEntity(
                subscriptionBaseUrl + "/subscriptions",
                new HttpEntity<>(request, headers),
                Void.class
        );
    }
}
