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
package org.mindswap.owls.process;

import org.mindswap.owl.OWLType;
import org.mindswap.owl.OWLValue;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owls.profile.Profile;
import org.mindswap.owls.service.Service;
import org.mindswap.swrl.Variable;

/**
 * Represents the OWL-S process parameter. It is the super class for Input and Output classes. 
 *   
 * 
 * OWL-S concept: http://www.daml.org/services/owl-s/1.0/Process.owl#Parameter
 * 
 * @author Evren Sirin
 *
 */
public interface Parameter extends OWLIndividual, Variable {
	/**
	 * Get the type of this parameter.
	 * 
	 * @return
	 */
	public OWLType getParamType();

	/**
	 * Set the type for this parameter 
	 * 
	 * @param type
	 */
	public void setParamType(OWLType type);

	
	/**
	 * Get the process object where this parameter occurs
	 * 
	 * @return
	 */
	public Process getProcess();
	
	/**
	 * Get the service object where this parameter occurs
	 * 
	 * @return
	 */
	public Service getService();
	
	/**
	 * Get the profile object where this parameter occurs
	 * 
	 * @return
	 */
	public Profile getProfile();
		
	/**
	 * Set the process for this parameter
	 * 
	 * @param process
	 */
	public void setProcess(Process process);
	
	/**
	 * 
	 * If the parameter has a constant value associated with it (specified
	 * with process:parameterValue property) return that value. There is no
	 * restriction as to the contents of the value. In general, it is supposed 
	 * to be an XMLLiteral
	 * 
	 * @return
	 */
	public OWLValue getConstantValue();
	
	/**
	 * 
	 * Set the constant value for this parameter. 
	 * 
	 * @param value
	 */
	public void setConstantValue(OWLValue value);
}
