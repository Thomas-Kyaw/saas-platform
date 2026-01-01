package com.thomaskyaw.identityservice.service;

import com.thomaskyaw.identityservice.dto.AcceptInvitationRequest;
import com.thomaskyaw.identityservice.dto.CreateInvitationRequest;
import com.thomaskyaw.identityservice.model.*;
import com.thomaskyaw.identityservice.repository.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
public class InvitationService {

    private final InvitationRepository invitationRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TenantRepository tenantRepository;
    private final TenantMembershipRepository tenantMembershipRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    public InvitationService(InvitationRepository invitationRepository,
                             UserRepository userRepository,
                             RoleRepository roleRepository,
                             TenantRepository tenantRepository,
                             TenantMembershipRepository tenantMembershipRepository,
                             UserRoleRepository userRoleRepository,
                             PasswordEncoder passwordEncoder) {
        this.invitationRepository = invitationRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.tenantRepository = tenantRepository;
        this.tenantMembershipRepository = tenantMembershipRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Invitation createInvitation(CreateInvitationRequest request, UUID tenantId) {
        // Verify tenant exists
        if (!tenantRepository.existsById(tenantId)) {
            throw new IllegalArgumentException("Tenant not found");
        }

        // Verify role exists
        if (!roleRepository.existsById(request.roleId())) {
            throw new IllegalArgumentException("Role not found");
        }

        Invitation invitation = new Invitation();
        invitation.setEmail(request.email());
        invitation.setTenantId(tenantId);
        invitation.setRoleId(request.roleId());
        invitation.setStatus("PENDING");
        invitation.setToken(UUID.randomUUID().toString());
        invitation.setExpiresAt(OffsetDateTime.now().plusDays(7)); // 7 days expiration

        return invitationRepository.save(invitation);
    }

    @Transactional
    public void acceptInvitation(AcceptInvitationRequest request) {
        Invitation invitation = invitationRepository.findByToken(request.token())
                .orElseThrow(() -> new IllegalArgumentException("Invalid token"));

        if (invitation.getExpiresAt().isBefore(OffsetDateTime.now())) {
            invitation.setStatus("EXPIRED");
            invitationRepository.save(invitation);
            throw new IllegalArgumentException("Invitation expired");
        }

        if (!"PENDING".equals(invitation.getStatus())) {
            throw new IllegalArgumentException("Invitation already accepted or invalid");
        }

        // Check if user already exists
        User user = userRepository.findByEmail(invitation.getEmail()).orElse(null);

        if (user == null) {
            // Create new user
            user = new User();
            user.setEmail(invitation.getEmail());
            user.setDisplayName(request.displayName());
            user.setPasswordHash(passwordEncoder.encode(request.password()));
            user.setStatus(UserStatus.ACTIVE);
            user = userRepository.save(user);
        }

        // Create tenant membership
        TenantMembership membership = new TenantMembership();
        membership.setUserId(user.getId());
        membership.setTenantId(invitation.getTenantId());
        tenantMembershipRepository.save(membership);

        // Assign role
        UserRole userRole = new UserRole();
        userRole.setUserId(user.getId());
        userRole.setTenantId(invitation.getTenantId());
        userRole.setRoleId(invitation.getRoleId());
        userRoleRepository.save(userRole);

        invitation.setStatus("ACCEPTED");
        invitationRepository.save(invitation);
    }
}
