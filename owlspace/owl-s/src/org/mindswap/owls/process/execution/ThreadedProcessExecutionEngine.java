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
 * Created on Apr 25, 2005
 */
package org.mindswap.owls.process.execution;

import org.mindswap.owls.process.Process;
import org.mindswap.query.ValueMap;

/**
 * <p>This class allows to run several executions in parallel threads by 
 * subclassing the default <code>ProcessExecutionEngine</code>. An ID is returned
 * from the first call to the <code>ThreadedProcessExecutionEngine</code>, which 
 * must be used for all further interaction.</p> 
 * 
 * <p>Logically interruptable (means nested) ControlConstruct's check before 
 * the Execution of every SubControlConstruct if the Execution was 
 * interrupted (with a call of interruptExecution()) by another thread and wait until the interruption ends (with a call of unfreeze()). </p> 
 * 
 * @author Michael Dänzer (University of Zurich)
 * @see org.mindswap.owls.process.execution.ProcessExecutionEngine  
 * @see org.mindswap.owls.process.execution.ProcessMonitor
 */

public interface ThreadedProcessExecutionEngine extends ProcessExecutionEngine {
	/**
	 * This is the default time in milli seconds, this threads sleeps in interrupted
	 * mode before checking the interruption state again.
	 * 
	 * @see #setSleepInterval(int)
	 * @see #getSleepInterval()
	 */
	public static final int DEFAULT_SLEEP_INTERVAL = 5000;
	
    /**
     * Executes the given process in a seperate thread.
     * 
     * @param process the process to execute
     * @param values a <code>ValueMap</code> containing the values for all inputs and locals.
     */
    public void executeInThread(Process process, ValueMap values);
    
	/**
	 * Finishes the interruption of the execution. 
	 * The Execution is not continued immediately!
	 */
	public void continueExecution();

	/**
	 * Interrupts the Execution after the execution of the 
	 * current atomic entity has finished and until unfreeze() is invoked.
	 */
	public void interruptExecution();
	
	/**
	 * Interrupts the Execution after the execution of the 
	 * current atomic entity has finished and until unfreeze() is invoked.
	 * 
	 * @param millisToSleep Interval in millisecond for which the ExecutionEngine sleeps before checking the interruption state
	 */
	public void interruptExecution(int millisToSleep);
   
    /**
     * Returns the interval an interrupted thread sleeps before checking 
     * whether the intrruption still holds or not
     * 
     * @return the interval in milliseconds
     */
    public int getSleepInterval();

    /**
  	 * Sets the interval for the thread of the given process to sleep before 
  	 * checking whether the intrruption still holds or not
     * 
     * @param sleepInterval the interval in millseconds
     */
    public void setSleepInterval(int sleepInterval);

    /**
     * Stops the current execution
     *
     */
	public void stopExecution();
}