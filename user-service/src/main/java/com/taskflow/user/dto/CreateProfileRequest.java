package com.taskflow.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

// Called internally when a new user registers in auth-service
public record CreateProfileRequest(
        @NotNull UUID userId,
        @NotBlank String name,
        @NotBlank @Email String email
) {}
