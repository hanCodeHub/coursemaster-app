package com.cs673team1.CourseMaster.exception;

import java.time.ZonedDateTime;

/**
 * Defines an exception wrapper object to hold properties of an HTTP error.
 */
public class ApiException {

    private final String reason;
    private final int status;
    private final String message;
    private final ZonedDateTime timestamp;

    public ApiException(String reason, int status, String message, ZonedDateTime timestamp) {
        this.reason = reason;
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getReason() {
        return reason;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }
}
