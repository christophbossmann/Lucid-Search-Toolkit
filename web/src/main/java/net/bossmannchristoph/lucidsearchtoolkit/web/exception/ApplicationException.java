package net.bossmannchristoph.lucidsearchtoolkit.web.exception;

public class ApplicationException extends Exception{

    public ApplicationException(String message) {
        super(message);
    }

    public ApplicationException(Throwable e) {
        super(e);
    }

   public ApplicationException(String message, Throwable e) {
        super(message, e);
    }
}
