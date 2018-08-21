package org.mindswap.owls.process.execution;

import java.io.PrintWriter;
import java.io.Writer;

import org.mindswap.exceptions.ExecutionException;
import org.mindswap.owls.process.AtomicProcess;
import org.mindswap.owls.process.Process;
import org.mindswap.query.ValueMap;

public abstract class AbstractMonitor implements ProcessMonitor {
    protected PrintWriter out;
    protected int monitorFilter;
    
    public AbstractMonitor() {
        this(new PrintWriter(System.out));
    }

    public AbstractMonitor(Writer writer) {
        monitorFilter = Process.ANY;
        
        setWriter( writer );
    }

	public void executionContinued(Process process) {
	}

	public void executionFailed(ExecutionException e) {
	}

	public void executionFinished() {
	}

	public void executionFinished(Process process, ValueMap inputs, ValueMap outputs) {
	}

	public void executionInterrupted(Process process, AtomicProcess atomicProcess) {
	}

	public void executionStarted() {
	}

	public void executionStarted(Process process, ValueMap inputs) {
	}

	public void intermediateResultsReceived(ValueMap values) {
	}

	public int getMonitorFilter() {
		return monitorFilter;
	}

	public void setMonitorFilter(int processType) {
		this.monitorFilter = processType;
	}
	
    private void setWriter( Writer writer ) {
        this.out = (writer instanceof PrintWriter) 
        	? (PrintWriter) writer 
        	: new PrintWriter( writer );
    }
}
