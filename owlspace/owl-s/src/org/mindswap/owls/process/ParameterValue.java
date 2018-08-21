/*
 * Created on Jul 7, 2004
 */
package org.mindswap.owls.process;

import org.mindswap.owl.OWLObject;


/**
 * Encapsulation for the concept of the value of a parameter of data binding. The 
 * OWL-S property <code>valueSpecifier</code> is used to link the binding to the 
 * parameter value. The concrete type of value is one of (OWL-S 1.1):
 * <li>
 * 		<ul>{@link org.mindswap.owls.process.ValueOf ValueOf}</ul>
 * 		<ul>{@link org.mindswap.owls.process.ValueData ValueData}</ul>
 * 		<ul>{@link org.mindswap.owls.process.ValueForm ValueForm}</ul>
 * 		<ul>{@link org.mindswap.owls.process.ValueFunction ValueFunction}</ul>
 * </li> 
 * @author Evren Sirin
 * @author Michael Dänzer
 */
public interface ParameterValue extends OWLObject {
	
	/**
	 * Returns the binding which encloses this parameter value.
	 * @return the data binding in which this parameter value is enclosed in
	 */
	public Binding getEnclosingBinding();
}
