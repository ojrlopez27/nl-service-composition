/*
 * Created on Jun 27, 2005
 */
package org.mindswap.owls.process.execution;

import java.util.Iterator;

import org.mindswap.exceptions.ExecutionException;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLValue;
import org.mindswap.owls.process.Input;
import org.mindswap.owls.process.Output;
import org.mindswap.owls.process.Process;
import org.mindswap.query.ValueMap;

/**
 * A simple process monitor implementation that prints the progress to console
 * 
 * @author Evren Sirin
 *
 */
public class DefaultProcessMonitor extends AbstractMonitor {
	
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
        out.println( "Inputs: ");      
        for(Iterator i = process.getInputs().iterator(); i.hasNext();) {
            Input input = (Input) i.next();
            out.print( input.toPrettyString() + " =  " );
            OWLValue value = inputs.getValue( input );
            if( value == null )
                value = input.getConstantValue();
            if( value == null )
                out.println( "<< NO VALUE >>" );
            else if( value.isDataValue() )
                out.println( value );
            else {
                OWLIndividual ind = (OWLIndividual) value;
                if( ind.isAnon() )
                    out.println( ind.toRDF( false ) );
                else
                    out.println( value );
            }
            
        }
        out.flush();
    }

    public void executionFinished(Process process, ValueMap inputs, ValueMap outputs) {
        out.println( "Execution finished for " + process );
        out.println( "Outputs: ");      
        for(Iterator i = process.getOutputs().iterator(); i.hasNext();) {
            Output output = (Output) i.next();
            out.print( output.toPrettyString() + " =  ");   
            OWLValue value = outputs.getValue( output );
            if( value == null )
                out.println( "<< NO VALUE >>" );
            else if( value.isDataValue() )
                out.println( value );
            else {
                OWLIndividual ind = (OWLIndividual) value;
                if( ind.isAnon() )
                    out.println( ind.toRDF( false ) );
                else
                    out.println( value );
            }
        }
        out.flush();
    }

    public void executionFailed(ExecutionException e) {
        out.println( "Execution failed: ");
        out.println( e );
        out.flush();
    }
}
