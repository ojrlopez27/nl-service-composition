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
package impl.owl.list;


import impl.owl.WrappedIndividual;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.mindswap.exceptions.InvalidListException;
import org.mindswap.owl.OWLDataValue;
import org.mindswap.owl.OWLFactory;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLIndividualList;
import org.mindswap.owl.OWLValue;
import org.mindswap.owl.list.ListVocabulary;
import org.mindswap.owl.list.RDFList;
import org.mindswap.owl.vocabulary.RDF;

/**
 * @author Evren Sirin
 * @author Michael Dänzer (University of Zurich)
 */
public class RDFListImpl extends WrappedIndividual implements RDFList {
    protected ListVocabulary vocabulary;
    
    private class RDFListIterator implements Iterator {
        private RDFList list;
        
        private RDFListIterator(RDFList list) {
            this.list = list;
        }
        
        public void remove() {
            throw new UnsupportedOperationException("Cannot remove from ObjList iterator");
        }

        public boolean hasNext() {
            return !list.isEmpty();
        }

        public Object next() {
            if(list.isEmpty())
                throw new NoSuchElementException();
                
            Object result = list.getFirst();            
            list = list.getRest();
            
            if(result == null || list == null)
                throw new InvalidListException();
            
            return result;
        }        
    } 
    
    public RDFListImpl(OWLIndividual ind) {
    	super(ind);
    	
    	setVocabulary(RDF.ListVocabulary);
    }
    
    public RDFList getRest() {
        return (RDFList) getProperty(vocabulary.rest()).castTo(RDFList.class);
    }
    
    public void setRest(RDFList rest) {
        setProperty(vocabulary.rest(), rest);
    }    
    
    public void setRestToNil() {
    	setProperty(vocabulary.rest(), vocabulary.nil());
    }

    public OWLIndividual getFirst() {
        return (OWLIndividual) getFirstValue();
    }
    
    public OWLValue getFirstValue() {
        OWLValue value = getProperty( vocabulary.firstD() );
        if( value == null )
            value = getProperty( vocabulary.first() );
        
        return value;
    }
    
    public void setFirst(OWLValue value) {
 //       if (isEmpty())
 //           throw new RuntimeException( "Cannot modify empty list (list:nil)" );
        
        if (value == null)
        	setProperty(vocabulary.firstD(), vocabulary.nil());
        else if (value instanceof OWLDataValue )
            setProperty(vocabulary.firstD(), (OWLDataValue) value );
        else
            setProperty(vocabulary.first(), (OWLIndividual) value );
    }

    public OWLIndividualList getAll() {
        OWLIndividualList result = OWLFactory.createIndividualList();
        for(RDFList list = this; !list.isEmpty(); list = list.getRest())
            result.add(list.getFirst());        
        
        return result;
    }
    
    public List getAllValues() {
        List result = new ArrayList();
        for(RDFList list = this; !list.isEmpty(); list = list.getRest())
            result.add(list.getFirstValue());        
        
        return result;
    }

    public OWLIndividual get(int index) {        
        return (OWLIndividual) getValue(index);
    }
    
    public OWLValue getValue(int index) {
        if(isEmpty())
            throw new IndexOutOfBoundsException();
        
        return (index == 0) ? getFirstValue() : getRest().getValue(index - 1);
    }

    public RDFList add(OWLValue item) {
        if (isEmpty())
            return insert( item );
        
        RDFList rest = getRest();
        
        if( rest.isEmpty() )
            setRest(rest.insert(item));
        else
            rest.add(item);
        
        return this;
    }
    
    public RDFList insert(OWLValue first) {
        RDFListImpl list = new RDFListImpl( getOntology().createInstance( vocabulary.List() ) );
        list.setVocabulary( vocabulary );
        list.setFirst( first );
        list.setRest( this );
                
        return list;
    }
        
    public RDFList insertAt(int index, OWLValue value) {
        if( index == 0 )
            return insert(value);

        if( index < 0 || isEmpty() )
            throw new IndexOutOfBoundsException();

        RDFList rest = getRest();
        return rest.insertAt( index - 1, value );
    }
    
    public RDFList remove(OWLValue value) {
    	if ((value == null) || (size() == 0))
    		return this;
    	if ((size() == 1) && (getFirstValue().equals(value)))
    		return remove();
    	    	    	
    	RDFList rest = this;
    	int i = 0;
    	while (!rest.isEmpty()) {    		
    		if (rest.getFirstValue().equals(value))     			    				
    			return removeAt(i);
    		i++;
    		rest = rest.getRest();
    	}
    	return this;
    }
    
    public RDFList removeAt(int index) {
        if (index == 0)
            return remove();

        if (index < 0 || isEmpty())
            throw new IndexOutOfBoundsException();
        
        setRest(getRest().removeAt(index - 1));
        return this;
    }
    
    public RDFList remove() {
    	RDFList list = getRest();
        
        if (size() > 1) {        	
        	list.setFirst(getRest().getFirstValue());        	
        	list.setRest(getRest().getRest());
        } else {   
        	list = (RDFList) vocabulary.nil().castTo(RDFList.class);        	        	        	
        }
        if (hasProperty(vocabulary.first()))
        	removeProperties(vocabulary.first());
        if (hasProperty(vocabulary.rest()))
        	removeProperties(vocabulary.rest());
        individual.delete();
        
        return list;
    }
        
    public RDFList removeAll() {
    	RDFList list = this;
    	while (list.size() > 0) 
    		list = list.remove();
    	return list;
    }

	@Override
	public void delete() {		
		removeAll();
		individual.delete();		
	} 
    
    public void set(int index, OWLValue value) {
        if( isEmpty() )
            throw new UnsupportedOperationException( "Cannot modify empty list (list:nil)" );
     
        int size = size();
        if( index > size )
            throw new IndexOutOfBoundsException();
        else if( index == size )
            add( value );
        else {
            RDFList list = this;
            for(int i = 0; i < index; i++) 
                list = list.getRest();
            
            list.setFirst( value );
        }
    }
    
    public Iterator iterator() {
        return new RDFListIterator(this);
    }

    public int size() {    	
        return isEmpty() ? 0 : 1 + getRest().size();
    }

    public boolean isEmpty() {
        return equals(vocabulary.nil()) || (getFirstValue() != null && getFirstValue().equals(vocabulary.nil()));
    }

    public ListVocabulary getVocabulary() {
    	return vocabulary;
    }

    public void setVocabulary(ListVocabulary vocabulary) {
    	this.vocabulary = vocabulary;
    }
}
