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
package impl.owls.process.constructs;

import impl.owl.WrappedIndividual;
import impl.owls.process.ValueOfImpl;

import java.util.Iterator;

import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLIndividualList;
import org.mindswap.owls.OWLSListFactory;
import org.mindswap.owls.process.BindingList;
import org.mindswap.owls.process.ControlConstruct;
import org.mindswap.owls.process.Perform;
import org.mindswap.owls.process.Process;
import org.mindswap.owls.process.ProcessList;
import org.mindswap.owls.process.ValueOf;
import org.mindswap.owls.vocabulary.OWLS;

/**
 * @author Evren Sirin
 *
 */
public abstract class ControlConstructImpl extends WrappedIndividual implements ControlConstruct {

	public ControlConstructImpl(OWLIndividual ind) {
		super(ind);		
	}	
    
    public ProcessList getAllProcesses() {
        return getAllProcesses(false);
    }
    
    public Process getParentProcess() {
    	OWLIndividualList processes = getIncomingProperties(OWLS.Process.composedOf);
    	if (processes.size() == 0)
    		return null;
    	if (processes.size() > 1)
    		return null;    	
    	else
    		return (Process) processes.individualAt(0).castTo(Process.class);
    }
    
	public BindingList getAllBindings() {			
		BindingList bindings = OWLSListFactory.createBindingList();		
		return getBindingsRecursively(this, bindings); 
	}
	
	private BindingList getBindingsRecursively(ControlConstruct cc, BindingList bindings) {		
		if (cc instanceof Perform) {
			Perform perform = (Perform) cc;

			// get bindings for flows to this perform
			bindings.addBindingWithoutDuplicate((BindingList)perform.getBindings());
			
			// get bindings for flows going out of this perform
			OWLIndividualList list = perform.getIncomingProperties(OWLS.Process.fromProcess);
			
			for (int index = 0; index < list.size(); index++) {
				ValueOf valueOf = new ValueOfImpl((OWLIndividual) list.get(index));
				bindings.addBindingWithoutDuplicate(valueOf.getEnclosingBinding());
			}
		} else {
			Iterator<ControlConstruct> ccs = cc.getConstructs().iterator();		
			while (ccs.hasNext()) 
				bindings = getBindingsRecursively(ccs.next(), bindings);
		}
			
		return bindings;
	}
	
	public boolean removeConstruct(ControlConstruct CC) {
		return true;
	}
}
