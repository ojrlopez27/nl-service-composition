/*
 * Created on Jun 1, 2004
 */
package org.mindswap.shop;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mindswap.owl.OWLIndividual;
import org.mindswap.owls.process.AtomicProcess;
import org.mindswap.owls.process.CompositeProcess;
import org.mindswap.owls.process.ControlConstruct;
import org.mindswap.owls.process.ControlConstructList;
import org.mindswap.owls.process.InputList;
import org.mindswap.owls.process.OutputList;
import org.mindswap.owls.process.Parameter;
import org.mindswap.owls.process.Process;
import org.mindswap.owls.process.Sequence;
import org.mindswap.owls.service.Service;
import org.mindswap.utils.QNameProvider;


/**
 * This is a experimental version of OWL-S to SHOP2 translator.
 * 
 * @author Evren Sirin
 */
public class SHOPTranslator {
	private QNameProvider qnames = new QNameProvider();
	
	private PrintWriter out;
	
	private List processes;
//	private Set  written;
	
	private String getSHOPAbbr(OWLIndividual resource) {
		String s = qnames.shortForm(resource.getURI());
		if(resource instanceof AtomicProcess)
			s += "!";
		return s; 
	}
	
	/* (non-Javadoc)
	 * @see org.mindswap.owls.io.OWLSWriter#write(org.mindswap.owls.service.Service, java.io.OutputStream)
	 */
	public void write(Service service, OutputStream out) throws IOException {
		write(service, new OutputStreamWriter(out, "UTF8"));	
	}
	
	/* (non-Javadoc)
	 * @see org.mindswap.owls.io.OWLSWriter#write(org.mindswap.owls.service.Service, java.io.Writer)
	 */
	public void write(Service service, Writer writer) {
		out = new PrintWriter(writer);
//		written = new HashSet();
		processes = new ArrayList();
		
		processes.add(service.getProcess());		
		for(int i = 0; i < processes.size(); i++)
			writeProcess((Process) processes.get(i));		
	}
	
	private void writeProcess(Process process) {
		if(process instanceof AtomicProcess) 
			writeAtomicProcess((AtomicProcess) process);
		else if(process instanceof CompositeProcess) 
			writeCompositeProcess((CompositeProcess) process);
	}	

	private void writeCompositeProcess(CompositeProcess process) {			
		ControlConstruct construct = process.getComposedOf();

		if(construct instanceof Sequence)
			writeSequenceProcess(process);
	}
	
	private Map createValueMap(Process process) {
		Map valueMap = new HashMap();

		InputList inputs = process.getInputs();
		for(int i = 0; i < inputs.size(); i++) {		
			Parameter p = inputs.parameterAt(i);
			valueMap.put(p, getSHOPAbbr(p));
		}
		
		OutputList outputs = process.getOutputs();
		for(int i = 0; i < outputs.size(); i++) {		
			Parameter p = outputs.parameterAt(i);
			valueMap.put(p, getSHOPAbbr(p));
		}			
		
		return valueMap;
	}

	private void writeTask(Process process){
		writeTask(process, new HashMap());
	}
	
	private void writeTask(Process process, Map valueMap){
		out.print("(" + getSHOPAbbr(process) + " ");
		
		InputList inputs = process.getInputs();
		for(int i = 0; i < inputs.size(); i++) {		
			Parameter p = inputs.parameterAt(i);
			String mappedValue = (String) valueMap.get(p);
			out.print(mappedValue + " ");
		}
		
		OutputList outputs = process.getOutputs();
		for(int i = 0; i < outputs.size(); i++) {		
			Parameter p = outputs.parameterAt(i);
			String mappedValue = (String) valueMap.get(p);
			out.print(mappedValue + " ");
		}
		out.println(")");
	}
	
	private void writeSequenceProcess(CompositeProcess process) {
		out.print("(:method");
		writeTask(process);
		writePreconditions(process);
		
		Map valueMap = createValueMap(process);
		out.println("(:ordered");

		Sequence sequence = (Sequence) process.getComposedOf();
		ControlConstructList components = sequence.getComponents();
		for(int i = 0; i < components.size(); i++){
			ControlConstruct p = components.constructAt(i);
			if(p instanceof Process)
				writeTask((Process) p, valueMap);
			else
				throw new RuntimeException("Cannot handle nested control constructs inside a sequence!");
		}
	} 	

	
	private void writeAtomicProcess(AtomicProcess process) {
		out.print("(");
		writeTask(process);
		writePreconditions(process);
		writeEffects(process);
	}
	
	private void writePreconditions(Process process) {
		out.println("()");
	}

	private void writeEffects(Process process) {
		out.println("()");
	}
}
