package org.mindswap.owls.process;

/**
 * Convenience interface for control constructs that are based on bags such as 
 * AnyOrder or Split.   
 * For more information refer to the OWL-S white paper at
 * the official <a href="http://www.daml.org/services/owl-s/">web site.</a>
 * 
 * @author Michael Dänzer (University of Zurich)
 */
public interface BagBasedControlConstruct extends ControlConstruct {
	/**
	 * Returns the control constructs on which this AnyOrder is composed of.
	 * @return a typed bag of control constructs on which this AnyOrder is composed of
	 */
	public ControlConstructBag getComponents();
	
	/**
	 * Adds a control construct to this AnyOrder composition. The new control construct
	 * is added at the end of the AnyOrder.
	 * 
	 * @param component the new control construct to add to the AnyOrder
	 */
	public void addComponent(ControlConstruct component);
	
	/**
	 * Removes the <code>Process:components</code> from the AnyOrder. The <code>ControlConstructBag</code>
	 * remains untouched. Use {@link #deleteComponents()} if you want to delete the bag as well.   
	 */
	public void removeComponents();
	
	/**
	 * Removes the components from this AnyOrder and deleted them all if possible 
	 * (if not used elsewhere in the KB).
	 */
	public void deleteComponents();
}
