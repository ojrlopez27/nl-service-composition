/*
 * Created on Aug 30, 2004
 */
package impl.owls.process.constructs;

import java.util.ArrayList;
import java.util.List;

import org.mindswap.owl.OWLIndividual;
import org.mindswap.owls.process.ControlConstruct;
import org.mindswap.owls.process.Iterate;
import org.mindswap.owls.process.ProcessList;

/**
 * @author Evren Sirin
 * @author Michael Dänzer (University of Zurich)
 */
public abstract class IterateImpl extends ControlConstructImpl implements Iterate {
    public IterateImpl(OWLIndividual ind) {
        super(ind);
    } 
    
	public List getConstructs() {
	    List list = new ArrayList();
	    list.add(getComponent());
	    return list;
	}
	
	public ProcessList getAllProcesses(boolean recursive) {
		return getComponent().getAllProcesses(recursive);
	}	
	
	@Override
	public boolean removeConstruct(ControlConstruct CC) {
		if (getComponent().equals(CC))
			setComponent(null);
		return true;
	}

	public void deleteComponent() {
		ControlConstruct cc = getComponent();
		removeComponent();
		cc.delete();
		individual.delete();
	}
}
