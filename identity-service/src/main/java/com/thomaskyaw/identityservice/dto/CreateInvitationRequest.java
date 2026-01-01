package com.thomaskyaw.identityservice.dto;

import java.util.UUID;

public record CreateInvitationRequest(
    String email,
    Long roleId
) {}
