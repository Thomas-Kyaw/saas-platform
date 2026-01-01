package com.thomaskyaw.identityservice.controller;

import com.thomaskyaw.identityservice.dto.AcceptInvitationRequest;
import com.thomaskyaw.identityservice.dto.CreateInvitationRequest;
import com.thomaskyaw.identityservice.model.Invitation;
import com.thomaskyaw.identityservice.service.InvitationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/invitations")
public class InvitationController {

    private final InvitationService invitationService;

    public InvitationController(InvitationService invitationService) {
        this.invitationService = invitationService;
    }

    @PostMapping
    public ResponseEntity<Invitation> createInvitation(@RequestBody CreateInvitationRequest request,
                                                       @RequestHeader("X-Tenant-Id") UUID tenantId) {
        // In a real app, we would verify if the authenticated user has permission to invite to this tenant.
        // For now, we assume the gateway or security filter handles basic auth,
        // and we trust the X-Tenant-Id header or extract it from the security context.
        // Since I don't see the security context setup here, I'll use the header for now.

        Invitation invitation = invitationService.createInvitation(request, tenantId);
        return ResponseEntity.ok(invitation);
    }

    @PostMapping("/accept")
    public ResponseEntity<Void> acceptInvitation(@RequestBody AcceptInvitationRequest request) {
        invitationService.acceptInvitation(request);
        return ResponseEntity.ok().build();
    }
}
