package com.relatia.notification_service.constants;

/**
 * Global constants used throughout the notification service.
 */
public class GlobalConstants {
    
    /** Base API path for notification endpoints */
    public static final String NOTIFICATION_API = "/api/v1/notifications";
    
    /** Base API path for organisation endpoints */
    public static final String ORGANISATION_API = "/api/v1/organisation";
    
    /** Service name for logging and identification */
    public static final String SERVICE_NAME = "notification-service";
    
    /** Default date-time format */
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    
    /**
     * Private constructor to prevent instantiation.
     */
    private GlobalConstants() {
        // Prevent instantiation
    }
}




