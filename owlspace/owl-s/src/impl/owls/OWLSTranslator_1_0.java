//The MIT License
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
 * Created on Dec 27, 2003
 *
 */
package impl.owls;

import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.mindswap.owl.OWLClass;
import org.mindswap.owl.OWLDataProperty;
import org.mindswap.owl.OWLDataValue;
import org.mindswap.owl.OWLDataValueList;
import org.mindswap.owl.OWLFactory;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLIndividualList;
import org.mindswap.owl.OWLKnowledgeBase;
import org.mindswap.owl.OWLObjectProperty;
import org.mindswap.owl.OWLOntology;
import org.mindswap.owl.OWLProperty;
import org.mindswap.owl.OWLType;
import org.mindswap.owl.list.RDFList;
import org.mindswap.owl.vocabulary.RDFS;
import org.mindswap.owls.OWLSVersionTranslator;
import org.mindswap.owls.generic.list.OWLSObjList;
import org.mindswap.owls.grounding.AtomicGrounding;
import org.mindswap.owls.grounding.Grounding;
import org.mindswap.owls.grounding.UPnPAtomicGrounding;
import org.mindswap.owls.grounding.WSDLAtomicGrounding;
import org.mindswap.owls.process.AnyOrder;
import org.mindswap.owls.process.AtomicProcess;
import org.mindswap.owls.process.Choice;
import org.mindswap.owls.process.CompositeProcess;
import org.mindswap.owls.process.Condition;
import org.mindswap.owls.process.Conditional;
import org.mindswap.owls.process.ControlConstruct;
import org.mindswap.owls.process.IfThenElse;
import org.mindswap.owls.process.Input;
import org.mindswap.owls.process.Iterate;
import org.mindswap.owls.process.MultiConditional;
import org.mindswap.owls.process.Output;
import org.mindswap.owls.process.Parameter;
import org.mindswap.owls.process.ParameterList;
import org.mindswap.owls.process.Perform;
import org.mindswap.owls.process.Process;
import org.mindswap.owls.process.RepeatUntil;
import org.mindswap.owls.process.RepeatWhile;
import org.mindswap.owls.process.Result;
import org.mindswap.owls.process.Sequence;
import org.mindswap.owls.process.SimpleProcess;
import org.mindswap.owls.process.Split;
import org.mindswap.owls.process.SplitJoin;
import org.mindswap.owls.profile.Profile;
import org.mindswap.owls.service.Service;
import org.mindswap.owls.vocabulary.FLAServiceOnt;
import org.mindswap.owls.vocabulary.OWLS_1_0;
import org.mindswap.owls.vocabulary.OWLS_1_1;

/**
 * @author Evren Sirin
 *
 */
public class OWLSTranslator_1_0 implements OWLSVersionTranslator {
	public static boolean DEBUG = false;
	
	private OWLKnowledgeBase kb;
	private OWLOntology inputOnt;
	private OWLOntology outputOnt;
	/*******************************************
	 ** added by guang huang @2005-4-12 **
	 ********************************************/
	private List profileInputURIs = new Vector();
	/*******************************************
	 ** end by guang huang **
	 ********************************************/
	private Map translation;
	
	/**
	 * 
	 */
	public OWLSTranslator_1_0() {
	}
	
	public String getVersion() {
		return "1.0";
	}
	
	public boolean canTranslate(OWLOntology ontology) {
	    return ontology.getInstances(OWLS_1_0.Service.Service).size() > 0;
	}
	    
	public OWLOntology translate(OWLOntology ont) {
	    kb = OWLFactory.createKB();
	    inputOnt = kb.load(ont);
		outputOnt = OWLFactory.createOntology(ont.getURI());
				
		translation = new HashMap();

		translateServices();
		translateProfiles();
		translateProcesses();
		translateGroundings();
		translateAtomicGroundings();
		
        for(Iterator i = ont.getImports().iterator(); i.hasNext(); ) {
            OWLOntology imported = (OWLOntology) i.next();
            outputOnt.addImport(imported);
        }
        
        outputOnt.setTranslationSource(inputOnt);
        /*******************************************
		 ** added by guang huang @2005-4-8 **
		 *corrrect source version bug
		 ********************************************/
        if (canTranslate(ont))
        	outputOnt.addType(ont.getInstances(OWLS_1_0.Service.Service).individualAt(0), OWLS_1_0.Service.Service);
        /*******************************************
		 ** end by guang huang **
		 ********************************************/
		return outputOnt;
	}
	
	private void translateServices() {
		OWLIndividualList list = inputOnt.getInstances(OWLS_1_0.Service.Service);
		for(int i = 0; i < list.size(); i++) {
			OWLIndividual serviceInfo = list.individualAt(i);
			createService(serviceInfo);
		}	    
	}
	
	private void translateProfiles() {
		OWLIndividualList list = inputOnt.getInstances(OWLS_1_0.Profile.Profile);
		for(int i = 0; i < list.size(); i++) {
			OWLIndividual profileInfo = list.individualAt(i);
			createProfile(profileInfo);
		}
	}
	
	private void translateProcesses() {
	    // we are not using reasoning so loop through all possible process
	    // instances
	    OWLClass[] processClass = {
	        OWLS_1_0.Process.AtomicProcess, 
	        OWLS_1_0.Process.CompositeProcess, 
	        OWLS_1_0.Process.SimpleProcess};
	    for(int i = 0; i < processClass.length; i++) {
			OWLIndividualList list = inputOnt.getInstances(processClass[i]);
			for(int j = 0; j < list.size(); j++) {
				OWLIndividual processInfo = list.individualAt(j);
				createProcess(processInfo);
			}	 
	    }
	}
	
	private void translateGroundings() {	    
	    OWLClass[] groundingClass = {	
	        OWLS_1_0.Grounding.WsdlGrounding, 
	        FLAServiceOnt.UPnPGrounding};
	    for(int i = 0; i < groundingClass.length; i++) {
	        OWLIndividualList list = inputOnt.getInstances(groundingClass[i]);
			for(int j = 0; j < list.size(); j++) {
				OWLIndividual groundingInfo = list.individualAt(j);
				createGrounding(groundingInfo);
			}
	    }
	}
	
