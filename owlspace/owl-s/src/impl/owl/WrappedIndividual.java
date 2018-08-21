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
 * Created on Dec 28, 2004
 */
package impl.owl;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Map;
import java.util.Set;

import org.mindswap.owl.OWLClass;
import org.mindswap.owl.OWLDataProperty;
import org.mindswap.owl.OWLDataValue;
import org.mindswap.owl.OWLDataValueList;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLIndividualList;
import org.mindswap.owl.OWLKnowledgeBase;
import org.mindswap.owl.OWLObject;
import org.mindswap.owl.OWLObjectProperty;
import org.mindswap.owl.OWLOntology;
import org.mindswap.owl.OWLProperty;
import org.mindswap.owl.OWLValue;

/**
 * @author Evren Sirin
 *
 */
public class WrappedIndividual extends OWLObjectImpl implements OWLIndividual {
    protected OWLIndividual individual;

    public WrappedIndividual(OWLIndividual ind) {
        individual = ind;

        setNextView(ind.getNextView());
        ind.setNextView(this);
    }

    public Object getImplementation() {
        return individual.getImplementation();
    }

    public boolean hasProperty(OWLProperty prop) {
        return individual.hasProperty(prop);
    }

    public boolean hasProperty(OWLProperty prop, OWLValue value) {
        return individual.hasProperty(prop, value);
    }

    public Map getProperties() {
        return individual.getProperties();
    }

    public OWLIndividual getProperty(OWLObjectProperty prop) {
        return individual.getProperty(prop);
    }

    protected OWLIndividualList getPropertiesAs(OWLObjectProperty prop, Class result) {
        return new CastingList(individual.getProperties(prop), result);
    }

    protected OWLObject getPropertyAs(OWLObjectProperty prop, Class result) {
        OWLObject value = individual.getProperty(prop);
        return (value == null) ? null : value.castTo(result);
    }

    protected OWLObject getPropertyAs(OWLDataProperty prop, Class result) {
        OWLObject value = individual.getProperty(prop);
        return (value == null) ? null : value.castTo(result);
    }

    protected String getPropertyAsString(OWLDataProperty prop) {
        OWLDataValue value = individual.getProperty(prop);
        return (value == null) ? null : value.toString();
    }

    protected String getPropertyAsString(OWLDataProperty prop, String lang) {
        OWLDataValue value = individual.getProperty(prop, lang);
        return (value == null) ? null : value.toString();
    }

    protected URI getPropertyAsURI(OWLDataProperty prop) {
        OWLDataValue value = individual.getProperty(prop);
        return (value == null) ? null : URI.create(value.getLexicalValue().trim());
    }

    protected URL getPropertyAsURL(OWLDataProperty prop) {
        OWLDataValue value = individual.getProperty(prop);

        if(value != null) {
            try {
                return new URL(value.toString().trim());
            } catch(MalformedURLException e) {
            }
        }

        return null;
    }

    /* (non-Javadoc)
     * @see org.mindswap.owl.OWLIndividual#getProperties(org.mindswap.owl.OWLObjectProperty)
     */
    public OWLIndividualList getProperties(OWLObjectProperty prop) {
        return individual.getProperties(prop);
    }

    /* (non-Javadoc)
     * @see org.mindswap.owl.OWLIndividual#getProperty(org.mindswap.owl.OWLDataProperty)
     */
    public OWLDataValue getProperty(OWLDataProperty prop) {
        return individual.getProperty(prop);
    }

    /* (non-Javadoc)
     * @see org.mindswap.owl.OWLIndividual#getProperty(org.mindswap.owl.OWLDataProperty, java.lang.String)
     */
    public OWLDataValue getProperty(OWLDataProperty prop, String lang) {
        return individual.getProperty(prop, lang);
    }

    public OWLDataValueList getProperties(OWLDataProperty prop) {
        return individual.getProperties(prop);
    }

    public OWLIndividualList getIncomingProperties() {
        return individual.getIncomingProperties();
    }

