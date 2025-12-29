package com.thomaskyaw.identityservice.dto;

import java.util.UUID;

public record UserResponseDTO(
        UUID id,
        String email,
        String displayName,
        String status
) {
}
