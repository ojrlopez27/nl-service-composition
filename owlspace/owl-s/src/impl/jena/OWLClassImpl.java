/*
 * Created on Dec 12, 2004
 */
package impl.jena;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.mindswap.owl.OWLClass;
import org.mindswap.owl.OWLIndividualList;
import org.mindswap.owl.OWLOntology;
import org.mindswap.owl.OWLType;

import com.hp.hpl.jena.rdf.model.Resource;

/**
 * @author Evren Sirin
 * @author Michael Dänzer (University of Zurich)
 */
public class OWLClassImpl extends OWLEntityImpl implements OWLClass {
    public OWLClassImpl(OWLOntology ont, Resource resource) {
        super(ont, resource);
    }

    public boolean isSubClassOf(OWLClass c) {
        return kb.isSubClassOf(this, c);
    }

    public boolean isDisjoint(OWLClass c) {
        return kb.isDisjoint(this, c);
    }

    public Map getProperties() {
    	return kb.getProperties(this);
    }
    
    public List getDeclaredProperties() {
    	return kb.getDeclaredProperties(this);
    }
    
    public List getDeclaredProperties(boolean direct) {
    	return kb.getDeclaredProperties(this, direct);
    }
    
    public Set getSubClasses() {
        return kb.getSubClasses(this);
    }

    public Set getSubClasses( boolean direct ) {
        return kb.getSubClasses( this, direct );
    }

    public Set getSuperClasses() {
        return kb.getSuperClasses( this );
    }
    
    public Set getSuperClasses( boolean direct ) {
        return kb.getSuperClasses( this, direct );
    }

    public Set getEquivalentClasses() {
        return kb.getEquivalentClasses(this);
    }

    public OWLIndividualList getInstances() {
        return kb.getInstances(this);
    }

    public boolean isDataType() {
        return false;
    }

    public boolean isClass() {
        return true;
    }

    public boolean isSubTypeOf(OWLType type) {
        return kb.isSubTypeOf(this, type);
    }

    public boolean isEquivalent(OWLType type) {
        return kb.isEquivalent(this, type);
    }

    public boolean isEnumerated() {        
        return kb.isEnumerated(this);
    }

    public OWLIndividualList getEnumerations() {
        return kb.getEnumerations(this);
    }
}
