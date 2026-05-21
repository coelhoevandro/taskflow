package com.taskflow.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateProfileRequest(
        @NotBlank @Size(max = 255) String name,
        @Size(max = 512) String avatarUrl,
        @Size(max = 255) String jobTitle,
        @Size(max = 255) String department
) {}
