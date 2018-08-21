/*
 * Created on Jul 7, 2004
 */
package org.mindswap.owls.generic.expression;

import java.net.URI;

import org.mindswap.owl.OWLIndividual;

/**
 * @author Evren Sirin
 */
public interface LogicLanguage extends OWLIndividual {	
//	public LogicLanguage KIF  = (LogicLanguage) OWLS_1_1.instance.Expression.KIF.convertTo(LogicLanguage.class);
//	public LogicLanguage SWRL = (LogicLanguage) OWLS_1_1.instance.Expression.SWRL.convertTo(LogicLanguage.class);	
//	public LogicLanguage DRS  = (LogicLanguage) OWLS_1_1.instance.Expression.DRS.convertTo(LogicLanguage.class);
	
	public URI getRefURI();
}
