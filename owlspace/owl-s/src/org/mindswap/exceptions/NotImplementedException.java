/*
 * Created on Dec 13, 2004
 */
package org.mindswap.exceptions;

/**
 * @author Evren Sirin
 */
public class NotImplementedException extends RuntimeException {

    /**
     * 
     */
    public NotImplementedException() {
        super("Not implemented yet!");
    }

    public NotImplementedException(String msg) {
        super(msg);
    }
}
