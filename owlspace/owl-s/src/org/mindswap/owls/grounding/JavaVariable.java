package org.mindswap.owls.grounding;

import org.mindswap.owl.OWLIndividual;
import org.mindswap.owls.process.Parameter;

/**
 * 
 * @author Michael Dänzer (University of Zurich)
 * @date 28.02.2007
 */
public interface JavaVariable extends OWLIndividual {
	public void setJavaType(String type);
	public String getJavaType();
	public void removeJavaType();
	
	public void setOWLSParameter(Parameter parameter);
	public Parameter getOWLSParameter();	
	public void removeOWLSParameter();
	
	public void setTransformator(String transformator);
	public String getTransformator();
	public void removeTransformator();
}
