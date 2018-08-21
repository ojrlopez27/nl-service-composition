/*
 * Created on Aug 30, 2004
 */
package impl.owls.process.binding;


import impl.owl.CastingList;

import java.net.URI;

import org.mindswap.owl.OWLIndividualList;
import org.mindswap.owls.process.Binding;
import org.mindswap.owls.process.BindingList;
import org.mindswap.owls.process.Parameter;

/**
 * @author Evren Sirin
 */
public class BindingListImpl extends CastingList implements BindingList {

	public BindingListImpl() {
        super(Binding.class);
    }
    
    public BindingListImpl(OWLIndividualList list) {
        super(list, Binding.class);
    }

    public Binding bindingAt(int index) {
         return (Binding) get(index);
    }

    public Binding getBinding(URI bindingURI) {
        return (Binding) getIndividual(bindingURI);
    }

    public Binding getBindingFor(Parameter param) {
        for(int i = 0; i < size(); i++) {
            Binding binding = bindingAt(i);
            
            if( binding.getParameter().equals( param ) )
                return binding;
        }
        
        return null;
    }
    
// seems not to work    
//    public boolean containsBinding(Binding binding) {
//    	if (size() == 0)
//    		return false;
//    	
//    	Iterator<Binding> iter = iterator();
//    	while (iter.hasNext()) {
//    		Binding toCompare = iter.next();
//    		// TODO dmi this is a dirty hack to overcome input and output binding mixtures and resulting class cast exceptions
//    		Parameter p = (Parameter) binding.getProperty(OWLS.Process.toParam).castTo(Parameter.class);    		
//    		if (!toCompare.getParameter().getURI().equals(p.getURI()))
//    			return false;
//    		if (!toCompare.getValue().equals(binding.getValue()))
//    			return false;
//    	}
//
//		return false;
//	}

	public void addBindingWithoutDuplicate(Binding binding) {
		if (!contains(binding))
			add(binding);	
	}
	
	public void addBindingWithoutDuplicate(BindingList list) {
		for (int index = 0; index < list.size(); index++) {
			Binding binding = (Binding) list.get(index);			
			addBindingWithoutDuplicate(binding);
		}
	}
}
