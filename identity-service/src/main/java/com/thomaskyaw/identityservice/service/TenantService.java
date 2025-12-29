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

    public TenantService(TenantRepository tenantRepository,
                         com.thomaskyaw.identityservice.repository.UserRepository userRepository,
                         com.thomaskyaw.identityservice.repository.TenantMembershipRepository tenantMembershipRepository,
                         com.thomaskyaw.identityservice.repository.RoleRepository roleRepository) {
        this.tenantRepository = tenantRepository;
        this.userRepository = userRepository;
        this.tenantMembershipRepository = tenantMembershipRepository;
        this.roleRepository = roleRepository;
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
        // Assign default role (e.g., ADMIN or MEMBER) - assuming 'ADMIN' exists or creating it
        // For now, let's just create the membership. Role assignment might need Role lookup.
        // membership.setRoles(...);
        tenantMembershipRepository.save(membership);

        // TODO: Handle password creation (call Auth Service or publish event)

        return new com.thomaskyaw.identityservice.dto.UserResponseDTO(user.getId(), user.getEmail(), user.getDisplayName(), user.getStatus().name());
    }
}
