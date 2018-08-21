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
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.mindswap.owl.OWLFactory;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLKnowledgeBase;
import org.mindswap.owls.process.Input;
import org.mindswap.owls.process.Process;
import org.mindswap.owls.service.Service;
import org.mindswap.query.ValueMap;
import org.mindswap.utils.ProcessUtils;

/**
 * 
 * Example to show how precondition check can be used to find valid values. 
 * 
 * @author Evren Sirin
 */
public class PreconditionUsage {
	public void run() throws Exception {
		OWLKnowledgeBase kb = OWLFactory.createKB();
		
		kb.setReasoner("Pellet");

		// language ontology
		String langOnt = "http://www.daml.org/2003/09/factbook/languages#";
		
		Service service = kb.readService("http://www.mindswap.org/2004/owl-s/1.1/BabelFishTranslator.owl");
		Process process = service.getProcess();

		Input inLang = process.getInput("InputLanguage");
		Input outLang = process.getInput("OutputLanguage");
		
		OWLIndividual English = kb.getIndividual(URI.create(langOnt + "English"));
		OWLIndividual Italian = kb.getIndividual(URI.create(langOnt + "Italian"));
		OWLIndividual Russian = kb.getIndividual(URI.create(langOnt + "Russian"));

        ValueMap values = new ValueMap();
		
        System.out.println( "Find all possible languages that can be used" );
        System.out.println( "------------------------------------------------" );
        printAllowedValues( process, values );
        
        System.out.println( "Find languages that English can be translated to" );
        System.out.println( "------------------------------------------------" );
        values.setValue( inLang, English );
        printAllowedValues( process, values );
        
        System.out.println( "Find languages that Italian can be translated to" );
        System.out.println( "------------------------------------------------" );
        values.clear();
        values.setValue( inLang, Italian );
        printAllowedValues( process, values );
        
        System.out.println( "Find languages that can be translated from Russian" );
        System.out.println( "--------------------------------------------------" );
        values.clear();
        values.setValue( outLang, Russian );
        printAllowedValues( process, values );
        
	}	
	
	public static void printAllowedValues( Process process, ValueMap values ) {
	    System.out.println( "Given Binding:" );
	    if( values.isEmpty() )
	        System.out.println( "   <NONE>" );
	    else {
	        for(Iterator i = values.getVariables().iterator(); i.hasNext();) {
                Input input = (Input) i.next();
                OWLIndividual value = values.getIndividualValue( input );
                System.out.println( "   " + input.getLocalName() + " = " + value.getLocalName() );
            }
	    }
	    
	    System.out.println( "Allowed values:" );
	    Map allowedValues = ProcessUtils.getAllowedValues( process, values );
	    for(Iterator i = allowedValues.keySet().iterator(); i.hasNext();) {
            Input input = (Input) i.next();
            Set set = (Set) allowedValues.get( input );
            System.out.print( "   " + input.getLocalName() + " = [" );
            for(Iterator j = set.iterator(); j.hasNext();) {
                OWLIndividual value = (OWLIndividual) j.next();
                System.out.print( value.getLocalName() );
                if( j.hasNext() )
                    System.out.print( ", " );    
            }
            System.out.println( "]" );
        }
        System.out.println();	    
	}
	
	public static void main(String[] args) throws Exception {		
		PreconditionUsage test = new PreconditionUsage();
		
		test.run();		
	}
}
