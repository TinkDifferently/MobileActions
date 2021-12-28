package io.dimension.exceptions;

public class NotSupportedPlatformException extends InternalException {
    public NotSupportedPlatformException() {
    }

    public NotSupportedPlatformException(String message) {
        super(message);
    }

    public NotSupportedPlatformException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotSupportedPlatformException(Throwable cause) {
        super(cause);
    }

    public NotSupportedPlatformException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
