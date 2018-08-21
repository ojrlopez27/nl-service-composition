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

package org.mindswap.utils;

import java.io.StringReader;

import org.mindswap.owl.OWLFactory;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLKnowledgeBase;
import org.mindswap.owl.OWLOntology;
import org.mindswap.owl.vocabulary.RDF;

public class RDFUtils {
    public static String addRDFTag( String rdf ) {
        return
	        "<rdf:RDF xmlns:rdf=\"" + RDF.getURI() + "\">" +
	        rdf +
	        "</rdf:RDF>";        
    }
    
    public static String removeRDFTag( String rdf ) {
	    int rdfTagBegin = rdf.indexOf( "<rdf:RDF" );
	    int rdfTagEnd = rdf.indexOf( ">", rdfTagBegin + 1 );
	    int rdfEndTag = rdf.indexOf( "</rdf:RDF>" );
	    
	    rdf = rdf.substring( rdfTagEnd + 1, rdfEndTag );      
	    
	    return rdf;
    }    
    
    public static String removeRDFTag( String rdf, boolean keepNamespaces ) {
	    int rdfTagBegin = rdf.indexOf( "<rdf:RDF" );
	    int rdfTagEnd = rdf.indexOf( ">", rdfTagBegin + 1 );
	    int rdfEndTag = rdf.indexOf( "</rdf:RDF>" );
	    
        if( keepNamespaces ) {
            StringBuffer buffer = new StringBuffer();
            buffer.append( rdf.substring( 0, rdfTagEnd + 1) );
            buffer.append( "<rdf:Description><rdfs:label rdf:parseType=\"Literal\">");
            buffer.append( rdf.substring( rdfTagEnd + 1, rdfEndTag ) );
            buffer.append( "</rdfs:label></rdf:Description>");            
            buffer.append( rdf.substring( rdfEndTag ) );
            
            OWLKnowledgeBase kb = OWLFactory.createKB();
            OWLOntology ont = kb.read( new StringReader( buffer.toString() ), null );
            OWLIndividual ind = ont.getIndividuals().individualAt( 0 );
            rdf = ind.getLabel();
        }
        else
            rdf = rdf.substring( rdfTagEnd + 1, rdfEndTag );      
	    
	    return rdf;
    }  
}
