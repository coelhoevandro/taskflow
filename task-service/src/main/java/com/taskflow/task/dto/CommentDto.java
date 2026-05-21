package com.taskflow.task.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record CommentDto(UUID id, UUID authorId, String content, LocalDateTime createdAt) {}
