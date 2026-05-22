package com.taskflow.task.dto;

public record DashboardStatsDto(
        long totalTasks,
        long todo,
        long inProgress,
        long inReview,
        long done,
        long overdue
) {}
