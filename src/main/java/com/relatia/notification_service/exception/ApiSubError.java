package com.relatia.notification_service.exception;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Abstract base class for API sub-errors.
 * Used to represent validation errors and other detailed error information.
 */
@Schema(description = "Base class for API sub-errors")
public abstract class ApiSubError {
    // This is a marker class for all API sub-errors
}
