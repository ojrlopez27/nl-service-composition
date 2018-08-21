/*
 * Created on Jan 4, 2005
 */
package org.mindswap.owls.process;


/**
 * @author Evren Sirin
 *
 */
public interface Produce extends ControlConstruct {
    public OutputBinding getBinding();
    
    public OutputBindingList getBindings();
    
    public void addBinding(OutputBinding binding);
    	
	public void addBinding(Output output, ParameterValue paramValue);
	
	public void addBinding(Output output, Perform perform, Parameter param);    
    
    public void setBinding(OutputBinding binding);
}
