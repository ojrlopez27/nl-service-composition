/*
 * Created on Dec 28, 2004
 */
package impl.swrl;

import impl.owl.WrappedIndividual;

import org.mindswap.owl.OWLIndividual;
import org.mindswap.swrl.SWRLIndividual;

/**
 * @author Evren Sirin
 *
 */
public class SWRLIndividualImpl extends WrappedIndividual implements SWRLIndividual {
    public SWRLIndividualImpl(OWLIndividual individual) {
        super(individual);
    }

    public boolean isVariable() {
        return false;
    }
        
    public String toString() {
        return isAnon() ? ("_" + hashCode()) : individual.getLocalName();
    } 
}
