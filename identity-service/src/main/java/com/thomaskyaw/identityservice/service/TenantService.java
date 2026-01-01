package com.thomaskyaw.identityservice.service;

import com.thomaskyaw.identityservice.dto.TenantResponseDTO;
import com.thomaskyaw.identityservice.mapper.TenantResponseMapper;
import com.thomaskyaw.identityservice.model.Tenant;
import com.thomaskyaw.identityservice.repository.TenantRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TenantService {
    private final TenantRepository tenantRepository;
    private final com.thomaskyaw.identityservice.repository.UserRepository userRepository;
    private final com.thomaskyaw.identityservice.repository.TenantMembershipRepository tenantMembershipRepository;
    private final com.thomaskyaw.identityservice.repository.RoleRepository roleRepository;
    private final com.thomaskyaw.identityservice.repository.UserRoleRepository userRoleRepository;
    private final com.thomaskyaw.identityservice.repository.InvitationRepository invitationRepository;

    public TenantService(TenantRepository tenantRepository,
                         com.thomaskyaw.identityservice.repository.UserRepository userRepository,
                         com.thomaskyaw.identityservice.repository.TenantMembershipRepository tenantMembershipRepository,
                         com.thomaskyaw.identityservice.repository.RoleRepository roleRepository,
                         com.thomaskyaw.identityservice.repository.UserRoleRepository userRoleRepository,
                         com.thomaskyaw.identityservice.repository.InvitationRepository invitationRepository) {
        this.tenantRepository = tenantRepository;
        this.userRepository = userRepository;
        this.tenantMembershipRepository = tenantMembershipRepository;
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
        this.invitationRepository = invitationRepository;
    }

    public List<TenantResponseDTO> getTenants() {
        List<Tenant> tenants = tenantRepository.findAll();
        return tenants.stream().map(TenantResponseMapper::toDTO).toList();
    }

    public TenantResponseDTO createTenant(com.thomaskyaw.identityservice.dto.CreateTenantRequest request) {
        Tenant tenant = new Tenant();
        tenant.setName(request.name());
        tenant.setStatus("ACTIVE");
        tenant = tenantRepository.save(tenant);
        return TenantResponseMapper.toDTO(tenant);
    }

    @org.springframework.transaction.annotation.Transactional
    public com.thomaskyaw.identityservice.dto.UserResponseDTO createUserForTenant(java.util.UUID tenantId, com.thomaskyaw.identityservice.dto.CreateUserRequest request) {
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new RuntimeException("Tenant not found"));

        com.thomaskyaw.identityservice.model.User user = new com.thomaskyaw.identityservice.model.User();
        user.setEmail(request.email());
        user.setDisplayName(request.displayName());
        user.setStatus(com.thomaskyaw.identityservice.model.UserStatus.ACTIVE);
        user = userRepository.save(user);

        com.thomaskyaw.identityservice.model.TenantMembership membership = new com.thomaskyaw.identityservice.model.TenantMembership();
        membership.setTenantId(tenant.getId());
        membership.setUserId(user.getId());
        membership.setStatus("ACTIVE");
        tenantMembershipRepository.save(membership);

        com.thomaskyaw.identityservice.model.Role ownerRole = roleRepository.findByTenantIdAndName(tenantId, "owner")
                .orElseGet(() -> {
                    com.thomaskyaw.identityservice.model.Role role = new com.thomaskyaw.identityservice.model.Role();
                    role.setTenantId(tenantId);
                    role.setName("owner");
                    return roleRepository.save(role);
                });

        com.thomaskyaw.identityservice.model.UserRole userRole = new com.thomaskyaw.identityservice.model.UserRole();
        userRole.setTenantId(tenantId);
        userRole.setUserId(user.getId());
        userRole.setRoleId(ownerRole.getId());
        userRoleRepository.save(userRole);

        return new com.thomaskyaw.identityservice.dto.UserResponseDTO(user.getId(), user.getEmail(), user.getDisplayName(), user.getStatus().name());
    }

    public void inviteUser(java.util.UUID tenantId, com.thomaskyaw.identityservice.dto.CreateInvitationRequest request) {
        // Check if user already exists
        if (userRepository.findByEmail(request.email()).isPresent()) {
            // Logic for existing user (e.g., add membership directly or send different email)
            // For now, let's assume we just create an invitation anyway
        }

        com.thomaskyaw.identityservice.model.Invitation invitation = new com.thomaskyaw.identityservice.model.Invitation();
        invitation.setEmail(request.email());
        invitation.setTenantId(tenantId);
        invitation.setRoleId(request.roleId());
        invitation.setStatus("PENDING");
        invitation.setToken(java.util.UUID.randomUUID().toString());
        invitation.setExpiresAt(java.time.OffsetDateTime.now().plusDays(7));
        invitationRepository.save(invitation);

        // Send email (mock)
        System.out.println("Sending invitation email to " + request.email() + " with token " + invitation.getToken());
    }

    @org.springframework.transaction.annotation.Transactional
    public void acceptInvitation(com.thomaskyaw.identityservice.dto.AcceptInvitationRequest request) {
        com.thomaskyaw.identityservice.model.Invitation invitation = invitationRepository.findByToken(request.token())
                .orElseThrow(() -> new IllegalArgumentException("Invalid token"));

        if (invitation.getExpiresAt().isBefore(java.time.OffsetDateTime.now())) {
            invitation.setStatus("EXPIRED");
            invitationRepository.save(invitation);
            throw new IllegalStateException("Invitation expired");
        }

        if (!"PENDING".equals(invitation.getStatus())) {
            throw new IllegalStateException("Invitation already accepted or invalid");
        }

        // Create user if not exists
        com.thomaskyaw.identityservice.model.User user = userRepository.findByEmail(invitation.getEmail())
                .orElseGet(() -> {
                    com.thomaskyaw.identityservice.model.User newUser = new com.thomaskyaw.identityservice.model.User();
                    newUser.setEmail(invitation.getEmail());
                    newUser.setDisplayName(request.displayName());
                    // Hash password here (omitted for brevity)
                    newUser.setStatus(com.thomaskyaw.identityservice.model.UserStatus.ACTIVE);
                    return userRepository.save(newUser);
                });

        // Add membership
        com.thomaskyaw.identityservice.model.TenantMembership membership = new com.thomaskyaw.identityservice.model.TenantMembership();
        membership.setTenantId(invitation.getTenantId());
        membership.setUserId(user.getId());
        membership.setStatus("ACTIVE");
        tenantMembershipRepository.save(membership);

        // Assign role
        com.thomaskyaw.identityservice.model.UserRole userRole = new com.thomaskyaw.identityservice.model.UserRole();
        userRole.setTenantId(invitation.getTenantId());
        userRole.setUserId(user.getId());
        userRole.setRoleId(invitation.getRoleId());
        userRoleRepository.save(userRole);

        invitation.setStatus("ACCEPTED");
        invitationRepository.save(invitation);
    }
}
