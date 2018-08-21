package org.mindswap.owl;

/**
 * 
 * @author Michael Dänzer (University of Zurich)
 * @date 20.03.2007
 */
public interface OWLTransformator {
	public OWLValue transformToOWL(Object object);
	
	public Object transformFromOWL(OWLValue ind);
	
	public OWLClass getOWLClass();
}
