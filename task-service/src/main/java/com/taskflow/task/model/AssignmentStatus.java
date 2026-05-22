package com.taskflow.task.model;

// Tracks whether the saga for task assignment completed successfully.
// PENDING_NOTIFICATION means we emitted the Kafka event but haven't heard back yet.
public enum AssignmentStatus {
    UNASSIGNED,
    PENDING_NOTIFICATION,
    CONFIRMED
}
