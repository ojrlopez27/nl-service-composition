/*
 * Created on Dec 23, 2004
 */
package org.mindswap.exceptions;

/**
 * @author evren
 *
 */
public class InvalidURIException extends RuntimeException {

    /**
     * 
     */
    public InvalidURIException() {
        super();
    }

    /**
     * @param message
     */
    public InvalidURIException(String message) {
        super(message);
    }
}
