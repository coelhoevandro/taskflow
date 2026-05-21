package com.taskflow.task.command;

import com.taskflow.task.model.TaskPriority;
import com.taskflow.task.model.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record UpdateTaskCommand(
        @NotBlank @Size(max = 500) String title,
        String description,
        TaskStatus status,
        TaskPriority priority,
        LocalDate dueDate
) {}
