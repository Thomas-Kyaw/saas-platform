package com.thomaskyaw.identityservice.controller;

import com.thomaskyaw.identityservice.dto.TenantResponseDTO;
import com.thomaskyaw.identityservice.service.TenantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tenants")
public class TenantController {
    private final TenantService tenantService;

    public TenantController(TenantService tenantService) {
        this.tenantService = tenantService;
    }

    @GetMapping
    public ResponseEntity<List<TenantResponseDTO>> getTenants() {
        List<TenantResponseDTO> tenants = tenantService.getTenants();
        return ResponseEntity.ok().body(tenants);
    }

    @org.springframework.web.bind.annotation.PostMapping
    public ResponseEntity<TenantResponseDTO> createTenant(@org.springframework.web.bind.annotation.RequestBody @jakarta.validation.Valid com.thomaskyaw.identityservice.dto.CreateTenantRequest request) {
        TenantResponseDTO tenant = tenantService.createTenant(request);
        return ResponseEntity.ok(tenant);
    }

    @org.springframework.web.bind.annotation.PostMapping("/{tenantId}/users")
    public ResponseEntity<com.thomaskyaw.identityservice.dto.UserResponseDTO> createUserForTenant(
            @org.springframework.web.bind.annotation.PathVariable java.util.UUID tenantId,
            @org.springframework.web.bind.annotation.RequestBody @jakarta.validation.Valid com.thomaskyaw.identityservice.dto.CreateUserRequest request) {
        com.thomaskyaw.identityservice.dto.UserResponseDTO user = tenantService.createUserForTenant(tenantId, request);
        return ResponseEntity.ok(user);
    }
}
