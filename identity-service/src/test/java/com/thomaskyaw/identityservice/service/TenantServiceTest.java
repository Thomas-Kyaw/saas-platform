package com.thomaskyaw.identityservice.service;

import com.thomaskyaw.identityservice.dto.CreateInvitationRequest;
import com.thomaskyaw.identityservice.model.Invitation;
import com.thomaskyaw.identityservice.repository.InvitationRepository;
import com.thomaskyaw.identityservice.repository.RoleRepository;
import com.thomaskyaw.identityservice.repository.TenantMembershipRepository;
import com.thomaskyaw.identityservice.repository.TenantRepository;
import com.thomaskyaw.identityservice.repository.UserRepository;
import com.thomaskyaw.identityservice.repository.UserRoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

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
    @Mock
    private UserRoleRepository userRoleRepository;
    @Mock
    private InvitationRepository invitationRepository;

    private TenantService tenantService;

    @BeforeEach
    void setUp() {
        tenantService = new TenantService(
            tenantRepository,
            userRepository,
            tenantMembershipRepository,
            roleRepository,
            userRoleRepository,
            invitationRepository
        );
    }

    @Test
    void inviteUser_Success() {
        UUID tenantId = UUID.randomUUID();
        CreateInvitationRequest request = new CreateInvitationRequest("test@example.com", 1L);

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        tenantService.inviteUser(tenantId, request);

        verify(invitationRepository).save(argThat(i ->
            i.getEmail().equals("test@example.com") &&
            i.getTenantId().equals(tenantId) &&
            i.getRoleId().equals(1L) &&
            "PENDING".equals(i.getStatus())
        ));
    }
}
