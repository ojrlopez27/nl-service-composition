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
package impl.owls.process;


import org.mindswap.owl.OWLIndividual;
import org.mindswap.owls.process.BindingList;
import org.mindswap.owls.process.CompositeProcess;
import org.mindswap.owls.process.ControlConstruct;
import org.mindswap.owls.vocabulary.OWLS;

/**
 * @author Evren Sirin
 *
 */
public class CompositeProcessImpl extends ProcessImpl implements CompositeProcess {
	public CompositeProcessImpl(OWLIndividual ind) {
		super(ind); 
	}

	/**
	 * Returns the control construct with which the composite process is composed of.
	 */
	public ControlConstruct getComposedOf() {		
		return (ControlConstruct) getPropertyAs(OWLS.Process.composedOf, ControlConstruct.class);
	}

	/**
	 * Sets the control construct with which the composite process is composed of.
	 */
	public void setComposedOf(ControlConstruct construct) {
	    setProperty(OWLS.Process.composedOf, construct);
	}

	/** 
	 * Returns all bindings which are set within this composite process
	 */
	public BindingList getAllBindings() {
		ControlConstruct cc = getComposedOf();
		return cc.getAllBindings();		
	}

	/**
	 * Delete the control construct with which this composite process is composed of
	 */
	public void deleteComposedOf() {
		ControlConstruct cc = getComposedOf();
		
		removeComposedOf();
		
		if (cc != null)
			cc.delete();
	}

	/**
	 * Removes the control construct with which this composite process is composed of
	 * by breaking the property <code>process:composedOf</code>
	 */
	public void removeComposedOf() {
		if (hasProperty(OWLS.Process.composedOf))
			removeProperties(OWLS.Process.composedOf);
	}

	public void delete() {
		super.delete();
		deleteComposedOf();	
		individual.delete();
	}
	
}
