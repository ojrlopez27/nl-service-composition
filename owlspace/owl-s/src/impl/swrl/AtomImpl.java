/*
 * Created on Dec 28, 2004
 */
package impl.swrl;

import impl.owl.WrappedIndividual;

import org.mindswap.owl.OWLIndividual;
import org.mindswap.swrl.Atom;

/**
 * @author Evren Sirin
 *
 */
public abstract class AtomImpl extends WrappedIndividual implements Atom {
    public AtomImpl(OWLIndividual ind) {
        super(ind);
    }    
}
