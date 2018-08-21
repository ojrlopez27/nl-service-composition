/*
 * Created on Nov 20, 2004
 */
package impl.jena;

import impl.owl.OWLObjectImpl;

import java.net.URI;

import org.mindswap.owl.OWLConfig;
import org.mindswap.owl.OWLDataValue;
import org.mindswap.owl.OWLDataValueList;
import org.mindswap.owl.OWLEntity;
import org.mindswap.owl.OWLFactory;
import org.mindswap.owl.OWLKnowledgeBase;
import org.mindswap.owl.OWLOntology;
import org.mindswap.owl.vocabulary.RDFS;
import org.mindswap.utils.URIUtils;

import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.rdf.model.StmtIterator;

/**
 * @author Evren Sirin
 */
public abstract class OWLEntityImpl extends OWLObjectImpl implements OWLEntity {
    protected Resource resource;
    
    protected OWLKnowledgeBase kb;
    protected OWLOntology ontology;

	public OWLEntityImpl(OWLOntology ontology, Resource resource) {
		this.resource = resource;
		this.ontology = ontology;
		this.kb = ontology.getKB();
	}
	
    public OWLKnowledgeBase getKB() {
        return kb;
    }

    public OWLOntology getOntology() {
        return ontology;
    }
	
    public boolean isAnon() {
        return resource.isAnon();
    }

    public URI getURI() {
		if(isAnon()) return null;
		
		return URI.create(resource.getURI());
    }
    
    public String getLocalName() {
		if(isAnon()) return null;
		
        return URIUtils.getLocalName(resource.getURI());
    }

    public String getQName() {
        if(isAnon()) return null;
        
        return kb.getQNames().shortForm( resource.getURI() );
    }
    
    public String getNamespace() {
    	return resource.getNameSpace();
    }
    
    public String getLabel() {
	    OWLDataValue value = getAnnotation(RDFS.label);
		return (value == null) ? null : value.getLexicalValue();
    }

    /* (non-Javadoc)
     * @see org.mindswap.owl.OWLObject#getLabel(java.lang.String)
     */
    public String getLabel(String lang) {
	    OWLDataValue value = getAnnotation(RDFS.label, lang);
		return (value == null) ? null : value.getLexicalValue();
    }

    /* (non-Javadoc)
     * @see org.mindswap.owl.OWLObject#getLabels()
     */
    public OWLDataValueList getLabels() {
        return getAnnotations(RDFS.label);
    }

    /* (non-Javadoc)
     * @see org.mindswap.owl.OWLObject#setLabel(java.lang.String)
     */
    public void setLabel(String label) {
        setAnnotation(RDFS.label, label);
    }

    /* (non-Javadoc)
     * @see org.mindswap.owl.OWLObject#setLabel(java.lang.String, java.lang.String)
     */
    public void setLabel(String label, String lang) {
        setAnnotation(RDFS.label, label, lang);
    }

    /* (non-Javadoc)
     * @see org.mindswap.owl.OWLObject#getAnnotation(org.mindswap.owl.OWLAnnotationProperty)
     */
    public OWLDataValue getAnnotation(URI propURI) {
        Property prop = ResourceFactory.createProperty(propURI.toString());
		
		return getDataValue(prop); 
    }

    /* (non-Javadoc)
     * @see org.mindswap.owl.OWLObject#getAnnotations(org.mindswap.owl.OWLAnnotationProperty)
     */
    public OWLDataValueList getAnnotations(URI propURI) {
        Property prop = ResourceFactory.createProperty(propURI.toString());
		
		return getDataValues(prop);
    }

    /* (non-Javadoc)
     * @see org.mindswap.owl.OWLObject#getAnnotation(java.net.URI, java.lang.String)
     */
    public OWLDataValue getAnnotation(URI propURI, String lang) {
        Property prop = ResourceFactory.createProperty(propURI.toString());
	
		return getDataValue(prop, lang);
    }

    /* (non-Javadoc)
     * @see org.mindswap.owl.OWLObject#setAnnotation(java.net.URI, org.mindswap.owl.OWLDataValue)
     */
    public void addAnnotation(URI propURI, OWLDataValue value) {
        Property prop = ResourceFactory.createProperty(propURI.toString());

		resource.addProperty(prop, (Literal) value.getImplementation());
    }
    
