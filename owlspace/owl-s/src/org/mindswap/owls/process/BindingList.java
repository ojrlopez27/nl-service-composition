/*
 * Created on Aug 26, 2004
 */
package org.mindswap.owls.process;

import org.mindswap.owl.OWLIndividualList;

/**
 * @author Evren Sirin
 */
public interface BindingList extends OWLIndividualList {
	public Binding bindingAt(int index);
	
	public Binding getBindingFor(Parameter param);
	
	public void addBindingWithoutDuplicate(Binding binding);

	public void addBindingWithoutDuplicate(BindingList list);
}
