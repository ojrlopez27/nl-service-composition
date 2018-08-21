/*
 * Created on Dec 12, 2004
 */
package impl.jena;

import org.mindswap.owl.OWLObjectProperty;
import org.mindswap.owl.OWLOntology;

import com.hp.hpl.jena.rdf.model.Property;

/**
 * @author Evren Sirin
 */
public class OWLObjectPropertyImpl extends OWLPropertyImpl implements OWLObjectProperty {
    public OWLObjectPropertyImpl(OWLOntology model, Property prop) {
        super(model, prop);
    }
}
