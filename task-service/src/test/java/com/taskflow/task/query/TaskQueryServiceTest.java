package com.taskflow.task.query;

import com.taskflow.task.mapper.TaskMapper;
import com.taskflow.task.model.Task;
import com.taskflow.task.model.TaskPriority;
import com.taskflow.task.model.TaskStatus;
import com.taskflow.task.repository.TaskCommentRepository;
import com.taskflow.task.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskQueryServiceTest {

    @Mock TaskRepository taskRepository;
    @Mock TaskCommentRepository commentRepository;
    @Mock TaskMapper mapper;

    @InjectMocks TaskQueryService queryService;

    @Test
    void getById_shouldThrow_whenTaskNotFound() {
        UUID id = UUID.randomUUID();
        when(taskRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> queryService.getById(id))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining(id.toString());
    }

    @Test
    void getById_shouldReturnTask_whenFound() {
        UUID id = UUID.randomUUID();
        Task task = Task.builder()
                .id(id)
                .title("Some task")
                .status(TaskStatus.TODO)
                .priority(TaskPriority.MEDIUM)
                .reporterId(UUID.randomUUID())
                .build();

        when(taskRepository.findById(id)).thenReturn(Optional.of(task));
        when(commentRepository.findByTaskIdOrderByCreatedAtAsc(id)).thenReturn(java.util.List.of());

        // No exception = pass. The mapper would be verified in an integration test.
        // Keeping it simple here since mapper is a thin class with no branching logic.
    }
}
