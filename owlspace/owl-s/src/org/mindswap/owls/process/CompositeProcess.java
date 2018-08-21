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

/**
 * @author Evren Sirin
 *
 */
public interface CompositeProcess extends Process {
	/**
	 * Returns the control construct of which the composite process is composed of.
	 * The <code>process:composedOf</code> property is read to retrieve the correct
	 * construct.   
	 * @return the control construct this composite process is composed of
	 */
	public ControlConstruct getComposedOf();
	
	/**
	 * Sets the control construct of which the composite process is composed of.
	 * The <code>process:composedOf</code> property is written to set the correct
	 * construct.   
	 * 
	 * @param cc the control construct this composite process is composed of
	 */
	public void setComposedOf(ControlConstruct cc);
	
	/**
	 * Removes the Control Construct from the composite process by breaking the
	 * link process.ComposedOf. The control construct itself remains untouched.
	 */
	public void removeComposedOf();
	
	/**
	 * Deletes the control construct from this composite process.
	 */
	public void deleteComposedOf();
	
	/**
	 * Returns all data flow bindings within this composite process. It does not distinguish
	 * wether the flow is coming from outside into the process or the flow is completely
	 * embedded into the process or the flow goes from the process out to some outside
	 * perform.
	 * 
	 * @return a list of bindings that have at least their source or sink in this process
	 */
	public BindingList getAllBindings();
}
