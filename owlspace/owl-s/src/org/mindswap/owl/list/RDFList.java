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
 * Created on Dec 23, 2004
 */
package org.mindswap.owl.list;

import java.util.Iterator;
import java.util.List;

import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLIndividualList;
import org.mindswap.owl.OWLValue;

/**
 * @author Evren Sirin
 *
 */
public interface RDFList extends OWLIndividual {
    public OWLIndividual getFirst();
    
    public OWLValue getFirstValue();

    public void setFirst(OWLValue first);

    public RDFList getRest();

    public void setRest(RDFList rest);
        
    public ListVocabulary getVocabulary();

    public OWLIndividualList getAll();

    public List getAllValues();
    
    public OWLIndividual get(int index); 
    
    public OWLValue getValue(int index);
    
    public RDFList add(OWLValue item);
    
    public RDFList insert(OWLValue item);
    
    public RDFList insertAt(int index, OWLValue item);

    public void set(int index, OWLValue item);    
        
    public Iterator iterator();
    
    public int size();
    
    public boolean isEmpty(); 
    
    public RDFList remove(OWLValue value);
    
    public RDFList removeAt(int index);
    
    public RDFList remove();
        
    public RDFList removeAll();
}
