package com.thomaskyaw.identityservice.service;

import com.thomaskyaw.identityservice.dto.AcceptInvitationRequest;
import com.thomaskyaw.identityservice.dto.CreateInvitationRequest;
import com.thomaskyaw.identityservice.model.*;
import com.thomaskyaw.identityservice.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InvitationServiceTest {

    @Mock
    private InvitationRepository invitationRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private TenantRepository tenantRepository;
    @Mock
    private TenantMembershipRepository tenantMembershipRepository;
    @Mock
    private UserRoleRepository userRoleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private InvitationService invitationService;

    private UUID tenantId;
    private Long roleId;
    private String email;

    @BeforeEach
    void setUp() {
        tenantId = UUID.randomUUID();
        roleId = 1L;
        email = "test@example.com";
    }

    @Test
    void createInvitation_Success() {
        CreateInvitationRequest request = new CreateInvitationRequest(email, roleId);

        when(tenantRepository.existsById(tenantId)).thenReturn(true);
        when(roleRepository.existsById(roleId)).thenReturn(true);
        when(invitationRepository.save(any(Invitation.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Invitation result = invitationService.createInvitation(request, tenantId);

        assertNotNull(result);
        assertEquals(email, result.getEmail());
        assertEquals(tenantId, result.getTenantId());
        assertEquals(roleId, result.getRoleId());
        assertEquals("PENDING", result.getStatus());
        assertNotNull(result.getToken());
        assertNotNull(result.getExpiresAt());
    }

    @Test
    void acceptInvitation_Success_NewUser() {
        String token = UUID.randomUUID().toString();
        AcceptInvitationRequest request = new AcceptInvitationRequest(token, "Test User", "password");
        Invitation invitation = new Invitation();
        invitation.setToken(token);
        invitation.setEmail(email);
        invitation.setTenantId(tenantId);
        invitation.setRoleId(roleId);
        invitation.setStatus("PENDING");
        invitation.setExpiresAt(OffsetDateTime.now().plusDays(1));

        when(invitationRepository.findByToken(token)).thenReturn(Optional.of(invitation));
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(UUID.randomUUID());
            return user;
        });

        invitationService.acceptInvitation(request);

        verify(userRepository).save(any(User.class));
        verify(tenantMembershipRepository).save(any(TenantMembership.class));
        verify(userRoleRepository).save(any(UserRole.class));
        assertEquals("ACCEPTED", invitation.getStatus());
    }
}
