package exceptions;

public class NoSuchActionException extends ConfigurationException {
    public NoSuchActionException() {
    }

    public NoSuchActionException(String message) {
        super(message);
    }

    public NoSuchActionException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchActionException(Throwable cause) {
        super(cause);
    }

    public NoSuchActionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
