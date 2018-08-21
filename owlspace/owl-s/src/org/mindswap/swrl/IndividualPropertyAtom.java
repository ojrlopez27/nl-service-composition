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

import org.mindswap.owl.OWLObjectProperty;
import org.mindswap.query.ValueMap;

/**
 * @author Evren Sirin
 * @author Michael Dänzer (University of Zurich)
 */
public interface IndividualPropertyAtom extends Atom {
	
	/**
	 * Returns the object property predicate for this SWRL individualvaluedProperty atom 
	 * 
	 * @return the object property predicate for this this SWRL individualvaluedProperty atom
	 */
    public OWLObjectProperty getPropertyPredicate();
	/**
	 * Sets the data property predicate for this SWRL individualvaluedProperty atom 
	 * 
	 * @param p the object property predicate for this this SWRL individualvaluedProperty atom
	 */
    public void setPropertyPredicate(OWLObjectProperty p);
    
    /**
     * Retuns the subject of the property of this SWRL individualvaluedProperty atom.
     *  
     * It is of one of the explicit or inferred types of the domain of the OWL data property
     * @return the subject of the property of this SWRL individualvaluedProperty atom.
     */
    public SWRLIndividualObject getArgument1();
    /**
     * Sets the subject of the property of this SWRL individualvaluedProperty atom. 
     * It must be of one of the explicit or inferred types of the domain of the OWL data property
     * 
     * @param obj the subject of the property of this SWRL individualvaluedProperty atom.
     */
    public void setArgument1(SWRLIndividualObject obj);
    
    /**
     * Retuns the object of the property of this SWRL individualvaluedProperty atom.
     *  
     * It is of one of the explicit or inferred types of the range of the OWL data property
     * @return the object of the property of this SWRL individualvaluedProperty atom.
     */
    public SWRLIndividualObject getArgument2();
    
    /**
     * Sets the object of the property of this SWRL individualvaluedProperty atom. 
     * It must be of one of the explicit or inferred types of the range of the OWL data property
     * 
     * @param obj the object of the property of this SWRL individualvaluedProperty atom.
     */
    public void setArgument2(SWRLIndividualObject obj);
    
    /**
     * Evaluates the atom. Sets the argument 2 as value for the object property predicate of argument 1 
     * 
     * @param values the set of values bound to the process (and its super-processes)
     * for which this atom (i.e. this corresponfing atom) is encapsulated.
     */
    public void evaluate(ValueMap values);
}
