package com.taskflow.notification.consumer;

import com.taskflow.notification.client.UserClient;
import com.taskflow.notification.model.Notification;
import com.taskflow.notification.model.NotificationType;
import com.taskflow.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.slf4j.MDC;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;

// This is the saga participant.
// We consume task.assigned, validate the assignee, create the notification,
// then emit the result so task-service can confirm or roll back.
@Component
@RequiredArgsConstructor
@Slf4j
public class TaskEventConsumer {

    private final NotificationRepository notificationRepository;
    private final UserClient userClient;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaListener(topics = "task.assigned", groupId = "taskflow-notifications")
    public void handleTaskAssigned(ConsumerRecord<String, TaskAssignedPayload> record) {
        String correlationId = extractCorrelationId(record);
        MDC.put("correlationId", correlationId);
        try {
            TaskAssignedPayload event = record.value();
            log.debug("Received task.assigned event for task {}", event.taskId());

            // Verify assignee still exists — could have been deleted between assignment and this event
            boolean userExists = checkUserExists(event.assigneeId());

            if (!userExists) {
                log.warn("Assignee {} not found, emitting failure for task {}", event.assigneeId(), event.taskId());
                emitResult(event.taskId(), event.assigneeId(), false, "Assignee user not found", correlationId);
                return;
            }

            Notification notification = Notification.builder()
                    .userId(event.assigneeId())
                    .taskId(event.taskId())
                    .title("You have been assigned a task")
                    .message(String.format("'%s' was assigned to you by %s", event.taskTitle(), event.reporterName()))
                    .type(NotificationType.TASK_ASSIGNED)
                    .build();

            notificationRepository.save(notification);
            log.info("Notification created for user {} (task {})", event.assigneeId(), event.taskId());

            emitResult(event.taskId(), event.assigneeId(), true, null, correlationId);
        } finally {
            MDC.clear();
        }
    }

    private boolean checkUserExists(UUID userId) {
        try {
            Map<String, Boolean> response = userClient.existsById(userId);
            return Boolean.TRUE.equals(response.get("exists"));
        } catch (Exception e) {
            log.error("Failed to check user existence for {}", userId, e);
            return false;
        }
    }

    private void emitResult(UUID taskId, UUID assigneeId, boolean success, String failureReason, String correlationId) {
        NotificationResultPayload result = new NotificationResultPayload(taskId, assigneeId, success, failureReason);
        ProducerRecord<String, Object> producerRecord = new ProducerRecord<>("notification.result", taskId.toString(), result);
        producerRecord.headers().add(new RecordHeader("X-Correlation-Id", correlationId.getBytes(StandardCharsets.UTF_8)));
        kafkaTemplate.send(producerRecord);
    }

    private String extractCorrelationId(ConsumerRecord<?, ?> record) {
        Header header = record.headers().lastHeader("X-Correlation-Id");
        if (header != null) {
            return new String(header.value(), StandardCharsets.UTF_8);
        }
        return UUID.randomUUID().toString();
    }

    // Inner records for Kafka payloads — keeps things simple without separate event classes
    public record TaskAssignedPayload(UUID taskId, String taskTitle, UUID assigneeId, UUID reporterId, String reporterName) {}
    public record NotificationResultPayload(UUID taskId, UUID assigneeId, boolean success, String failureReason) {}
}
