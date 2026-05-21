package com.taskflow.auth.dto;

import java.util.UUID;

// Used by the gateway to validate tokens without exposing JWT internals to other services
public record ValidateTokenResponse(
        boolean valid,
        UUID userId,
        String email,
        String role
) {}
