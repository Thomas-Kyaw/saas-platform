package com.thomaskyaw.usageservice.repository;

import com.thomaskyaw.usageservice.model.UsageRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UsageRepository extends JpaRepository<UsageRecord, UUID> {
}
