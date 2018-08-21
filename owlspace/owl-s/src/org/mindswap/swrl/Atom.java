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
 * Created on Oct 26, 2004
 */
package org.mindswap.swrl;

import org.mindswap.owl.OWLIndividual;
import org.mindswap.query.ValueMap;



/**
 * Encapsulation for SWRL Atom.
 * 
 * @author Evren Sirin
 * @author Michael Dänzer (University of Zurich)
 * 
 * @see <a href="http://www.w3.org/Submission/SWRL/">SWRL on W3C</a>
 */
public interface Atom extends OWLIndividual {
	/**
	 * Returns the number of arguments
	 * @return the number of arguments
	 */
    public int getArgumentCount();
    
    /**
     * Returns the argument at position <code>index</code>
     * @param index the integer index of the argument in the argument list
     * @return the argument athe given index. <code>null</code> if there is no argument at th egiven index
     */
    public SWRLObject getArgument( int index );
    
    /**
     * Sets the argument at the given position
     * 
     * @param index the position to which the argument is added
     * @param arg the argument to add at the given position
     */
    public void setArgument( int index, SWRLObject arg );
    
    /**
     * Evaluates the atom. See description for each specific interface.
     * 
     * @param values the set of values bound to the process (and its super-processes)
     * for which this atom (i.e. this corresponfing atom) is encapsulated.
     */
    public void evaluate(ValueMap values);
}
