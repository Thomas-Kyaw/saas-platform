package com.thomaskyaw.common.web;

import com.thomaskyaw.common.context.UserContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class TenantInterceptor extends OncePerRequestFilter {

    public static final String X_TENANT_ID = "X-Tenant-Id";
    public static final String X_USER_ID = "X-User-Id";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String tenantId = request.getHeader(X_TENANT_ID);
        String userId = request.getHeader(X_USER_ID);

        if (tenantId != null && !tenantId.isBlank()) {
            UserContext.setTenantId(tenantId);
        }
        if (userId != null && !userId.isBlank()) {
            UserContext.setUserId(userId);
        }
        try {
            filterChain.doFilter(request, response);
        } finally {
            // We don't clear here because UserContext might be used by other filters or the controller.
            // Typically, context is cleared at the end of the request lifecycle or by a specific filter/interceptor that started it.
            // However, since this filter sets it, it should probably clear it if it's the one that "owns" the context for this request.
            // But UserContext is ThreadLocal, so we MUST clear it to avoid leakage in thread pools.
            UserContext.clear();
        }
    }
}
