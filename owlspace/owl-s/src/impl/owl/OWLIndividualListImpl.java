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
package impl.owl;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLIndividualList;


/**
 * @author Evren Sirin
 *
 */
public class OWLIndividualListImpl extends WrappingList implements OWLIndividualList {
    public OWLIndividualListImpl() {
        this(new ArrayList());
    }
    
    public OWLIndividualListImpl(List list) {
        super(list);
    }    
    
	public OWLIndividual getIndividual(URI uri) {
		for(int i = 0; i < size(); i++) {
			OWLIndividual r = individualAt(i); 
			if(r.getURI().equals(uri))
				return r;
		}
		
		return null;
	}

	public OWLIndividual individualAt(int index) {
		return (OWLIndividual) get(index);
	}

	public OWLIndividual getIndividual(String localName) {
		for(int i = 0; i < size(); i++) {
			OWLIndividual r = individualAt(i); 
			if(!r.isAnon() && r.getLocalName().equals(localName))
				return r;
		}
		
		return null;
	}
}
