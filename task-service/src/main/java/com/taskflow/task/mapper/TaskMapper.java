package com.taskflow.task.mapper;

import com.taskflow.task.dto.CommentDto;
import com.taskflow.task.dto.TaskDto;
import com.taskflow.task.dto.TaskSummaryDto;
import com.taskflow.task.model.Task;
import com.taskflow.task.model.TaskComment;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TaskMapper {

    public TaskDto toDto(Task task, List<TaskComment> comments) {
        return new TaskDto(
                task.getId(), task.getTitle(), task.getDescription(),
                task.getStatus(), task.getPriority(),
                task.getAssigneeId(), task.getReporterId(),
                task.getDueDate(), task.isOverdue(),
                task.getAssignmentStatus(),
                comments.stream().map(this::toCommentDto).toList(),
                task.getCreatedAt(), task.getUpdatedAt()
        );
    }

    public TaskSummaryDto toSummaryDto(Task task) {
        return new TaskSummaryDto(
                task.getId(), task.getTitle(),
                task.getStatus(), task.getPriority(),
                task.getAssigneeId(), task.getReporterId(),
                task.getDueDate(), task.isOverdue(),
                task.getCreatedAt()
        );
    }

    public CommentDto toCommentDto(TaskComment c) {
        return new CommentDto(c.getId(), c.getAuthorId(), c.getContent(), c.getCreatedAt());
    }
}
