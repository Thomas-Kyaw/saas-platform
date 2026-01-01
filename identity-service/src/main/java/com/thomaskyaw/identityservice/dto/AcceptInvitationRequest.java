package com.thomaskyaw.identityservice.dto;

public record AcceptInvitationRequest(
    String token,
    String displayName,
    String password // Assuming we set password on acceptance if user doesn't exist
) {}
