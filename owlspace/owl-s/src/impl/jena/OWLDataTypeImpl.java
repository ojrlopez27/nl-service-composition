/*
 * Created on Dec 13, 2004
 */
package impl.jena;

import impl.owl.OWLObjectImpl;

import java.net.URI;

import org.mindswap.owl.OWLDataType;
import org.mindswap.owl.OWLFactory;
import org.mindswap.owl.OWLKnowledgeBase;
import org.mindswap.owl.OWLType;

import com.hp.hpl.jena.datatypes.TypeMapper;

/**
 * @author Evren Sirin
 */
public class OWLDataTypeImpl extends OWLObjectImpl implements OWLDataType {
    // this is a dummy KB
    private static OWLKnowledgeBase kb = OWLFactory.createKB();
    
    private URI uri;
    
    public OWLDataTypeImpl(URI datatypeURI) {
        uri = datatypeURI;
    }

    
    public Object getImplementation() {
        return TypeMapper.getInstance().getTypeByName(uri.toString());
    }
    
    /* (non-Javadoc)
     * @see org.mindswap.owl.OWLType#isDataType()
     */
    public boolean isDataType() {
        return true;
    }

    /* (non-Javadoc)
     * @see org.mindswap.owl.OWLType#isClass()
     */
    public boolean isClass() {
        return false;
    }

    /* (non-Javadoc)
     * @see org.mindswap.owl.OWLType#getURI()
     */
    public URI getURI() {
        return uri;
    }
    
    public String debugString() {
        return uri.toString();
    }
    
    public boolean equals(Object object) {
        if(object instanceof OWLDataTypeImpl) {
            return uri.equals(((OWLDataTypeImpl)object).uri);
        }
        
        return false;
    }
    
    public int hashCode() {
        return uri.hashCode();
    }

    public boolean isSubTypeOf(OWLDataType dataType) {
        return kb.isSubTypeOf(this, dataType);
    }

    public boolean isSubTypeOf(OWLType type) {
        return kb.isSubTypeOf(this, type);        
    }

    public boolean isEquivalent(OWLType type) {
        return kb.isEquivalent(this, type);  
    }
    
	public String toString() {
	    URI uri = getURI();
	    return uri.toString();
	}
}
