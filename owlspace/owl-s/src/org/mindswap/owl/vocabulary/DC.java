/*
 * Created on 08.09.2005
 */
package org.mindswap.owl.vocabulary;

import java.net.URI;

import org.mindswap.owl.EntityFactory;
import org.mindswap.owl.OWLDataProperty;
import org.mindswap.utils.URIUtils;

/**
 * Class with static references to the Dublin Core (DC) ontology.
 * 
 * @author Michael Daenzer (University of Zurich)
 * @see <a href="http://dublincore.org/">Dublin Core Metadata Initiative</a>
 * @see <a href="http://dublincore.org/2003/03/24/dces#">Dublin Core Ontology</a>
 */
public class DC {
	public final static String ns = "http://purl.org/dc/elements/1.1.owl#";

	public final static URI getURI() { return URI.create(ns); } 
	
	public final static OWLDataProperty creator = 
	    EntityFactory.createDataProperty(URIUtils.createURI(ns + "creator"));
	public final static OWLDataProperty contributor = 
	    EntityFactory.createDataProperty(URIUtils.createURI(ns + "contributor"));
	public final static OWLDataProperty date = 
	    EntityFactory.createDataProperty(URIUtils.createURI(ns + "date"));
	public final static OWLDataProperty rights = 
	    EntityFactory.createDataProperty(URIUtils.createURI(ns + "rights"));

}
