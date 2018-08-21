/*
 * Created on Oct 30, 2004
 */
package impl.jena;

import impl.owl.OWLObjectImpl;

import java.net.URI;

import org.mindswap.owl.OWLDataValue;

import com.hp.hpl.jena.rdf.model.Literal;

/**
 * @author Evren Sirin
 */
public class OWLDataValueImpl extends OWLObjectImpl implements OWLDataValue {
    protected Literal literal;
    
    public OWLDataValueImpl(Literal literal) {
        this.literal = literal;       
    }

    public URI getDatatypeURI() {
        String datatypeURI = literal.getDatatypeURI();
        return (datatypeURI == null)
        	? null
        	: URI.create(literal.getDatatypeURI());
    }

    /* (non-Javadoc)
     * @see org.mindswap.owl.OWLDataValue#getLanguage()
     */
    public String getLanguage() {
        return literal.getLanguage();
    }

    /* (non-Javadoc)
     * @see org.mindswap.owl.OWLDataValue#getLexicalValue()
     */
    public String getLexicalValue() {
        return literal.getLexicalForm();
    }

    /* (non-Javadoc)
     * @see org.mindswap.owl.OWLDataValue#getValue()
     */
    public Object getValue() {
        return literal.getValue();
    }
    
    
    public Object getImplementation() {
        return literal;
    }

    public String toString() {
        return getValue().toString();
    }

    /* (non-Javadoc)
     * @see org.mindswap.owl.OWLValue#isDataValue()
     */
    public boolean isDataValue() {
         return true;
    }

    /* (non-Javadoc)
     * @see org.mindswap.owl.OWLValue#isIndividual()
     */
    public boolean isIndividual() {
         return false;
    }
}
