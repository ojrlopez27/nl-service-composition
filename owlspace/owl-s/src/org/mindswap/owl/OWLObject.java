/*
 * Created on Dec 12, 2004
 */
package org.mindswap.owl;

/**
 * @author Evren Sirin
 */
public interface OWLObject {
    public Object getImplementation();

    public OWLObject getNextView();
    
    public void setNextView(OWLObject nextView);

    public OWLObject castTo(Class javaClass);
    
    public boolean canCastTo(Class javaClass);
    
    public String debugString();
}
