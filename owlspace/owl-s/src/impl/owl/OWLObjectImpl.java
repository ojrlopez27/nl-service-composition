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
 * Created on Dec 12, 2004
 */
package impl.owl;

import org.mindswap.exceptions.ConversionException;
import org.mindswap.owl.OWLConfig;
import org.mindswap.owl.OWLObject;
import org.mindswap.owl.OWLObjectConverter;

/**
 * @author Evren Sirin
 */
public abstract class OWLObjectImpl implements OWLObject {
    private OWLObject next;
    
	public OWLObjectImpl() {
	    next = this;
    }
    
	public OWLObject getNextView() {
	    return next;
	}
	
	public void setNextView(OWLObject nextView) {
	    next = nextView;
	}
	
	private void addView(OWLObject newView) {
	    newView.setNextView(next);
	    next = newView;
	}
	
	private OWLObject findView(Class javaClass) {
        OWLObject obj = this;            
        do {    
            if(javaClass.isInstance(obj)) 
                return obj;
            
            obj = obj.getNextView();
        } while(obj != this);
        
        return null;
	}
    
    public OWLObject castTo(Class javaClass) {
        OWLObject view = findView(javaClass);
        
        if(view == null) {
 		    OWLObjectConverter converter = OWLConfig.getConverter(javaClass);
		    
		    if(converter == null)
		        throw new ConversionException("No converter found for " + javaClass);
		    
		    view = converter.cast(this);
		    
		    addView(view);
        }
	    
	    return view;
    }

    public boolean canCastTo(Class javaClass) {
		OWLObjectConverter converter = OWLConfig.getConverter(javaClass);
		
		return (converter != null && converter.canCast(this));
    }

    public String debugString() {
        return getImplementation().toString();
    }
   
    public boolean equals(Object object) {
        if(object instanceof OWLObjectImpl) {
            return getImplementation().equals(((OWLObjectImpl)object).getImplementation());
        }
        
        return false;
    }
    
    public int hashCode() {
        int hash = getImplementation().hashCode();
        return hash;
    }
}
