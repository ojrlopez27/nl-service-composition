/*
 * Created on 21.04.2005
 */
package impl.owls.process.execution;

import java.util.Iterator;

import org.mindswap.exceptions.ExecutionException;
import org.mindswap.owls.process.AtomicProcess;
import org.mindswap.owls.process.ControlConstruct;
import org.mindswap.owls.process.Process;
import org.mindswap.owls.process.execution.ProcessMonitor;
import org.mindswap.owls.process.execution.ThreadedProcessExecutionEngine;
import org.mindswap.query.ValueMap;

/**
 * @author Michael Daenzer (University of Zurich)
 */

//TODO dmi introduce new Factory classes for easy handling of several parallel executions.
//TODO dmi think about execute method for processes (this is OO)
public class ThreadedProcessExecutionEngineImpl extends ProcessExecutionEngineImpl
		implements Runnable, ThreadedProcessExecutionEngine {	
	
    private int sleepInterval = DEFAULT_SLEEP_INTERVAL;
    
    private boolean interrupted = false;
	private boolean stopped = false;
	private Process process;
	private ValueMap values;
	private AtomicProcess processInExecution;
		
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() { 
	    super.execute(this.process, this.values);
	}
	
	/* (non-Javadoc)
	 * @see org.mindswap.owls.process.execution.ProcessExecutionEngine#execute(org.mindswap.owls.process.Process, org.mindswap.query.ValueMap)
	 */
	public void executeInThread(Process process, ValueMap values) {
		this.process = process;
		this.values = values;
						
		Thread threadedExec = new Thread(this);
		threadedExec.start();			
	}
	
	/*
	 * (non-Javadoc)
	 * @see impl.owls.process.execution.ProcessExecutionEngineImpl#executeAtomicProcess(org.mindswap.owls.process.AtomicProcess, org.mindswap.query.ValueMap)
	 */
	protected ValueMap executeAtomicProcess(AtomicProcess process, ValueMap values) {
		processInExecution = process;
		ValueMap result = super.executeAtomicProcess(process, values);
		monitorOutputs(result);
		return result;
	}
    
    /*
     * (non-Javadoc)
     * @see impl.owls.process.execution.ProcessExecutionEngineImpl#executeConstruct(org.mindswap.owls.process.ControlConstruct)
     */
	protected void executeConstruct(ControlConstruct cc) {		
		if (isInterrupted()) 
			processInterruption();
	
		if (isStopped())
			processStop();
		
		super.executeConstruct(cc);
	}

	/*
	 * (non-Javadoc)
	 * @see org.mindswap.owls.process.execution.ThreadedProcessExecutionEngine#continueExecution()
	 */
	public void continueExecution() {		 
		setInterrupted(false);	
		monitorContinuation();
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.mindswap.owls.process.execution.ThreadedProcessExecutionEngine#interruptExecution()
	 */
	public void interruptExecution() {
		interruptExecution(sleepInterval);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.mindswap.owls.process.execution.ThreadedProcessExecutionEngine#interruptExecution(int)
	 */
    public void interruptExecution(int millisToSleep) {    	
        setInterrupted(true);
        setSleepInterval(millisToSleep);
        monitorInterruption();
    }
	
	public void stopExecution() throws ExecutionException {
		setStopped(true);
		executionFailed("Execution stopped by user");		
	} 
    
    /**
     * Checks whether interrupted flag is set or not
     * 
     * @return true, if flag set. false, otherwise.
     */
	protected boolean isInterrupted() {
		return interrupted;
	}
	
	/**
	 * Sets the interrupted flag for the given process
	 * 
	 * @param interrupted the new value of the flag
	 */
	protected void setInterrupted(boolean interrupted) {
		this.interrupted = interrupted;		
	}

    /**
     * Checks whether interrupted flag is set or not
     * 
     * @return true, if flag set. false, otherwise.
     */
	protected boolean isStopped() {
		return stopped;
	}
	
	/**
	 * Sets the interrupted flag for the given process
	 * 
	 * @param interrupted the new value of the flag
	 */
	protected void setStopped(boolean stopped) {
		this.stopped = stopped;		
	}
	
	/**
	 * Handles the interruption in a loop checking each <code>sleepInterval</code>.
	 * milliseconds the interruption state.  
	 */	
	private void processInterruption() {
		while (isInterrupted()) {
			try {
				Thread.sleep(sleepInterval);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Handles a stop of the execution and loops until object is garbage collected
	 */
	private void processStop() {
		while (true) {
			try {
				Thread.sleep(sleepInterval);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * invokes the executionInterrupted() method on all registred monitors
	 */
	protected void monitorInterruption() {
        for(Iterator i = monitors.iterator(); i.hasNext();) {
            ProcessMonitor monitor = (ProcessMonitor) i.next();
            monitor.executionInterrupted(process, processInExecution);
        }
	}
	
	/**
	 * invokes the executionContinued() method on all registered monitors
	 */
	protected void monitorContinuation() {
        for(Iterator i = monitors.iterator(); i.hasNext();) {
            ProcessMonitor monitor = (ProcessMonitor) i.next();
            monitor.executionContinued(process);
        }
	}
		
	/**
	 * invokes the getProcessResults() method on all registered monitors
	 */
	protected void monitorOutputs(ValueMap values) {
        for(Iterator i = monitors.iterator(); i.hasNext();) {
            ProcessMonitor monitor = (ProcessMonitor) i.next();
            monitor.intermediateResultsReceived(values);
        }
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.mindswap.owls.process.execution.ThreadedProcessExecutionEngine#getSleepInterval()
	 */
    public int getSleepInterval() {
        return sleepInterval;
    }

    /*
     * (non-Javadoc)
     * @see org.mindswap.owls.process.execution.ThreadedProcessExecutionEngine#setSleepInterval(int)
     */
    public void setSleepInterval(int sleepInterval) {
        this.sleepInterval = sleepInterval;
    }        
}
