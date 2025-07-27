package com.relatia.notification_service.notification;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.relatia.notification_service.exception.ErrorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class NotificationControllerTest {

    private MockMvc mockMvc;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private NotificationController notificationController;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private NotificationResponse testNotification;
    private NotificationRequest testRequest;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(notificationController)
                .setControllerAdvice(new com.relatia.notification_service.exception.GlobalExceptionHandler())
                .build();

        // Setup test data
        testNotification = NotificationResponse.builder()
                .id(1L)
                .title("Test Notification")
                .message("This is a test notification")
                .recipientId(123L)
                .read(false)
                .type(Notification.NotificationType.SYSTEM)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        testRequest = new NotificationRequest();
        testRequest.setTitle("Test Notification");
        testRequest.setMessage("This is a test notification");
        testRequest.setRecipientId(123L);
        testRequest.setType(Notification.NotificationType.SYSTEM);
    }

    @Test
    void createNotification_ShouldReturnCreated() throws Exception {
        when(notificationService.createNotification(any(NotificationRequest.class))).thenReturn(testNotification);

        mockMvc.perform(post("/api/v1/notifications")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Test Notification")));

        verify(notificationService, times(1)).createNotification(any(NotificationRequest.class));
    }

    @Test
    void getNotification_ShouldReturnNotification() throws Exception {
        when(notificationService.getNotificationById(1L)).thenReturn(testNotification);

        mockMvc.perform(get("/api/v1/notifications/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Test Notification")));

        verify(notificationService, times(1)).getNotificationById(1L);
    }

    @Test
    void getNotificationsByRecipient_ShouldReturnNotifications() throws Exception {
        List<NotificationResponse> notifications = Arrays.asList(testNotification);
        when(notificationService.getNotificationsByRecipientId(123L)).thenReturn(notifications);

        mockMvc.perform(get("/api/v1/notifications/recipient/123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].recipientId", is(123)));

        verify(notificationService, times(1)).getNotificationsByRecipientId(123L);
    }

    @Test
    void getUnreadNotifications_ShouldReturnUnread() throws Exception {
        List<NotificationResponse> notifications = Arrays.asList(testNotification);
        when(notificationService.getUnreadNotifications(123L)).thenReturn(notifications);

        mockMvc.perform(get("/api/v1/notifications/unread/123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].read", is(false)));

        verify(notificationService, times(1)).getUnreadNotifications(123L);
    }

    @Test
    void markAsRead_ShouldUpdateNotification() throws Exception {
        testNotification.setRead(true);
        when(notificationService.markAsRead(1L)).thenReturn(testNotification);

        mockMvc.perform(put("/api/v1/notifications/1/read"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.read", is(true)));

        verify(notificationService, times(1)).markAsRead(1L);
    }

    @Test
    void deleteNotification_ShouldReturnNoContent() throws Exception {
        doNothing().when(notificationService).deleteNotification(1L);

        mockMvc.perform(delete("/api/v1/notifications/1"))
                .andExpect(status().isNoContent());

        verify(notificationService, times(1)).deleteNotification(1L);
    }

    @Test
    void getUnreadCount_ShouldReturnCount() throws Exception {
        when(notificationService.getUnreadCount(123L)).thenReturn(5L);

        mockMvc.perform(get("/api/v1/notifications/unread-count/123"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));

        verify(notificationService, times(1)).getUnreadCount(123L);
    }

    @Test
    void createNotification_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        NotificationRequest invalidRequest = new NotificationRequest();
        invalidRequest.setMessage(""); // Title is required and message is empty

        mockMvc.perform(post("/api/v1/notifications")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message", containsString("Validation failed")));

        verify(notificationService, never()).createNotification(any(NotificationRequest.class));
    }

    @Test
    void getNonExistentNotification_ShouldReturnNotFound() throws Exception {
        when(notificationService.getNotificationById(999L))
                .thenThrow(new com.relatia.notification_service.exception.ResourceNotFoundException("Notification not found with id: 999"));

        mockMvc.perform(get("/api/v1/notifications/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", containsString("Notification not found")));
    }
}
