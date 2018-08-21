/*
 * Created on 16.04.2005
 */
package examples;

import impl.jena.OWLDataTypeImpl;

import java.net.URI;

import org.mindswap.owl.OWLFactory;
import org.mindswap.owl.OWLOntology;
import org.mindswap.owl.vocabulary.XSD;
import org.mindswap.owls.OWLSFactory;
import org.mindswap.owls.grounding.Grounding;
import org.mindswap.owls.grounding.JavaAtomicGrounding;
import org.mindswap.owls.process.AtomicProcess;
import org.mindswap.owls.process.Input;
import org.mindswap.owls.process.Output;
import org.mindswap.owls.process.execution.ProcessExecutionEngine;
import org.mindswap.owls.service.Service;
import org.mindswap.query.ValueMap;


/**
 * <p>
 * This example shows how to create a service grounded to a simple Java method. 
 * A sample service is generated and a grounding is created, which matches to
 * the following method call:
 * <br>
 * 		<pre>public String testIt(int i, Double y)</pre>
 * </p>
 * 
 * @author Michael Daenzer (University of Zurich)
 */
public class CreateJavaGrounding {

	public static void main(String[] args) throws Exception {
		CreateJavaGrounding test = new CreateJavaGrounding();
		test.run();
	}
	
	public void run() throws Exception  {
		String baseURL = "http://www.ifi.unizh.ch/ddis/ont/owl-s/";
		String baseURI = baseURL + "MyTestService#";
		
		OWLOntology ont = OWLFactory.createOntology(URI.create(baseURI));
		
		Service service = ont.createService(URI.create(baseURI + "MyService"));
		AtomicProcess process = ont.createAtomicProcess(URI.create(baseURI + "MyProcess"));
		service.setProcess(process);
		
		Input input1 = ont.createInput(URI.create(baseURI + "myInput1"));
		input1.setParamType(new OWLDataTypeImpl(XSD.nonNegativeInteger));
		input1.setProcess(process);

		Input input2 = ont.createInput(URI.create(baseURI + "myInput2"));
		input2.setParamType(new OWLDataTypeImpl(XSD.nonNegativeInteger));
		input2.setProcess(process);
		
		Output output = ont.createOutput(URI.create(baseURI + "myOutput")); 
		output.setParamType(new OWLDataTypeImpl(XSD.nonNegativeInteger));
		output.setProcess(process);
		
		JavaAtomicGrounding jAtomicGround = ont.createJavaAtomicGrounding(URI.create(baseURI + "MyJAtomGround"));
		jAtomicGround.setOutput(baseURI + "JPar1", "java.lang.String", output);
		jAtomicGround.setInputParameter(baseURI + "JIn1", "int", 1, input1);
		jAtomicGround.setInputParameter(baseURI + "JIn2", "java.lang.Double", 2, input2);
		jAtomicGround.setClaz("examples.CreateJavaGrounding");
		jAtomicGround.setMethod("testIt");
		jAtomicGround.setProcess(process);
			
		Grounding jGrounding = ont.createJavaGrounding(URI.create(baseURI + "MyJGrounding"));
		jGrounding.addGrounding(jAtomicGround);
		jGrounding.setService(service);
		
		ont.write(System.out);
		
		System.out.println();
		System.out.println( "Executing service:" );
		
		ValueMap values = new ValueMap();
		ProcessExecutionEngine exec = OWLSFactory.createExecutionEngine();
		
		// get the parameter using the local name
		values.setDataValue(process.getInput("myInput1"), "2");		
		values.setDataValue(process.getInput("myInput2"), "3");
		values = exec.execute(process, values);
	}
	
	public String testIt(int i, Double y) throws Exception {
		// wait some time to show interruption feature
		Thread.sleep(8000);
		// calc some value
        double s = i * y.doubleValue();
    	// something to show correct invocation
    	System.out.println("FirstParameter * SecondParameter = " + s);
    	// provoke exception
    	//s = Double.parseDouble("ThisThrowsAnException");
    	return new String("Return value of JavaGrounding " + s);
    }
}
