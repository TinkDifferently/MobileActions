package exceptions;

import utils.CustomLogger;

public class LoggedException extends InternalException {
    public LoggedException() {
        super();
        CustomLogger.fail("",this);
    }

    public LoggedException(String message) {
        super(message);
        CustomLogger.fail(message,this);
    }

    public LoggedException(String message, Throwable cause) {
        super(message, cause);
        CustomLogger.fail(message,cause);
    }

    public LoggedException(Throwable cause) {
        super(cause);
        CustomLogger.fail("",cause);
    }

    public LoggedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        CustomLogger.fail(message,cause);
    }
}
