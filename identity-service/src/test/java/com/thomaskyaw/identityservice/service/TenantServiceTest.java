package com.thomaskyaw.identityservice.service;

import com.thomaskyaw.identityservice.dto.CreateTenantRequest;
import com.thomaskyaw.identityservice.dto.CreateUserRequest;
import com.thomaskyaw.identityservice.dto.TenantResponseDTO;
import com.thomaskyaw.identityservice.dto.UserResponseDTO;
import com.thomaskyaw.identityservice.model.Tenant;
import com.thomaskyaw.identityservice.model.TenantMembership;
import com.thomaskyaw.identityservice.model.User;
import com.thomaskyaw.identityservice.repository.RoleRepository;
import com.thomaskyaw.identityservice.repository.TenantMembershipRepository;
import com.thomaskyaw.identityservice.repository.TenantRepository;
import com.thomaskyaw.identityservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TenantServiceTest {

    @Mock
    private TenantRepository tenantRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TenantMembershipRepository tenantMembershipRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private TenantService tenantService;

    @Test
    void createTenant_ShouldReturnTenantResponse() {
        CreateTenantRequest request = new CreateTenantRequest("Acme Corp");
        Tenant tenant = new Tenant();
        tenant.setId(UUID.randomUUID());
        tenant.setName("Acme Corp");
        tenant.setStatus("ACTIVE");
        tenant.setCreatedAt(java.time.OffsetDateTime.now());

        when(tenantRepository.save(any(Tenant.class))).thenReturn(tenant);

        TenantResponseDTO response = tenantService.createTenant(request);

        assertNotNull(response);
        assertEquals("Acme Corp", response.getName());
        verify(tenantRepository).save(any(Tenant.class));
    }

    @Test
    void createUserForTenant_ShouldReturnUserResponse() {
        UUID tenantId = UUID.randomUUID();
        CreateUserRequest request = new CreateUserRequest("admin@acme.com", "Admin", "password");

        Tenant tenant = new Tenant();
        tenant.setId(tenantId);

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail("admin@acme.com");
        user.setDisplayName("Admin");
        user.setStatus(com.thomaskyaw.identityservice.model.UserStatus.ACTIVE);

        when(tenantRepository.findById(tenantId)).thenReturn(Optional.of(tenant));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(tenantMembershipRepository.save(any(TenantMembership.class))).thenReturn(new TenantMembership());

        UserResponseDTO response = tenantService.createUserForTenant(tenantId, request);

        assertNotNull(response);
        assertEquals("admin@acme.com", response.email());
        verify(tenantRepository).findById(tenantId);
        verify(userRepository).save(any(User.class));
        verify(tenantMembershipRepository).save(any(TenantMembership.class));
    }
}