    /* (non-Javadoc)
     * @see org.mindswap.owl.OWLObject#setAnnotation(java.net.URI, java.lang.String)
     */
    public void addAnnotation(URI propURI, String value) {
        addAnnotation(propURI, value, "");
    }

    /* (non-Javadoc)
     * @see org.mindswap.owl.OWLObject#setAnnotation(java.net.URI, java.lang.String, java.lang.String)
     */
    public void addAnnotation(URI propURI, String value, String lang) {
        addAnnotation(propURI, kb.createDataValue(value, lang));
    }
    
    /* (non-Javadoc)
     * @see org.mindswap.owl.OWLObject#setAnnotation(java.net.URI, org.mindswap.owl.OWLDataValue)
     */
    public void setAnnotation(URI propURI, OWLDataValue value) {
        Property prop = ResourceFactory.createProperty(propURI.toString());
        
		removeDataValues(prop, value.getLanguage());
		
		resource.addProperty(prop, (Literal) value.getImplementation());
    }
    
    /* (non-Javadoc)
     * @see org.mindswap.owl.OWLObject#setAnnotation(java.net.URI, java.lang.String)
     */
    public void setAnnotation(URI propURI, String value) {
        setAnnotation(propURI, value, "");
    }

    /* (non-Javadoc)
     * @see org.mindswap.owl.OWLObject#setAnnotation(java.net.URI, java.lang.String, java.lang.String)
     */
    public void setAnnotation(URI propURI, String value, String lang) {
        setAnnotation(propURI, kb.createDataValue(value, lang));
    }
    
    protected OWLDataValueList getDataValues(Property prop) {
	    OWLDataValueList list = OWLFactory.createDataValueList();
	    
		StmtIterator i = resource.listProperties(prop);
		while(i.hasNext()) {
			RDFNode node = i.nextStatement().getObject();
			
			if(node instanceof Literal) {
				Literal literal = (Literal) node;

				list.add(new OWLDataValueImpl(literal));					
			}
		} 
		
		return list;        
    }
    
    protected OWLDataValue getDataValue(Property prop) {
	    OWLDataValue value = null;
		
		for(int i = 0; value == null && i < OWLConfig.DEFAULT_LANGS.length; i++)
			value = getDataValue(prop, OWLConfig.DEFAULT_LANGS[i]);
		
		return value;         
    }
    
	protected OWLDataValue getDataValue(Property prop, String lang) {
		StmtIterator i = resource.listProperties(prop);
		while(i.hasNext()) {
			RDFNode node = i.nextStatement().getObject();
			
			if(node instanceof Literal) {
				Literal literal = (Literal) node;

				if(lang == null) 
					return new OWLDataValueImpl(literal);				
				else {
					if(lang.equals(literal.getLanguage()))
						return new OWLDataValueImpl(literal);			
				}					
			}
		}
				
		return null;
	}
	
	protected void removeDataValues(Property prop, String lang) {
		StmtIterator i = resource.listProperties(prop);
		while(i.hasNext()) {
			RDFNode node = i.nextStatement().getObject();
			
			if(node instanceof Literal) {
				Literal literal = (Literal) node;

				if(lang == null) 
					i.remove();				
				else {
					if(lang.equals(literal.getLanguage()))
						i.remove();			
				}					
			}
		}
	}

	public String toString() {
	    URI uri = getURI();
	    return (uri == null) 
	    	? "Anonymous (" + getAnonID() + ")"
	    	: uri.toString();
	}

	public String toPrettyString() {
		String value = getLabel();
		if( value == null ) {
		    URI uri = getURI();
		    if( uri == null )
				value = "Anonymous (" + getAnonID() + ")";
		    else
		        value = (kb == null)
				    ? uri.toString() 
				    : kb.getQNames().shortForm(uri);
		}
		
		return value;	    
	}
	
	public String debugString() {
	    return toString();
	}
	
    /* (non-Javadoc)
     * @see org.mindswap.owl.OWLObject#removeAnnotations(java.net.URI)
     */
    public void removeAnnotations(URI propURI) {
        Property prop = ResourceFactory.createProperty(propURI.toString());
        
        resource.removeAll(prop);
    }
    
    public Object getImplementation() {
        return resource;
    }

    /* (non-Javadoc)
     * @see org.mindswap.owl.OWLObject#getID()
     */
    public Object getAnonID() {
        return resource.getId();
    }
}
