/*
 * Created on Jul 7, 2004
 */
package org.mindswap.owls.generic.expression;

import org.mindswap.owl.OWLIndividual;
import org.mindswap.swrl.AtomList;

/**
 * @author Evren Sirin
 */
public interface Expression extends OWLIndividual {
	public LogicLanguage getLanguage();
	public void setLanguage(LogicLanguage lang);
	
	public AtomList getBody();
	public void setBody(AtomList body);	
}
