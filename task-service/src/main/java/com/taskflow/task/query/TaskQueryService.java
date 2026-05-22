package com.taskflow.task.query;

import com.taskflow.task.dto.DashboardStatsDto;
import com.taskflow.task.dto.TaskDto;
import com.taskflow.task.dto.TaskSummaryDto;
import com.taskflow.task.mapper.TaskMapper;
import com.taskflow.task.model.Task;
import com.taskflow.task.model.TaskStatus;
import com.taskflow.task.repository.TaskCommentRepository;
import com.taskflow.task.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

// Read side — separado dos command handlers para que a lógica de leitura
// não se misture com regras de escrita. Poderia ter projeções otimizadas
// no futuro sem mexer nos handlers.
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskQueryService {

    private final TaskRepository taskRepository;
    private final TaskCommentRepository commentRepository;
    private final TaskMapper mapper;

    public Page<TaskSummaryDto> listTasks(int page, int size, TaskStatus status, UUID assigneeId) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        if (status != null) {
            return taskRepository.findByStatusOrderByCreatedAtDesc(status, pageable)
                    .map(mapper::toSummaryDto);
        }
        if (assigneeId != null) {
            return taskRepository.findByAssigneeIdOrderByCreatedAtDesc(assigneeId, pageable)
                    .map(mapper::toSummaryDto);
        }

        return taskRepository.findAllByOrderByCreatedAtDesc(pageable).map(mapper::toSummaryDto);
    }

    public TaskDto getById(UUID id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Task not found: " + id));
        return mapper.toDto(task, commentRepository.findByTaskIdOrderByCreatedAtAsc(id));
    }

    public List<TaskSummaryDto> getOverdue() {
        return taskRepository.findOverdue(LocalDate.now())
                .stream().map(mapper::toSummaryDto).toList();
    }

    public DashboardStatsDto getDashboardStats() {
        long total     = taskRepository.count();
        long todo      = taskRepository.countByStatus(TaskStatus.TODO);
        long inProgress = taskRepository.countByStatus(TaskStatus.IN_PROGRESS);
        long inReview  = taskRepository.countByStatus(TaskStatus.IN_REVIEW);
        long done      = taskRepository.countByStatus(TaskStatus.DONE);
        long overdue   = taskRepository.findOverdue(LocalDate.now()).size();

        return new DashboardStatsDto(total, todo, inProgress, inReview, done, overdue);
    }
}
