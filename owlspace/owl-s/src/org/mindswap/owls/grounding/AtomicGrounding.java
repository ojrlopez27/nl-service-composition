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
package org.mindswap.owls.grounding;

import java.net.URL;

import org.mindswap.exceptions.ExecutionException;
import org.mindswap.owl.OWLKnowledgeBase;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owls.process.AtomicProcess;
import org.mindswap.owls.process.Parameter;
import org.mindswap.query.ValueMap;

/**
 * Represents a grounding for AtomicProcesses. There is no corresponding concept in OWL-S ontology because OWL-S
 * ontology only describes WSDL gorunding. But this class is a base class for both
 * {@link org.mindswap.owls.grounding.WSDLAtomicGrounding WSDLAtomicGrounding} and
 * {@link org.mindswap.owls.grounding.UPnPAtomicGrounding UPnPAtomicGrounding} classes.
 * 
 * @author Evren Sirin
 *
 */
public interface AtomicGrounding extends OWLIndividual {
	public final static String GROUNDING_WSDL = "WSDL";
	public final static String GROUNDING_UPNP = "UPNP";
	public final static String GROUNDING_JAVA = "Java";
	
	/**
	 * Get a URL that describes the properties of this grounding. The nature of the file
	 * depends on the actual grounding type, e.g. WSDL grounding will return the URL of 
	 * the WSDL document whereas the UPNP grounding returns the UPnP presentation URL  
	 * 
	 * @return
	 */
	public URL getDescriptionURL();
	
	/**
	 * Get the AtomicProcess object for this grounding
	 * 
	 * @return
	 */
	public AtomicProcess getProcess();
	
	/**
	 * Set the AtomicProcess object for this grounding
	 * 
	 * @param process
	 */
	public void setProcess(AtomicProcess process);
	
	public void addMessageMap(MessageMap map);
	
	public void addMessageMap(Parameter owlsParameter, String groundingParameter);
	
	public void addMessageMap(Parameter owlsParameter, String groundingParameter, String xsltTransformation);
	
	public void addInputMap(MessageMap map);
	
	public void addOutputMap(MessageMap map);
	
	/**
	 * Get the mappings between the OWL-S inputs and grounding operation inputs
	 * 
	 * @return
	 */
	public MessageMapList getInputMap();
	
	/**
	 * Get the mappings between the OWL-S outputs and grounding operation outputs
	 * 
	 * @return
	 */
	public MessageMapList getOutputMap();	
	
	/**
	 * Invoke this grounding with the given input values.
	 * 
	 * @param values
	 * @return
	 * @throws Exception
	 */
	public ValueMap invoke(ValueMap values) throws ExecutionException;
	
	/**
	 * Invoke this grounding with the given input values and use the given KB to
	 * store the output values.
	 * 
	 * @param values Input values
	 * @param kb The execution environment where output data is stored 
	 * @return A ValueMap from output paramaters to <code>OWLValue</code>s.
	 * @throws ExecutionException
	 */
	public ValueMap invoke(ValueMap values, OWLKnowledgeBase kb) throws ExecutionException;
	
	/**
	 * Returns the type of grounding such as WSDL or Java.
	 * @return the type of the grounding
	 */
	public String getGroundingType();
}
