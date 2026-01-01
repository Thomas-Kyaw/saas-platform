package com.thomaskyaw.identityservice.repository;

import com.thomaskyaw.identityservice.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByTenantIdAndName(UUID tenantId, String name);
}
