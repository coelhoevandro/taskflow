package com.taskflow.task.event;

import com.taskflow.task.model.AssignmentStatus;
import com.taskflow.task.model.Task;
import com.taskflow.task.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.slf4j.MDC;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

// Handles the saga compensation step.
// When notification-service fails to notify the assignee, we roll back the assignment.
@Component
@RequiredArgsConstructor
@Slf4j
public class SagaEventConsumer {

    private final TaskRepository taskRepository;

    @KafkaListener(topics = "notification.result", groupId = "task-service-group")
    @Transactional
    public void handleNotificationResult(ConsumerRecord<String, NotificationResultEvent> record) {
        Header header = record.headers().lastHeader("X-Correlation-Id");
        if (header != null) {
            MDC.put("correlationId", new String(header.value(), StandardCharsets.UTF_8));
        } else {
            MDC.put("correlationId", UUID.randomUUID().toString());
        }
        try {
            NotificationResultEvent event = record.value();
            taskRepository.findById(event.taskId()).ifPresent(task -> {
                if (event.success()) {
                    confirmAssignment(task);
                } else {
                    rollbackAssignment(task, event.failureReason());
                }
            });
        } finally {
            MDC.clear();
        }
    }

    private void confirmAssignment(Task task) {
        task.setAssignmentStatus(AssignmentStatus.CONFIRMED);
        taskRepository.save(task);
        log.info("Assignment confirmed for task {}", task.getId());
    }

    private void rollbackAssignment(Task task, String reason) {
        // Saga compensation: undo the assignment
        task.setAssigneeId(null);
        task.setAssignmentStatus(AssignmentStatus.UNASSIGNED);
        taskRepository.save(task);
        log.warn("Assignment rolled back for task {} — reason: {}", task.getId(), reason);
    }
}
