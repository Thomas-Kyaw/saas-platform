package com.thomaskyaw.identityservice.service;

import com.thomaskyaw.identityservice.repository.RoleRepository;
import com.thomaskyaw.identityservice.repository.UserRepository;
import com.thomaskyaw.identityservice.repository.UserRoleRepository;
import com.thomaskyaw.saas.identity.v1.GetUserByEmailRequest;
import com.thomaskyaw.saas.identity.v1.GetUserResponse;
import com.thomaskyaw.saas.identity.v1.IdentityServiceGrpc;
import com.thomaskyaw.saas.identity.v1.TenantRole;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IdentityGrpcService extends IdentityServiceGrpc.IdentityServiceImplBase {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;

    public IdentityGrpcService(UserRepository userRepository,
                               UserRoleRepository userRoleRepository,
                               RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public void getUserByEmail(GetUserByEmailRequest request, StreamObserver<GetUserResponse> responseObserver) {
        userRepository.findByEmail(request.getEmail())
                .ifPresentOrElse(
                    user -> {
                            List<TenantRole> tenantRoles = userRoleRepository.findByUserId(user.getId()).stream()
                                    .map(userRole -> {
                                        String roleName = roleRepository.findById(userRole.getRoleId())
                                                .map(com.thomaskyaw.identityservice.model.Role::getName)
                                                .orElse("UNKNOWN");
                                        return TenantRole.newBuilder()
                                                .setTenantId(userRole.getTenantId().toString())
                                                .setRole(roleName)
                                                .build();
                                    })
                                    .toList();

                            GetUserResponse response = GetUserResponse.newBuilder()
                                    .setUserId(user.getId().toString())
                                    .setEmail(user.getEmail())
                                    .setActive(com.thomaskyaw.identityservice.model.UserStatus.ACTIVE.equals(user.getStatus()))
                                    .addAllRoles(tenantRoles)
                                    .build();
                            responseObserver.onNext(response);
                            responseObserver.onCompleted();
                        },
                        () -> responseObserver.onError(io.grpc.Status.NOT_FOUND
                                .withDescription("User not found with email: " + request.getEmail())
                                .asRuntimeException())
                );
    }
}
