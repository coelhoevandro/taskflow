package com.taskflow.task.command.handler;

import com.taskflow.task.command.UpdateTaskCommand;
import com.taskflow.task.dto.TaskDto;
import com.taskflow.task.mapper.TaskMapper;
import com.taskflow.task.model.Task;
import com.taskflow.task.repository.TaskCommentRepository;
import com.taskflow.task.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UpdateTaskHandler {

    private final TaskRepository taskRepository;
    private final TaskCommentRepository commentRepository;
    private final TaskMapper mapper;

    @Transactional
    public TaskDto handle(UUID taskId, UpdateTaskCommand command) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NoSuchElementException("Task not found: " + taskId));

        task.setTitle(command.title());
        task.setDescription(command.description());
        task.setPriority(command.priority());
        task.setDueDate(command.dueDate());

        if (command.status() != null) {
            task.setStatus(command.status());
        }

        return mapper.toDto(taskRepository.save(task),
                commentRepository.findByTaskIdOrderByCreatedAtAsc(taskId));
    }
}
