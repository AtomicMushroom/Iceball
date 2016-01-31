package net.iceball.mgfw.impl.arena.exceptions;

/**
 * Created by Floris on 23-07-15.
 */
public class ArenaNullException extends Exception {
    public ArenaNullException() {
        super();
    }

    public ArenaNullException(String message) {
        super(message);
    }

    public ArenaNullException(String message, Throwable cause) {
        super(message, cause);
    }

    public ArenaNullException(Throwable cause) {
        super(cause);
    }

    protected ArenaNullException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
