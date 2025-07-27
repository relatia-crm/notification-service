package com.relatia.notification_service.notification;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for notification responses.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response object for notification operations")
public class NotificationResponse {

    @Schema(description = "Unique identifier of the notification", example = "1")
    private Long id;

    @Schema(description = "Title of the notification", example = "New Message")
    private String title;

    @Schema(description = "Content of the notification", example = "You have a new message from John Doe")
    private String message;

    @Schema(description = "ID of the recipient user", example = "123")
    private Long recipientId;

    @Schema(description = "Read status of the notification", example = "false")
    private boolean read;

    @Schema(description = "Type of the notification", example = "SYSTEM")
    private Notification.NotificationType type;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Timestamp when the notification was created", example = "2023-07-27T10:15:30")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Timestamp when the notification was last updated", example = "2023-07-27T10:15:30")
    private LocalDateTime updatedAt;
}
