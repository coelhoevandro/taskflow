package com.taskflow.task.command.handler;

import com.taskflow.task.command.AddCommentCommand;
import com.taskflow.task.dto.CommentDto;
import com.taskflow.task.model.Task;
import com.taskflow.task.model.TaskComment;
import com.taskflow.task.repository.TaskCommentRepository;
import com.taskflow.task.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AddCommentHandler {

    private final TaskRepository taskRepository;
    private final TaskCommentRepository commentRepository;

    @Transactional
    public CommentDto handle(UUID taskId, AddCommentCommand command) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NoSuchElementException("Task not found: " + taskId));

        TaskComment comment = TaskComment.builder()
                .task(task)
                .authorId(command.authorId())
                .content(command.content())
                .build();

        TaskComment saved = commentRepository.save(comment);
        return new CommentDto(saved.getId(), saved.getAuthorId(), saved.getContent(), saved.getCreatedAt());
    }
}
