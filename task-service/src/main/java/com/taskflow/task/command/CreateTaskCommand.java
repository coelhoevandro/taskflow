package com.taskflow.task.command;

import com.taskflow.task.model.TaskPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

public record CreateTaskCommand(
        @NotBlank @Size(max = 500) String title,
        String description,
        TaskPriority priority,
        LocalDate dueDate,
        @NotNull UUID reporterId
) {}
