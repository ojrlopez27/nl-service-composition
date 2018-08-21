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

package org.mindswap.owls;

import impl.owls.OWLSFactoryImpl;

import java.net.URI;
import java.util.Map;

import org.mindswap.owl.OWLFactory;
import org.mindswap.owls.process.execution.ProcessExecutionEngine;
import org.mindswap.owls.process.execution.ThreadedProcessExecutionEngine;
import org.mindswap.query.ValueMap;

/**
 * @author Evren Sirin
 *
 */
public class OWLSFactory extends OWLSListFactory {	
    public interface Interface {
    	public String getVersion();    	
    	
    	public Map getDefaultConverters();
    	
    	public ProcessExecutionEngine createExecutionEngine();
    	
    	// added by Michael Daenzer
    	public ThreadedProcessExecutionEngine createThreadedExecutionEngine();
    	// end added by Michael Daenzer
    	
    	public OWLSVersionTranslator createVersionTranslator();
    	
    	public OWLSVersionTranslator createVersionTranslator(String version);             
    }
    
    private static OWLSFactory.Interface factory = new OWLSFactoryImpl();

	public static String getVersion() {
		return factory.getVersion();
	}
	
	public static ProcessExecutionEngine createExecutionEngine() {
		return factory.createExecutionEngine();
	}

	// added by Michael Daenzer
	/**
	 * Returns an instance of the <code>ThreadedProcessExecutionEngine</code> to perform  multiple 
	 * process executions multithreaded.
	 */
	public static ThreadedProcessExecutionEngine createThreadedExecutionEngine() {
		return factory.createThreadedExecutionEngine();
	}
	// end added by Michael Daenzer
	
    /**
	 * @return
	 */
	public static OWLSVersionTranslator createVersionTranslator() { 
	    return factory.createVersionTranslator(); 
	}
	
	/**
	 * @param version
	 * @return
	 */
	public static OWLSVersionTranslator createVersionTranslator(String version) {
	    return factory.createVersionTranslator(version);
	}

	/**
	 * @deprecated Use OWLFactory.createKB() instead
	 */
    public static OWLSKnowledgeBase createKB() {
        return (OWLSKnowledgeBase) OWLFactory.createKB();
    } 

	/**
	 * @deprecated Use OWLFactory.createOntology() instead
	 */
    public static OWLSOntology createOntology() {
        return (OWLSOntology) OWLFactory.createOntology();
    }

	/**
	 * @deprecated Use OWLFactory.createOntology(URI) instead
	 */
    public static OWLSOntology createOntology(URI uri) {
        return (OWLSOntology) OWLFactory.createOntology(uri);
    }
    
    public static Map getDefaultConverters() {
        return factory.getDefaultConverters();
    }

    /**
     * @deprecated Use new ValueMap() to create a ValueMap
     */
    public static ValueMap createValueMap() {
        return new ValueMap();
    }
}
