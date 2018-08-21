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
public class MultipleSatisfiedPreconditionException extends PreconditionException {
    public MultipleSatisfiedPreconditionException(Process process, Condition condition) {
        super( process, condition );
    }
    
    public String getMessage() {
        return "There are multiple different bindings possible for the precondition " + 
        condition + " of process " + process + " is not true!";
    }
}
