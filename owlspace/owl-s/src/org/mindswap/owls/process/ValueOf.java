/*
 * Created on Jul 7, 2004
 */
package org.mindswap.owls.process;

import org.mindswap.owl.OWLIndividual;

/**
 * @author Evren Sirin
 */
public interface ValueOf extends ParameterValue, OWLIndividual {
	public Perform getPerform();
	public void setPerform(Perform perform);
	
	public Parameter getParameter();
	public void setParameter(Parameter param);
}
