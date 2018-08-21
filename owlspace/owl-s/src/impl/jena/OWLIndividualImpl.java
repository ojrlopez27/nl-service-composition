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
package impl.jena;

import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.mindswap.owl.OWLClass;
import org.mindswap.owl.OWLDataProperty;
import org.mindswap.owl.OWLDataValue;
import org.mindswap.owl.OWLDataValueList;
import org.mindswap.owl.OWLEntity;
import org.mindswap.owl.OWLFactory;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLIndividualList;
import org.mindswap.owl.OWLObjectProperty;
import org.mindswap.owl.OWLOntology;
import org.mindswap.owl.OWLProperty;
import org.mindswap.owl.OWLValue;
import org.mindswap.owl.OWLWriter;
import org.mindswap.utils.RDFUtils;

import com.hp.hpl.jena.rdf.model.Resource;

/**
 * @author Evren Sirin
 *
 */
public class OWLIndividualImpl extends OWLEntityImpl implements OWLIndividual {
    public OWLIndividualImpl(OWLOntology ontology, Resource resource) {
        super(ontology, resource);
    }

	public OWLEntity inOntology(OWLOntology ont) {
        if(ontology.equals(ont))
            return this;

        return new OWLIndividualImpl(ontology, resource);
    }

    public boolean hasProperty(OWLProperty prop) {
        return kb.hasProperty(this, prop);
    }

    public OWLIndividual getProperty(OWLObjectProperty prop) {
        return kb.getProperty(this, prop);
    }

    public OWLIndividualList getProperties(OWLObjectProperty prop) {
        return kb.getProperties(this, prop);
    }

    public Map getProperties() {
        return kb.getProperties(this);
    }

    public OWLDataValue getProperty(OWLDataProperty prop) {
        return kb.getProperty(this, prop);
    }

    public OWLDataValue getProperty(OWLDataProperty prop, String lang) {
        return kb.getProperty(this, prop, lang);
    }

    public OWLDataValueList getProperties(OWLDataProperty prop) {
        return kb.getProperties(this, prop);
    }

    public OWLIndividual getIncomingProperty(OWLObjectProperty prop) {
        return kb.getIncomingProperty(prop, this);
    }

    public OWLIndividualList getIncomingProperties(OWLObjectProperty prop) {
        return kb.getIncomingProperties(prop, this);
    }

    public OWLIndividualList getIncomingProperties() {
        return kb.getIncomingProperties(this);
    }

    public void setProperty(OWLDataProperty prop, String value) {
        ontology.setProperty(this, prop, value);
    }

    public void setProperty(OWLDataProperty prop, Object value) {
        ontology.setProperty(this, prop, value);
    }

    public void setProperty(OWLDataProperty prop, OWLDataValue value) {
        ontology.setProperty(this, prop, value);
    }

    public void addProperty(OWLDataProperty prop, OWLDataValue value) {
        ontology.addProperty(this, prop, value);
    }


    public void addProperty(OWLDataProperty prop, String value) {
        ontology.addProperty(this, prop, value);
    }

    public void addProperty(OWLDataProperty prop, Object value) {
        ontology.addProperty(this, prop, value);
    }


    public void removeProperties(OWLProperty prop) {
        ontology.removeProperties(this, prop);
    }

    public void removeProperty(OWLProperty theProp, OWLValue theValue) {
        ontology.removeProperty(this,theProp,theValue);
    }

    public void addProperty(OWLObjectProperty prop, OWLIndividual value) {
        ontology.addProperty(this, prop, value);
    }

    public void setProperty(OWLObjectProperty prop, OWLIndividual value) {
        ontology.setProperty(this, prop, value);
    }

    public Set getTypes() {
        return kb.getTypes(this);
    }

    public boolean hasProperty(OWLProperty prop, OWLValue value) {
        return kb.hasProperty(this, prop, value);
    }

    /* (non-Javadoc)
     * @see org.mindswap.owl.OWLIndividual#getType()
     */
    public OWLClass getType() {
        return kb.getType(this);
    }

    /* (non-Javadoc)
     * @see org.mindswap.owl.OWLIndividual#isType(org.mindswap.owl.OWLClass)
     */
    public boolean isType(OWLClass c) {
        return kb.isType(this, c);
    }

    public boolean isDataValue() {
        return false;
    }

    public boolean isIndividual() {
         return true;
    }

    /* (non-Javadoc)
     * @see org.mindswap.owl.OWLIndividual#addType(org.mindswap.owl.OWLClass)
     */
    public void addType(OWLClass c) {
        ontology.addType(this, c);
    }

    /* (non-Javadoc)
     * @see org.mindswap.owl.OWLIndividual#removeType(org.mindswap.owl.OWLClass)
     */
    public void removeTypes() {
        ontology.removeTypes(this);
    }

    public String toRDF() {
        return toRDF( true, true );
    }

    public String toRDF( boolean printRDFTag ) {
        return toRDF( printRDFTag, false );
    }
    
    public String toRDF( boolean printRDFTag, boolean keepNamespaces ) {
        OWLOntology ont = OWLFactory.createOntology();

        getBnodeClosure( this, ont );

        StringWriter sw = new StringWriter();
        OWLWriter writer = ont.getWriter();
        boolean show = writer.isShowXmlDeclaration();
        writer.setShowXmlDeclaration( false );
        
        ont.write( sw );
        
        writer.setShowXmlDeclaration( show );

        String rdf = sw.toString();

        if( printRDFTag )
            return rdf;
        else 
            return RDFUtils.removeRDFTag( rdf, keepNamespaces );
    }

    private void getBnodeClosure( OWLIndividual ind, OWLOntology ont ) {
        OWLClass c = ind.getOntology().getType( ind );
        // FIXME Do  we really want to look outside the ontology?
        if( c == null )
            c = ind.getKB().getType( ind );
        if( c != null )
            ont.addType( ind, c );

        Map props = ind.getProperties();
        for( Iterator i = props.keySet().iterator(); i.hasNext(); ){
            OWLProperty p = (OWLProperty) i.next();
            List values = (List) props.get( p );
            for(Iterator j = values.iterator(); j.hasNext();) {
                OWLValue value = (OWLValue) j.next();
                ont.addProperty( ind, p, value );
                if( value instanceof OWLIndividual ) {
                    OWLIndividual obj = (OWLIndividual) value;
                    if( obj.isAnon() )
                        getBnodeClosure( obj, ont );
                }
            }
        }
    }

    public OWLOntology getSourceOntology() {
        return getOntology().getTranslationSource();
    }

    /**
     * @deprecated Use getOntology() instead
     */
    public org.mindswap.owls.OWLSOntology getOWLSOntology() {
        return (org.mindswap.owls.OWLSOntology) getOntology();
    }

    public boolean isSameAs(OWLIndividual other) {
        return kb.isSameAs(this, other);
    }

    public OWLIndividualList getSameIndividuals() {
        return kb.getSameIndividuals( this );
    }

    public boolean isDifferentFrom(OWLIndividual other) {
        return kb.isDifferentFrom(this, other);
    }

    public OWLIndividualList getDifferentIndividuals() {
        return kb.getDifferentIndividuals( this );
    }

	public void delete() {
		ontology.removeIndividuals(this);
	}	    
}
