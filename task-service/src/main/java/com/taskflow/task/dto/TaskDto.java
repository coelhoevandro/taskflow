package com.taskflow.task.dto;

import com.taskflow.task.model.AssignmentStatus;
import com.taskflow.task.model.TaskPriority;
import com.taskflow.task.model.TaskStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record TaskDto(
        UUID id,
        String title,
        String description,
        TaskStatus status,
        TaskPriority priority,
        UUID assigneeId,
        UUID reporterId,
        LocalDate dueDate,
        boolean overdue,
        AssignmentStatus assignmentStatus,
        List<CommentDto> comments,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
