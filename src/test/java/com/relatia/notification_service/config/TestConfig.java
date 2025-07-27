package com.relatia.notification_service.config;

import com.relatia.notification_service.notification.NotificationMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfig {
    
    @Bean
    public NotificationMapper notificationMapper() {
        return new NotificationMapper();
    }
}
