package org.mindswap.owls.process.execution;

import java.util.Iterator;

import org.mindswap.exceptions.ExecutionException;
import org.mindswap.owls.process.Process;
import org.mindswap.query.ValueMap;
import org.mindswap.swrl.Variable;

/**
 * Simple monitor to show monitoring for threaded execution.
 * 
 * @author Michael Dänzer (University of Zurich)
 * @date 04.09.2006
 */

public class SimpleThreadedMonitor extends DefaultProcessMonitor {
	
	public void executionFailed(ExecutionException e) {
		super.executionFailed(e);
		System.out.println("--------------");
	}

	public void executionFinished(Process process, ValueMap inputs, ValueMap outputs) {
		super.executionFinished(process, inputs, outputs);
		System.out.println("--------------");
	}

	public void executionStarted(Process process, ValueMap inputs) {
		super.executionStarted(process, inputs);
		System.out.println("--------------");
	}

	public void executionContinued(Process process) {
		System.out.println("Execution of process: " + process.getLocalName() + " has continued");
		System.out.println("--------------");
	}

	public void executionInterrupted(Process process) {
		System.out.println("Execution of process: " + process.getLocalName() + " has been interrupted");
		System.out.println("--------------");
	}

	public void getExecutionResults(ValueMap values) {
		Iterator iter = values.getVariables().iterator();
		while (iter.hasNext()) {
			Variable var = (Variable) iter.next();
			System.out.println(values.getStringValue(var));
		}
		System.out.println("--------------");
	}

}
