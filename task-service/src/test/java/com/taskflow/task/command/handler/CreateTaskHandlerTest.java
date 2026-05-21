package com.taskflow.task.command.handler;

import com.taskflow.task.command.CreateTaskCommand;
import com.taskflow.task.dto.TaskDto;
import com.taskflow.task.mapper.TaskMapper;
import com.taskflow.task.model.Task;
import com.taskflow.task.model.TaskPriority;
import com.taskflow.task.model.TaskStatus;
import com.taskflow.task.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateTaskHandlerTest {

    @Mock TaskRepository taskRepository;
    @Mock TaskMapper mapper;

    @InjectMocks CreateTaskHandler handler;

    @Test
    void handle_shouldCreateTaskWithDefaultPriority_whenNoneProvided() {
        UUID reporterId = UUID.randomUUID();
        CreateTaskCommand command = new CreateTaskCommand(
                "Fix login bug", null, null, null, reporterId
        );

        Task savedTask = Task.builder()
                .id(UUID.randomUUID())
                .title("Fix login bug")
                .priority(TaskPriority.MEDIUM)
                .status(TaskStatus.TODO)
                .reporterId(reporterId)
                .build();

        TaskDto expectedDto = new TaskDto(
                savedTask.getId(), "Fix login bug", null,
                TaskStatus.TODO, TaskPriority.MEDIUM,
                null, reporterId, null, false, null, List.of(), null, null
        );

        when(taskRepository.save(any())).thenReturn(savedTask);
        when(mapper.toDto(savedTask, List.of())).thenReturn(expectedDto);

        TaskDto result = handler.handle(command);

        assertThat(result.title()).isEqualTo("Fix login bug");
        assertThat(result.priority()).isEqualTo(TaskPriority.MEDIUM);
        assertThat(result.status()).isEqualTo(TaskStatus.TODO);
    }

    @Test
    void handle_shouldUseProvidedPriority() {
        UUID reporterId = UUID.randomUUID();
        CreateTaskCommand command = new CreateTaskCommand(
                "Critical outage", null, TaskPriority.CRITICAL, null, reporterId
        );

        Task savedTask = Task.builder()
                .id(UUID.randomUUID())
                .title("Critical outage")
                .priority(TaskPriority.CRITICAL)
                .status(TaskStatus.TODO)
                .reporterId(reporterId)
                .build();

        TaskDto expectedDto = new TaskDto(
                savedTask.getId(), "Critical outage", null,
                TaskStatus.TODO, TaskPriority.CRITICAL,
                null, reporterId, null, false, null, List.of(), null, null
        );

        when(taskRepository.save(any())).thenReturn(savedTask);
        when(mapper.toDto(savedTask, List.of())).thenReturn(expectedDto);

        TaskDto result = handler.handle(command);
        assertThat(result.priority()).isEqualTo(TaskPriority.CRITICAL);
    }
}
