package com.thomaskyaw.authservice.client.dto;

public record CreateUserRequest(
        String email,
        String displayName
) {
}
