/*
 * Created on Dec 12, 2004
 */
package org.mindswap.exceptions;

/**
 * @author Evren Sirin
 */
public class ConversionException extends RuntimeException {
    /**
     * 
     */
    public ConversionException() {
        super();
    }

    /**
     * @param message
     */
    public ConversionException(String message) {
        super(message);
    }
}
