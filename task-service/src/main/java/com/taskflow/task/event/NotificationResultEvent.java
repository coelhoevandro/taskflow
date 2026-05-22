package com.taskflow.task.event;

import java.util.UUID;

// Saga response: notification-service tells us if it succeeded or failed.
// On failure we roll back the assignment.
public record NotificationResultEvent(
        UUID taskId,
        UUID assigneeId,
        boolean success,
        String failureReason
) {}
