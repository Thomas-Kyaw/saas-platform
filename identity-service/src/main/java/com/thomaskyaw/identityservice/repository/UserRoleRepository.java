package com.thomaskyaw.identityservice.repository;

import com.thomaskyaw.identityservice.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UserRole.UserRoleId> {

    List<UserRole> findByUserId(UUID userId);

    @Query(value = "SELECT r.name FROM user_roles ur JOIN roles r ON ur.role_id = r.id WHERE ur.tenant_id = :tenantId AND ur.user_id = :userId", nativeQuery = true)
    List<String> findRoleNamesByTenantIdAndUserId(@Param("tenantId") UUID tenantId, @Param("userId") UUID userId);

    @Query(value = "SELECT DISTINCT p.key FROM permissions p " +
           "JOIN role_permissions rp ON p.id = rp.permission_id " +
           "JOIN user_roles ur ON rp.role_id = ur.role_id " +
           "WHERE ur.tenant_id = :tenantId AND ur.user_id = :userId", nativeQuery = true)
    List<String> findPermissionKeysByTenantIdAndUserId(@Param("tenantId") UUID tenantId, @Param("userId") UUID userId);
}
