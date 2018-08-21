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
 * Created on Mar 19, 2004
 */
package examples;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.mindswap.exceptions.ExecutionException;
import org.mindswap.owl.OWLFactory;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLKnowledgeBase;
import org.mindswap.owl.OWLValue;
import org.mindswap.owls.OWLSFactory;
import org.mindswap.owls.process.Input;
import org.mindswap.owls.process.Output;
import org.mindswap.owls.process.Process;
import org.mindswap.owls.process.execution.DefaultProcessMonitor;
import org.mindswap.owls.process.execution.ProcessExecutionEngine;
import org.mindswap.owls.process.execution.ProcessMonitor;
import org.mindswap.owls.service.Service;
import org.mindswap.query.ValueMap;

/**
 * 
 * @author Evren Sirin
 */
public class ExecutionMonitor {	
    public static class CustomProcessMonitor extends DefaultProcessMonitor implements ProcessMonitor {
        JTextArea textArea;
        JButton execButton;
        
        public CustomProcessMonitor() {
           setMonitorFilter( Process.ATOMIC );
          
           JFrame info = new JFrame( "Execution results" );
           textArea = new JTextArea(15, 40);
           textArea.setEditable(false);
           textArea.setLineWrap(true);
           textArea.setWrapStyleWord(true);
           execButton = new JButton( "Continue" );
           execButton.setEnabled(false);
           execButton.addActionListener(new ActionListener() {
               public void actionPerformed(ActionEvent e) {
                   execButton.setEnabled(false);
               }
           });
           
           print( "Initializing..." );

           info.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
           info.getContentPane().setLayout(new BoxLayout(info.getContentPane(), BoxLayout.Y_AXIS));
           info.getContentPane().add(new JScrollPane(textArea));
           info.getContentPane().add(execButton);
           execButton.setAlignmentX(Component.CENTER_ALIGNMENT);
           info.pack();
           info.setVisible(true);
        }
        
        private void print( String msg ) {
            textArea.append( msg );
            textArea.setCaretPosition( textArea.getText().length() );
        }

        private void println( String msg ) {
            textArea.append( msg + "\n");
            textArea.setCaretPosition( textArea.getText().length() );
        }

        private void println() {
            textArea.append( "\n");
            textArea.setCaretPosition( textArea.getText().length() );
        }
        
        public void executionStarted() {        
            println( "done." );
            println();
        }
        
        public void executionFinished() {
            println();
            println( "No more processes to execute, execution finished succesfully!" );
            println();
        }
        
        public void executionStarted(Process process, ValueMap inputs) {
            println( "Process: " + process );
            println( "Inputs: ");      
            for(Iterator i = process.getInputs().iterator(); i.hasNext();) {
                Input input = (Input) i.next();
                print( input.getLabel() + " =  " );
                OWLValue value = inputs.getValue( input );
                if( value.isDataValue() )
                    println( value.toString() );
                else {
                    OWLIndividual ind = (OWLIndividual) value;
                    if( ind.isAnon() )
                        println( ind.toRDF( false ) );
                    else
                        println( value.toString() );
                }                
            }      
            println( );
            println( "Click 'Continue' to execute" );
            execButton.setEnabled(true);

    		while (execButton.isEnabled()) {
    			try {
    				Thread.sleep( 1000 );
    			} catch (InterruptedException e) {
    				e.printStackTrace();
    			}
    		}
    		
    		println( );
    		print( "Executing...");
        }

        public void executionFinished(Process process, ValueMap inputs, ValueMap outputs) {
            println( "done." );
            println( );
            
            println( "Outputs: ");      
            for(Iterator i = process.getOutputs().iterator(); i.hasNext();) {
                Output output = (Output) i.next();
                print( output.getLabel() + " =  ");   
                OWLValue value = outputs.getValue( output );
                if( value.isDataValue() )
                    println( value.toString() );
                else {
                    OWLIndividual ind = (OWLIndividual) value;
                    if( ind.isAnon() )
                        println( ind.toRDF( false ) );
                    else
                        println( value.toString() );
                }
            }
            
            println( );
            println( "Click 'Continue' to go to next process execution" );

            execButton.setEnabled(true);
            
    		while (execButton.isEnabled()) {
    			try {
    				Thread.sleep( 1000 );
    			} catch (InterruptedException e) {
    				e.printStackTrace();
    			}
    		}
    		
    		println( );            
        }

        public void executionFailed(ExecutionException e) {
            println();
            println( "Execution failed: ");
            println( e.toString() );
            e.printStackTrace();
        }
    }
    
	public static void main(String[] args) throws Exception {		
		// create an execution engine 
		ProcessExecutionEngine exec = OWLSFactory.createExecutionEngine();
		
		// Attach a listener to the execution engine
		exec.addMonitor(new CustomProcessMonitor());	
		
	    OWLKnowledgeBase kb = OWLFactory.createKB();
	    // we need a reasoner that can evaluate the precondition of the translator
	    kb.setReasoner("Pellet");

	    Service service = kb.readService("http://www.mindswap.org/2004/owl-s/1.1/FrenchDictionary.owl");
		Process process = service.getProcess();
		
		// initialize the input values to be empty
		ValueMap values = new ValueMap();
		
		values.setDataValue(process.getInput("InputString"), "mere");
		values = exec.execute(process, values);				
	}
}
