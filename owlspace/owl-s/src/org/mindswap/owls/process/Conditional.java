/*
 * Created on Aug 26, 2004
 */
package org.mindswap.owls.process;

import org.mindswap.owl.OWLIndividual;

/**
 * General interface to define a conditional construct such as If-Then-Else, Repeat-While, etc.
 * 
 * @author Evren Sirin
 */
public interface Conditional extends OWLIndividual {		
	/**
	 * Return the condition. If there is more than one return any one of them.
	 * 
	 * @return
	 */
	public Condition getCondition();
	
	public void setCondition(Condition condition);
}
