package com.taskflow.task.command;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AssignTaskCommand(@NotNull UUID assigneeId) {}
