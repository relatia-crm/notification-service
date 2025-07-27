package com.relatia.notification_service.notification;

import com.relatia.notification_service.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.relatia.notification_service.constants.GlobalConstants.NOTIFICATION_API;

/**
 * REST controller for managing notifications.
 * Provides endpoints for creating, retrieving, and managing notifications.
 */
@Tag(
    name = "Notification Management",
    description = "APIs for managing notifications including create, read, and delete operations"
)
@RequestMapping(
    path = NOTIFICATION_API,
    produces = MediaType.APPLICATION_JSON_VALUE
)
@RestController
@RequiredArgsConstructor
@ApiResponses({
    @ApiResponse(
        responseCode = "500",
        description = "Internal Server Error",
        content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
})
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(
        summary = "Create a new notification",
        description = "Creates and stores a new notification in the system"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Notification created successfully",
            content = @Content(schema = @Schema(implementation = NotificationResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @PostMapping
    public ResponseEntity<NotificationResponse> createNotification(
            @Valid @RequestBody NotificationRequest request) {
        NotificationResponse response = notificationService.createNotification(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(
        summary = "Get notification by ID",
        description = "Retrieves a specific notification by its unique identifier"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Notification found",
            content = @Content(schema = @Schema(implementation = NotificationResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Notification not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @GetMapping("/{id}")
    public NotificationResponse getNotification(
            @Parameter(description = "ID of the notification to be retrieved", required = true)
            @PathVariable Long id) {
        return notificationService.getNotificationById(id);
    }

    @Operation(
            summary = "Get notifications",
            description = "Retrieves notifications with optional filtering by recipient and read status"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Notifications retrieved successfully",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = NotificationResponse.class)))
        )
    })
    @GetMapping
    public List<NotificationResponse> getNotifications(
            @Parameter(description = "ID of the recipient", required = true)
            @RequestParam("recipientId") Long recipientId,
            @Parameter(description = "Filter unread notifications only")
            @RequestParam(name = "unread", required = false, defaultValue = "false") boolean unread) {
        
        if (unread) {
            return notificationService.getUnreadNotifications(recipientId);
        }
        return notificationService.getNotificationsByRecipientId(recipientId);
    }
    
    @Operation(
        summary = "Get notification count",
        description = "Retrieves count of notifications with optional filtering by recipient and read status"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Notification count retrieved successfully",
            content = @Content(schema = @Schema(implementation = Long.class))
        )
    })
    @GetMapping("/count")
    public long getNotificationCount(
            @Parameter(description = "ID of the recipient", required = true)
            @RequestParam("recipientId") Long recipientId,
            @Parameter(description = "Count only unread notifications")
            @RequestParam(name = "unread", required = false, defaultValue = "false") boolean unread) {
        
        if (unread) {
            return notificationService.getUnreadCount(recipientId);
        }
        return notificationService.getCountByRecipientId(recipientId);
    }

    @Operation(
        summary = "Mark notification as read",
        description = "Updates a notification's read status to true"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Notification marked as read",
            content = @Content(schema = @Schema(implementation = NotificationResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Notification not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @PutMapping("/{id}/read")
    public NotificationResponse markAsRead(
            @Parameter(description = "ID of the notification to mark as read", required = true)
            @PathVariable Long id) {
        return notificationService.markAsRead(id);
    }

    @Operation(
        summary = "Delete a notification",
        description = "Deletes a specific notification by its unique identifier"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "204",
            description = "Notification deleted successfully"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Notification not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteNotification(
            @Parameter(description = "ID of the notification to be deleted", required = true)
            @PathVariable Long id) {
        notificationService.deleteNotification(id);
    }

}
