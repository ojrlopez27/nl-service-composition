/*
 * Created on Aug 26, 2004
 */
package org.mindswap.owls.process;

import java.net.URI;

import org.mindswap.owl.OWLIndividualList;

/**
 * @author Evren Sirin
 */
public interface ProcessList extends OWLIndividualList {
	public Process processAt(int index);	
	
	public Process getProcess(URI processURI);	
}
