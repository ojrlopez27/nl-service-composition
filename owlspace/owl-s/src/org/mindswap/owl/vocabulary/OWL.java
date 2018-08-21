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
 * Created on Dec 28, 2003
 *
 */
package org.mindswap.owl.vocabulary;

import java.net.URI;

import org.mindswap.owl.EntityFactory;
import org.mindswap.owl.OWLClass;
import org.mindswap.utils.URIUtils;

/**
 * @author Evren Sirin
 *
 */
public class OWL {
	public final static String ns = "http://www.w3.org/2002/07/owl#";

	public final static URI getURI() { return URI.create(ns); } 
	
	public final static OWLClass Thing   = 
	    EntityFactory.createClass(URIUtils.createURI(ns + "Thing"));
	public final static OWLClass Nothing = 
	    EntityFactory.createClass(URIUtils.createURI(ns + "Nothing"));
 
	public final static OWLClass Ontology = 
		EntityFactory.createClass(URIUtils.createURI(ns + "Ontology"));	
	
	public final static URI imports       = URI.create(ns + "imports");	
	public final static URI inverseOf       = URI.create(ns + "inverseOf");
	public final static URI versionInfo   = URI.create(ns + "versionInfo");
	public final static URI backwardCompatibleWith   = URI.create(ns + "backwardCompatibleWith");
	public final static URI priorVersion   = URI.create(ns + "priorVersion");
	public final static URI incompatibleWith   = URI.create(ns + "incompatibleWith");
	
	public final static URI sameAs   		= URI.create(ns + "sameAs");
	public final static URI differentFrom   = URI.create(ns + "differentFrom");
}
