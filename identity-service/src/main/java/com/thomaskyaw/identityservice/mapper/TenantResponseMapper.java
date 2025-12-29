package com.thomaskyaw.identityservice.mapper;

import com.thomaskyaw.identityservice.dto.TenantResponseDTO;
import com.thomaskyaw.identityservice.model.Tenant;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

public class TenantResponseMapper {
    public static TenantResponseDTO toDTO(Tenant tenant) {
        TenantResponseDTO tenantDTO = new TenantResponseDTO();
        tenantDTO.setId(tenant.getId().toString());
        tenantDTO.setName(tenant.getName());
        tenantDTO.setStatus(tenant.getStatus());
        tenantDTO.setCreatedAt(tenant.getCreatedAt().toString());

        return tenantDTO;
    }

    public static Tenant toModel(TenantResponseDTO tenantDTO) {
        Tenant tenant = new Tenant();
        tenant.setName(tenantDTO.getName());
        tenant.setStatus(tenant.getStatus());
        tenant.setCreatedAt(OffsetDateTime.from(LocalDateTime.parse(tenantDTO.getCreatedAt())));
        return tenant;
    }
}
