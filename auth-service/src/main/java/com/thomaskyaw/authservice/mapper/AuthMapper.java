package com.thomaskyaw.authservice.mapper;

import com.thomaskyaw.saas.identity.v1.TenantRole;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class AuthMapper {

    public List<Map<String, String>> toTenantRoleMaps(List<TenantRole> roles) {
        return roles.stream()
                .map(role -> Map.of(
                        "tenantId", role.getTenantId(),
                        "role", role.getRole()
                ))
                .toList();
    }
}