	private void translateAtomicGroundings() {
	    OWLClass[] groundingClass = {
	        OWLS_1_0.Grounding.WsdlAtomicProcessGrounding, 
	        FLAServiceOnt.UPnPAtomicProcessGrounding};
	    for(int i = 0; i < groundingClass.length; i++) {
	        OWLIndividualList list = inputOnt.getInstances(groundingClass[i]);
			for(int j = 0; j < list.size(); j++) {
				OWLIndividual groundingInfo = list.individualAt(j);
				createAPGrounding(groundingInfo);
			}	    
	    }
	}
	
	private Perform getCachedPerform(Process process, OWLIndividual ind) {	    
		List components = ((CompositeProcess)process).getComposedOf().getConstructs();
		Iterator i = components.iterator();
		while(i.hasNext()) {
			ControlConstruct cc = (ControlConstruct) i.next();
			if(cc instanceof Perform && ((Perform) cc).getProcess().equals(ind)) {
			    return (Perform) cc;
			}
		}
		
		return null;
	}

	private OWLIndividual translate(OWLIndividual ind, OWLClass owlClass) {
	    OWLIndividual translated = (OWLIndividual) translation.get(ind);
	    if(translated == null) {
	        if(ind.isAnon())
	            translated = outputOnt.createInstance(owlClass);
	        else
	            translated = outputOnt.createInstance(owlClass, ind.getURI());
	        if(owlClass != null)
	            translated.addType(owlClass);
	        translation.put(ind, translated);
	    }
	    
	    return translated;
	}
	
	private OWLIndividual translate(OWLIndividual ind, Class javaClass, OWLClass owlClass) {
	    OWLIndividual translated = (OWLIndividual) translation.get(ind);
	    if(translated == null) {
	        OWLIndividual newInd;
	        if(ind.isAnon())
	            newInd = outputOnt.createInstance(owlClass);
	        else
	            newInd = outputOnt.createInstance(owlClass, ind.getURI());
	        translated = (OWLIndividual) newInd.castTo(javaClass);
	        translation.put(ind, translated);
	    }
	    
	    return translated;
	}
	
	private Service createService(OWLIndividual serviceInfo) {
	    try{ 
		    OWLIndividual translated = (OWLIndividual) translation.get(serviceInfo);
		    if(translated != null) {
		        return (Service) translated.castTo(Service.class);
		    }
		    
			Service service = (Service) translate(serviceInfo, Service.class, OWLS_1_1.Service.Service);
	
		    OWLIndividual profileInfo = inputOnt.getProperty(serviceInfo, OWLS_1_0.Service.presents);
			OWLIndividual processModelInfo = inputOnt.getProperty(serviceInfo, OWLS_1_0.Service.describedBy);
			OWLIndividual groundingInfo = inputOnt.getProperty(serviceInfo, OWLS_1_0.Service.supports);
			
			Process process = createProcessModel(processModelInfo);
			Profile profile = createProfile(profileInfo);		
			Grounding grounding = createGrounding(groundingInfo);
			
			service.setProcess(process);
			service.setProfile(profile);
			service.setGrounding(grounding);
			translateAll(serviceInfo);			
			
			return service;
		} catch (RuntimeException e) {
			error("Invalid service description");
			e.printStackTrace();
			return null;
		}
	}
		
	private Process createProcessModel(OWLIndividual processModelInfo) {
	    OWLIndividual processInfo = inputOnt.getProperty(processModelInfo, OWLS_1_0.Process.hasProcess);
		return createProcess(processInfo);
	}
	
	private Process createProcess(OWLIndividual processInfo) { 
	    try {
		    OWLIndividual translated = (OWLIndividual) translation.get(processInfo);
		    if(translated != null) {
		        return (Process) translated.castTo(Process.class);
		    }
		    
			Process process = null;
			if(inputOnt.isType(processInfo, OWLS_1_0.Process.AtomicProcess))
				process = createAtomicProcess(processInfo);
			else if(inputOnt.isType(processInfo, OWLS_1_0.Process.CompositeProcess))
				process = createCompositeProcess(processInfo);
			else if(inputOnt.isType(processInfo, OWLS_1_0.Process.SimpleProcess))
				process = createSimpleProcess(processInfo);
			else {
			    error("Unknown process type " + processInfo);
			    return null;
			}
					
			copyPropertyValues(processInfo, OWLS_1_0.Process.name, process, OWLS_1_1.Process.name);
			
			createProcessParams(process, true, processInfo);		
			createProcessParams(process, false, processInfo);
			
			createCondition(process, inputOnt.getProperties(processInfo, OWLS_1_0.Profile.hasPrecondition));
					
			createDataFlow(process, processInfo);
			
			return process;
		} catch (RuntimeException e) {
			error("Invalid process description");
			e.printStackTrace();
			return null;
		}
	}
	
	private AtomicProcess createAtomicProcess(OWLIndividual processInfo) {	
		AtomicProcess process =	(AtomicProcess) translate(processInfo, AtomicProcess.class, OWLS_1_1.Process.AtomicProcess);
		
		return process;
	}
	
	private SimpleProcess createSimpleProcess(OWLIndividual processInfo) {	
	    SimpleProcess process =	(SimpleProcess) translate(processInfo, SimpleProcess.class, OWLS_1_1.Process.SimpleProcess);
		
	    // FIXME translate SimpleProcess properties
	    
		return process;
	}

	private CompositeProcess createCompositeProcess(OWLIndividual processInfo) {
	    CompositeProcess process = (CompositeProcess) translate(processInfo, CompositeProcess.class, OWLS_1_1.Process.CompositeProcess);
	    
	    OWLIndividual composedInfo = inputOnt.getProperty(processInfo, OWLS_1_0.Process.composedOf);
		
		if(composedInfo == null) {
			error("Cannot find the components for composite process (" + 
				  "\n      process: " + processInfo + ")");	
		}
		else {		
			ControlConstruct controlConstruct = createControlConstruct(composedInfo);
			
			process.setComposedOf(controlConstruct);
		}
		
		return process;   
	}	
		
