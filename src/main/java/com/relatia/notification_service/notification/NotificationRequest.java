package com.relatia.notification_service.notification;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO for creating or updating a notification.
 */
@Data
@Schema(description = "Request object for creating or updating a notification")
public class NotificationRequest {

    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title must not exceed 100 characters")
    @Schema(description = "Title of the notification", example = "New Message")
    private String title;

    @NotBlank(message = "Message is required")
    @Schema(description = "Content of the notification", example = "You have a new message from John Doe")
    private String message;

    @NotNull(message = "Recipient ID is required")
    @Schema(description = "ID of the recipient user", example = "123")
    private Long recipientId;

    @Schema(description = "Type of the notification", example = "EMAIL")
    private Notification.NotificationType type = Notification.NotificationType.SYSTEM;
}
