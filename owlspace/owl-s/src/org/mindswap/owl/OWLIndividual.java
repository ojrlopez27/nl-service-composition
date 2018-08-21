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
package org.mindswap.owl;

import java.net.URI;
import java.util.Map;
import java.util.Set;


/**
 * The interface for OWL individuals.
 *
 * @author Evren Sirin
 *
 */
public interface OWLIndividual extends OWLEntity, OWLValue {
    /**
     * Return true if a value for the given property exists.
     *
     * @param propURI
     * @return
     */
    public boolean hasProperty(OWLProperty prop);

    /**
     * Return true if the given value for the property exists.
     *
     * @param propURI
     * @return
     */
    public boolean hasProperty(OWLProperty prop, OWLValue value);

    /**
     * Get the value for the given object property. If the resource has more than one value for this property
     * a random one will be returned
     *
     * @param propURI
     * @return
     */
    public OWLIndividual getProperty(OWLObjectProperty prop);


    /**
     * Get all the values for the given object property.
     *
     * @param propURI
     * @return
     */
    public OWLIndividualList getProperties(OWLObjectProperty prop);

    /**
     * Get the value for the given datatype property. If the resource has more than one value for this property
     * with different language identifiers than the returned value will be determined according to the
     * settings defined in {@link org.mindswap.owl.OWLConfig#DEFAULT_LANGS OWLConfig}
     *
     * @param propURI
     * @return
     */
    public OWLDataValue getProperty(OWLDataProperty prop);

    /**
     * Get the value for the given property URI with the specified language identifier. If the value
     * for the given language does not exist return null even if a value is found for another language.
     * Use {@link org.mindswap.owl.OWLIndividual#getProperty(URI) getProperty(URI)} to be more flexible.
     *
     * @param propURI
     * @param lang
     * @return
     */
    public OWLDataValue getProperty(OWLDataProperty prop, String lang);

    /**
     * Get all the values for the given datatype property.
     *
     * @param propURI
     * @return
     */
    public OWLDataValueList getProperties(OWLDataProperty prop);

    /**
     * Get all the properties asserted about this individual.
     */
    public Map getProperties();

    public OWLIndividual getIncomingProperty(OWLObjectProperty prop);

    public OWLIndividualList getIncomingProperties(OWLObjectProperty prop);

    public OWLIndividualList getIncomingProperties();

    /**
     * Set the value for the given data property to the given plain literal
     * value (no language identifier). All the existing data values (that has
     * no language identifier) will be removed.
     *
     * @param propURI
     * @param value
     */
    public void setProperty(OWLDataProperty prop, String value);

    /**
     * Set the value for the given data property to the given literal by
     * determining the RDF datatype from Java class. This function is
     * equivalent to <code>setProperty(prop, OWLFactory.createDataValue(value))</code>.
     *
     * @param prop
     * @param value
     */
    public void setProperty(OWLDataProperty prop, Object value);

    /**
     * Set the value for the given data property. All the existing data values
     * (that has the same language identifier with the given value) will be removed.
     *
     * @param propURI
     * @param value
     */
    public void setProperty(OWLDataProperty prop, OWLDataValue value);

    public void addProperty(OWLDataProperty prop, OWLDataValue value);

    public void addProperty(OWLDataProperty prop, String value);

    public void addProperty(OWLDataProperty prop, Object value);

    public void removeProperties(OWLProperty prop);

    public void removeProperty(OWLProperty theProp, OWLValue theValue);

    public void addProperty(OWLObjectProperty prop, OWLIndividual value);

    public void setProperty(OWLObjectProperty prop, OWLIndividual value);

    public void addType(OWLClass c);

    public void removeTypes();
    
    /**
     * Deletes this individual from its enclosing ontology
     */
    public void delete();

    public OWLClass getType();

    public Set getTypes();

    public boolean isType(OWLClass c);

    /**
     * Return the RDF/XML representation of this individual. The returned RDF/XML is
     * supposed to be a nested RDF statement which is the b-node closure of the
     * individual.
     */
    public String toRDF();

    /**
     * Return the RDF/XML representation of this individual. The returned RDF/XML is
     * supposed to be a nested RDF statement which is the b-node closure of the
     * individual.
     *
     * @param withRDFTag If false the enclosing <rdf:RDF> tag will be omitted
     * @return
     */
    public String toRDF(boolean withRDFTag);
    
    /**
     * Return the RDF/XML representation of this individual. The returned RDF/XML is
     * supposed to be a nested RDF statement which is the b-node closure of the
     * individual.
     *
     * @param withRDFTag If false the enclosing <rdf:RDF> tag will be omitted
     * @param keepNamespaces If true namespace declarations will be kept even if 
     * there is no enclosing enclosing <rdf:RDF> tag (good to quote whole expression)
     * @return
     */
    public String toRDF(boolean withRDFTag, boolean keepNamespaces);


    /**
     * Return the original OWL ontology this individual comes from. If the
     * OWL-S ontology this individual comes from is the latest version then
     * this function will return the same value as <code>getOntology()</code>.
     * If the original ontology was from an older version of OWL-S and
     * translated to the latest version then this function will return a
     * reference to the original ontology. This way the information that
     * might have been lost during translation, e.g. non-OWL-S descriptions
     * in the original file, can still be accessed.
     *
     * @return
     */
    public OWLOntology getSourceOntology();

    /**
     * @deprecated Use getOntology() instead
     */
    public org.mindswap.owls.OWLSOntology getOWLSOntology();


    /**
     * Return true if this individuals is same as the given individual 
     * (according to the semantics of owl:differentFrom).
     * 
     * @param other
     * @return
     */
    public boolean isSameAs(OWLIndividual other);

    /**
     * Return all then individuals that are same as this individual.
     * 
     * @return
     */
    public OWLIndividualList getSameIndividuals();

    /**
	 * Return true if given this individual is different from the given 
	 * individual (according to the semantics of owl:differentFrom).
	 * 
     * @param ind1
     * @param ind2
     * @return
     */
    public boolean isDifferentFrom(OWLIndividual other);

    /**
     * Get all the individuals that are different from this individual.
     * 
     * @return
     */
    public OWLIndividualList getDifferentIndividuals();

}
