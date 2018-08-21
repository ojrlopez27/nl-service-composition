//The MIT License
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
 * Created on Dec 10, 2004
 */
package org.mindswap.owl.list;

import java.net.URI;

import org.mindswap.owl.EntityFactory;
import org.mindswap.owl.OWLClass;
import org.mindswap.owl.OWLDataProperty;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLObjectProperty;
import org.mindswap.utils.URIUtils;

/**
 * @author Evren Sirin
 */
public class GenericListVocabulary implements ListVocabulary {
    private OWLClass List;
    
    private OWLObjectProperty first;
    private OWLDataProperty firstD;
    private OWLObjectProperty rest;
    
    private OWLIndividual nil;
    
    private Class javaClass;

    public GenericListVocabulary(URI baseURI) {
		this.List = EntityFactory.createClass(URIUtils.createURI(baseURI, "List"));
		this.first = EntityFactory.createObjectProperty(URIUtils.createURI(baseURI, "first"));
		this.firstD = EntityFactory.createDataProperty(URIUtils.createURI(baseURI, "first"));
		this.rest = EntityFactory.createObjectProperty(URIUtils.createURI(baseURI, "rest"));
		this.nil = EntityFactory.createIndividual(URIUtils.createURI(baseURI, "nil"));
	}
    
    public GenericListVocabulary(URI List, URI first, URI rest, URI nil) {
		this.List = EntityFactory.createClass(List);
		this.first = EntityFactory.createObjectProperty(first);
		this.firstD = EntityFactory.createDataProperty(first);
		this.rest = EntityFactory.createObjectProperty(rest);
		this.nil = EntityFactory.createIndividual(nil);
	}

    public GenericListVocabulary(URI List, URI first, URI firstD, URI rest, URI nil) {
		this.List = EntityFactory.createClass(List);
		this.first = EntityFactory.createObjectProperty(first);
		this.firstD = EntityFactory.createDataProperty(firstD);
		this.rest = EntityFactory.createObjectProperty(rest);
		this.nil = EntityFactory.createIndividual(nil);
	}

    public GenericListVocabulary(OWLClass List, OWLObjectProperty first, OWLDataProperty firstD,
                                 OWLObjectProperty rest, OWLIndividual nil) {
        this.List = List;
        this.first = first;
        this.firstD = firstD;
        this.rest = rest;
        this.nil = nil;
    }
    
    public OWLClass List() {
        return List;
    }

    public OWLObjectProperty first() {
        return first;
    }

    public OWLDataProperty firstD() {
        return firstD;
    }    
    
    public OWLObjectProperty rest() {
        return rest;
    }

    public OWLIndividual nil() {
        return nil;
    }
    
    public Class getJavaClass() {
        return javaClass;
    }

    public void setJavaClass(Class javaClass) {
        this.javaClass = javaClass;
    }
    
    public ListVocabulary specialize(OWLClass listType) {
        return  new GenericListVocabulary(listType, first, firstD, rest, nil);
    }

}