    public OWLIndividual getIncomingProperty(OWLObjectProperty prop) {
        return individual.getIncomingProperty(prop);
    }

    public OWLIndividualList getIncomingProperties(OWLObjectProperty prop) {
        return individual.getIncomingProperties(prop);
    }

    public void setProperty(OWLDataProperty prop, String value) {
        individual.setProperty(prop, value);
    }

    public void setProperty(OWLDataProperty prop, Object value) {
        individual.setProperty(prop, value);
    }

    public void setProperty(OWLDataProperty prop, OWLDataValue value) {
        individual.setProperty(prop, value);
    }

    public void addProperty(OWLDataProperty prop, OWLDataValue value) {
        individual.addProperty(prop, value);
    }

    public void addProperty(OWLDataProperty prop, Object value) {
        individual.addProperty(prop, value);
    }

    public void addProperty(OWLDataProperty prop, String value) {
        individual.addProperty(prop, value);
    }

    public void removeProperties(OWLProperty prop) {
        individual.removeProperties(prop);
    }

    public void removeProperty(OWLProperty theProp, OWLValue theValue) {
        individual.removeProperty(theProp,theValue);
    }

    public void addProperty(OWLObjectProperty prop, OWLIndividual value) {
        individual.addProperty(prop, value);
    }

    public void setProperty(OWLObjectProperty prop, OWLIndividual value) {
        individual.setProperty(prop, value);
    }

    public void addType(OWLClass c) {
        individual.addType(c);
    }

    public void removeTypes() {
        individual.removeTypes();
    }

    public OWLClass getType() {
        return individual.getType();
    }

    public Set getTypes() {
        return individual.getTypes();
    }

    public boolean isType(OWLClass c) {
        return individual.isType(c);
    }

    public boolean isAnon() {
        return individual.isAnon();
    }

    /* (non-Javadoc)
     * @see org.mindswap.owl.OWLObject#getURI()
     */
    public URI getURI() {
        return individual.getURI();
    }



    /* (non-Javadoc)
     * @see org.mindswap.owl.OWLObject#getAnonID()
     */
    public Object getAnonID() {
        return individual.getAnonID();
    }

    /* (non-Javadoc)
     * @see org.mindswap.owl.OWLObject#getLabel()
     */
    public String getLabel() {
        return individual.getLabel();
    }

    /* (non-Javadoc)
     * @see org.mindswap.owl.OWLObject#getLabel(java.lang.String)
     */
    public String getLabel(String lang) {
        return individual.getLabel(lang);
    }

    /* (non-Javadoc)
     * @see org.mindswap.owl.OWLObject#getLabels()
     */
    public OWLDataValueList getLabels() {
        return individual.getLabels();
    }

    /* (non-Javadoc)
     * @see org.mindswap.owl.OWLObject#setLabel(java.lang.String)
     */
    public void setLabel(String label) {
        individual.setLabel(label);
    }

    /* (non-Javadoc)
     * @see org.mindswap.owl.OWLObject#setLabel(java.lang.String, java.lang.String)
     */
    public void setLabel(String label, String lang) {
        individual.setLabel(label, lang);
    }

    /* (non-Javadoc)
     * @see org.mindswap.owl.OWLObject#getAnnotation(java.net.URI)
     */
    public OWLDataValue getAnnotation(URI prop) {
        return individual.getAnnotation(prop);
    }

    /* (non-Javadoc)
     * @see org.mindswap.owl.OWLObject#getAnnotation(java.net.URI, java.lang.String)
     */
    public OWLDataValue getAnnotation(URI prop, String lang) {
        return individual.getAnnotation(prop, lang);
    }

    /* (non-Javadoc)
     * @see org.mindswap.owl.OWLObject#getAnnotations(java.net.URI)
     */
    public OWLDataValueList getAnnotations(URI prop) {
        return individual.getAnnotations(prop);
    }

