/*
 * Created on Dec 12, 2004
 */
package org.mindswap.exceptions;

import org.mindswap.swrl.Variable;

/**
 * @author Evren Sirin
 */
public class UnboundVariableException extends RuntimeException {
    Variable var;

    public UnboundVariableException(Variable var) {
        super();
        this.var = var;
    }

    public UnboundVariableException(Variable var, String message) {
        super(message);
        this.var = var;
    }
    
    public Variable getVariable() {
        return var;
    }
    
    public String getMessage() {
        return (var == null) ? null : var + " is not bound!";
    }
}
