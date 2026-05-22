package com.taskflow.task.command.handler;

import com.taskflow.task.command.CreateTaskCommand;
import com.taskflow.task.dto.TaskDto;
import com.taskflow.task.mapper.TaskMapper;
import com.taskflow.task.model.Task;
import com.taskflow.task.model.TaskPriority;
import com.taskflow.task.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CreateTaskHandler {

    private final TaskRepository taskRepository;
    private final TaskMapper mapper;

    @Transactional
    public TaskDto handle(CreateTaskCommand command) {
        Task task = Task.builder()
                .title(command.title())
                .description(command.description())
                .priority(command.priority() != null ? command.priority() : TaskPriority.MEDIUM)
                .dueDate(command.dueDate())
                .reporterId(command.reporterId())
                .build();

        return mapper.toDto(taskRepository.save(task), List.of());
    }
}
