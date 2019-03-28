package edu.cmu.inmind.services.muf.components.middleware.commons;

/**
 * Created by oscarr on 2/14/18.
 */

public class MiddlewareException extends RuntimeException {

    public MiddlewareException(String errorCode) {
        super(errorCode);
    }

    public MiddlewareException(String errorCode, Object... params) {
        super(String.format(errorCode, params));
    }

    public MiddlewareException(Exception exception) {
        super (exception);
    }

    public MiddlewareException(Throwable throwable) {
        super (throwable);
    }
}
