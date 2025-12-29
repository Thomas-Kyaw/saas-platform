package com.thomaskyaw.subscriptionservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thomaskyaw.subscriptionservice.dto.PlanResponse;
import com.thomaskyaw.subscriptionservice.model.PlanTier;
import com.thomaskyaw.subscriptionservice.model.SubscriptionPlan;
import com.thomaskyaw.subscriptionservice.service.PlanService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PlanController.class)
class PlanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlanService planService;

    @Test
    void getAllPlans_ShouldReturnListOfPlans() throws Exception {
        SubscriptionPlan plan1 = new SubscriptionPlan();
        plan1.setId(UUID.randomUUID());
        plan1.setName("Basic");
        plan1.setTier(PlanTier.BASIC);
        plan1.setMonthlyPrice(BigDecimal.valueOf(10));

        SubscriptionPlan plan2 = new SubscriptionPlan();
        plan2.setId(UUID.randomUUID());
        plan2.setName("Pro");
        plan2.setTier(PlanTier.PRO);
        plan2.setMonthlyPrice(BigDecimal.valueOf(30));

        when(planService.getAllPlans()).thenReturn(Arrays.asList(plan1, plan2));

        mockMvc.perform(get("/plans"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Basic"))
                .andExpect(jsonPath("$[1].name").value("Pro"));

        verify(planService).getAllPlans();
    }

    @Test
    void getPlanById_ShouldReturnPlan_WhenExists() throws Exception {
        UUID planId = UUID.randomUUID();
        SubscriptionPlan plan = new SubscriptionPlan();
        plan.setId(planId);
        plan.setName("Enterprise");
        plan.setTier(PlanTier.ENTERPRISE);
        plan.setMonthlyPrice(BigDecimal.valueOf(100));

        when(planService.getPlanById(planId)).thenReturn(plan);

        mockMvc.perform(get("/plans/" + planId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Enterprise"))
                .andExpect(jsonPath("$.tier").value("ENTERPRISE"));

        verify(planService).getPlanById(planId);
    }

    @Test
    void getPlanById_ShouldReturn404_WhenNotFound() throws Exception {
        UUID planId = UUID.randomUUID();

        when(planService.getPlanById(planId)).thenThrow(new RuntimeException("Plan not found"));

        mockMvc.perform(get("/plans/" + planId))
                .andExpect(status().isInternalServerError());

        verify(planService).getPlanById(planId);
    }
}
