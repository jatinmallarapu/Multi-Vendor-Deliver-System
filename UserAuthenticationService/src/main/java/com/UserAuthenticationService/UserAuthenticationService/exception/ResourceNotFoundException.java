package com.UserAuthenticationService.UserAuthenticationService.exception;

/**
 * Exception thrown when a requested resource is not found
 * Returns HTTP 404 Not Found
 */
public class ResourceNotFoundException extends RuntimeException {
    
    public ResourceNotFoundException(String message) {
        super(message);
    }
    
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s: '%s'", resourceName, fieldName, fieldValue));
    }
}
