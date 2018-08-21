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
 * Created on Dec 27, 2003
 *
 */
package org.mindswap.owls.process;

import java.net.URI;

import org.mindswap.owl.OWLDataValueList;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLType;
import org.mindswap.owls.profile.Profile;
import org.mindswap.owls.service.Service;

/**
 * Represents the OWL-S process. It represents the super class for Atomic, Composite and Simple 
 * processes. Properties that all three process classes share are defined here. 
 * 
 * OWL-S concept: http://www.daml.org/services/owl-s/1.0/Process.owl#Process
 * 
 * @author Evren Sirin
 *
 */
public interface Process extends OWLIndividual, MultiConditional {
    public int ANY       = 7;
    public int ATOMIC    = 1;
    public int COMPOSITE = 2;
    public int SIMPLE    = 4;
    
	/**
	 * Set the service this process belongs to. 
	 * 
	 * @param service
	 */
	public void setService(Service service);
	
	/**
	 * Get the service this process belongs to. Actually a process may be used in multiple service
	 * descriptions. Unfortunately, OWL-S 1.0 specification does not make a distinction between
	 * process definition and process occurence. This implementation treats each process object as
	 * a process occurence and returns the service object this process is used in.   
	 * 
	 * @return the service to which this process is bound
	 */
	public Service getService();
	
	/**
	 * Removes the service from this process by breaking the link 
	 * <code>service:describes</code>. The service itself remains untouched.
	 *
	 */
	public void removeService();
	
	/**
	 * Get the profile for the service of this project. This is equivalent to getService().getProfile()
	 * 
	 * @return
	 */
	public Profile getProfile();
	
	public void addInput(Input input);
	
	public Input createInput(URI uri, OWLType paramType);
	
	public void addInputs(InputList inputs);
	
	public void addOutput(Output output);

	public Output createOutput(URI uri, OWLType paramType);		
	
	public void addOutputs(OutputList inputs);
	
	public void addLocal(Local local);
	
	public Local createLocal(URI uri, OWLType paramType);
	
	public void addParameter(Parameter param);

	public void addResult(Result result);
	
	public void setResult(Result result);
			
    public Result createResult();
    
	public Result createResult( URI uri );

	public ParameterList getLocals();
	
	/**
	 * Get the inputs of this process. An empty list is returned if there are no inputs.
	 * 
	 * @return
	 */
	public InputList getInputs();
	
	/**
	 * Return the first input or null if there is no input.
	 * 
	 * @return
	 */
	public Input getInput();
	
	public Input getInput(int i);
	
	public Input getInput(String localName);
	
	/**
	 * Get the outputs of this process. An empty list is returned if there are no outputs.
	 * 
	 * @return
	 */
	public OutputList getOutputs();
	
	/**
	 * Return the first output or null if there is no input.
	 * 
	 * @return
	 */
	public Output getOutput();
	
	public Output getOutput(int i);
	
	public Output getOutput(String localName);
	
	public Result getResult();
	
	public ResultList getResults();
	
	public void removeResult(Result result);
	
	public void removeResults();
	
	/**
	 * Get all the parameters of this process. This list includes inputs, outputs and local parameters.
	 * 
	 * @return
	 */
	public ParameterList getParameters();	
	

	/**
	 * Return the parameter (input or output) with the given URI. First check if input list 
	 * contains the parameter and then check the output list. A null value is returned if 
	 * the given URI does not exist in either list.
	 * 
	 * @param parameterURI
	 * @return
	 */
	public Parameter getParameter(URI parameterURI);
	
	/**
	 * Get the name defined for this process. See {@link org.mindswap.owl.OWLConfig#DEFAULT_LANGS OWLConfig}
	 * to learn how the language identifiers will be resolved when searching for a name. 
	 * 
	 * @return
	 */
	public String getName();
	
	/**
	 * Get the name defined for this process. The associated name should have the same 
	 * language identifier as given in the parameter. If a name for that language is not found 
	 * null value will be returned even if there is another name with a different language 
	 * identifier. 
	 * 
	 * @param lang
	 * @return
	 */
	public String getName(String lang);
	
	/**
	 * Return all process names written in any language.
	 * 
	 * @return
	 */
	public OWLDataValueList getNames();
	
	
	//TODO dmi add this to the OWL-S specification
	/**
	 * Returns the perform to which this process is possibly bound. Attention: This is only a in-memory
	 * property and is not stored at all as OWL-S does not define a link from Process to Perform.
	 * @return the perform to which this process is bound. null, if the process is not bound to any perform
	 */
	public Perform getPerform();
	
	/**
	 * Sets the perform to which this process is bound. Attention: This is only a in-memory
	 * property and is not stored at all as OWL-S does not define a link from Process to Perform.
	 * @param perform the perform to which this process is bound
	 */
	public void setPerform(Perform perform);	
	
	/** 
	 * Removes the given input by breaking the property <code>process:hasInput</code>
	 * The input itself is not touched at all.
	 * 
	 * @param input the input to remove
	 */
	public void removeInput(Input input);
	
	/**
	 * Removes all inputs from the process by breaking the property <code>process:hasInput</code>
	 * The inputs themselves are not touched at all.
	 */
	public void removeInputs();
	
	/**
	 * Removes the given output by breaking the property <code>process:hasOutput</code>
	 * The output itself is not touched at all.
	 * 
	 * @param output the output to remove
	 */
	public void removeOutput(Output output);
	
	/**
	 * Removes all outputs from the process by breaking the property <code>process:hasOutput</code>
	 * The outputs themselves are not touched at all.
	 */
	public void removeOutputs();
	
	/**
	 * Removes the given local by breaking the property <code>process:hasLocal</code>
	 * The local itself is not touched at all.
	 * 
	 * @param output the local to remove
	 */
	public void removeLocal(Local local);
	
	/**
	 * Removes all locals from the process by breaking the property <code>process:hasLocal</code>
	 * The locals themselves are not touched at all.
	 */
	public void removeLocals();		
	
	/**
	 * Deletes the given input from the ontology
	 * 
	 * @param input the input to delete
	 */
	public void deleteInput(Input input);
	
	/**
	 * Deletes all inputs of this process from the ontology
	 */
	public void deleteInputs();
	
	/**
	 * Deletes the given output from the ontology
	 * 
	 * @param output the output to delete
	 */
	public void deleteOutput(Output output);
	
	/**
	 * Deletes all outputs of this process from the ontology
	 */
	public void deleteOutputs();
	
	/**
	 * Deletes the given local from the ontology
	 * 
	 * @param local the local to delete
	 */
	public void deleteLocal(Local local);
	
	/**
	 * Deletes all locals of this process from the ontology
	 */
	public void deleteLocals();		
	
	/**
	 * Deletes the given result from the ontology
	 * 
	 * @param result the result to delete
	 */
	public void deleteResult(Result result);
	
	/**
	 * Deletes all results of this process from the ontology
	 */
	public void deleteResults();		
}
