/*
 * Created on Nov 20, 2004
 */
package org.mindswap.owl;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Evren Sirin
 * @author Michael Dänzer (University of Zurich)
 */
public interface OWLClass extends OWLEntity, OWLType {
	public boolean isSubClassOf(OWLClass c);
	
	public boolean isDisjoint(OWLClass c);

	public Set getSubClasses();
	
	public Set getSubClasses( boolean direct );

	public Set getSuperClasses();

	public Set getSuperClasses( boolean direct );

	public Set getEquivalentClasses();
	
	public boolean isEnumerated();
	
	public OWLIndividualList getEnumerations();
	
	public OWLIndividualList getInstances();    
	
	/**
	 * Returns the instance properties at class defintion. Use <code>getDeclaredProperties()</code>
	 * whenever you need the declared properties of an OWL class
	 * 
	 * @return a map with all instance properties at class definition
	 */
	public Map getProperties();
	
	/**
	 * Returns all declared properties of a class. Use <code>getProperties()</code>
	 * whenever you need the instance properties of an OWL class. Convenience method for
	 * <code>getDeclaredProperties(true)</code>
	 * 
	 * @return a map with all instance properties at class definition
	 */
	public List getDeclaredProperties();
	
	/**
	 * Returns all declared properties of a class. Use <code>getProperties()</code>
	 * whenever you need the instance properties of an OWL class
	 * 
	 * @param direct true, if only properties of the given class itself should be returned. false, if properties of superclasses should be returned too 
	 * @return a map with all instance properties at class definition
	 */
	public List getDeclaredProperties(boolean direct);
}
