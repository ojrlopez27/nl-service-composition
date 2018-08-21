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

import impl.owls.process.execution.ProcessExecutionEngineImpl;

import java.net.URI;

import org.mindswap.owl.OWLClass;
import org.mindswap.owl.OWLDataProperty;
import org.mindswap.owl.OWLFactory;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLKnowledgeBase;
import org.mindswap.owl.OWLOntology;
import org.mindswap.owl.list.RDFList;
import org.mindswap.owls.OWLSFactory;
import org.mindswap.owls.process.CompositeProcess;
import org.mindswap.owls.process.ForEach;
import org.mindswap.owls.process.Input;
import org.mindswap.owls.process.Local;
import org.mindswap.owls.process.Perform;
import org.mindswap.owls.process.Process;
import org.mindswap.owls.process.execution.ProcessExecutionEngine;
import org.mindswap.owls.service.Service;
import org.mindswap.owls.vocabulary.OWLS;
import org.mindswap.query.ValueMap;
import org.mindswap.wsdl.WSDLOperation;

/**
 * 
 * Example to show how to create and execute a forEach control construct.
 * 
 * @author Evren Sirin
 */
public class ForEachExample {
	public void run() throws Exception {
		String ns = "http://www.example.org/test#";		
		
		WSDLOperation.DEBUG = true;
		// print the inputs and outputs during each iteration of the loop
		ProcessExecutionEngineImpl.DEBUG = true;
		ProcessExecutionEngine exec = OWLSFactory.createExecutionEngine();	
		
		OWLKnowledgeBase kb = OWLFactory.createKB();		
		
		Service service = kb.readService("http://www.mindswap.org/2004/owl-s/1.1/FindLatLong.owl");
		Process process = service.getProcess();
		
		OWLOntology ont = kb.createOntology();
		CompositeProcess cp = ont.createCompositeProcess();
		Input in = ont.createInput(URI.create( ns + "in" ));
		in.setParamType(OWLS.ObjList.List());
		cp.addInput(in);

		// create a ForEach construct
		ForEach forEach = ont.createForEach();
		Local loopVar = ont.createLocal( URI.create( ns + "loopVar") );
		cp.setComposedOf(forEach);
		forEach.setListValue( Perform.TheParentPerform, in );
		forEach.setLoopVar( loopVar );

		// perform the process by passing the loop variable 
		Perform perform = ont.createPerform();
		perform.setProcess(process);
		perform.addBinding(process.getInput(), Perform.TheParentPerform, loopVar);
		
		forEach.setComponent(perform);
		
		// display how the construct looks like in RDF/XML
		ont.write(System.out);
		
		// create some zip code values
	    String zipcodeOnt = "http://www.daml.org/2001/10/html/zipcode-ont#";
	    OWLClass ZipCode = kb.getClass(URI.create(zipcodeOnt + "ZipCode"));
	    OWLDataProperty zip = kb.getDataProperty(URI.create(zipcodeOnt + "zip"));
	    
	    OWLIndividual zip1 = ont.createInstance(ZipCode);
	    zip1.setProperty(zip, "20740");
	    OWLIndividual zip2 = ont.createInstance(ZipCode);
	    zip2.setProperty(zip, "11430");
	    OWLIndividual zip3 = ont.createInstance(ZipCode);
	    zip3.setProperty(zip, "94102");
	    
	    // put them in a list
	    RDFList list = ont.createList(zip1).add(zip2).add(zip3);
		
	    ValueMap values = new ValueMap();
		values.setValue(cp.getInput("in"), list);
		
		exec.execute( cp , values );
	}	
	
	public static void main(String[] args) throws Exception {		
		ForEachExample test = new ForEachExample();
		
		test.run();		
	}
}
