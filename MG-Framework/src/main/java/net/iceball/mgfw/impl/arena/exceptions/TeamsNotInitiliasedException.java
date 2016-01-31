package net.iceball.mgfw.impl.arena.exceptions;

/**
 * Created by Floris on 22-07-15.
 */
public class TeamsNotInitiliasedException extends Exception {
    public TeamsNotInitiliasedException() {
        super();
    }

    public TeamsNotInitiliasedException(String message) {
        super(message);
    }

    public TeamsNotInitiliasedException(String message, Throwable cause) {
        super(message, cause);
    }

    public TeamsNotInitiliasedException(Throwable cause) {
        super(cause);
    }

    protected TeamsNotInitiliasedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
