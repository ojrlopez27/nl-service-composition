package impl.owls.process.constructs;

import impl.owls.process.ProcessListImpl;

import java.util.List;

import org.mindswap.owl.OWLIndividual;
import org.mindswap.owls.process.ControlConstruct;
import org.mindswap.owls.process.ControlConstructBag;
import org.mindswap.owls.process.ProcessList;
import org.mindswap.owls.vocabulary.OWLS;

public abstract class BagBasedControlConstructImpl extends ControlConstructImpl implements
		org.mindswap.owls.process.BagBasedControlConstruct {

	public BagBasedControlConstructImpl(OWLIndividual ind) {
		super(ind);
	}

	public void addComponent(ControlConstruct component) {
	    ControlConstructBag components = getComponents();
	    if(components == null) {
	        components = getOntology().createControlConstructBag(component);
	        addProperty(OWLS.Process.components, components);
	    }
	    else
	        components.add(component);
	}
	
	public ControlConstructBag getComponents() {
	    return (ControlConstructBag) getPropertyAs(OWLS.Process.components, ControlConstructBag.class);
	}		
	
	public List getConstructs() {
	    return getComponents().getAll();
	}
	
	public ProcessList getAllProcesses(boolean recursive) {
		ProcessList list = new ProcessListImpl();
		ControlConstructBag components = getComponents();
		while(!components.isEmpty()) {
			ControlConstruct cc = (ControlConstruct) components.getFirst();
			list.addAll(cc.getAllProcesses(recursive));
			components = (ControlConstructBag) components.getRest();
		}
		
		return list;
	}

	public boolean removeConstruct(ControlConstruct CC) {
		ControlConstructBag components = getComponents();
		components = (ControlConstructBag) components.remove(CC);
		setProperty(OWLS.Process.components, components);
		return true;
	}
	
	public void removeComponents() {
		if (hasProperty(OWLS.Process.components))
			removeProperties(OWLS.Process.components);
	}
	
	public void deleteComponents() {
		ControlConstructBag bag = getComponents();
		removeComponents();
	
		bag.delete();			
	}
	
	@Override
	public void delete() {	
		deleteComponents();	
		super.delete();
	}
	
}
