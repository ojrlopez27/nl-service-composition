/*
 * Created on Dec 17, 2004
 */
package org.mindswap.owls.process;

/**
 * @author Evren Sirin
 */
public interface MultiConditional extends Conditional {
	/**
	 * Get all the conditions.
	 * 
	 * @return
	 */
	public ConditionList getConditions();
	
	public void addCondition(Condition condition);
}
