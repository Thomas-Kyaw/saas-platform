package com.thomaskyaw.identityservice.controller;

import com.thomaskyaw.identityservice.dto.UserPermissionsResponseDTO;
import com.thomaskyaw.identityservice.dto.UserStatusResponseDTO;
import com.thomaskyaw.identityservice.service.InternalIdentityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/internal")
public class InternalIdentityController {

    private final InternalIdentityService internalIdentityService;

    public InternalIdentityController(InternalIdentityService internalIdentityService) {
        this.internalIdentityService = internalIdentityService;
    }

    @GetMapping("/users/by-email")
    public ResponseEntity<UserStatusResponseDTO> getUserByEmail(@RequestParam("email") String email) {
        return internalIdentityService.getUserStatusByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/users/{userId}/tenants")
    public ResponseEntity<List<UUID>> getUserTenants(@PathVariable("userId") UUID userId) {
        return ResponseEntity.ok(internalIdentityService.getUserTenants(userId));
    }

    @GetMapping("/tenants/{tenantId}/users/{userId}/permissions")
    public ResponseEntity<UserPermissionsResponseDTO> getUserPermissions(@PathVariable("tenantId") UUID tenantId,
                                                                         @PathVariable("userId") UUID userId) {
        return ResponseEntity.ok(internalIdentityService.getUserPermissions(tenantId, userId));
    }
}