	private boolean isProcess(OWLIndividual processInfo) {
		return inputOnt.isType(processInfo, OWLS_1_0.Process.AtomicProcess) ||
			   inputOnt.isType(processInfo, OWLS_1_0.Process.CompositeProcess) ||
			   inputOnt.isType(processInfo, OWLS_1_0.Process.SimpleProcess);
	}
	
	private ControlConstruct createControlConstruct(OWLIndividual ccInfo) {
		OWLClass ccType = ccInfo.getType();
		
		ControlConstruct cc = null;
		if(isProcess(ccInfo))
			cc = createPerform(ccInfo);
		else if(inputOnt.isType(ccInfo, OWLS_1_0.Process.Sequence))
			cc = createSequence(ccInfo);
		else if(inputOnt.isType(ccInfo, OWLS_1_0.Process.Sequence))
			cc = createSequence(ccInfo);
		else if(inputOnt.isType(ccInfo, OWLS_1_0.Process.Split))
			cc = createSplit(ccInfo);
		else if(inputOnt.isType(ccInfo, OWLS_1_0.Process.SplitJoin))
			cc = createSplitJoin(ccInfo);
		else if(inputOnt.isType(ccInfo, OWLS_1_0.Process.Unordered))
			cc = createAnyOrder(ccInfo);
		else if(inputOnt.isType(ccInfo, OWLS_1_0.Process.IfThenElse))
			cc = createIfThenElse(ccInfo);
		else if(inputOnt.isType(ccInfo, OWLS_1_0.Process.Choice))
			cc = createChoice(ccInfo);
		else if(inputOnt.isType(ccInfo, OWLS_1_0.Process.Iterate))
			cc = createIterate(ccInfo);
		else if(inputOnt.isType(ccInfo, OWLS_1_0.Process.RepeatUntil))
			cc = createRepeatUntil(ccInfo);
		else if(inputOnt.isType(ccInfo, OWLS_1_0.Process.RepeatWhile))
			cc = createRepeatWhile(ccInfo);
		else
			error("Don't know how to translate the control construct " + ccType);
		
		return cc;
	}	
	
	private Perform createPerform(OWLIndividual processInfo) {
		Perform perform = outputOnt.createPerform();
		
		Process process = createProcess(processInfo);		
		perform.setProcess(process);
		
		return perform;
	}
	
	private Sequence createSequence(OWLIndividual sequenceInfo) {
		Sequence sequence = (Sequence) translate(sequenceInfo, Sequence.class, OWLS_1_1.Process.Sequence);
		
		createComponents(sequence, sequenceInfo);		
		
		return sequence;
	}	

	private IfThenElse createIfThenElse(OWLIndividual ifThenElseInfo) {
	    IfThenElse ifThenElse = (IfThenElse) translate(ifThenElseInfo, IfThenElse.class, OWLS_1_1.Process.IfThenElse);
	    
		if(inputOnt.hasProperty(ifThenElseInfo, OWLS_1_0.Process.ifCondition)) {
			createCondition(ifThenElse, inputOnt.getProperties(ifThenElseInfo, OWLS_1_0.Process.ifCondition));
		}
		else
			error("If-Then-Else does not have a process:ifCondition associated with it (" + 
				"\n     ifThenElse: " + ifThenElseInfo + ")");
		
		if(inputOnt.hasProperty(ifThenElseInfo, OWLS_1_0.Process.thenP)) {
			OWLIndividual thenComponentInfo = inputOnt.getProperty(ifThenElseInfo, OWLS_1_0.Process.thenP);
			ControlConstruct thenComponent = createControlConstruct(thenComponentInfo);
			if(thenComponent != null)
			    ifThenElse.setThen(thenComponent);
			else
				error("If-Then-Else has an invalid process:then associated with it (" + 
						"\n     ifThenElse: " + ifThenElseInfo + ")");
		}
		else
			error("If-Then-Else does not have a process:then associated with it (" + 
				"\n     ifThenElse: " + ifThenElseInfo + ")");
	
		if(inputOnt.hasProperty(ifThenElseInfo, OWLS_1_0.Process.elseP)) {
			OWLIndividual elseComponentInfo = inputOnt.getProperty(ifThenElseInfo, OWLS_1_0.Process.elseP);
			ControlConstruct elseComponent = createControlConstruct(elseComponentInfo);
			if(elseComponent != null)
			    ifThenElse.setThen(elseComponent);
			else
				error("If-Then-Else has an invalid process:else associated with it (" + 
						"\n     ifThenElse: " + ifThenElseInfo + ")");
		}
		
		return ifThenElse;
	}

    private Split createSplit(OWLIndividual splitInfo) {
		Split split = (Split) translate(splitInfo, Split.class, OWLS_1_1.Process.Split);
		
		createComponents(split, splitInfo);		
		
		return split;
	}	
	
	private SplitJoin createSplitJoin(OWLIndividual splitInfo) {
		SplitJoin split = (SplitJoin) translate(splitInfo, SplitJoin.class, OWLS_1_1.Process.SplitJoin);
		
		createComponents(split, splitInfo);		
		
		return split;
	}	
		
	private AnyOrder createAnyOrder(OWLIndividual anyOrderInfo) {
	    AnyOrder unordered = (AnyOrder) translate(anyOrderInfo, AnyOrder.class, OWLS_1_1.Process.AnyOrder);
		
		createComponents(unordered, anyOrderInfo);		
		
		return unordered;
	}		

	private Iterate createIterate(OWLIndividual iterateInfo) {
	    Iterate iterate = (Iterate) translate(iterateInfo, Iterate.class, OWLS_1_1.Process.Iterate);
		
//		createComponents(iterate, iterateInfo);		
//		if(iterate.getComponents().size() > 1)
//		    error("Iterate should have only one component " + iterateInfo);    
		
		return iterate;
	}

