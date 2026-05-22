package com.taskflow.user.dto;

import java.util.UUID;

public record UserProfileDto(
        UUID id,
        String name,
        String email,
        String avatarUrl,
        String jobTitle,
        String department,
        boolean active
) {}
