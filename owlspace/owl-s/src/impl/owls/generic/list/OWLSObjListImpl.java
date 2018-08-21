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
package impl.owls.generic.list;


import impl.owl.list.RDFListImpl;

import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLValue;
import org.mindswap.owl.list.RDFList;
import org.mindswap.owls.generic.list.OWLSObjList;
import org.mindswap.owls.vocabulary.OWLS;


/**
 * @author Evren Sirin
 * @author Michael Dänzer (University of Zurich)
 */
public class OWLSObjListImpl extends RDFListImpl implements OWLSObjList {
    public OWLSObjListImpl(OWLIndividual ind) {
    	super(ind);
    	
    	setVocabulary(OWLS.ObjList);
    }
    
    public RDFList getRest() {
        return (OWLSObjList) getProperty(vocabulary.rest()).castTo(OWLSObjList.class);
    }
    
    // TODO returning an instance of the superclass as replacement for the instance this operation was called is really bad.
    public RDFList insert(OWLValue item) {
        OWLSObjListImpl list = new OWLSObjListImpl(getOntology().createInstance( vocabulary.List()));
        list.setVocabulary(vocabulary);
        
        list.setFirst( item );
        list.setRest( this );
                
        return list;
    }  
    
    public RDFList remove() {
    	OWLSObjList bag = (OWLSObjList) getRest();
        
        if (size() > 1) {        	
        	bag.setFirst(getRest().getFirstValue());        	
        	bag.setRest((OWLSObjList) getRest().getRest());
        } else {   
        	bag = (OWLSObjList) vocabulary.nil().castTo(OWLSObjList.class);        	        	        	
        }
        if (hasProperty(vocabulary.first()))
        	removeProperties(vocabulary.first());
        if (hasProperty(vocabulary.rest()))
        	removeProperties(vocabulary.rest());
        individual.delete();
        
        return bag;
    }
        
    public RDFList removeAll() {
    	OWLSObjList list = this;
    	while (list.size() > 0) 
    		list = (OWLSObjList) list.remove();    
    	return list;
    }
}
