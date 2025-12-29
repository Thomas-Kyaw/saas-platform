package com.thomaskyaw.subscriptionservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thomaskyaw.common.context.UserContext;
import com.thomaskyaw.subscriptionservice.dto.CreateSubscriptionRequest;
import com.thomaskyaw.subscriptionservice.dto.UpgradeSubscriptionRequest;
import com.thomaskyaw.subscriptionservice.model.PlanTier;
import com.thomaskyaw.subscriptionservice.model.SubscriptionPlan;
import com.thomaskyaw.subscriptionservice.model.SubscriptionStatus;
import com.thomaskyaw.subscriptionservice.model.TenantSubscription;
import com.thomaskyaw.subscriptionservice.service.SubscriptionService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SubscriptionController.class)
class SubscriptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SubscriptionService subscriptionService;

    @AfterEach
    void tearDown() {
        UserContext.clear();
    }

    @Test
    void getSubscription_ShouldReturnSubscription() throws Exception {
        UUID tenantId = UUID.randomUUID();
        UUID subscriptionId = UUID.randomUUID();

        TenantSubscription subscription = new TenantSubscription();
        subscription.setId(subscriptionId);
        subscription.setTenantId(tenantId);
        subscription.setStatus(SubscriptionStatus.ACTIVE);
        subscription.setStartDate(OffsetDateTime.now());

        SubscriptionPlan plan = new SubscriptionPlan();
        plan.setName("Pro");
        plan.setTier(PlanTier.PRO);
        subscription.setPlan(plan);

        when(subscriptionService.getSubscription()).thenReturn(subscription);

        mockMvc.perform(get("/subscriptions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ACTIVE"));

        verify(subscriptionService).getSubscription();
    }

    @Test
    void createSubscription_ShouldReturn201() throws Exception {
        UUID planId = UUID.randomUUID();
        UUID tenantId = UUID.randomUUID();
        CreateSubscriptionRequest request = new CreateSubscriptionRequest(planId);

        TenantSubscription subscription = new TenantSubscription();
        subscription.setId(UUID.randomUUID());
        subscription.setTenantId(tenantId);
        subscription.setStatus(SubscriptionStatus.ACTIVE);

        when(subscriptionService.createSubscription(planId)).thenReturn(subscription);

        mockMvc.perform(post("/subscriptions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("ACTIVE"));

        verify(subscriptionService).createSubscription(planId);
    }

    @Test
    void upgradeSubscription_ShouldReturn200() throws Exception {
        UUID newPlanId = UUID.randomUUID();
        UpgradeSubscriptionRequest request = new UpgradeSubscriptionRequest(newPlanId);

        TenantSubscription subscription = new TenantSubscription();
        subscription.setStatus(SubscriptionStatus.ACTIVE);

        when(subscriptionService.upgradePlan(newPlanId)).thenReturn(subscription);

        mockMvc.perform(put("/subscriptions/upgrade")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ACTIVE"));

        verify(subscriptionService).upgradePlan(newPlanId);
    }

    @Test
    void cancelSubscription_ShouldReturn204() throws Exception {
        doNothing().when(subscriptionService).cancelSubscription();

        mockMvc.perform(delete("/subscriptions"))
                .andExpect(status().isNoContent());

        verify(subscriptionService).cancelSubscription();
    }

    @Test
    void createSubscription_ShouldReturn400_WhenPlanIdMissing() throws Exception {
        String invalidRequest = "{}";

        mockMvc.perform(post("/subscriptions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidRequest))
                .andExpect(status().isBadRequest());

        verify(subscriptionService, never()).createSubscription(any());
    }
}
