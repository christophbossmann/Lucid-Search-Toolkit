package net.bossmannchristoph.lucidsearchtoolkit.web.exception;

import java.time.LocalDateTime;

public class ErrorMessage {


    private final int statuscode;
    private final LocalDateTime timestamp;
    private final String message;


    public ErrorMessage(String message, int statuscode) {
        this.statuscode = statuscode;
        this.message = message;
        timestamp = LocalDateTime.now();
    }

    public int getStatus() { return statuscode; }
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "ErrorMessage{" +
                "statuscode=" + statuscode +
                ", timestamp=" + timestamp +
                ", message='" + message + '\'' +
                '}';
    }
}
