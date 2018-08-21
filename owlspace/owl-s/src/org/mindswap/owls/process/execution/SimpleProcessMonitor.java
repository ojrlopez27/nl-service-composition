/*
 * Created on Jun 27, 2005
 */
package org.mindswap.owls.process.execution;

import org.mindswap.exceptions.ExecutionException;
import org.mindswap.owls.process.Process;
import org.mindswap.query.ValueMap;

/**
 * A simple process monitor implementation that prints the progress to console
 * 
 * @author Evren Sirin
 *
 */
public class SimpleProcessMonitor extends AbstractMonitor {
   
    public void executionStarted() {        
        out.println();
        out.flush();
    }
    
    public void executionFinished() {
        out.println();
        out.flush();
    }
    
    public void executionStarted(Process process, ValueMap inputs) {
        out.println( "Start executing process " + process );
        out.flush();
    }

    public void executionFinished(Process process, ValueMap inputs, ValueMap outputs) {
        out.println( "Execution finished for " + process );
        out.flush();
    }

    public void executionFailed(ExecutionException e) {
        out.println( "Execution failed: ");
        out.println( e );
        out.flush();
    }
}
