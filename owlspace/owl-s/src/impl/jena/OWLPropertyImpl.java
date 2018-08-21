/*
 * Created on Mar 29, 2005
 */
package impl.jena;

import org.mindswap.owl.OWLOntology;
import org.mindswap.owl.OWLProperty;

import com.hp.hpl.jena.rdf.model.Resource;

/**
 * @author Evren Sirin
 *
 */
public abstract class OWLPropertyImpl extends OWLEntityImpl implements OWLProperty {
    public OWLPropertyImpl(OWLOntology ontology, Resource resource) {
        super(ontology, resource);
    }
}
