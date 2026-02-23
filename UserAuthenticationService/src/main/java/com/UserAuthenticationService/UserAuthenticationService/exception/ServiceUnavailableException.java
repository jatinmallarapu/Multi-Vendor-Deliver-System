package com.UserAuthenticationService.UserAuthenticationService.exception;

/**
 * Exception thrown when a dependent service is unavailable
 * Returns HTTP 503 Service Unavailable
 */
public class ServiceUnavailableException extends RuntimeException {

    /*
    public ServiceUnavailableException(String message) {
        super(message);
    }


     */
    public ServiceUnavailableException(String serviceName) {
        super(String.format("Service '%s' is currently unavailable", serviceName));
    }
    
    public ServiceUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
