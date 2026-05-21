package com.taskflow.task.repository;

import com.taskflow.task.model.TaskComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TaskCommentRepository extends JpaRepository<TaskComment, UUID> {

    List<TaskComment> findByTaskIdOrderByCreatedAtAsc(UUID taskId);
}
