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
 * Created on 13.04.2005
 */
package org.mindswap.owls.vocabulary;

import org.mindswap.owl.EntityFactory;
import org.mindswap.owl.OWLClass;
import org.mindswap.owl.OWLDataProperty;
import org.mindswap.owl.OWLObjectProperty;
import org.mindswap.utils.URIUtils;

/**
 * Static description for the additional OWL-S Extensions groundings
 * 
 * @author Michael Daenzer (University of Zurich)
 * @see <a href="http://www.ifi.unizh.ch/ddis/ont/MoreGroundings.owl">MoreGroundings.owl</a>
 */
public class MoreGroundings {
    public static String baseURI = "http://www.ifi.unizh.ch/ddis/ont/owl-s/";
    public static String version = "";
	public static String URI = baseURI + version + "MoreGroundings.owl#";
		
	public static OWLObjectProperty owlsProcess;
	public static OWLObjectProperty owlsParameter;
		
	public static OWLClass JavaGrounding;

	public static OWLObjectProperty hasAtomicProcessGrounding;
	public static OWLClass JavaAtomicProcessGrounding;

	public static OWLDataProperty javaClass;
	public static OWLDataProperty javaMethod;
	
	public static OWLClass JavaVariable;
	public static OWLDataProperty javaType;
	public static OWLClass JavaParameter;
	public static OWLDataProperty paramIndex;
	public static OWLObjectProperty javaOutput;
	public static OWLObjectProperty hasJavaParameter;
	public static OWLDataProperty transformatorClass;
	
	static {
		JavaGrounding = EntityFactory.createClass(URIUtils.createURI(URI + "JavaGrounding"));
		
		JavaAtomicProcessGrounding = EntityFactory.createClass(URIUtils.createURI(URI + "JavaAtomicProcessGrounding"));
		hasAtomicProcessGrounding = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "hasAtomicProcessGrounding"));
		
		javaClass	      = EntityFactory.createDataProperty(URIUtils.createURI(URI + "javaClass"));
		javaMethod	      = EntityFactory.createDataProperty(URIUtils.createURI(URI + "javaMethod"));
		
		JavaVariable      = EntityFactory.createClass(URIUtils.createURI(URI + "JavaVariable"));
		javaType          = EntityFactory.createDataProperty(URIUtils.createURI(URI + "javaType"));
		JavaParameter     = EntityFactory.createClass(URIUtils.createURI(URI + "JavaParameter"));
		paramIndex        = EntityFactory.createDataProperty(URIUtils.createURI(URI + "paramIndex"));
		transformatorClass = EntityFactory.createDataProperty(URIUtils.createURI(URI + "transformatorClass"));
		
		javaOutput        = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "javaOutput"));
		hasJavaParameter  = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "hasJavaParameter"));
		
		owlsProcess       = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "owlsProcess"));
		owlsParameter     = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "owlsParameter"));				
	}
	

}
