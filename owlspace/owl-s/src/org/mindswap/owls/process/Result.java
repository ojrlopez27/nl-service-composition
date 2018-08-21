/*
 * Created on Jul 7, 2004
 */
package org.mindswap.owls.process;

import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLIndividualList;
import org.mindswap.owls.generic.expression.Expression;

/**
 * @author Evren Sirin
 */
public interface Result extends OWLIndividual, MultiConditional {
	public OWLIndividualList getEffects();
	
	public Expression getEffect();
	
	public void setEffect(Expression expr);

	public void addEffect(Expression expr);
	
	public void addBinding(OutputBinding binding);
	
	public void addBinding(Output output, ParameterValue paramValue);
	
	public void addBinding(Output output, Perform perform, Parameter param);
	
	public OutputBindingList getBindings();
	
	/**
	 * 
	 * Return the result parameters defined with process:hasResultVar property; 
	 * 
	 * @return
	 */
	public ParameterList getParameters();
}
