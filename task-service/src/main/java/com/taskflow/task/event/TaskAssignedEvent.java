package com.taskflow.task.event;

import java.util.UUID;

// Kafka event emitted when a task is assigned to a user.
// Notification service listens for this to create the notification.
public record TaskAssignedEvent(
        UUID taskId,
        String taskTitle,
        UUID assigneeId,
        UUID reporterId,
        String reporterName   // included so notification service doesn't need to look it up
) {}
