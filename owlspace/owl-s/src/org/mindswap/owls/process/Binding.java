/*
 * Created on Jul 7, 2004
 */
package org.mindswap.owls.process;

import org.mindswap.owl.OWLIndividual;

/**
 * @author Evren Sirin
 */
public interface Binding extends OWLIndividual {
	public Parameter getParameter();
	public void setParameter(Parameter param);
	
	public ParameterValue getValue();
	public void setValue(ParameterValue paramValue);
}
