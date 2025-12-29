package com.thomaskyaw.authservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thomaskyaw.authservice.dto.LoginRequest;
import com.thomaskyaw.authservice.dto.LoginResponse;
import com.thomaskyaw.authservice.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @Test
    void login_ShouldReturnToken_WhenCredentialsValid() throws Exception {
        String email = "user@example.com";
        String password = "password123";
        String expectedToken = "jwt-token-here";
        LoginRequest request = new LoginRequest(email, password);

        when(authService.login(email, password)).thenReturn(expectedToken);

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(expectedToken));

        verify(authService).login(email, password);
    }

    @Test
    void login_ShouldReturn401_WhenCredentialsInvalid() throws Exception {
        String email = "user@example.com";
        String password = "wrong-password";
        LoginRequest request = new LoginRequest(email, password);

        when(authService.login(email, password)).thenThrow(new BadCredentialsException("Invalid credentials"));

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());

        verify(authService).login(email, password);
    }

    @Test
    void login_ShouldReturn403_WhenUserDisabled() throws Exception {
        String email = "user@example.com";
        String password = "password123";
        LoginRequest request = new LoginRequest(email, password);

        when(authService.login(email, password)).thenThrow(new DisabledException("User is disabled"));

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());

        verify(authService).login(email, password);
    }

    @Test
    void login_ShouldReturn400_WhenEmailMissing() throws Exception {
        String requestJson = "{\"password\":\"password123\"}";

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isBadRequest());

        verify(authService, never()).login(anyString(), anyString());
    }

    @Test
    void logout_ShouldReturn200_WhenTokenValid() throws Exception {
        String token = "Bearer valid-token";

        doNothing().when(authService).logout(token);

        mockMvc.perform(post("/logout")
                .header("Authorization", token))
                .andExpect(status().isOk());

        verify(authService).logout(token);
    }

    @Test
    void logout_ShouldReturn400_WhenAuthorizationHeaderMissing() throws Exception {
        mockMvc.perform(post("/logout"))
                .andExpect(status().isBadRequest());

        verify(authService, never()).logout(anyString());
    }
}
