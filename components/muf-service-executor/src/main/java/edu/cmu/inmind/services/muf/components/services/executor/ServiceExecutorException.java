package edu.cmu.inmind.services.muf.components.services.executor;

/**
 * Created by adangi.
 */
public class ServiceExecutorException extends RuntimeException {

    public ServiceExecutorException(String errorCode) {
        super(errorCode);
    }

    public ServiceExecutorException(String errorCode, Object... params) {
        super(String.format(errorCode, params));
    }

    public ServiceExecutorException(Exception exception) {
        super (exception);
    }

    public ServiceExecutorException(Throwable throwable) {
        super (throwable);
    }
}
