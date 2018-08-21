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
 * Created on Dec 28, 2003
 *
 */
package impl.owls.grounding;

import impl.owl.WrappedIndividual;

import org.mindswap.owl.OWLIndividual;
import org.mindswap.owls.grounding.AtomicGrounding;
import org.mindswap.owls.grounding.MessageMap;
import org.mindswap.owls.process.AtomicProcess;
import org.mindswap.owls.process.Input;
import org.mindswap.owls.process.Output;
import org.mindswap.owls.process.Parameter;
import org.mindswap.owls.vocabulary.OWLS;

/**
 * @author Evren Sirin
 *
 */
public abstract class AtomicGroundingImpl extends WrappedIndividual implements AtomicGrounding {
	public AtomicGroundingImpl(OWLIndividual ind) {
		super(ind);
	}

    public void addMessageMap(Parameter owlsParameter, String groundingParameter) {
        addMessageMap(owlsParameter, groundingParameter, null);
    }

	public void addMessageMap(MessageMap map) {
	    Parameter param = map.getOWLSParameter();
	    if(param instanceof Input)
	        addInputMap(map);
	    else if(param instanceof Output)
	        addOutputMap(map);
	    else
	        throw new IllegalArgumentException("OWL-S parameter in the MessageMap is neither an Input nor Output!");
	}
	
	public void addInputMap(MessageMap map) {
	    addProperty(OWLS.Grounding.wsdlInput, map);
	}
	
	public void addOutputMap(MessageMap map) {
	    addProperty(OWLS.Grounding.wsdlOutput, map);
	}
	
	/* (non-Javadoc)
	 * @see org.mindswap.owls.grounding.AtomicProcessGrounding#getProcess()
	 */
	public AtomicProcess getProcess() {
	    return (AtomicProcess) getPropertyAs(OWLS.Grounding.owlsProcess, AtomicProcess.class);
	}

	/* (non-Javadoc)
	 * @see org.mindswap.owls.grounding.AtomicProcessGrounding#getProcess(org.mindswap.owls.process.AtomicProcess)
	 */
	public void setProcess(AtomicProcess process) {
	    if(hasProperty(OWLS.Grounding.owlsProcess, process))
	        return;
	    
	    setProperty(OWLS.Grounding.owlsProcess, process);
		process.setGrounding(this);
	}
	
	public abstract String getGroundingType();
}
