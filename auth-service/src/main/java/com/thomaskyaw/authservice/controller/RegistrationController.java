package com.thomaskyaw.authservice.controller;

import com.thomaskyaw.authservice.dto.PlanTier;
import com.thomaskyaw.authservice.dto.RegisterRequest;
import com.thomaskyaw.authservice.dto.RegisterResponse;
import com.thomaskyaw.authservice.service.RegistrationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/register")
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
        RegisterResponse response = registrationService.register(request);
        HttpStatus status = request.planTier() == PlanTier.ENTERPRISE ? HttpStatus.ACCEPTED : HttpStatus.OK;
        return ResponseEntity.status(status).body(response);
    }
}
