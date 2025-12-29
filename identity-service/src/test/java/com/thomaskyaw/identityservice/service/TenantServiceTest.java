package com.thomaskyaw.identityservice.service;

import com.thomaskyaw.identityservice.dto.CreateTenantRequest;
import com.thomaskyaw.identityservice.dto.CreateUserRequest;
import com.thomaskyaw.identityservice.dto.TenantResponseDTO;
import com.thomaskyaw.identityservice.dto.UserResponseDTO;
import com.thomaskyaw.identityservice.mapper.TenantResponseMapper;
import com.thomaskyaw.identityservice.model.Tenant;
import com.thomaskyaw.identityservice.model.TenantMembership;
import com.thomaskyaw.identityservice.model.User;
import com.thomaskyaw.identityservice.model.UserStatus;
import com.thomaskyaw.identityservice.repository.TenantMembershipRepository;
import com.thomaskyaw.identityservice.repository.TenantRepository;
import com.thomaskyaw.identityservice.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
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
    private com.thomaskyaw.identityservice.repository.RoleRepository roleRepository;

    @InjectMocks
    private TenantService tenantService;

    @Test
    void createTenant_ShouldSaveTenant() {
        String tenantName = "Acme Corp";
        CreateTenantRequest request = new CreateTenantRequest(tenantName);

        Tenant savedTenant = new Tenant();
        savedTenant.setId(UUID.randomUUID());
        savedTenant.setName(tenantName);
        savedTenant.setStatus("ACTIVE");

        TenantResponseDTO expectedResponse = new TenantResponseDTO();
        expectedResponse.setName(tenantName);
        expectedResponse.setStatus("ACTIVE");

        when(tenantRepository.save(any(Tenant.class))).thenReturn(savedTenant);

        try (MockedStatic<TenantResponseMapper> mockedMapper = mockStatic(TenantResponseMapper.class)) {
            mockedMapper.when(() -> TenantResponseMapper.toDTO(any(Tenant.class))).thenReturn(expectedResponse);

            TenantResponseDTO result = tenantService.createTenant(request);

            assertNotNull(result);
            assertEquals(tenantName, result.getName());
            assertEquals("ACTIVE", result.getStatus());
            verify(tenantRepository).save(any(Tenant.class));
        }
    }

    @Test
    void createTenant_ShouldSetStatusToActive() {
        String tenantName = "Test Tenant";
        CreateTenantRequest request = new CreateTenantRequest(tenantName);
        ArgumentCaptor<Tenant> tenantCaptor = ArgumentCaptor.forClass(Tenant.class);

        TenantResponseDTO mockResponse = new TenantResponseDTO();

        when(tenantRepository.save(any(Tenant.class))).thenAnswer(invocation -> invocation.getArgument(0));

        try (MockedStatic<TenantResponseMapper> mockedMapper = mockStatic(TenantResponseMapper.class)) {
            mockedMapper.when(() -> TenantResponseMapper.toDTO(any(Tenant.class))).thenReturn(mockResponse);

            tenantService.createTenant(request);

            verify(tenantRepository).save(tenantCaptor.capture());
            Tenant capturedTenant = tenantCaptor.getValue();
            assertEquals("ACTIVE", capturedTenant.getStatus());
        }
    }

    @Test
    void createUserForTenant_ShouldCreateUserAndMembership() {
        UUID tenantId = UUID.randomUUID();
        String email = "admin@acme.com";
        String displayName = "Admin User";
        String password = "password123";
        CreateUserRequest request = new CreateUserRequest(email, displayName, password);

        Tenant tenant = new Tenant();
        tenant.setId(tenantId);
        tenant.setName("Acme Corp");

        User savedUser = new User();
        savedUser.setId(UUID.randomUUID());
        savedUser.setEmail(email);
        savedUser.setDisplayName(displayName);
        savedUser.setStatus(UserStatus.ACTIVE);

        when(tenantRepository.findById(tenantId)).thenReturn(Optional.of(tenant));
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(tenantMembershipRepository.save(any(TenantMembership.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserResponseDTO result = tenantService.createUserForTenant(tenantId, request);

        assertNotNull(result);
        assertEquals(email, result.email());
        assertEquals(displayName, result.displayName());
        assertEquals("ACTIVE", result.status());
        verify(userRepository).save(any(User.class));
        verify(tenantMembershipRepository).save(any(TenantMembership.class));
    }

    @Test
    void createUserForTenant_ShouldThrowException_WhenTenantNotFound() {
        UUID tenantId = UUID.randomUUID();
        CreateUserRequest request = new CreateUserRequest("admin@acme.com", "Admin User", "pass123");

        when(tenantRepository.findById(tenantId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            tenantService.createUserForTenant(tenantId, request);
        });

        assertTrue(exception.getMessage().contains("Tenant not found"));
        verify(userRepository, never()).save(any(User.class));
        verify(tenantMembershipRepository, never()).save(any(TenantMembership.class));
    }

    @Test
    void createUserForTenant_ShouldSetUserStatusToActive() {
        UUID tenantId = UUID.randomUUID();
        CreateUserRequest request = new CreateUserRequest("user@test.com", "Test User", "password");

        Tenant tenant = new Tenant();
        tenant.setId(tenantId);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        when(tenantRepository.findById(tenantId)).thenReturn(Optional.of(tenant));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(tenantMembershipRepository.save(any(TenantMembership.class))).thenAnswer(invocation -> invocation.getArgument(0));

        tenantService.createUserForTenant(tenantId, request);

        verify(userRepository).save(userCaptor.capture());
        User capturedUser = userCaptor.getValue();
        assertEquals(UserStatus.ACTIVE, capturedUser.getStatus());
    }

    @Test
    void createUserForTenant_ShouldCreateMembershipWithCorrectIds() {
        UUID tenantId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        CreateUserRequest request = new CreateUserRequest("member@test.com", "Member", "pass");

        Tenant tenant = new Tenant();
        tenant.setId(tenantId);

        User savedUser = new User();
        savedUser.setId(userId);
        savedUser.setEmail(request.email());
        savedUser.setStatus(UserStatus.ACTIVE);

        ArgumentCaptor<TenantMembership> membershipCaptor = ArgumentCaptor.forClass(TenantMembership.class);

        when(tenantRepository.findById(tenantId)).thenReturn(Optional.of(tenant));
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(tenantMembershipRepository.save(any(TenantMembership.class))).thenAnswer(invocation -> invocation.getArgument(0));

        tenantService.createUserForTenant(tenantId, request);

        verify(tenantMembershipRepository).save(membershipCaptor.capture());
        TenantMembership capturedMembership = membershipCaptor.getValue();
        assertEquals(userId, capturedMembership.getUserId());
        assertEquals(tenantId, capturedMembership.getTenantId());
        assertEquals("ACTIVE", capturedMembership.getStatus());
    }
}
