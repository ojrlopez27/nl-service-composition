/*
 * Created on Nov 21, 2004
 */
package org.mindswap.owl;


/**
 * @author Evren Sirin
 */
public interface OWLDataType extends OWLType {
    public boolean isSubTypeOf(OWLDataType dataType);
}
