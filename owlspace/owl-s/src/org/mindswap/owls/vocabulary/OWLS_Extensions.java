package org.mindswap.owls.vocabulary;

import org.mindswap.owl.EntityFactory;
import org.mindswap.owl.OWLObjectProperty;
import org.mindswap.utils.URIUtils;

public class OWLS_Extensions {
	public static String baseURI = "http://www.ifi.unizh.ch/ddis/ont/owl-s/OWLSExtensions.owl#";	
	
	public static class Process {			
		public static OWLObjectProperty hasPerform;
		
		static {
			hasPerform = EntityFactory.createObjectProperty(URIUtils.createURI(baseURI + "hasPerform"));			
		}		
	}
}
