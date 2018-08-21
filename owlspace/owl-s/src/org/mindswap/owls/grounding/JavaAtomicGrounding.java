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
 * Created on 13.04.2005
 */
package org.mindswap.owls.grounding;

import org.mindswap.owl.OWLIndividualList;
import org.mindswap.owls.process.Input;
import org.mindswap.owls.process.Output;

/**
 * <p>This interface encapsulates the access to a JavaAtomicProcessGrounding.</p>
 * 
 * <p>A JavaAtomicGrounding grounds an OWL-S Service to a Java method invocation. The method call
 * is specified by its method signature in an OWL-S Ontology.</p>
 * 
 * <p>Static methods can be invoked, Exceptions during the execution are catched and redirected in a ExecutionExecpetion. 
 * At the time, only primitive datatypes (such as int, boolean and so on) and thier adapters (such as Integer, Boolean and so on)
 * are support as Parameter and ReturnValue types.</p>
 * 
 * <p>The driving parts are:
 * <ul>
 * 	<li>fully qualified class name</li>
 * 	<li>method name</li>
 * 	<li>a map of all input parameters (at the time only primitive datatypes and their adapter classes)</li>
 * 	<li>an output type (at the time only primitive datatypes and their adapter classes)</li>
 * </ul></p>
 * 
 * @author Michael Daenzer (University of Zurich)
 * @see <a href="http://www.ifi.unizh.ch/ddis/ont/owl-s/MoreGroundings.owl">MoreGroundings.owl</a>
 * @see org.mindswap.owls.grounding.AtomicGrounding 
 * @see org.mindswap.owls.vocabulary.MoreGroundings
 */
public interface JavaAtomicGrounding extends AtomicGrounding {
	/**
	 * Sets the name of the class, on which the method in the Grounding should be executed
	 * 
	 * @param claz Fully qualified name of the Java class, which implements the method to invoke
	 */
    public void setClaz(String claz);
    /**
     * Returns the name of the class, on which the method in the Grounding should be executed
     * 
     * @return Name of the class specified in the JavaAtomicProcessGrounding
     */
    public String getClaz();
    
    /**
     * Sets the name of the method which should be executed
     * 
     * @param method Name of the method to invoke
     */
    public void setMethod(String method);
    /**
     * Returns the name of the method which should be executed
     * 
     * @return Name of the method specified in the JavaAtomicProcessGrounding
     */
    public String getMethod();
    /**
     * Sets the Output part of the Java Grounding. Declares the return value of the Java method to invoke
     * 
     * @param name Name of the OWL-S JavaVariable instance
     * @param type Fully qualified Java type of the return value of the method to invoke 
     * @param owlsParameter Reference to the OWL-S Output Variable
     */
    public void setOutput(String name, String type, Output owlsParameter); 
    /**
     * Sets an Input Parameter of the Java Grounding. Declares one Parameter of the Jave method to invoke
     * 
     * @param name Name of the OWL-S JavaParameter instance 
     * @param type Fully qualified Java type of the Java Parameter to set
     * @param index Number of order for this Parameter in the Parameter-List
     * @param owlsParameter Reference to the OWL-S Input Variable
     */
    public void setInputParameter(String name, String type, int index, Input owlsParameter);
    
    /**
     * Returns the java input parameter related to given OWL-S input 
     * @return the java input parameter related to given OWL-S input
     */
    public JavaParameter getInputParamter(Input input);
    
    /**
     * Returns the input parameters for this java method in correct order 
     * @see org.mindswap.owls.vocabulary.MoreGroundings#paramIndex
     * @return a list with all inputs for this java method in correct order 
     */
    public OWLIndividualList getInputParameters(); 
    
    /**
     * Returns the java output variable related to given OWL-S output
     * @return the java output variable related to given OWL-S output
     */
    public JavaVariable getOutput();
}
