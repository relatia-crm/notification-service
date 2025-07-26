package com.relatia.notification_service.config;

import com.relatia.notification_service.model.OrganisationInfo;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties(OrganisationInfo.class)
@Configuration
public class Properties {
}
