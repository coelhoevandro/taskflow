package com.taskflow.task.dto;

import com.taskflow.task.model.TaskPriority;
import com.taskflow.task.model.TaskStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

// Lighter projection for list views — avoid loading comments on every row
public record TaskSummaryDto(
        UUID id,
        String title,
        TaskStatus status,
        TaskPriority priority,
        UUID assigneeId,
        UUID reporterId,
        LocalDate dueDate,
        boolean overdue,
        LocalDateTime createdAt
) {}
