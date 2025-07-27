package com.relatia.notification_service.notification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for managing notification data.
 */
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    
    /**
     * Find all notifications for a specific recipient.
     *
     * @param recipientId the ID of the recipient
     * @return list of notifications for the recipient
     */
    List<Notification> findByRecipientIdOrderByCreatedAtDesc(Long recipientId);
    
    /**
     * Find all unread notifications for a specific recipient.
     *
     * @param recipientId the ID of the recipient
     * @return list of unread notifications for the recipient
     */
    List<Notification> findByRecipientIdAndReadFalseOrderByCreatedAtDesc(Long recipientId);
    
    /**
     * Count all unread notifications for a specific recipient.
     *
     * @param recipientId the ID of the recipient
     * @return count of unread notifications for the recipient
     */
    long countByRecipientIdAndReadFalse(Long recipientId);
}
