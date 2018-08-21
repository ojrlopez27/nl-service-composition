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
 * This is super interface for loop based control constructs such as 
 * {@link RepeatUntil}, {@link RepeatWhile} amd the non OWL-S standard
 * {@link ForEach}. 
 * 
 * @author Evren Sirin
 * @author Michael Dänzer (University of Zurich)
 */
public interface Iterate extends ControlConstruct {
	/**
	 * Returns the control construct within the loop
	 * @return the control construct within the loop
	 */
    public ControlConstruct getComponent();
    
    /**
     * Sets the control construct within the loop
     * @param component the control construct within the loop
     */
    public void setComponent(ControlConstruct component);
    
	/**
	 * Removes the control construct within the loop. The construct itslef remains
	 * untouched. Use {@link #deleteComponent() instead.
	 */
	public void removeComponent();
	/**
	 * Deletes the control construct within the loop and removes it from the ontology (if possible)
	 */
	public void deleteComponent();
}
