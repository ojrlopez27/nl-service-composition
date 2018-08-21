/*
 * Created on Jul 7, 2004
 */
package org.mindswap.owls.process;

import java.util.List;

/**
 * @author Evren Sirin
 */
public interface ValueFunction extends ParameterValue {
	public String getFunction();
	
	public List getParameters();
}
