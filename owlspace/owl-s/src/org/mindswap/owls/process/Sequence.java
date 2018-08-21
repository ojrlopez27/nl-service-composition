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
package org.mindswap.owls.process;


/**
 * A sequence is a chain of constructs which is executed sequentially one 
 * after the another. Fur more information refer to the OWL-S white paper at
 * the official <a href="http://www.daml.org/services/owl-s/">web site.</a>
 *  
 * 
 * @author Evren Sirin
 * @author Michael Dänzer (University of Zurich)
 */
public interface Sequence extends ControlConstruct {
	
	/**
	 * Returns the control constructs on which this sequence is composed of.
	 * @return a typed list of control constructs on which this sequence is composed of
	 */
	public ControlConstructList getComponents();
	
	/**
	 * Adds a control construct to this sequence composition. The new control construct
	 * is added at the end of the sequence.
	 * 
	 * @param component the new control construct to add to the sequence
	 */
	public void addComponent(ControlConstruct component);
	
	/**
	 * Removes the <code>Process:components</code> from the sequence. The <code>ControlConstructList</code>
	 * remains untouched. Use {@link #deleteComponents()} if you want to delete the list as well.   
	 */
	public void removeComponents();
	
	/**
	 * Removes the components from this sequence and deleted them all if possible 
	 * (if not used elsewhere in the KB).
	 */
	public void deleteComponents();
}
