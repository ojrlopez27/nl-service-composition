/*
 * Created on Dec 21, 2004
 */
package impl.owls.grounding;

import impl.owl.CastingList;

import org.mindswap.owl.OWLIndividualList;
import org.mindswap.owls.grounding.AtomicGrounding;
import org.mindswap.owls.grounding.AtomicGroundingList;
import org.mindswap.owls.process.AtomicProcess;

/**
 * @author evren
 *
 */
public class AtomicGroundingListImpl extends CastingList implements AtomicGroundingList {
    public AtomicGroundingListImpl() {
        super(AtomicGrounding.class);
    }

    public AtomicGroundingListImpl(OWLIndividualList list) {
        super(list, AtomicGrounding.class);
    }
    public AtomicGrounding groundingAt(int index) {
        return (AtomicGrounding) get(index);
    }

    public AtomicGrounding getGrounding(AtomicProcess process) {
		for (int i = 0; i < size(); i++) {
		    AtomicGrounding grounding = groundingAt(i);
			if (grounding.getProcess().equals(process))
				return grounding;
		}
		return null;
    }

}
