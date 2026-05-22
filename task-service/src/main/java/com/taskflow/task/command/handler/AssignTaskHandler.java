package com.taskflow.task.command.handler;

import com.taskflow.task.client.UserClient;
import com.taskflow.task.command.AssignTaskCommand;
import com.taskflow.task.dto.TaskDto;
import com.taskflow.task.event.TaskAssignedEvent;
import com.taskflow.task.mapper.TaskMapper;
import com.taskflow.task.model.AssignmentStatus;
import com.taskflow.task.model.Task;
import com.taskflow.task.repository.TaskCommentRepository;
import com.taskflow.task.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.slf4j.MDC;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class AssignTaskHandler {

    private final TaskRepository taskRepository;
    private final TaskCommentRepository commentRepository;
    private final UserClient userClient;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final TaskMapper mapper;

    @Transactional
    public TaskDto handle(UUID taskId, AssignTaskCommand command) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NoSuchElementException("Task not found: " + taskId));

        Map<String, Boolean> response = userClient.existsById(command.assigneeId());
        if (!Boolean.TRUE.equals(response.get("exists"))) {
            throw new IllegalArgumentException("Assignee not found: " + command.assigneeId());
        }

        task.setAssigneeId(command.assigneeId());
        task.setAssignmentStatus(AssignmentStatus.PENDING_NOTIFICATION);
        taskRepository.save(task);

        TaskAssignedEvent event = new TaskAssignedEvent(
                task.getId(),
                task.getTitle(),
                command.assigneeId(),
                task.getReporterId(),
                "TaskFlow"
        );

        // Propaga o correlation ID no header do evento Kafka.
        // Isso permite rastrear o fluxo completo: HTTP request → Kafka event → notification consumer
        // nos logs de todos os serviços usando o mesmo cid.
        ProducerRecord<String, Object> record = new ProducerRecord<>("task.assigned", taskId.toString(), event);
        String correlationId = MDC.get("correlationId");
        if (correlationId != null) {
            record.headers().add(new RecordHeader("X-Correlation-Id", correlationId.getBytes(StandardCharsets.UTF_8)));
        }

        kafkaTemplate.send(record);
        log.info("Task {} assigned to {}, saga started", taskId, command.assigneeId());

        return mapper.toDto(task, commentRepository.findByTaskIdOrderByCreatedAtAsc(taskId));
    }
}
