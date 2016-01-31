package network.iceball.bukkit.exceptions;

/**
 * Created by Floris on 17-07-15.
 */
public class RankNotFoundException extends Exception {
    public RankNotFoundException() {
        super();
    }

    public RankNotFoundException(String message) {
        super(message);
    }

    public RankNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public RankNotFoundException(Throwable cause) {
        super(cause);
    }

    protected RankNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
