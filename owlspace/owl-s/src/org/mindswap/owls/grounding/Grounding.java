// The MIT License
//
// Copyright (c) 2004 Evren Sirin
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to
// deal in the Software without restriction, including without limitation the
// rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
// sell copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
// FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
// IN THE SOFTWARE.

/*
 * Created on Dec 27, 2003
 *
 */
package org.mindswap.owls.grounding;

import org.mindswap.owl.OWLIndividual;
import org.mindswap.owls.process.AtomicProcess;
import org.mindswap.owls.service.Service;

/**
 * Represents the OWL-S grounding. Note that there is not a subclass named WsdlGrounding. The reason is
 * a Grounding object may contain many {@link org.mindswap.owls.grounding.AtomicGrounding AtomicGrounding}
 * objects that are either WSDL or UPnP. Only {@link org.mindswap.owls.grounding.AtomicGrounding AtomicGrounding}
 * objects are labeld as WSDL or UPnP. 
 * 
 * OWL-S concept: http://www.daml.org/services/owl-s/1.0/Grounding#Grounding
 *  
 * @author Evren Sirin
 *
 */
public interface Grounding extends OWLIndividual {
	/**
	 * setService
	 * 
	 * @param service
	 */
	public void setService(Service service);
	
	/**
	 * getService
	 * 
	 * @return
	 */
	public Service getService();
	
	/**
	 * Removes the service from this grounding by breaking the link 
	 * <code>service:supportedBy</code>. The service itself remains untouched.2
	 */
	public void removeService();
	
	/**
	 * addGrounding
	 * 
	 * @param apg
	 */
	public void addGrounding(AtomicGrounding apg);

	/**
	 * Return the AtomicProcessGroundings defined for this process.
	 * 
	 * @param process
	 * @return
	 */
	public AtomicGrounding getAtomicGrounding(AtomicProcess process);

	/**
	 * Return all the AtomicProcessGroundings defined in this grounding.
	 * 
	 * @return
	 */
	public AtomicGroundingList getAtomicGroundings();
	
	/**
	 * getAtomicGroundings
	 * 
	 * @param process
	 * @return
	 */
	public AtomicGroundingList getAtomicGroundings(AtomicProcess process);
	
	/**
	 * Removes the atomic groundings for the given atomic process from this
	 * grounding by breaking the property grounding:hasAtomicProcessGrounding. The 
	 * atomic grounding itself is not changed at all.
	 * 
	 * @param process the atomic process to which the removeable atomic groundings are bound
	 */
	public void removeAtomicGroundings(AtomicProcess process);
	
	/**
	 * Deletes the atomic groundings for the given atomic process from this grounding
	 * 
	 * @param process the atomic process to which the deleteable atomic groundings are bound
	 */
	public void deleteAtomicGroundings(AtomicProcess process);
	
	/**
	 * Removes all atomic groundings from this grounding by breaking the property grounding:hasAtomicProcessGrounding. The 
	 * atomic grounding itself is not changed at all.
	 */
	public void removeAtomicGroundings();
	
	/**
	 * Deletes all atomic groundings from this grounding
	 */
	public void deleteAtomicGroundings();
}
