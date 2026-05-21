package com.taskflow.task.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AddCommentCommand(
        @NotNull UUID authorId,
        @NotBlank String content
) {}
