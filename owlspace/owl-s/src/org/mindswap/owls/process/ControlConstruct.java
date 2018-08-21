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

import java.util.List;

import org.mindswap.owl.OWLIndividual;

/**
 * @author Evren Sirin
 *
 */
public interface ControlConstruct extends OWLIndividual {
    /**
     * Returns a list of all the Process objects used (directly or indirectly) in this control 
     * construct. These include all the processes that are performed as a result of executing
     * this control construct. 
     * 
     * @param recursive If true all the processes inside a Perform will be recursively added
     * to the results
     * @return List of Process objects
     */
	public ProcessList getAllProcesses( boolean recursive );
    
    /**
     * Equivalent to the function call {@link #getAllProcesses(false)}
     * @return
     */
    public ProcessList getAllProcesses();
    
    /**
     * Returns a list of all the Binding objects used (directly or indirectly) in this control 
     * construct. 
     * 
     * @param recursive If true all the bindings inside a Perform will be recursively added
     * to the results
     * @return List of bindings objects
     */
	public BindingList getAllBindings();
	
	/**
	 * Returns the immediate sub-constructs of this ControlConstruct. Unlike getComponents()
	 * function this function do not look at the process:components property. The result value
	 * of this function is computed differently for each construct. for example, IfThenElse
	 * construct would return the a list of size two (if it has both a process:then and 
	 * process:else value) or a list of size one (if there is no process:else value). 
	 *
	 */
	public List getConstructs();
	
	/**
	 * Returns the name of this construct which is equals to local name defined in OWL-S
	 * process ontology, i.e. Sequence, Choice, If-Then-Else, etc. 
	 * 
	 * @return
	 */
	public String getConstructName();
	
	/**
	 * Returns the process to which this control construct belongs. 
	 * 
	 * @return the parent process of this control construct. 
	 */
	public Process getParentProcess();

	
	/**
	 * Removes the given CC, which is contained in this CC. Control flow is rerouted from
	 * predecessor to successor of the given CC. Data flow from and to the given CC
	 * is removed too.
	 * 
	 * @param CC the ControlConstruct to remove
	 * @return true, if removal was succesful. false otherwise
	 * @see #getAllBindings() used for retrieving the bindings from and to the given CC
	 */
	public boolean removeConstruct(ControlConstruct CC);
	
}
