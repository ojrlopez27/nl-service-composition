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
package impl.owls.process.constructs;

import impl.owls.process.ProcessListImpl;

import java.util.List;

import org.mindswap.owl.OWLIndividual;
import org.mindswap.owls.process.ControlConstruct;
import org.mindswap.owls.process.ControlConstructList;
import org.mindswap.owls.process.ProcessList;
import org.mindswap.owls.process.Sequence;
import org.mindswap.owls.vocabulary.OWLS;

/**
 * @author Evren Sirin
 * @author Michael Dänzer (University of Zurich)
 */
public class SequenceImpl extends ControlConstructImpl implements Sequence {

	/**
	 * @param resource
	 */
	public SequenceImpl(OWLIndividual ind) {
		super(ind);
	}
	
	public void addComponent(ControlConstruct component) {
	    ControlConstructList components = getComponents();
	    if(components == null) {
	        components = getOntology().createControlConstructList(component);
	        addProperty(OWLS.Process.components, components);
	    }
	    else
	        components.add(component);
	}
	
	public ControlConstructList getComponents() {
	    return (ControlConstructList) getPropertyAs(OWLS.Process.components, ControlConstructList.class);
	}
	
	public List getConstructs() {
	    return getComponents().getAll();
	}
	
	public ProcessList getAllProcesses(boolean recursive) {
		ProcessList list = new ProcessListImpl();
		ControlConstructList components = getComponents();
		while(!components.isEmpty()) {
			ControlConstruct cc = (ControlConstruct) components.getFirst();
			list.addAll(cc.getAllProcesses(recursive));
			components = (ControlConstructList) components.getRest();
		}
		
		return list;
	}
	
    public String getConstructName() {
        return "Sequence";
    }

	public boolean removeConstruct(ControlConstruct CC) {
		ControlConstructList components = getComponents();
		components = (ControlConstructList) components.remove(CC);
		setProperty(OWLS.Process.components, components);
		return true;
	}
	
	public void removeComponents() {
		if (hasProperty(OWLS.Process.components))
			removeProperties(OWLS.Process.components);
	}
	
	public void deleteComponents() {
		ControlConstructList list = getComponents();
		removeComponents();
	
		list.delete();			
	}
	
	@Override
	public void delete() {	
		deleteComponents();	
		super.delete();
	}
}