	private RepeatUntil createRepeatUntil(OWLIndividual repeatInfo) {
	    RepeatUntil repeat = (RepeatUntil) translate(repeatInfo, RepeatUntil.class, OWLS_1_1.Process.RepeatUntil);

		if(inputOnt.hasProperty(repeatInfo, OWLS_1_0.Process.untilCondition)) {
			createCondition(repeat, inputOnt.getProperties(repeatInfo, OWLS_1_0.Process.untilCondition));
		}
		else
			error("RepeatUntil does not have a process:untilCondition associated with it (" + 
				"\n     RepeatUntil: " + repeat + ")");
		
		if(inputOnt.hasProperty(repeatInfo, OWLS_1_0.Process.components)) {
		    error("RepeatUntil cannot have a process:components property. Use process:untilProcess instead!");
		}
		
		if(inputOnt.hasProperty(repeatInfo, OWLS_1_0.Process.untilProcess)) {
			OWLIndividual untilComponentInfo = inputOnt.getProperty(repeatInfo, OWLS_1_0.Process.untilProcess);
			ControlConstruct untilComponent = createControlConstruct(untilComponentInfo);
			if(untilComponent != null)
			    repeat.setComponent(untilComponent);
			else
				error("RepeatUntil has an invalid process:untilProcess associated with it (" + 
						"\n     RepeatUntil: " + repeatInfo + ")");
		}
		else
			error("RepeatUntil does not have a process:untilProcess associated with it (" + 
				"\n     RepeatUntil: " + repeat + ")");

		return repeat;
	}
	
	private RepeatWhile createRepeatWhile(OWLIndividual repeatInfo) {
	    RepeatWhile repeat = (RepeatWhile) translate(repeatInfo, RepeatWhile.class, OWLS_1_1.Process.RepeatWhile);

		if(inputOnt.hasProperty(repeatInfo, OWLS_1_0.Process.whileCondition)) {
			createCondition(repeat, inputOnt.getProperties(repeatInfo, OWLS_1_0.Process.whileCondition));
		}
		else
			error("RepeatUntil does not have a process:ifCondition associated with it (" + 
				"\n     RepeatWhile: " + repeat + ")");
		
		if(inputOnt.hasProperty(repeatInfo, OWLS_1_0.Process.components)) {
		    error("RepeatWhile cannot have a process:components property. Use process:untilProcess instead!");
		}
		
		if(inputOnt.hasProperty(repeatInfo, OWLS_1_0.Process.whileProcess)) {
			OWLIndividual whileComponentInfo = inputOnt.getProperty(repeatInfo, OWLS_1_0.Process.whileProcess);
			ControlConstruct whileComponent = createControlConstruct(whileComponentInfo);
			if(whileComponent != null)
			    repeat.setComponent(whileComponent);
			else
				error("RepeatWhile has an invalid process:whileProcess associated with it (" + 
						"\n     RepeatWhile: " + repeatInfo + ")");
		}
		else
			error("RepeatWhile does not have a process:whileProcess associated with it (" + 
				"\n     RepeatWhile: " + repeat + ")");

		return repeat;
	}
	
	private Choice createChoice(OWLIndividual choiceInfo) {
		Choice choice = (Choice) translate(choiceInfo, Choice.class, OWLS_1_1.Process.Choice);
		
		createComponents(choice, choiceInfo);		
		
		return choice;
	}
	
	private void createComponents(ControlConstruct cc, OWLIndividual ccInfo) {	
		OWLIndividual componentsInfo = inputOnt.getProperty(ccInfo, OWLS_1_0.Process.components);

	    if(componentsInfo == null) {
			error(cc.getConstructName() + " construct does not have any components associated with it (" + 
				"\n     Construct: " + ccInfo + ")");	        
			return;
	    }
		
		OWLSObjList list = (OWLSObjList) componentsInfo.castTo(RDFList.class);
		Iterator i = list.iterator();
		while(i.hasNext()) {
			OWLIndividual componentInfo = (OWLIndividual) i.next();
			ControlConstruct component = createControlConstruct(componentInfo);
			
			if(component == null)
				warning("There is an invalid component description for " + cc);
			else if(cc instanceof Sequence)
				((Sequence)cc).addComponent(component);
			else if(cc instanceof AnyOrder)
				((AnyOrder)cc).addComponent(component);
			else if(cc instanceof Choice)
				((Choice)cc).addComponent(component);
			else if(cc instanceof Split)
				((Split)cc).addComponent(component);
			else if(cc instanceof SplitJoin)
				((SplitJoin)cc).addComponent(component);
			else
			    throw new RuntimeException("Invalid control construct!");
		}
	}	
	
	private void createDataFlow(Process process, OWLIndividual processComponentInfo) {
		try {	
			OWLIndividualList list = inputOnt.getProperties(processComponentInfo, OWLS_1_0.Process.sameValues);
			for(int i = 0; i < list.size(); i++) {                
				OWLSObjList sameValuesList = (OWLSObjList) list.individualAt(i).castTo(RDFList.class);
				OWLIndividual value1 = sameValuesList.get(0);
				OWLIndividual value2 = sameValuesList.get(1);
					
				OWLIndividual p1 = inputOnt.getProperty(value1, OWLS_1_0.Process.theParameter);
				OWLIndividual p2 = inputOnt.getProperty(value2, OWLS_1_0.Process.theParameter);
				
				Parameter param1 = (Parameter) translation.get(p1);
				Parameter param2 = (Parameter) translation.get(p2);
				
				OWLIndividual process1 = inputOnt.getProperty(value1, OWLS_1_0.Process.atProcess);
				OWLIndividual process2 = inputOnt.getProperty(value2, OWLS_1_0.Process.atProcess);
									
				if(process1.equals(process) && process2.equals(process)) {
				    // FIXME handle this case using ThisPerform
				    error("Same process used twice in the ValueOf!");
				}
				else if(process1.equals(process)) {
				    Perform perform = getCachedPerform(process, process2);
				    if(param1 instanceof Input) {
				        // add binding to the perform				        
				        perform.addBinding((Input) param1, Perform.TheParentPerform, (Input) param2);
				    }
				    else {
				        // add result to the process
				        Result result = process.getResult();
				        if(result == null) {
				            result = outputOnt.createResult();
				            process.setResult(result);
				        }
				        
				        result.addBinding((Output) param1, perform, (Output) param2);
				    }
				}
				else if(process2.equals(process)) {
				    Perform perform = getCachedPerform(process, process1);
				    if(param2 instanceof Input) {
				        // add binding to the perform				        
				        perform.addBinding((Input) param2, Perform.TheParentPerform, (Input) param1);
				    }
				    else {
				        // add result to the process
				        Result result = process.getResult();
				        if(result == null) {
				            result = outputOnt.createResult();
				            process.setResult(result);
				        }
				        
				        result.addBinding((Output) param2, perform, (Output) param1);
				    }
				}
				else {
				    Perform perform1 = getCachedPerform(process, process1);
				    Perform perform2 = getCachedPerform(process, process2);
				    if(param1 instanceof Input) {
				        // add binding to the perform1		        
				        perform1.addBinding((Input) param1, perform2, (Output) param2);
				    }
				    else {
				        // add binding to the perform1		        
				        perform2.addBinding((Input) param2, perform1, (Output) param1);
				    }
				}	
			}
		} catch(Exception e) {
			error("Invalid data flow specification ");
			e.printStackTrace();
		}
	}	

