/*
 * Created on Dec 12, 2004
 */
package org.mindswap.exceptions;

/**
 * @author Evren Sirin
 */
public class CastingException extends RuntimeException {
    /**
     * 
     */
    public CastingException() {
        super();
    }

    /**
     * @param message
     */
    public CastingException(String message) {
        super(message);
    }
}
