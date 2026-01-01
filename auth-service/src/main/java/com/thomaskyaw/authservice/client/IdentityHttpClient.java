package com.thomaskyaw.authservice.client;

import com.thomaskyaw.authservice.client.dto.CreateTenantRequest;
import com.thomaskyaw.authservice.client.dto.CreateUserRequest;
import com.thomaskyaw.authservice.client.dto.TenantResponse;
import com.thomaskyaw.authservice.client.dto.UserResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Component
public class IdentityHttpClient {

    private final RestTemplate restTemplate;
    private final String identityBaseUrl;

    public IdentityHttpClient(RestTemplate restTemplate,
                              @Value("${identity.service.http-url:http://localhost:4002}") String identityBaseUrl) {
        this.restTemplate = restTemplate;
        this.identityBaseUrl = identityBaseUrl;
    }

    public TenantResponse createTenant(CreateTenantRequest request) {
        return restTemplate.postForObject(identityBaseUrl + "/tenants", request, TenantResponse.class);
    }

    public UserResponse createUserForTenant(UUID tenantId, CreateUserRequest request) {
        return restTemplate.postForObject(identityBaseUrl + "/tenants/" + tenantId + "/users", request, UserResponse.class);
    }
}
