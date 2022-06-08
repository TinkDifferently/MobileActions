package io.dimension.exceptions;

public class BadTypeException extends InternalException {
    public BadTypeException() {
        super();
    }

    public BadTypeException(String message) {
        super(message);
    }

    public BadTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public BadTypeException(Throwable cause) {
        super(cause);
    }

    public BadTypeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
