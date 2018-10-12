package edu.cmu.inmind.services.muf.components.services.registry;

/**
 * Created by adangi.
 */
public class ServiceRegistryException extends RuntimeException {

    public ServiceRegistryException(String errorCode) {
        super(errorCode);
    }

    public ServiceRegistryException(String errorCode, Object... params) {
        super(String.format(errorCode, params));
    }

    public ServiceRegistryException(Exception exception) {
        super (exception);
    }

    public ServiceRegistryException(Throwable throwable) {
        super (throwable);
    }
}
