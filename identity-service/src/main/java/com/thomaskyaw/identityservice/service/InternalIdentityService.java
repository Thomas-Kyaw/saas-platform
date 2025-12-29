package com.thomaskyaw.identityservice.service;

import com.thomaskyaw.identityservice.dto.UserPermissionsResponseDTO;
import com.thomaskyaw.identityservice.dto.UserStatusResponseDTO;
import com.thomaskyaw.identityservice.model.TenantMembership;
import com.thomaskyaw.identityservice.repository.TenantMembershipRepository;
import com.thomaskyaw.identityservice.repository.UserRepository;
import com.thomaskyaw.identityservice.repository.UserRoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class InternalIdentityService {

    private final UserRepository userRepository;
    private final TenantMembershipRepository tenantMembershipRepository;
    private final UserRoleRepository userRoleRepository;

    public InternalIdentityService(UserRepository userRepository,
                                   TenantMembershipRepository tenantMembershipRepository,
                                   UserRoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.tenantMembershipRepository = tenantMembershipRepository;
        this.userRoleRepository = userRoleRepository;
    }

    public Optional<UserStatusResponseDTO> getUserStatusByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(user -> new UserStatusResponseDTO(user.getId(), user.getStatus().name()));
    }

    public List<UUID> getUserTenants(UUID userId) {
        return tenantMembershipRepository.findByUserId(userId).stream()
                .map(TenantMembership::getTenantId)
                .collect(Collectors.toList());
    }

    public UserPermissionsResponseDTO getUserPermissions(UUID tenantId, UUID userId) {
        List<String> roles = userRoleRepository.findRoleNamesByTenantIdAndUserId(tenantId, userId);
        List<String> permissions = userRoleRepository.findPermissionKeysByTenantIdAndUserId(tenantId, userId);
        return new UserPermissionsResponseDTO(roles, permissions);
    }
}
