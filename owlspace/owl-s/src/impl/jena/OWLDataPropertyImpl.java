/*
 * Created on Dec 12, 2004
 */
package impl.jena;

import org.mindswap.owl.OWLDataProperty;
import org.mindswap.owl.OWLOntology;

import com.hp.hpl.jena.rdf.model.Property;

/**
 * @author Evren Sirin
 */
public class OWLDataPropertyImpl extends OWLPropertyImpl implements OWLDataProperty {
    public OWLDataPropertyImpl(OWLOntology model, Property prop) {
        super(model, prop);
    }
}
