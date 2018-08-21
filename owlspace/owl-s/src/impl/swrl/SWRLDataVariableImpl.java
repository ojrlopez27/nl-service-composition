/*
 * Created on Dec 28, 2004
 */
package impl.swrl;

import impl.owl.WrappedIndividual;

import org.mindswap.owl.OWLIndividual;
import org.mindswap.swrl.SWRLDataVariable;

/**
 * @author Evren Sirin
 *
 */
public class SWRLDataVariableImpl extends WrappedIndividual implements SWRLDataVariable {
    public SWRLDataVariableImpl(OWLIndividual individual) {
        super(individual);
    }

    public boolean isVariable() {
        return true;
    }
    
    public String toString() {
        return isAnon() ? ("?_" + hashCode()) : ("?" + individual.getLocalName());
    }   
}
