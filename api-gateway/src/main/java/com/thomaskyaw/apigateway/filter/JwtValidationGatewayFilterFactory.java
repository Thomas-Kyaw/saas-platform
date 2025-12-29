package com.thomaskyaw.apigateway.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Component
public class JwtValidationGatewayFilterFactory extends AbstractGatewayFilterFactory<JwtValidationGatewayFilterFactory.Config> {

    private final SecretKey secretKey;
    private final org.springframework.data.redis.core.ReactiveStringRedisTemplate redisTemplate;

    public JwtValidationGatewayFilterFactory(@Value("${jwt.secret}") String secret, org.springframework.data.redis.core.ReactiveStringRedisTemplate redisTemplate) {
        super(Config.class);
        byte[] keyBytes = Base64.getDecoder().decode(secret.getBytes(StandardCharsets.UTF_8));
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
        this.redisTemplate = redisTemplate;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String token = extractToken(exchange.getRequest());

            if (token == null) {
                return onError(exchange, "Missing authorization header", HttpStatus.UNAUTHORIZED);
            }

            return redisTemplate.hasKey("blacklist:" + token)
                    .flatMap(isBlacklisted -> {
                        if (Boolean.TRUE.equals(isBlacklisted)) {
                            return onError(exchange, "Token is blacklisted", HttpStatus.UNAUTHORIZED);
                        }

                        try {
                            Claims claims = Jwts.parser()
                                    .verifyWith(secretKey)
                                    .build()
                                    .parseSignedClaims(token)
                                    .getPayload();

                            String userId = claims.getSubject();
                            String tenantId = exchange.getRequest().getHeaders().getFirst("X-Tenant-Id");

                            ServerHttpRequest.Builder requestBuilder = exchange.getRequest().mutate()
                                    .header("X-User-Id", userId);

                            if (tenantId != null) {
                                requestBuilder.header("X-Tenant-Id", tenantId);
                            }

                            return chain.filter(exchange.mutate().request(requestBuilder.build()).build());

                        } catch (Exception e) {
                            return onError(exchange, "Invalid token", HttpStatus.UNAUTHORIZED);
                        }
                    });
        };
    }

    private String extractToken(ServerHttpRequest request) {
        String bearerToken = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        exchange.getResponse().setStatusCode(httpStatus);
        return exchange.getResponse().setComplete();
    }

    public static class Config {
        // Put configuration properties here
    }
}
