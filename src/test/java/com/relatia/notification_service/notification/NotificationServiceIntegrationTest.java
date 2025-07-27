package com.relatia.notification_service.notification;

import com.relatia.notification_service.NotificationServiceApplication;
import com.relatia.notification_service.config.TestConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
@ContextConfiguration(classes = {NotificationServiceApplication.class, TestConfig.class})
class NotificationServiceIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationMapper notificationMapper;

    private NotificationService notificationService;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        notificationService = new NotificationService(notificationRepository, notificationMapper);
    }

    @Test
    void whenSave_thenReturnSavedNotification() {
        // given
        NotificationRequest request = new NotificationRequest();
        request.setTitle("Test Notification");
        request.setMessage("This is a test");
        request.setRecipientId(1L);
        request.setType(Notification.NotificationType.EMAIL);

        // when
        NotificationResponse saved = notificationService.createNotification(request);

        // then
        assertNotNull(saved.getId());
        assertEquals("Test Notification", saved.getTitle());
        assertEquals(1L, saved.getRecipientId());
        assertFalse(saved.isRead());
    }

    @Test
    void whenFindByRecipientId_thenReturnNotifications() {
        // given
        Notification notification1 = createTestNotification(1L, "Test 1", false);
        Notification notification2 = createTestNotification(1L, "Test 2", true);
        Notification notification3 = createTestNotification(2L, "Test 3", false);

        entityManager.persist(notification1);
        entityManager.persist(notification2);
        entityManager.persist(notification3);
        entityManager.flush();

        // when
        List<NotificationResponse> found = notificationService.getNotificationsByRecipientId(1L);

        // then
        assertEquals(2, found.size());
        assertThat(found).extracting(NotificationResponse::getTitle)
                .containsExactlyInAnyOrder("Test 1", "Test 2");
    }

    @Test
    void whenMarkAsRead_thenNotificationIsRead() {
        // given
        Notification notification = createTestNotification(1L, "Test", false);
        entityManager.persistAndFlush(notification);
        Long id = notification.getId();

        // when
        NotificationResponse updated = notificationService.markAsRead(id);

        // then
        assertTrue(updated.isRead());
        
        // Verify in database
        Notification fromDb = entityManager.find(Notification.class, id);
        assertTrue(fromDb.isRead());
    }

    @Test
    void whenGetUnreadCount_thenReturnCorrectCount() {
        // given
        entityManager.persist(createTestNotification(1L, "Test 1", false));
        entityManager.persist(createTestNotification(1L, "Test 2", true));
        entityManager.persist(createTestNotification(1L, "Test 3", false));
        entityManager.persist(createTestNotification(2L, "Test 4", false));
        entityManager.flush();

        // when
        long count = notificationService.getUnreadCount(1L);

        // then
        assertEquals(2, count);
    }

    private Notification createTestNotification(Long recipientId, String title, boolean read) {
        return Notification.builder()
                .title(title)
                .message("Test message for " + title)
                .recipientId(recipientId)
                .read(read)
                .type(Notification.NotificationType.SYSTEM)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
