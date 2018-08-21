package test;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.Iterator;

import org.mindswap.owl.OWLFactory;
import org.mindswap.owl.OWLKnowledgeBase;
import org.mindswap.owls.generic.list.OWLSObjList;
import org.mindswap.owls.process.CompositeProcess;
import org.mindswap.owls.process.ControlConstructList;
import org.mindswap.owls.process.Input;
import org.mindswap.owls.process.Output;
import org.mindswap.owls.process.Perform;
import org.mindswap.owls.process.Sequence;
import org.mindswap.owls.service.Service;
import org.mindswap.utils.URIUtils;

public class RemovalTests {
	String uri = "http://example.com/";
	private OWLKnowledgeBase kb;
	private Perform p1;
	private Perform p2;
	private Perform p3;
	private Perform p4;
	
	public static void main(String[] args) {
		RemovalTests test = new RemovalTests();
		test.pelletErrorTest();
		test.removeCCTest();
		test.removeList();
	}

	public RemovalTests() {
		kb = OWLFactory.createKB();
		kb.setReasoner("Pellet");		
	}
	
	private void pelletErrorTest() {
		kb.getReader().getCache().setLocalCacheDirectory("E://Workspaces//NExT//Ontologies//ont_cache");
		kb.getReader().getCache().setForced(true);
		
		Service service;
		try {
			service = kb.readService("http://www.ifi.unizh.ch/ddis/ont/next/ProcessSpace/Projectgigimgii.owl#gigimgii");
			service.getOntology().write(System.out);
			service.deleteProcess();
			service.getOntology().write(System.out);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	private void removeCCTest() {				
		String uri = "http://example.com/p";
		
		p1 = kb.createPerform(URIUtils.createURI(uri + "1"));		
		p2 = kb.createPerform(URIUtils.createURI(uri + "2"));		
		p3 = kb.createPerform(URIUtils.createURI(uri + "3"));			
		p4 = kb.createPerform(URIUtils.createURI(uri + "4"));	
		
		Sequence sequence = kb.createSequence(URIUtils.createURI(uri + "sequence"));
		sequence.addComponent(p4);
		sequence.addComponent(p3);
		sequence.addComponent(p2);
		sequence.addComponent(p1);
		
		Input in1 = kb.createInput(URIUtils.createURI(uri + "in1"));
		Input in2 = kb.createInput(URIUtils.createURI(uri + "in2"));
		Output out1 = kb.createOutput(URIUtils.createURI(uri + "out1"));
		
		CompositeProcess process = kb.createCompositeProcess(URIUtils.createURI(uri + "process"));
		process.setComposedOf(sequence);
		process.addInput(in1);
		process.addInput(in2);
		process.addOutput(out1);
		
		Service service = kb.createService(URIUtils.createURI(uri + "Service"));
		service.setProcess(process);
		
		service.deleteProcess();
		
		kb.write(System.out);
	}	
	
	private void printSizeAndMembers(OWLSObjList list) {
		System.out.println("List size " + list.size());
		System.out.println("List members:");
		Iterator iter = list.iterator();
		while (iter.hasNext()) {
			System.out.println("\t" + iter.next().toString());
		}
	}
	
	private void removeList() {
		String uri = "http://example.com/p";
		
		p1 = kb.createPerform(URIUtils.createURI(uri + "1"));		
		p2 = kb.createPerform(URIUtils.createURI(uri + "2"));		
		p3 = kb.createPerform(URIUtils.createURI(uri + "3"));			
		p4 = kb.createPerform(URIUtils.createURI(uri + "4"));	
		
		Sequence sequence = kb.createSequence(URIUtils.createURI(uri + "sequence"));
		sequence.addComponent(p4);
		sequence.addComponent(p3);
		sequence.addComponent(p2);
		sequence.addComponent(p1);			
		
		sequence.removeConstruct(p4);
		sequence.removeConstruct(p2);
		
		ControlConstructList list = sequence.getComponents();		
		printSizeAndMembers(list);
		System.out.println("---------------------");
		kb.write(System.out);
	}
}
