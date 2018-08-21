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
 * Created on Dec 29, 2003
 *
 */
package org.mindswap.owl;

import java.net.URI;

import org.mindswap.owl.vocabulary.OWL;


/**
 * @author Evren Sirin
 *
 */
public class EntityFactory {    
    private static OWLOntology factory = OWLFactory.createOntology();

    public static OWLIndividual createIndividual() {
        return factory.createInstance(OWL.Thing);
    }
    
    public static OWLIndividual createIndividual(URI uri) {
        return factory.createIndividual(uri);
    }
  
    public static OWLDataValue createDataValue(String value) {
        return factory.createDataValue(value);
    }

    public static OWLDataValue createDataValue(String value, String language) {
        return factory.createDataValue(value, language);
    }

    public static OWLDataValue createDataValue(Object value, URI datatypeURI) {
        return factory.createDataValue(value, datatypeURI);
    }
    
    public static OWLDataValue createDataValue(Object value) {
        return factory.createDataValue(value);
    }

    public static OWLClass createClass(URI uri) {
        return factory.createClass(uri);
    }

    public static OWLObjectProperty createObjectProperty(URI uri) {
        return factory.createObjectProperty(uri);
    }

    public static OWLDataProperty createDataProperty(URI uri) {
        return factory.createDataProperty(uri);
    }
}
