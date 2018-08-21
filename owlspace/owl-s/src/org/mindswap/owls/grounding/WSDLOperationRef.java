/*
 * Created on Dec 17, 2004
 */
package org.mindswap.owls.grounding;

import java.net.URI;

import org.mindswap.owl.OWLIndividual;

/**
 * @author Evren Sirin
 * @author Michael Dänzer (University of Zurich)
 */
public interface WSDLOperationRef extends OWLIndividual {
	public void setOperation(URI op);
	public URI getOperation();
	public void removeOperation();
	
	public void setPortType(URI port);
	public URI getPortType();	
	public void removePortType();	
}
