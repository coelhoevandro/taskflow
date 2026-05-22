package com.taskflow.notification.service;

import com.taskflow.notification.dto.NotificationDto;
import com.taskflow.notification.model.Notification;
import com.taskflow.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository repository;

    public List<NotificationDto> getForUser(UUID userId) {
        return repository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream().map(this::toDto).toList();
    }

    public List<NotificationDto> getUnreadForUser(UUID userId) {
        return repository.findByUserIdAndReadFalseOrderByCreatedAtDesc(userId)
                .stream().map(this::toDto).toList();
    }

    public long countUnread(UUID userId) {
        return repository.countByUserIdAndReadFalse(userId);
    }

    public void markAsRead(String notificationId) {
        repository.findById(notificationId).ifPresent(n -> {
            n.setRead(true);
            repository.save(n);
        });
    }

    public void markAllAsRead(UUID userId) {
        List<Notification> unread = repository.findByUserIdAndReadFalseOrderByCreatedAtDesc(userId);
        unread.forEach(n -> n.setRead(true));
        repository.saveAll(unread);
    }

    private NotificationDto toDto(Notification n) {
        return new NotificationDto(
                n.getId(), n.getUserId(), n.getTaskId(),
                n.getTitle(), n.getMessage(), n.getType(),
                n.isRead(), n.getCreatedAt()
        );
    }
}