    /* (non-Javadoc)
     * @see org.mindswap.owl.OWLObject#addAnnotation(java.net.URI, org.mindswap.owl.OWLDataValue)
     */
    public void addAnnotation(URI prop, OWLDataValue value) {
        individual.addAnnotation(prop, value);
    }

    /* (non-Javadoc)
     * @see org.mindswap.owl.OWLObject#addAnnotation(java.net.URI, java.lang.String)
     */
    public void addAnnotation(URI prop, String value) {
        individual.addAnnotation(prop, value);
    }

    /* (non-Javadoc)
     * @see org.mindswap.owl.OWLObject#addAnnotation(java.net.URI, java.lang.String, java.lang.String)
     */
    public void addAnnotation(URI prop, String value, String lang) {
        individual.addAnnotation(prop, value, lang);
    }

    /* (non-Javadoc)
     * @see org.mindswap.owl.OWLObject#setAnnotation(java.net.URI, org.mindswap.owl.OWLDataValue)
     */
    public void setAnnotation(URI prop, OWLDataValue value) {
        individual.setAnnotation(prop, value);
    }

    /* (non-Javadoc)
     * @see org.mindswap.owl.OWLObject#setAnnotation(java.net.URI, java.lang.String)
     */
    public void setAnnotation(URI prop, String value) {
        individual.setAnnotation(prop, value);
    }

    /* (non-Javadoc)
     * @see org.mindswap.owl.OWLObject#setAnnotation(java.net.URI, java.lang.String, java.lang.String)
     */
    public void setAnnotation(URI prop, String value, String lang) {
        individual.addAnnotation(prop, value, lang);
    }

    /* (non-Javadoc)
     * @see org.mindswap.owl.OWLObject#removeAnnotations(java.net.URI)
     */
    public void removeAnnotations(URI prop) {
        individual.removeAnnotations(prop);
    }

    /* (non-Javadoc)
     * @see org.mindswap.owl.OWLObject#getModel()
     */
    public OWLKnowledgeBase getKB() {
        return individual.getKB();
    }

    /* (non-Javadoc)
     * @see org.mindswap.owl.OWLObject#debugString()
     */
    public String debugString() {
        return individual.debugString();
    }

    /* (non-Javadoc)
     * @see org.mindswap.owl.OWLValue#isDataValue()
     */
    public boolean isDataValue() {
        return false;
    }

    /* (non-Javadoc)
     * @see org.mindswap.owl.OWLValue#isIndividual()
     */
    public boolean isIndividual() {
        return true;
    }

    /* (non-Javadoc)
     * @see org.mindswap.owl.OWLObject#getOntology()
     */
    public OWLOntology getOntology() {
        return individual.getOntology();
    }

    public String toString() {
        return individual.toString();
    }

    public String getLocalName() {
        return individual.getLocalName();
    }

    public String getQName() {
        return individual.getQName();
    }

    public String toRDF() {
        return individual.toRDF();
    }

    public String toRDF(boolean withRDFTag) {
        return individual.toRDF(withRDFTag);
    }

    public String toRDF(boolean withRDFTag, boolean keepNamespaces) {
        return individual.toRDF(withRDFTag, keepNamespaces);
    }

    public OWLOntology getSourceOntology() {
        return individual.getSourceOntology();
    }

    /**
     * @deprecated Use getOntology() instead
     */
    public org.mindswap.owls.OWLSOntology getOWLSOntology() {
        return individual.getOWLSOntology();
    }

    public boolean isSameAs(OWLIndividual other) {        
        return individual.isSameAs( other );
    }

    public OWLIndividualList getSameIndividuals() {
        return individual.getSameIndividuals();
    }

    public boolean isDifferentFrom(OWLIndividual other) {
        return individual.isDifferentFrom( other );
    }
    
    public OWLIndividualList getDifferentIndividuals() {
        return individual.getDifferentIndividuals();
    }	
    

	public String toPrettyString() {
		return individual.toPrettyString();	    
	}

	public void delete() {
		individual.delete();
	}

	public String getNamespace() {
		return individual.getNamespace();
	}    
}
