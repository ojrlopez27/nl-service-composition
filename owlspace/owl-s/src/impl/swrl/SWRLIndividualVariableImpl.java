/*
 * Created on Dec 28, 2004
 */
package impl.swrl;

import impl.owl.WrappedIndividual;

import org.mindswap.owl.OWLIndividual;
import org.mindswap.swrl.SWRLIndividualVariable;

/**
 * @author Evren Sirin
 *
 */
public class SWRLIndividualVariableImpl extends WrappedIndividual implements SWRLIndividualVariable {
    public SWRLIndividualVariableImpl(OWLIndividual individual) {
        super(individual);
    }

    public boolean isVariable() {
        return true;
    }
        
    public String toString() {
        return isAnon() ? ("?_" + hashCode()) : ("?" + individual.getLocalName());
    }    
}
