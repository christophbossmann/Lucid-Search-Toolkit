package net.bossmannchristoph.lucidsearchtoolkit.web.api.model;

public class ResponseMessage extends Response{
    String message;

    public ResponseMessage(String message, String status, int statuscode) {
        super(status, statuscode);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
