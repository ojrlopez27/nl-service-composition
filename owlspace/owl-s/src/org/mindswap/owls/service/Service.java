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
package org.mindswap.owls.service;

import org.mindswap.owl.OWLDataValueList;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owls.grounding.Grounding;
import org.mindswap.owls.process.Process;
import org.mindswap.owls.profile.Profile;


/**
 * Represents the OWL-S service.
 * 
 * OWL-S concept: http://www.daml.org/services/owl-s/1.0/Service.owl#Service
 * 
 * @author Evren Sirin
 *
 */
public interface Service extends OWLIndividual {	
		
	/**
	 * Return the Profile that belongs to this Service. Multiple profiles
	 * for the same service is not supported.
	 * 
	 * @return the profile of the service 
	 */
	public Profile getProfile();
	
	/**
	 * Convenience method to get the Process from the ProcessModel. This function
	 * call is equivalent to getProcessModel().getProcess()
	 * 
	 * @return the process of the service 
	 */
	public Process getProcess();
	
	/**
	 * Return the Grounding that belongs to this service
	 * 
	 * @return the grounding of the service
	 */
	public Grounding getGrounding();
	
	/**
	 * Set the Profile for this service
	 * 
	 * @param profile
	 */
	public void setProfile(Profile profile);
	
	/**
	 * Set the Process for this service
	 * 
	 * @param profile
	 */
	public void setProcess(Process process);
	
	/**
	 * Set the Grounding for this service
	 * 
	 * @param grounding
	 */
	public void setGrounding(Grounding grounding);
	
	/**
	 * Removes the Profile for this service by breaking the link 
	 * <code>service:presents</code>. The profile itself remains untouched.
	 * 
	 * @param profile
	 */
	public void removeProfile(Profile profile);
	
	/**
	 * Removes the process for this service by breaking the link 
	 * <code>service:describedBy</code>. The process itself remains untouched.
	 */
	public void removeProcess();
	
	/**
	 * Removes the grounding for this service by breaking the link 
	 * <code>service:supports</code>. The grounding itself remains untouched.
	 * 
	 * @param grounding
	 */
	public void removeGrounding(Grounding grounding);
	
	/**
	 * Deletes the profile for this service
	 * 
	 * @param profile
	 */
	public void deleteProfile(Profile profile);
	
	/**
	 * Deletes the process for this service
	 */
	public void deleteProcess();
	
	/**
	 * Removes the grounding for this service
	 * 
	 * @param grounding
	 */
	public void deleteGrounding(Grounding grounding);
	
	/**
	 * Get the service name defined in the profile of this service. See {@link org.mindswap.owl.OWLConfig#DEFAULT_LANGS OWLConfig}
	 * to learn how the language identifiers will be resolved when searching for a name. 
	 * 
	 * @return
	 */
	public String getName();
	
	/**
	 * Get the service name defined in the profile of this service. The associated serviceName should have the same 
	 * language identifier as given in the parameter. If a serviceName for that language is not found 
	 * null value will be returned even if there is another serviceName with a different language 
	 * identifier. 
	 * 
	 * @param lang
	 * @return
	 */
	public String getName(String lang);
	
	public void setName(String name);
	
	/**
	 * Return all service names written in any language.
	 * 
	 * @return
	 */
	public OWLDataValueList getNames();

}
