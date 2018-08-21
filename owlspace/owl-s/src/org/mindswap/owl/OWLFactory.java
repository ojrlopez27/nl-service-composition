// The MIT License
//
// Copyright (c) 2004 Evren Sirin
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to
// deal in the Software without restriction, including without limitation the
// rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
// sell copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
// FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
// IN THE SOFTWARE.

/*
 * Created on Dec 29, 2003
 *
 */
package org.mindswap.owl;

import java.net.URI;
import java.util.Map;

import org.mindswap.owls.vocabulary.OWLS;
import org.mindswap.query.ABoxQueryParser;


/**
 * @author Evren Sirin
 *
 */
public class OWLFactory {
    private static OWLKnowledgeBase kb;
    private static OWLOntology owlsService; 
    private static OWLOntology owlsProfile; 
    private static OWLOntology owlsGrounding;
    private static OWLOntology owlsProcess; 
        
    public interface Interface {
        public Map getReasoners();
        
        public Object getReasoner(String reasonerName);
        
	    public OWLKnowledgeBase createKB();
	    
	    public OWLOntology createOntology();
	    
	    public OWLOntology createOntology(URI uri);
	    
	    public OWLReader createReader();
	    
	    public OWLWriter createWriter();

		public OWLDataValueList createDataValueList();

		public OWLIndividualList createIndividualList();
		
		public ABoxQueryParser createRDQLParser(OWLModel model);
		
		public Map getDefaultConverters();
    }
    
    private static String[] implementations = {"impl.jena.JenaOWLFactory"};
    
    private static OWLFactory.Interface factory = createFactory();
    
    private static OWLFactory.Interface createFactory() {
        for(int i = 0; (factory == null) && (i < implementations.length); i++) {
            try {
                Class impl = Class.forName(implementations[i]);
                factory = (OWLFactory.Interface) impl.newInstance();
            } catch(Exception e) {
                System.err.println("Cannot create OWLFactory!");
                e.printStackTrace();
            }
        }
        
        return factory;
    }
    
    public static OWLKnowledgeBase createKB() {
        return factory.createKB();
    }

    public static OWLOntology createOntology() {        
        return createOntology(false);
    }
    
    public static OWLOntology createOntology(URI uri) {
        return createOntology(uri, false);
    }

    /**
     * Create an empty OWL ontology and optionally import the
     * OWL-S ontologies (Service, Profile, Process and Grounding 
     * ontologies).
     * 
     * @param importOWLS
     * @return
     */
    public static OWLOntology createOntology( boolean importOWLS ) {        
        OWLOntology ont = factory.createOntology();
        if( importOWLS )
            addOWLSImports( ont );
        return ont;
    }
    
    /**
     * Create an empty OWL ontology with the given logical URI
     * and optionally import the OWL-S ontologies (Service, Profile, Process
     * and Grounding ontologies)
     * 
     * @param importOWLS
     * @return
     */
    public static OWLOntology createOntology( URI uri, boolean importOWLS ) {
        OWLOntology ont = factory.createOntology(uri);
        if( importOWLS )
            addOWLSImports( ont );
        return ont;
    }
    
    /**
     * Import the OWL-S ontologies (Service, Profile, Process
     * and Grounding ontologies) in the given ontology
     * 
     * @param ont
     * @return
     */

    public static void addOWLSImports( OWLOntology ont ) {
        loadOWLSOntologies();
        
        ont.addImport( owlsService );
        ont.addImport( owlsProfile );
        ont.addImport( owlsProcess );
        ont.addImport( owlsGrounding );
    }
        
    private static void loadOWLSOntologies() {
        if( kb == null ) {
            kb = createKB();
            
            try {
                owlsService = kb.read( OWLS.Service.URI );
                owlsProfile = kb.read( OWLS.Profile.URI );
                owlsProcess = kb.read( OWLS.Process.URI );
                owlsGrounding = kb.read( OWLS.Grounding.URI );
            }
            catch( Exception e ) {
                owlsService = kb.createOntology( URI.create( OWLS.Service.URI ) ); 
                owlsProfile = kb.createOntology( URI.create( OWLS.Profile.URI ) ); 
                owlsProcess = kb.createOntology( URI.create( OWLS.Process.URI ) ); 
                owlsGrounding = kb.createOntology( URI.create( OWLS.Grounding.URI ) );     
            }
        }
    }
    
    public static OWLReader createReader() {
        return factory.createReader();
    }

    public static OWLWriter createWriter() {
        return factory.createWriter();
    }

    public static OWLDataValueList createDataValueList() {
        return factory.createDataValueList();
    }

    public static OWLIndividualList createIndividualList() {
        return factory.createIndividualList();
    }


    public static Map getReasoners() {
        return factory.getReasoners();
    }
    
    public static Object getReasoner(String reasonerName) {
        return factory.getReasoner(reasonerName);
    }
    
    public static ABoxQueryParser createRDQLParser(OWLModel model) {
        return factory.createRDQLParser(model);
    }
    
    public static Map getDefaultConverters() {
        return factory.getDefaultConverters();
    }
}
