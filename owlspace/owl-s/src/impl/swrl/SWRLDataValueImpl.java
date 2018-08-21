/*
 * Created on Dec 28, 2004
 */
package impl.swrl;

import impl.jena.OWLDataValueImpl;

import org.mindswap.owl.OWLDataValue;
import org.mindswap.swrl.SWRLDataValue;

import com.hp.hpl.jena.rdf.model.Literal;

/**
 * @author Evren Sirin
 *
 */
public class SWRLDataValueImpl extends OWLDataValueImpl implements SWRLDataValue {
    public SWRLDataValueImpl(Literal literal) {
        super(literal);
    }

    public SWRLDataValueImpl(OWLDataValue value) {
        super((Literal) value.getImplementation());
    }

    public boolean isVariable() {
        return false;
    }
}
