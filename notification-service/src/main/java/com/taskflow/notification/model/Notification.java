package com.taskflow.notification.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Document(collection = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    @Id
    private String id;

    @Indexed
    private UUID userId;

    private UUID taskId;

    private String title;

    private String message;

    private NotificationType type;

    @Builder.Default
    private boolean read = false;

    @Indexed
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
