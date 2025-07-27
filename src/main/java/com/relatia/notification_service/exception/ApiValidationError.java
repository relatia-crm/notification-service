package com.relatia.notification_service.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

/**
 * Represents a validation error for a specific field.
 */
@Getter
@Schema(description = "Represents a validation error for a specific field")
public class ApiValidationError extends ApiSubError {

    @Schema(description = "The object that was validated", example = "NotificationRequest")
    private final String object;

    @Schema(description = "The field that failed validation", example = "title")
    private final String field;

    @Schema(description = "The rejected field value", example = "")
    private final Object rejectedValue;

    @Schema(description = "The error message", example = "Title is required")
    private final String message;

    /**
     * Creates a new ApiValidationError.
     *
     * @param object the object that was validated
     * @param message the error message
     */
    public ApiValidationError(String object, String message) {
        this.object = object;
        this.field = null;
        this.rejectedValue = null;
        this.message = message;
    }

    /**
     * Creates a new ApiValidationError with field and rejected value.
     *
     * @param object the object that was validated
     * @param field the field that failed validation
     * @param rejectedValue the rejected field value
     * @param message the error message
     */
    public ApiValidationError(String object, String field, Object rejectedValue, String message) {
        this.object = object;
        this.field = field;
        this.rejectedValue = rejectedValue;
        this.message = message;
    }
}
