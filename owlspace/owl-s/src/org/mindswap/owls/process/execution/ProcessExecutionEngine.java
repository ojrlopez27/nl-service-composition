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
 * Created on Jun 27, 2003
 */
package org.mindswap.owls.process.execution;

import java.util.EventListener;

import org.mindswap.owl.OWLKnowledgeBase;
import org.mindswap.owls.process.Perform;
import org.mindswap.owls.process.Process;
import org.mindswap.query.ValueMap;

/**
 * @author Evren Sirin
 * 
 */
public interface ProcessExecutionEngine extends EventListener {
    /**
     * @deprecated Use addMonitor(ProcessMonitor monitor) instead
     */
	public void addExecutionListener(ProcessExecutionListener listener);
	
	public void addMonitor(ProcessMonitor monitor);
	
	/**
	 * Set the KB where preconditions will be evaluated and the results will be put. This
	 * is simply the execution environment.
	 */
	public void setKB(OWLKnowledgeBase kb);
	
	/**
	 * Get the KB.
	 */
	public OWLKnowledgeBase getKB();
	
	/**
	 * Enable/disable precondition evaluation. Note that if there are local variables
	 * bound by the precondition then the execution may fail when precondition evaluation
	 * is disabled.  
	 */
	public void setPreconditionCheck(boolean checkPreconditions);
	
	/**
	 * Get if the execution engine verifies preconditions.
	 */
	public boolean isPreconditionCheck();
	
	/**
	 * Set the behavior for cases where evaluating a precondition may yield multiple different
	 * values for a local variable. If this option is enabled then the execution engine will
	 * simply pick the first value from the possible bindings and conintue. Otherwise an
	 * ExecutionException will be thrown indicating that execution failed.
	 * 
	 * @param allow If true one of the multiple bindings for a precondition is chosen else
	 * an ExecutionException is thrown
	 */
    public void setAllowMultipleSatisifedPreconditions(boolean allow);

    /**
     * Returns if multiple satisifed preconditions are allowed
     * @return true if multiple satisifed preconditions are allowed. false, otherwise
     */
    public boolean isAllowMultipleSatisifedPreconditions();      
	
	/**
	 * Execute the given process with no value bindings for input parameters. Process is 
	 * assumed to have no input parameters or all the value bindings were already specified 
	 * in the defaultValues 
	 * 
	 * @param p Process to be executed
	 * @return Value bindings for the output parameters after the execution. Returns null if
	 * execution is not successful
	 */
	public ValueMap execute(Process p) throws Exception;
	
	
	/**
	 * Execute the given OWL-S process with the given input value bindings. If a value is not
	 * specified for a parameter in the valueMap then the default values of the process is
	 * searched.
	 * 
	 * @param p Process to be executed
	 * @param map Value bindings for the input parameters
	 * @return Value bindings for the output parameters after the execution. Returns null if
	 * execution is not successful
	 */
	public ValueMap execute(Process p, ValueMap bindings) throws Exception;
	
	/**
	 * Execute the given perform construct. Perform shoud contain all the necessary input
	 * bindings specified.
	 * 
	 * @param perform
	 * @return
	 * @throws Exception
	 */
	public ValueMap execute(Perform perform) throws Exception;	
}