    private Parameter createParam(OWLIndividual paramInfo) {	        
    	    OWLIndividual translated = (OWLIndividual) translation.get(paramInfo);
    	    if(translated != null) {
    	        return (Parameter) translated.castTo(Parameter.class);
    	    }
    	    
			Parameter param = null;
			if(inputOnt.isType(paramInfo, OWLS_1_0.Process.Input))
			    param = (Parameter) translate(paramInfo, Input.class, OWLS_1_1.Process.Input);
			else if(inputOnt.isType(paramInfo, OWLS_1_0.Process.Output))
			    param = (Parameter) translate(paramInfo, Output.class, OWLS_1_1.Process.Output);
			else {
			    error("Unknown parameter type " + paramInfo);
			    return null;
			}
			
			if( inputOnt.hasProperty(paramInfo, OWLS_1_0.Process.parameterType)) {
				URI typeURI = inputOnt.getProperty(paramInfo, OWLS_1_0.Process.parameterType).getURI();
				if(typeURI != null) {					
					OWLType type = inputOnt.getType(typeURI);
					if(type == null)
					    type = outputOnt.createClass(typeURI);
					
					param.setParamType(type);
				}
				else {
				    // FIXME default values
				}
			}
			else
				error("Cannot find the type for the process parameter (" + 
						"\n    parameter: " + param + ")");		
			/*******************************************
			 ** added by guang huang @2005-4-8 **
			 *Keep <fla:useRandomInput>true</fla:useRandomInput> in 
			 *process:Input or process:Onput 
			 ********************************************/
			keepFLAProperties(paramInfo);
			if(inputOnt.isType(paramInfo, OWLS_1_0.Process.Input)) {
				try {
					if ((!profileInputURIs.contains(param.getURI())) && (paramInfo.getProperties().size() <= 1)) {
						param.addProperty(FLAServiceOnt.useRandomInput, "true");
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			
			/*******************************************
			 ** end by guang huang **
			 ********************************************/
			return param;
	}	

    private void createProcessParams(Process process, boolean isInput, OWLIndividual processInfo) {	
		OWLObjectProperty prop = isInput ? OWLS_1_0.Process.hasInput : OWLS_1_0.Process.hasOutput;		
		OWLIndividualList list = inputOnt.getProperties(processInfo, prop);
		for(int i = 0; i < list.size(); i++) {
			OWLIndividual p = list.individualAt(i);

			Parameter param = null;
			if(isInput) {
				param = createParam(p);
				process.addInput((Input) param);
			}
			else {
			    param = createParam(p);
				process.addOutput((Output) param);
			}		
			
			copyPropertyValues(p, RDFS.label, param, RDFS.label);
			
			if(DEBUG) {
				System.out.println("  Process   " + process.getURI() + "\n" + 
								   (isInput ? "  Input     ":"  Output    " ) +
								   param.getURI() + "\n" + 
								   "  Type      " + param.getParamType() + "\n");
			}			
		}
	}	
	
    private Profile createProfile(OWLIndividual profileInfo) {
		try {
		    OWLIndividual translated = (OWLIndividual) translation.get(profileInfo);
		    if(translated != null) {
		        return (Profile) translated.castTo(Profile.class);
		    }
		    		    
			Profile profile = (Profile) translate(profileInfo, Profile.class, OWLS_1_1.Profile.Profile);
			
			copyPropertyValues(profileInfo, OWLS_1_0.Profile.serviceName, profile, OWLS_1_1.Profile.serviceName);
			copyPropertyValues(profileInfo, RDFS.label, profile, RDFS.label);
			copyPropertyValues(profileInfo, OWLS_1_0.Profile.textDescription, profile,
			    OWLS_1_1.Profile.textDescription);
			
			createProfileParams(profile, true, profileInfo);
			createProfileParams(profile, false, profileInfo);
			
			createCondition(profile, inputOnt.getProperties(profileInfo, OWLS_1_0.Profile.hasPrecondition));			
			
			createServiceParameters(profile, profileInfo);
			translateAll(profileInfo);
			/*******************************************
			 ** added by guang huang @2005-4-8 **
			 *Keep FLAServiceParameter
			 ********************************************/
			keepFLAProperties(profileInfo);
			/*******************************************
			 ** end by guang huang **
			 ********************************************/
			return profile;
		} catch (RuntimeException e) {
			error("Invalid profile description");
			e.printStackTrace();
			return null;
		}		
	}
	
	private void createServiceParameters(Profile profile, OWLIndividual profileInfo) {
	    Set set = kb.getSubProperties(OWLS_1_0.Profile.serviceParameter);
	    for(Iterator it = set.iterator(); it.hasNext();) {
            OWLObjectProperty prop = (OWLObjectProperty) it.next();
        
		    OWLIndividualList list = profileInfo.getProperties(prop);
			for(int i = 0; i < list.size(); i++) {
				OWLIndividual serviceParamInfo = list.individualAt(i);
				OWLIndividual serviceParam = translate(serviceParamInfo, serviceParamInfo.getType());

				OWLIndividual serviceParamValue = serviceParamInfo.getProperty(OWLS_1_0.Profile.sParameter);
				serviceParamValue = translateAll(serviceParamValue);
				
				profile.addProperty(prop, serviceParam);
				serviceParam.addProperty(OWLS_1_1.Profile.sParameter, serviceParamValue);
				copyPropertyValues(serviceParamInfo, OWLS_1_0.Profile.serviceParameterName, serviceParam, OWLS_1_1.Profile.serviceParameterName);
			}
	    }
	}
	
	private OWLIndividual translateAll(OWLIndividual ind) {
	    OWLIndividual translated = (OWLIndividual) translation.get(ind);
	    if(translated == null) {
	        if(isProcess(ind))
	            return createProcess(ind);
	        
		    translated = translate(ind, ind.getType());
		    Map map = kb.getProperties(ind);
		    for(Iterator it = map.keySet().iterator(); it.hasNext();) {	        
	            OWLProperty prop = (OWLProperty) it.next();
	        
	            copyPropertyValues(ind, RDFS.label, translated, RDFS.label);
	            if(prop instanceof OWLDataProperty)
	                copyPropertyValues(ind, (OWLDataProperty) prop, translated, (OWLDataProperty) prop);
	            else {
	                OWLObjectProperty objProp = (OWLObjectProperty) prop;
				    OWLIndividualList list = inputOnt.getProperties(ind, objProp);
					for(int i = 0; i < list.size(); i++) {
						OWLIndividual value = list.individualAt(i);
						value = translate(value, value.getType());
						
						translated.addProperty(objProp, value);
					}
	            }
		    }
	    }
	    return translated;
	}
	
	private void createProfileParams(Profile profile, boolean isInput, OWLIndividual profileInfo) {
		OWLObjectProperty prop = isInput ? OWLS_1_0.Profile.hasInput : OWLS_1_0.Profile.hasOutput;
	    	
		OWLIndividualList list = inputOnt.getProperties(profileInfo, prop);
		for(int i = 0; i < list.size(); i++) {
			OWLIndividual p = list.individualAt(i);
						
			Parameter refersTo = (Parameter) translation.get(p);
						
			if(refersTo == null) {
				error("The parameter defined in profile does not exist in the process model (" + 
					"\n    parameter: " + p + ", " +
					"\n   in profile: " + profile.getURI() + ")");
			}	
			else {
				if(isInput) {
					profile.addInput((Input) refersTo);
					if (refersTo.getURI() != null) {
						profileInputURIs.add(refersTo.getURI());						
					}
				}
				else
					profile.addOutput((Output) refersTo);
				
				if(DEBUG) {
					System.out.println("  Profile   " + profile.getURI() + "\n" + 
							(isInput ? "  Input     ":"  Output    " ) +
							p.getURI() + "\n" + 
							"  Refers to " + refersTo + "\n");
				}				
			}
		}	
	}
		
	
	private void createCondition(Conditional conditional, OWLIndividualList list) {
	    if(list.size() == 0)
	        return;

	    OWLIndividual conditionInfo = list.individualAt(0);

		Condition condition = (Condition) translate(conditionInfo, Condition.class, OWLS_1_1.Expression.Condition);
		
		conditional.setCondition(condition);
		
	    if(list.size() > 1)
	        error("Multiple conditions defined for " + conditional);
	}
	
	private void createCondition(MultiConditional conditional, OWLIndividualList list) {
		for(int i = 0; i < list.size(); i++) {
		    OWLIndividual conditionInfo = list.individualAt(i);

			Condition condition = (Condition) translate(conditionInfo, Condition.class, OWLS_1_1.Expression.Condition);
			
			conditional.addCondition(condition);
		}
	}

	private Grounding createGrounding(OWLIndividual groundingInfo) {
	    OWLIndividual translated = (OWLIndividual) translation.get(groundingInfo);
	    if(translated != null) {
	        return (Grounding) translated.castTo(Grounding.class);
	    }
	    
		Grounding grounding = (Grounding) translate(groundingInfo, Grounding.class, OWLS_1_1.Grounding.WsdlGrounding);

		OWLIndividualList list = inputOnt.getProperties(groundingInfo, OWLS_1_0.Grounding.hasAtomicProcessGrounding); 
		for(int i = 0; i < list.size(); i++) {
			OWLIndividual apGroundingInfo = list.individualAt(i);
			
			AtomicGrounding apGrounding = createAPGrounding(apGroundingInfo);
			if(apGrounding != null)
				grounding.addGrounding(apGrounding);
			else
				error("Invalid AtomicProcess grounding " + apGroundingInfo);
		}
		list = inputOnt.getProperties(groundingInfo, FLAServiceOnt.hasUPnPAtomicProcessGrounding); 
		for(int i = 0; i < list.size(); i++) {
			OWLIndividual apGroundingInfo = list.individualAt(i);
			
			AtomicGrounding apGrounding = createAPGrounding(apGroundingInfo);
			if(apGrounding != null)
				grounding.addGrounding(apGrounding);
			else
				error("Invalid AtomicProcess grounding " + apGroundingInfo);
		}			
		
		if(grounding.getAtomicGroundings().size() == 0)
			warning("The grounding of the service is empty (" + 
					"\n  grounding " + grounding.getURI() + ")");	
		
		return grounding;
	}	
		
	private AtomicGrounding createAPGrounding(OWLIndividual groundingInfo) {
	    try { 
		    OWLIndividual translated = (OWLIndividual) translation.get(groundingInfo);
		    if(translated != null) {
		        return (AtomicGrounding) translated.castTo(AtomicGrounding.class);
		    }
		    
			AtomicGrounding grounding = null;
			
			if(inputOnt.isType(groundingInfo, OWLS_1_0.Grounding.WsdlAtomicProcessGrounding))		
				grounding =  createWSDLGrounding(groundingInfo);
			else if(inputOnt.isType(groundingInfo, FLAServiceOnt.UPnPAtomicProcessGrounding))
				grounding =  createUPnPGrounding(groundingInfo);
			
			return grounding;
		} catch (RuntimeException e) {
			error("Invalid profile description");
			e.printStackTrace();
			return null;
		}
	}	
	
	private URI getGroundingURI(OWLIndividual groundingInfo, OWLDataProperty p) {
	    OWLDataValue value = inputOnt.getProperty(groundingInfo, p);
	    if(value == null) 
			error(groundingInfo + " does not have a grounding:"
					+ p.getURI().getFragment() + " property");

	    try {
		    return new URI(value.getLexicalValue().trim());
		} catch(Exception e) {
			error("The value of grounding:" + p.getURI().getFragment()
					+ " property (" + value.getLexicalValue() + ") in "
					+ groundingInfo + " is not a valid URI literal");
		}
		
		return null;
	}
	
	private AtomicProcess getGroundingProcess(OWLIndividual groundingInfo) {
	    OWLIndividual processInfo = inputOnt.getProperty(groundingInfo, OWLS_1_0.Grounding.owlsProcess);
	    
		if(!inputOnt.hasProperty(groundingInfo, OWLS_1_0.Grounding.owlsProcess)) {
			error(groundingInfo + " does not have a grounding:owlsProcess property");
			return null;			
		}
		
		AtomicProcess process = (AtomicProcess) createProcess(processInfo);
		
  		if(process == null) {
  			error("The process specified in the grounding cannot be found (" + 
  					"\n    grounding: " + groundingInfo + ", " +
					"\n      process: " + processInfo + ")");
  			return null;
  		}
  		
  		return process;
	}

	private AtomicGrounding createWSDLGrounding(OWLIndividual groundingInfo) {
		AtomicProcess process = getGroundingProcess(groundingInfo);
		URI wsdlLoc = getGroundingURI(groundingInfo, OWLS_1_0.Grounding.wsdlDocument);		
		OWLIndividual operationInfo = inputOnt.getProperty(groundingInfo, OWLS_1_0.Grounding.wsdlOperation);		
		URI opName = getGroundingURI(operationInfo, OWLS_1_0.Grounding.operation);				
		URI portType = getGroundingURI(operationInfo, OWLS_1_0.Grounding.portType);
		
		WSDLAtomicGrounding g = (WSDLAtomicGrounding) translate(groundingInfo, WSDLAtomicGrounding.class, OWLS_1_1.Grounding.WsdlAtomicProcessGrounding); 
		g.setWSDL(wsdlLoc);
		g.setOperation(opName);
		g.setPortType(portType);

		if(inputOnt.hasProperty(groundingInfo, OWLS_1_0.Grounding.wsdlInputMessage))
			g.setInputMessage(getGroundingURI(groundingInfo, OWLS_1_0.Grounding.wsdlInputMessage));		

		if(inputOnt.hasProperty(groundingInfo, OWLS_1_0.Grounding.wsdlOutputMessage))
			g.setOutputMessage(getGroundingURI(groundingInfo, OWLS_1_0.Grounding.wsdlOutputMessage));
		
		if(process != null) {
		    g.setProcess(process);
			createMessageMapList(g, groundingInfo, true);
			createMessageMapList(g, groundingInfo, false);
		}
		else
		    error("No OWL-S parameter defined for AtomicProcessGrounding " + groundingInfo);

		if(DEBUG) {
			System.out.println("  Process   " + process.getURI() + "\n" + 
							   "  WSDL file " + wsdlLoc + "\n" + 
							   "  Operation " + opName + "\n");
		}
		
		return g;
	}
	
	private AtomicGrounding createUPnPGrounding( OWLIndividual groundingInfo) {
		OWLIndividual processInfo = inputOnt.getProperty(groundingInfo, OWLS_1_0.Grounding.owlsProcess);
		String upnpDevice = inputOnt.getProperty(groundingInfo, FLAServiceOnt.upnpDeviceURL).toString();
		String upnpService = inputOnt.getProperty(groundingInfo, FLAServiceOnt.upnpServiceID).toString();
		String upnpAction = inputOnt.getProperty(groundingInfo, FLAServiceOnt.upnpCommand).toString();
		
	    AtomicProcess process = (AtomicProcess) createProcess(processInfo);
		
		UPnPAtomicGrounding g = (UPnPAtomicGrounding) translate(groundingInfo, UPnPAtomicGrounding.class, FLAServiceOnt.UPnPAtomicProcessGrounding); 
		g.setProcess(process);
		g.setUPnPDescription(upnpDevice);
		g.setUPnPService(upnpService);
		g.setUPnPAction(upnpAction);
		
		createMessageMapList(g, groundingInfo, true);
		createMessageMapList(g, groundingInfo, false);

		if(DEBUG) {
			System.out.println("  Process " + process.getURI() + "\n" + 
							   "  Device  " + upnpDevice + "\n" + 
							   "  Service " + upnpService + "\n" +
							   "  Action  " + upnpAction + "\n");
		}
		
		return g;
	}
		
	private void createMessageMapList(AtomicGrounding g, OWLIndividual groundingInfo, boolean isInput) {
		Process process = g.getProcess();	
		OWLObjectProperty messageParts = null;
		OWLObjectProperty olderMessageParts = null;
		OWLDataProperty messagePart = null;
		
		if(g instanceof UPnPAtomicGrounding) {
			messageParts = isInput ? 
				FLAServiceOnt.upnpInputMapping: 
				FLAServiceOnt.upnpOutputMapping;
			olderMessageParts = null;
			messagePart = FLAServiceOnt.upnpParameter;
										
		}
		else if(g instanceof WSDLAtomicGrounding) {
			messageParts = isInput ? 
				OWLS_1_0.Grounding.wsdlInputs: 
				OWLS_1_0.Grounding.wsdlOutputs;
			olderMessageParts = isInput ?
				OWLS_1_0.Grounding.wsdlInputMessageParts: 
			    OWLS_1_0.Grounding.wsdlOutputMessageParts;
			messagePart = OWLS_1_0.Grounding.wsdlMessagePart;
		}
		
		OWLIndividual messageMaps = inputOnt.getProperty(groundingInfo, messageParts);
		// try older property name
		if(messageMaps == null && olderMessageParts != null) {
		    messageMaps = inputOnt.getProperty(groundingInfo, olderMessageParts);
			if(messageMaps != null)
			    warning("Using deprecated property " + olderMessageParts + " instead of " + messageParts);
		}
		
		if(messageMaps == null) {
		    ParameterList params = isInput ? (ParameterList) process.getInputs() : process.getOutputs(); 
		    if(!params.isEmpty()) {
		        warning("No mapping defined for parameters (" +
		                "\n parameters: " + params +
		                "\n    process: " + process + ")");
		    }
			return;		
		}
		/*******************************************
		 ** modified by guang huang @2005-4-14**
		 ********************************************/
		RDFList messageMapList = (RDFList) messageMaps.castTo(RDFList.class);
		/*******************************************
		 ** end by guang huang **
		 ********************************************/
		Iterator i = messageMapList.iterator();
		while(i.hasNext()) {
			OWLIndividual messageMap = (OWLIndividual) i.next();

			URI owlsParameterInfo = inputOnt.getProperty(messageMap, OWLS_1_0.Grounding.owlsParameter).getURI();
			Parameter owlsParameter = isInput ?
				process.getInputs().getParameter(owlsParameterInfo):
				process.getOutputs().getParameter(owlsParameterInfo);

								
			String wsdlMessagePartInfo = inputOnt.getProperty(messageMap, messagePart).toString().trim();

			String transformation = null;
			if(inputOnt.hasProperty(messageMap, OWLS_1_0.Grounding.xsltTransformation)) {
				transformation = inputOnt.getProperty(messageMap, OWLS_1_0.Grounding.xsltTransformation).toString();
			}
			
			
			if(owlsParameter == null) {
				error("Cannot find the target of message map for " + (isInput?"input":"output") + " parameter (" +
					"\n   wsdl parameter: " + wsdlMessagePartInfo + ", " +
					"\n       in process: " + process.getURI() + ", " +
					"\n        mapped to: " + owlsParameterInfo + ")");
			}
			else
				g.addMessageMap(owlsParameter, wsdlMessagePartInfo, transformation);
			
			if(DEBUG) {
				System.out.println("  Process   " + process.getURI() + "\n" + 
								   "  Param     " + owlsParameterInfo + "\n" + 
								   "  Grounding " + wsdlMessagePartInfo + "\n" + 
								   "  Transform " + transformation + "\n");
			}		
		}
	}
	
	private void copyPropertyValues(OWLIndividual src, OWLDataProperty srcProp, OWLIndividual target, OWLDataProperty targetProp) {		
		OWLDataValueList list = inputOnt.getProperties(src, srcProp); 
		for(int i = 0; i < list.size(); i++)
			target.addProperty(targetProp, list.valueAt(i));			
	}
	
	private void copyPropertyValues(OWLIndividual src, URI srcProp, OWLIndividual target, URI targetProp) {		
		OWLDataValueList list = src.getAnnotations(srcProp); 
		for(int i = 0; i < list.size(); i++)
			target.addAnnotation(targetProp, list.valueAt(i));			
	}
	
    private void warning(String string) {
        System.out.println("WARNING: " + string);
    }
    
    private void error(String string) {
        System.out.println("ERROR: " + string);
    }
    
    /*******************************************
	 ** added by guang huang @2005-4-8 **
	 ********************************************/
    private OWLIndividual keepFLAProperties(OWLIndividual ind) {
	    OWLIndividual translated = translateAll(ind);
	    if(translated != null) {    	
		    Map map = ind.getProperties();
		    for(Iterator it = map.keySet().iterator(); it.hasNext();) {	        
	            OWLProperty prop = (OWLProperty) it.next();
	            if(prop instanceof OWLDataProperty) {
	            	if (FLAServiceOnt.flaDataProperties.contains(prop)) {
	            		copyPropertyValues(ind, (OWLDataProperty) prop, translated, (OWLDataProperty) prop);
	            	}
	            } else {
	                OWLObjectProperty objProp = (OWLObjectProperty) prop;
	                if (FLAServiceOnt.flaObjectPropertiesToDataProperties.containsKey(prop)) {
	                	OWLIndividualList list = inputOnt.getProperties(ind, objProp);	                	
						for(int i = 0; i < list.size(); i++) {				
							OWLIndividual serviceParamInfo = list.individualAt(i);
							translated.addProperty((OWLDataProperty)FLAServiceOnt.flaObjectPropertiesToDataProperties.get(prop), serviceParamInfo);
						}
	                } else if (FLAServiceOnt.flaObjectProperties.containsKey(prop)) {
					    OWLIndividualList list = inputOnt.getProperties(ind, objProp);
						for(int i = 0; i < list.size(); i++) {							
							OWLIndividual serviceParamInfo = list.individualAt(i);
							OWLIndividual serviceParam = translate(serviceParamInfo, serviceParamInfo.getType());
					
							OWLIndividual serviceParamValue = serviceParamInfo.getProperty(OWLS_1_0.Profile.sParameter);
							serviceParamValue = translate(serviceParamValue, (OWLClass) FLAServiceOnt.flaObjectProperties.get(objProp));
							
							translated.addProperty(objProp, serviceParam);
							serviceParam.addProperty(OWLS_1_1.Profile.sParameter, serviceParamValue);							
						}
	            	}
		        }
		    }
	    }
	    return translated;	    
    }
    
//    private URI getURI(OWLDataValue value) {
//	    try {
//			return new URI(value.getLexicalValue().trim());
//		} catch (URISyntaxException e) {
//			// TODO Auto-generated catch block
////			e.printStackTrace();
//			try {
//				return new URI("http://fla.flacp.fujitsu.com/" + value.getLexicalValue().trim());
//			} catch (URISyntaxException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//				return null;
//			}
//		}
//    }
    /*******************************************
	 ** end by guang huang **
	 ********************************************/
}
