/*
 * Created on Jan 4, 2005
 */
package org.mindswap.owls.process;


/**
 * @author Evren Sirin
 *
 */
public interface ForEach extends Iterate {
    public Local getLoopVar();
    public void setLoopVar(Local var);
    
    public ValueOf getListValue();
    public void setListValue(ValueOf value);
    
    public void setListValue(Perform perform, Parameter parameter);
}
