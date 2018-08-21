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
 * Created on Dec 16, 2004
 */
package org.mindswap.owls.validation;

import java.util.Map;

import org.mindswap.owl.OWLOntology;


/**
 * Validate the <code>Service</code> description in the OWL-S ontology.
 * 
 * @author Evren Sirin
 */
public class OWLSValidator {
    /**
     * Return the validation errors associated with each service. There will be a list
     * of errors associated with each service. If a service description has no errors
     * in it there will not be any entries for that service. A result of empty map
     * means there were no errors (or no <code>Service</code> instances) found. 
     * 
     * @param ontology Ontology being validated
     * @return Map from Service objects to a list of <code>OWLValidationError</code>s.
     */
    public Map validate(OWLOntology ontology) {
        return null;
    }
}
