/*
 * Created on Dec 12, 2004
 */
package org.mindswap.exceptions;

/**
 * @author Evren Sirin
 */
public class LockNotSupportedException extends RuntimeException {
    /**
     * 
     */
    public LockNotSupportedException() {
        super();
    }

    /**
     * @param message
     */
    public LockNotSupportedException(String message) {
        super(message);
    }
}
