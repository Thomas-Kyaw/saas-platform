package com.thomaskyaw.identityservice.repository;

import com.thomaskyaw.identityservice.model.TenantMembership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TenantMembershipRepository extends JpaRepository<TenantMembership, TenantMembership.TenantMembershipId> {
    List<TenantMembership> findByUserId(UUID userId);
}
