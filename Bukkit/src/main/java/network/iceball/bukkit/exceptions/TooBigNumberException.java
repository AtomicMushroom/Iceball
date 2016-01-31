package network.iceball.bukkit.exceptions;

/**
 * Created by Floris on 09-07-15.
 */
public class TooBigNumberException extends Exception{

    protected TooBigNumberException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public TooBigNumberException(Throwable cause) {
        super(cause);
    }

    public TooBigNumberException(String message, Throwable cause) {
        super(message, cause);
    }

    public TooBigNumberException(String message) {
        super(message);
    }

    public TooBigNumberException() {
        super();
    }
}
