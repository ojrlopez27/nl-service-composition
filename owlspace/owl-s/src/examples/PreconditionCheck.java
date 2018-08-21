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

import java.net.URI;

import org.mindswap.exceptions.ExecutionException;
import org.mindswap.exceptions.PreconditionException;
import org.mindswap.owl.OWLFactory;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLKnowledgeBase;
import org.mindswap.owls.OWLSFactory;
import org.mindswap.owls.process.Process;
import org.mindswap.owls.process.execution.ProcessExecutionEngine;
import org.mindswap.owls.service.Service;
import org.mindswap.query.ValueMap;

/**
 * 
 * Example to show how precondition check works.
 * 
 * @author Evren Sirin
 */
public class PreconditionCheck {
	public void run() throws Exception {
		OWLKnowledgeBase kb = OWLFactory.createKB();
		ProcessExecutionEngine exec = OWLSFactory.createExecutionEngine();	
		
		kb.setReasoner("Pellet");

		// language ontology
		String langOnt = "http://www.daml.org/2003/09/factbook/languages#";
		
		Service service = kb.readService("http://www.mindswap.org/2004/owl-s/1.1/BabelFishTranslator.owl");
		Process process = service.getProcess();

		OWLIndividual English = kb.getIndividual(URI.create(langOnt + "English"));
		OWLIndividual German = kb.getIndividual(URI.create(langOnt + "German"));
		OWLIndividual Italian = kb.getIndividual(URI.create(langOnt + "Italian"));

        ValueMap values = new ValueMap();
		

		exec.setPreconditionCheck( false );
		
		System.out.println( "Precondition check disabled" );
		System.out.println( "---------------------------" );
		
		try {            		
			values.setDataValue(process.getInput("InputString"), "ciao mondo!");		
			values.setValue(process.getInput("InputLanguage"), Italian);		
			values.setValue(process.getInput("OutputLanguage"), German);		
			
			System.out.println( "Trying unsupported language pair..." );
		    System.out.println( "Precondition satisfied: " + process.getCondition().isTrue( values ) );
            exec.execute(process, values);
            System.out.println("Execution successecful!");
        } catch(PreconditionException e) {
            System.out.println("Precondition evaluation failed!");
            System.out.println(e);
        } catch(ExecutionException e) {
            System.out.println("Execution failed!");
            System.out.println(e);
        }	
	    System.out.println();

	    exec.setPreconditionCheck( true );
	    
	    System.out.println( "Precondition check enabled" );
		System.out.println( "---------------------------" );
	    	    
		try {            		
			values.setDataValue(process.getInput("InputString"), "ciao mondo!");		
			values.setValue(process.getInput("InputLanguage"), Italian);		
			values.setValue(process.getInput("OutputLanguage"), German);		

			System.out.println( "Trying unsupported language pair..." );
			System.out.println( "Precondition satisfied: " + process.getCondition().isTrue( values ) );
            exec.execute(process, values);
            System.out.println("Execution successecful!");
        } catch(PreconditionException e) {
            System.out.println("Precondition evaluation failed!");
            System.out.println(e);
        } catch(ExecutionException e) {
            System.out.println("Execution failed!");
            System.out.println(e);
        }	
        
	    System.out.println();
		try {            		
			values.setDataValue(process.getInput("InputString"), "ciao mondo!");		
			values.setValue(process.getInput("InputLanguage"), Italian);		
			values.setValue(process.getInput("OutputLanguage"), English);		

		    System.out.println( "Trying supported language pair..." );
		    System.out.println( "Precondition satisfied: " + process.getCondition().isTrue( values ) );
            exec.execute(process, values);
            System.out.println("Execution successecful!");
        } catch(PreconditionException e) {
            System.out.println("Precondition evaluation failed!");
            System.out.println(e);
        } catch(ExecutionException e) {
            System.out.println("Execution failed!");
            System.out.println(e);
        }
	}	
	
	public static void main(String[] args) throws Exception {		
		PreconditionCheck test = new PreconditionCheck();
		
		test.run();		
	}
}
