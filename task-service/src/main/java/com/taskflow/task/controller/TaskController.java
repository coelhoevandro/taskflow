package com.taskflow.task.controller;

import com.taskflow.task.command.*;
import com.taskflow.task.command.handler.*;
import com.taskflow.task.dto.*;
import com.taskflow.task.model.TaskStatus;
import com.taskflow.task.query.TaskQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final CreateTaskHandler createHandler;
    private final UpdateTaskHandler updateHandler;
    private final AssignTaskHandler assignHandler;
    private final AddCommentHandler commentHandler;
    private final TaskQueryService queryService;

    @GetMapping
    public Page<TaskSummaryDto> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) UUID assigneeId) {
        return queryService.listTasks(page, size, status, assigneeId);
    }

    @GetMapping("/{id}")
    public TaskDto getById(@PathVariable UUID id) {
        return queryService.getById(id);
    }

    @GetMapping("/overdue")
    public List<TaskSummaryDto> getOverdue() {
        return queryService.getOverdue();
    }

    @GetMapping("/stats")
    public DashboardStatsDto getStats() {
        return queryService.getDashboardStats();
    }

    @PostMapping
    public ResponseEntity<TaskDto> create(@Valid @RequestBody CreateTaskCommand command) {
        return ResponseEntity.status(HttpStatus.CREATED).body(createHandler.handle(command));
    }

    @PutMapping("/{id}")
    public TaskDto update(@PathVariable UUID id, @Valid @RequestBody UpdateTaskCommand command) {
        return updateHandler.handle(id, command);
    }

    @PostMapping("/{id}/assign")
    public TaskDto assign(@PathVariable UUID id, @Valid @RequestBody AssignTaskCommand command) {
        return assignHandler.handle(id, command);
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<CommentDto> addComment(@PathVariable UUID id,
                                                  @Valid @RequestBody AddCommentCommand command) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commentHandler.handle(id, command));
    }
}
