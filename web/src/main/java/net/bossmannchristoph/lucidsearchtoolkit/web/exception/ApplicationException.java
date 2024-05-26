package net.bossmannchristoph.lucidsearchtoolkit.web.exception;

public class ApplicationException extends Exception{

    private final String status;

    public String getStatus() {
        return status;
    }

    public ApplicationException(String message, String status) {
        super(message);
        this.status = status;
    }

    public ApplicationException(Throwable e) {
        super(e);
        this.status = e.getClass().getName();
    }

   public ApplicationException(String message, String status, Throwable e) {
        super(message, e);
        this.status = status;
    }
}
