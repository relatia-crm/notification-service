package com.relatia.notification_service.notification;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper for converting between Notification entities and DTOs.
 */
@Component
public class NotificationMapper {

    /**
     * Convert a NotificationRequest to a Notification entity.
     *
     * @param request the notification request
     * @return the notification entity
     */
    public Notification toEntity(NotificationRequest request) {
        if (request == null) {
            return null;
        }

        return Notification.builder()
                .title(request.getTitle())
                .message(request.getMessage())
                .recipientId(request.getRecipientId())
                .type(request.getType())
                .read(false) // New notifications are unread by default
                .build();
    }

    /**
     * Convert a Notification entity to a NotificationResponse.
     *
     * @param notification the notification entity
     * @return the notification response
     */
    public NotificationResponse toResponse(Notification notification) {
        if (notification == null) {
            return null;
        }

        return NotificationResponse.builder()
                .id(notification.getId())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .recipientId(notification.getRecipientId())
                .read(notification.isRead())
                .type(notification.getType())
                .createdAt(notification.getCreatedAt())
                .updatedAt(notification.getUpdatedAt())
                .build();
    }

    /**
     * Convert a list of Notification entities to a list of NotificationResponse objects.
     *
     * @param notifications the list of notification entities
     * @return the list of notification responses
     */
    public List<NotificationResponse> toResponseList(List<Notification> notifications) {
        if (notifications == null) {
            return null;
        }
        
        return notifications.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Update a Notification entity from a NotificationRequest.
     *
     * @param request the notification request with updated data
     * @param entity the notification entity to update
     */
    public void updateFromRequest(NotificationRequest request, Notification entity) {
        if (request == null || entity == null) {
            return;
        }

        if (request.getTitle() != null) {
            entity.setTitle(request.getTitle());
        }
        if (request.getMessage() != null) {
            entity.setMessage(request.getMessage());
        }
        if (request.getRecipientId() != null) {
            entity.setRecipientId(request.getRecipientId());
        }
        if (request.getType() != null) {
            entity.setType(request.getType());
        }
        // Note: 'read' status is not updated here as it's typically updated via specific endpoints
    }
}
