/*
 * Created on Jul 31, 2004
 */
package org.mindswap.owl.vocabulary;

import org.mindswap.owl.EntityFactory;
import org.mindswap.owl.OWLClass;
import org.mindswap.owl.OWLDataProperty;
import org.mindswap.owl.OWLObjectProperty;
import org.mindswap.owl.list.ListVocabulary;
import org.mindswap.utils.URIUtils;

/**
 * @author Evren Sirin
 */
public class SWRL {
	public final static String URI = "http://www.w3.org/2003/11/swrl#";

	public final static OWLClass AtomList = 
	    EntityFactory.createClass(URIUtils.createURI(URI + "AtomList"));
	public final static OWLClass Atom = 
	    EntityFactory.createClass(URIUtils.createURI(URI + "Atom"));	
	public final static OWLClass ClassAtom = 
	    EntityFactory.createClass(URIUtils.createURI(URI + "ClassAtom"));	
	public final static OWLClass IndividualPropertyAtom = 
	    EntityFactory.createClass(URIUtils.createURI(URI + "IndividualPropertyAtom"));	
	public final static OWLClass DifferentIndividualsAtom = 
	    EntityFactory.createClass(URIUtils.createURI(URI + "DifferentIndividualsAtom"));	
	public final static OWLClass SameIndividualAtom = 
	    EntityFactory.createClass(URIUtils.createURI(URI + "SameIndividualAtom"));	
	public final static OWLClass DatavaluedPropertyAtom = 
	    EntityFactory.createClass(URIUtils.createURI(URI + "DatavaluedPropertyAtom"));	
	public final static OWLClass DataRangeAtom = 
	    EntityFactory.createClass(URIUtils.createURI(URI + "DataRangeAtom"));	
	public final static OWLClass BuiltinAtom = 
	    EntityFactory.createClass(URIUtils.createURI(URI + "BuiltinAtom"));		
	
	public final static OWLClass Builtin = 
	    EntityFactory.createClass(URIUtils.createURI(URI + "Builtin"));	
	public final static OWLClass Variable = 
	    EntityFactory.createClass(URIUtils.createURI(URI + "Variable"));	

	public final static OWLObjectProperty classPredicate = 
	    EntityFactory.createObjectProperty(URIUtils.createURI(URI + "classPredicate"));	
	public final static OWLObjectProperty propertyPredicate = 
	    EntityFactory.createObjectProperty(URIUtils.createURI(URI + "propertyPredicate"));	
	public final static OWLObjectProperty argument1 = 
	    EntityFactory.createObjectProperty(URIUtils.createURI(URI + "argument1"));	
	public final static OWLObjectProperty argument2 = 
	    EntityFactory.createObjectProperty(URIUtils.createURI(URI + "argument2"));	
	public final static OWLDataProperty _argument2 = 
	    EntityFactory.createDataProperty(URIUtils.createURI(URI + "argument2"));		
	public final static OWLObjectProperty dataRange = 
	    EntityFactory.createObjectProperty(URIUtils.createURI(URI + "dataRange"));	
	public final static OWLObjectProperty builtin = 
	    EntityFactory.createObjectProperty(URIUtils.createURI(URI + "builtin"));	
	public final static OWLObjectProperty arguments = 
	    EntityFactory.createObjectProperty(URIUtils.createURI(URI + "arguments"));	
	
	public final static ListVocabulary AtomListVocabulary = 
	    RDF.ListVocabulary.specialize(AtomList);
}
