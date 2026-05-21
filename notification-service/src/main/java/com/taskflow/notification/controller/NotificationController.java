package com.taskflow.notification.controller;

import com.taskflow.notification.dto.NotificationDto;
import com.taskflow.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/user/{userId}")
    public List<NotificationDto> getForUser(@PathVariable UUID userId) {
        return notificationService.getForUser(userId);
    }

    @GetMapping("/user/{userId}/unread")
    public List<NotificationDto> getUnread(@PathVariable UUID userId) {
        return notificationService.getUnreadForUser(userId);
    }

    @GetMapping("/user/{userId}/unread-count")
    public Map<String, Long> getUnreadCount(@PathVariable UUID userId) {
        return Map.of("count", notificationService.countUnread(userId));
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable String id) {
        notificationService.markAsRead(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/user/{userId}/read-all")
    public ResponseEntity<Void> markAllAsRead(@PathVariable UUID userId) {
        notificationService.markAllAsRead(userId);
        return ResponseEntity.noContent().build();
    }
}
