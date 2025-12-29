package com.thomaskyaw.subscriptionservice.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "subscription_plans")
public class SubscriptionPlan {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", nullable = false, updatable = false, columnDefinition = "uuid")
    private UUID id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "tier", nullable = false, length = 20)
    private PlanTier tier;

    @Column(name = "monthly_price", precision = 10, scale = 2)
    private BigDecimal monthlyPrice;

    @Column(name = "annual_price", precision = 10, scale = 2)
    private BigDecimal annualPrice;

    @Column(name = "max_users")
    private Integer maxUsers;

    @Column(name = "max_api_calls_per_month")
    private Integer maxApiCallsPerMonth;

    @Column(name = "max_storage_gb")
    private Integer maxStorageGb;

    @Column(name = "features", columnDefinition = "TEXT")
    private String features; // JSON string

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PlanTier getTier() {
        return tier;
    }

    public void setTier(PlanTier tier) {
        this.tier = tier;
    }

    public BigDecimal getMonthlyPrice() {
        return monthlyPrice;
    }

    public void setMonthlyPrice(BigDecimal monthlyPrice) {
        this.monthlyPrice = monthlyPrice;
    }

    public BigDecimal getAnnualPrice() {
        return annualPrice;
    }

    public void setAnnualPrice(BigDecimal annualPrice) {
        this.annualPrice = annualPrice;
    }

    public Integer getMaxUsers() {
        return maxUsers;
    }

    public void setMaxUsers(Integer maxUsers) {
        this.maxUsers = maxUsers;
    }

    public Integer getMaxApiCallsPerMonth() {
        return maxApiCallsPerMonth;
    }

    public void setMaxApiCallsPerMonth(Integer maxApiCallsPerMonth) {
        this.maxApiCallsPerMonth = maxApiCallsPerMonth;
    }

    public Integer getMaxStorageGb() {
        return maxStorageGb;
    }

    public void setMaxStorageGb(Integer maxStorageGb) {
        this.maxStorageGb = maxStorageGb;
    }

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
