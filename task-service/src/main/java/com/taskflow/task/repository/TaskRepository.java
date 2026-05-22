package com.taskflow.task.repository;

import com.taskflow.task.model.Task;
import com.taskflow.task.model.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {

    Page<Task> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<Task> findByStatusOrderByCreatedAtDesc(TaskStatus status, Pageable pageable);

    Page<Task> findByAssigneeIdOrderByCreatedAtDesc(UUID assigneeId, Pageable pageable);

    List<Task> findByAssigneeIdAndStatusIn(UUID assigneeId, List<TaskStatus> statuses);

    // Overdue tasks: past due date, not done or cancelled
    @Query("SELECT t FROM Task t WHERE t.dueDate < :today AND t.status NOT IN ('DONE', 'CANCELLED')")
    List<Task> findOverdue(LocalDate today);

    long countByStatus(TaskStatus status);
}
