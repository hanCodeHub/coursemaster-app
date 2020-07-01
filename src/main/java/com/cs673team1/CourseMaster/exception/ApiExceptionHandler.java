package com.cs673team1.CourseMaster.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Global exception handler for custom HTTP exceptions at the controller level.
 */
@ControllerAdvice
public class ApiExceptionHandler {

    /**
     * Routes exceptions of type ResponseStatusException into this handler.
     * @param ex Exception from ResponseStatusException class
     * @return Exception details and HTTP Status within a ResponseEntity
     */
    @ExceptionHandler(value = {ResponseStatusException.class})
    public ResponseEntity<Object> handleResponseStatusException(ResponseStatusException ex) {
        // Create payload containing exception details
        var payload = new ApiException(
                ex.getReason(), // custom message
                ex.getStatus().value(), // status code
                ex.getStatus().getReasonPhrase(), // status name
                ZonedDateTime.now(ZoneId.of("Z")) // timestamp
        );
        return new ResponseEntity<>(payload, ex.getStatus());
    }
}
