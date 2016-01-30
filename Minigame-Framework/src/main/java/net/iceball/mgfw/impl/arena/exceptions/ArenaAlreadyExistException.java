package net.iceball.mgfw.impl.arena.exceptions;

/**
 * Created by Floris on 22-07-15.
 */
public class ArenaAlreadyExistException extends Exception {
    public ArenaAlreadyExistException() {
        super();
    }

    public ArenaAlreadyExistException(String message) {
        super(message);
    }

    public ArenaAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public ArenaAlreadyExistException(Throwable cause) {
        super(cause);
    }

    protected ArenaAlreadyExistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
