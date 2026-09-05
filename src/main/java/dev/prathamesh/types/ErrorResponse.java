package dev.prathamesh.types;

import java.time.OffsetDateTime;

public class ErrorResponse {

    private OffsetDateTime timestamp;
    private int status;
    private String error;
    private String message;

    public ErrorResponse(
            int status,
            String error,
            String message) {

        this.timestamp = OffsetDateTime.now();
        this.status = status;
        this.error = error;
        this.message = message;
    }

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }
}