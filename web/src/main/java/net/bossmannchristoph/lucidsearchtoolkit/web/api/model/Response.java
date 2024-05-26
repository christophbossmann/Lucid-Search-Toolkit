package net.bossmannchristoph.lucidsearchtoolkit.web.api.model;

import java.time.LocalDateTime;

public class Response {
    private final LocalDateTime timestamp;
    private final int statuscode;
    private final String status;

    public Response(String status, int statuscode) {
        timestamp = LocalDateTime.now();
        this.statuscode = statuscode;
        this.status = status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public int getStatusCode() { return statuscode; }
    public String getStatus() {return status;}


}
