package com.relatia.notification_service.notification;

import com.relatia.notification_service.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for managing notifications.
 */
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    /**
     * Create a new notification.
     *
     * @param request the notification request
     * @return the created notification response
     */
    @Transactional
    public NotificationResponse createNotification(NotificationRequest request) {
        Notification notification = notificationMapper.toEntity(request);
        Notification savedNotification = notificationRepository.save(notification);
        return notificationMapper.toResponse(savedNotification);
    }

    /**
     * Get a notification by ID.
     *
     * @param id the notification ID
     * @return the notification response
     * @throws ResourceNotFoundException if notification is not found
     */
    public NotificationResponse getNotificationById(Long id) {
        Notification notification = notificationRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + id));
        return notificationMapper.toResponse(notification);
    }

    /**
     * Get all notifications for a recipient.
     *
     * @param recipientId the recipient ID
     * @return list of notification responses
     */
    public List<NotificationResponse> getNotificationsByRecipientId(Long recipientId) {
        return notificationRepository.findByRecipientIdOrderByCreatedAtDesc(recipientId).stream()
            .map(notificationMapper::toResponse)
            .collect(Collectors.toList());
    }

    /**
     * Get all unread notifications for a recipient.
     *
     * @param recipientId the recipient ID
     * @return list of unread notification responses
     */
    public List<NotificationResponse> getUnreadNotifications(Long recipientId) {
        return notificationRepository.findByRecipientIdAndReadFalseOrderByCreatedAtDesc(recipientId).stream()
            .map(notificationMapper::toResponse)
            .collect(Collectors.toList());
    }

    /**
     * Mark a notification as read.
     *
     * @param id the notification ID
     * @return the updated notification response
     */
    @Transactional
    public NotificationResponse markAsRead(Long id) {
        Notification notification = notificationRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + id));
        
        if (!notification.isRead()) {
            notification.setRead(true);
            notification = notificationRepository.save(notification);
        }
        
        return notificationMapper.toResponse(notification);
    }

    /**
     * Delete a notification.
     *
     * @param id the notification ID
     */
    @Transactional
    public void deleteNotification(Long id) {
        if (!notificationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Notification not found with id: " + id);
        }
        notificationRepository.deleteById(id);
    }

    /**
     * Get count of unread notifications for a recipient.
     *
     * @param recipientId the recipient ID
     * @return count of unread notifications
     */
    public long getUnreadCount(Long recipientId) {
        return notificationRepository.countByRecipientIdAndReadFalse(recipientId);
    }
    
    /**
     * Get the count of notifications for a recipient.
     *
     * @param recipientId the recipient ID
     * @return count of notifications for the recipient
     */
    public long getCountByRecipientId(Long recipientId) {
        return notificationRepository.countByRecipientId(recipientId);
    }
}
