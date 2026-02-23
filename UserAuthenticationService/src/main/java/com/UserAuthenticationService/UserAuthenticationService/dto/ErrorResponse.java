package com.UserAuthenticationService.UserAuthenticationService.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Standardized error response structure for all API errors
 * This ensures consistent error format across all microservices
 */

public class ErrorResponse {
    
    // HTTP status code (e.g., 400, 401, 404, 500)
    private int status;
    
    // Error type/category (e.g., "Validation Error", "Unauthorized", "Not Found")
    private String error;
    
    // Human-readable error message
    private String message;
    
    // API endpoint path where error occurred
    private String path;
    
    // Timestamp when error occurred
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;
    
    // List of validation errors (for validation failures)
    // Example: ["email: must be a valid email", "password: must not be blank"]
    private List<String> validationErrors;
    
    /**
     * Constructor for simple errors without validation details
     */
    public ErrorResponse(){}
    public ErrorResponse(int status, String error, String message, String path) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
        this.timestamp = LocalDateTime.now();
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public List<String> getValidationErrors() {
        return validationErrors;
    }

    public void setValidationErrors(List<String> validationErrors) {
        this.validationErrors = validationErrors;
    }
}
