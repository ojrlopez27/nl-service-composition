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
 * Created on Dec 21, 2004
 */
package impl.owl;


import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.mindswap.owl.OWLIndividualList;
import org.mindswap.owl.OWLObject;

/**
 * @author Evren Sirin
 *
 */
public class CastingList extends OWLIndividualListImpl implements OWLIndividualList {
    private Class castTarget;
    
    public CastingList(Class castTarget) {
        super();
        
        this.castTarget = castTarget;
    }

    public CastingList(List list, Class castTarget) {
        super(list instanceof CastingList ? ((CastingList)list).getBaseList() : list);
        
        this.castTarget = castTarget;
    }

    public Object get(int index) {
        return ((OWLObject) super.get(index)).castTo(castTarget);
    }
    
	
    public Iterator iterator() {
        return listIterator(0);
    }

	public ListIterator listIterator() {
        return listIterator(0);
	}
	
	public ListIterator listIterator(final int index) {
	    return new ListIterator() {
			ListIterator i = CastingList.this.getListIterator(index);
	
			public boolean hasNext()     {return i.hasNext();}
			public Object next()         {
			    i.next(); 
				return get(i.previousIndex());
			}
			public boolean hasPrevious() {return i.hasPrevious();}
			public Object previous()     {
			    i.previous(); 
				return get(i.nextIndex());
			}
			public int nextIndex()       {return i.nextIndex();}
			public int previousIndex()   {return i.previousIndex();}	
			public void remove() { i.remove(); }
			public void set(Object o) { i.set(o); }
			public void add(Object o) { i.add(o); }
		    };
	}
	
	private ListIterator getListIterator(int index) {
	    return super.listIterator( index );
	}
	
    public Object[] toArray() {
        int size = size();
    	Object[] result = new Object[size];
        
    	for(int i = 0; i < size; i++)
            result[i] = get(i);

        return result;
    }	
    
    public Object[] toArray(Object a[]) {
        int size = size();
        if (a.length < size)
            a = (Object[])java.lang.reflect.Array.newInstance(
                                a.getClass().getComponentType(), size);

        for(int i = 0; i < size; i++)
            a[i] = get(i);

        if (a.length > size)
            a[size] = null;

        return a;
    }    
}
