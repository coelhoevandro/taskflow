package com.taskflow.notification.dto;

import com.taskflow.notification.model.NotificationType;

import java.time.LocalDateTime;
import java.util.UUID;

public record NotificationDto(
        String id,
        UUID userId,
        UUID taskId,
        String title,
        String message,
        NotificationType type,
        boolean read,
        LocalDateTime createdAt
) {}
