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
 * Created on Nov 20, 2004
 */
package org.mindswap.owl;

import java.net.URI;

/**
 * Genric interface for OWL classes, properties and individuals. 
 * 
 * @author Evren Sirin
 */
public interface OWLEntity extends OWLObject {
	public OWLOntology getOntology();
	
	public OWLKnowledgeBase getKB();
	
	/**
	 * Check if this resource represents an anonymous node
	 * 
	 * @return
	 */
	public boolean isAnon();
		
	/**
	 * Return the URI for this resource. If this resource is anonymous then null value will be returned
	 * 
	 * @return
	 */
	public URI getURI();
	
	public String getLocalName();
	
	public String getQName();
	
	/**
	 * Returns the namespace of this entity 
	 * @return the namespace of this entity
	 */
	public String getNamespace();
	
	public Object getAnonID();
	
	/**
	 * Get the rdfs:label for this resource. This function will look for labels with different language
	 * codes according to the settings defined in {@link org.mindswap.owl.OWLConfig#DEFAULT_LANGS OWLConfig}
	 * 
	 * @return
	 */
	public String getLabel();
	
	/**
	 * Get the rdfs:label with the specified language code. If the label for the given language does not
	 * exist return null even a label is found for another language. Use {@link #getLabel()
	 * getLabel()} to be more flexible.
	 * 
	 * @param lang
	 * @return
	 */
	public String getLabel(String lang);
	
	/**
	 * Return all labels written in any language.
	 * 
	 * @return List of data values or empty list
	 */
	public OWLDataValueList getLabels();

	/**
	 * Set the value of rdfs:label for this resource. Empty language identifier will be used.
	 * 
	 * @param label
	 */
	public void setLabel(String label);	
	
	public void setLabel(String label, String lang);	
	
	public OWLDataValue getAnnotation(URI prop);
	
	public OWLDataValue getAnnotation(URI prop, String lang);
	
	public OWLDataValueList getAnnotations(URI prop);
	
	public void addAnnotation(URI property, OWLDataValue value);
	
	public void addAnnotation(URI property, String value);
	
	public void addAnnotation(URI property, String value, String lang);
	
	public void setAnnotation(URI property, OWLDataValue value);
	
	public void setAnnotation(URI property, String value);
	
	public void setAnnotation(URI property, String value, String lang);
	
	public void removeAnnotations(URI property);
	
	/**
	 * Return a (more or less) user-friendly string representation. Tries getLabel() first and
	 * drops to toString() if there is no label. 
	 * 
	 * @return
	 */
	public String toPrettyString();
}
