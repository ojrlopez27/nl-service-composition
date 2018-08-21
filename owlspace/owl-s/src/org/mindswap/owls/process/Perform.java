/*
 * Created on Jul 7, 2004
 */
package org.mindswap.owls.process;

import org.mindswap.owl.OWLFactory;
import org.mindswap.owl.OWLOntology;
import org.mindswap.owls.vocabulary.OWLS;


/**
 * @author Evren Sirin
 */
public interface Perform extends ControlConstruct {
    static final OWLOntology owls = OWLFactory.createOntology();
    
	public static final Perform TheParentPerform = 
	    owls.createPerform(OWLS.Process.TheParentPerform.getURI());
	public static Perform ThisPerform = 
	    owls.createPerform(OWLS.Process.ThisPerform.getURI());
	
	public void addBinding(InputBinding binding);
	
	public void addBinding(Input input, ParameterValue paramValue);
	
	public void addBinding(Input input, Perform perform, Parameter param);
	
	public InputBindingList getBindings();
	
	public InputBinding getBindingFor(Input input);
	
	public Process getProcess();
	
	public void setProcess(Process process);
}
