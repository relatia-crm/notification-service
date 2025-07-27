package com.relatia.notification_service.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Standard error response format for API exceptions.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Standard error response format for API exceptions")
public class ErrorResponse {

    @Schema(description = "HTTP status code", example = "400")
    private int status;

    @Schema(description = "Error message", example = "Validation failed")
    private String message;

    @Schema(description = "Timestamp when the error occurred")
    private LocalDateTime timestamp;

    @Schema(description = "Map of field errors (for validation errors)")
    @JsonProperty("errors")
    private Map<String, String> errors;

    public ErrorResponse(int status, String message, LocalDateTime timestamp) {
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }

    /**
     * Creates a new ErrorResponse with the given status, message, and debug message.
     *
     * @param status the HTTP status code
     * @param message the error message
     * @param debugMessage the debug message
     * @return a new ErrorResponse instance
     */
    public static ErrorResponse of(int status, String message, String debugMessage) {
        ErrorResponse response = new ErrorResponse();
        response.setStatus(status);
        response.setMessage(message);
        return response;
    }
}
