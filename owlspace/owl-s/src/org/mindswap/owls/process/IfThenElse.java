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
 * An IfThenElse is a digital branching construct. Based on the evaluation of 
 * a condition, the execution branches to one of the branches. For deeper branching, 
 * nest this construct. 
 * For more information refer to the OWL-S white paper at
 * the official <a href="http://www.daml.org/services/owl-s/">web site.</a>
 * 
 * @author Evren Sirin
 * @author Michael Dänzer (University of Zurich)
 */
public interface IfThenElse extends ControlConstruct, Conditional {
	/**
	 * Returns the control construct on which the if then part consists
	 * @return the control construct on which the if then part consists
	 */
	public ControlConstruct getThen();
	/**
	 * Sets the control construct on which the if then part consists
	 * @param cc the control construct on which the if then part consists
	 */
	public void setThen(ControlConstruct cc);	
	/**
	 * Removes the <code>process:then</code> property from this construct.  
	 * Note, that the then part is not deleted. Use {@link #deleteThen()} instead.
	 */
	public void removeThen();
	/**
	 * Deletes the then part from this construct and removes it from the ontology (if possible)
	 */
	public void deleteThen();
	
	
	/**
	 * Returns the control construct on which the else part consists
	 * @return the control construct on which the else part consists
	 */
	public ControlConstruct getElse();
	/**
	 * Sets the control construct on which the else part consists
	 * @param cc the control construct on which the else part consists
	 */
	public void setElse(ControlConstruct cc); 
	/**
	 * Removes the <code>process:else</code> property from this construct.  
	 * Note, that the else part is not deleted. Use {@link #deleteElse()} instead.
	 */
	public void removeElse();
	/**
	 * Deletes the else part from this construct and removes it from the ontology (if possible)
	 */
	public void deleteElse();
}
