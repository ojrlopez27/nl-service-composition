/*
 * Created on Jan 5, 2005
 */
package org.mindswap.exceptions;

import org.mindswap.owls.process.Condition;
import org.mindswap.owls.process.Process;

/**
 * @author Evren Sirin
 *
 */
public class UnsatisfiedPreconditionException extends PreconditionException {   
    public UnsatisfiedPreconditionException(Process process, Condition condition) {
        super( process, condition );
    }
    
    public String getMessage() {
        return "The precondition " + condition + " of process " + process + " is not true!";
    }
}
