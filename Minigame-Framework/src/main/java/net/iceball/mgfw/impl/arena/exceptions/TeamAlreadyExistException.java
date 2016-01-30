package net.iceball.mgfw.impl.arena.exceptions;

/**
 * Created by Floris on 23-07-15.
 */
public class TeamAlreadyExistException extends Exception {
    public TeamAlreadyExistException() {
        super();
    }

    public TeamAlreadyExistException(String message) {
        super(message);
    }

    public TeamAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public TeamAlreadyExistException(Throwable cause) {
        super(cause);
    }

    protected TeamAlreadyExistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
