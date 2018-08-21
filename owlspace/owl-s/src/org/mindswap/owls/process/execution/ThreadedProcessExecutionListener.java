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
 * Created on 22.04.2005
 */
package org.mindswap.owls.process.execution;

import org.mindswap.owls.process.AtomicProcess;
import org.mindswap.owls.process.Parameter;
import org.mindswap.owls.process.Process;

/**
 * A listener interface which observer can implement to monitor threaded process
 * executions performed by the <code>ThreadedProcessExecutionEngine</code>.
 * 
 * @author Michael Daenzer (University of Zurich)
 * @see org.mindswap.owls.process.execution.ProcessExecutionEngine
 * @see org.mindswap.owls.process.execution.ThreadedProcessExecutionEngine 
 * @deprecated
 */
public interface ThreadedProcessExecutionListener {
    /**
     * Notifies registred implementors about the start of an execution
     * @param processName a string indicating the name of the executed process
     */
    public void executionStarted(Process process);
	
    /**
     * Notifies registred implementors about the end of an execution
     * @param processName a string indicating the name of the executed process
     */
    public void executionFinished(Process process);
    
	/**
	 * Notifies registered listeners about an interruption of the process execution
	 * @param processName a string indicating the name of the executed process
	 */
    public void executionInterrupted(Process process);
    
    /**
     * Notifies registered listeners about the end of an interruption of the process execution
     * @param processName a string indicating the name of the executed process
     */
	public void executionContinued(Process process);
		
	/**
	 * Notifies registered listeners that the execution of an AtomicProcess started
	 * @param the AtomicProcess whose execution started
	 */
	public void atomicProcessStarted(AtomicProcess process);
	
	/**
	 * Notifies registered listeners that the execution of an AtomicProcess ended
	 * @param the AtomicProcess whose execution ended
	 */
	public void atomicProcessEnded(AtomicProcess process);
	
	/**
	 * Notifies registered listeners that the value of annotated Parameter changed (NextInput, NextOutput)
	 * @param process an instance of process indicating the executed process
	 * @param param the parameter which was set during the execution
	 * @param value the string value of the parameters new value
	 */
	public void parameterValueSet(Process process, Parameter param, String value);
}
