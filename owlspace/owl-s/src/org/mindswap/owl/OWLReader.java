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
 * Created on Dec 27, 2003
 *
 */
package org.mindswap.owl;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URI;
import java.util.Set;

/**
 * OWLReader provides various functions to read OWL ontologies. 
 * 
 * @author Evren Sirin
 *
 */
public interface OWLReader {		
	/**
	 * Reads the the ontology from the given URI into a model.   
	 * 
	 * @param uri URI of the ontology
	 * @param withImports if true load the imports closure of the ontology. otherwise only
	 * the given ontology is loaded, imported ones are discarded
	 * @return Jena model
	 * @throws FileNotFoundException
	 * @throws Exception
	 */
	public OWLOntology read(URI uri) throws FileNotFoundException;
	
	public OWLOntology read(Reader in, URI baseURI);

	public OWLOntology read(InputStream in, URI baseURI);

    public OWLOntology read(OWLKnowledgeBase kb, URI uri) throws FileNotFoundException;

    public OWLOntology read(OWLKnowledgeBase kb, Reader in, URI uri);
	
	public OWLOntology read(OWLKnowledgeBase kb, InputStream in, URI baseURI);
	
	public Set getIgnoredOntologies();
	
	public void addIgnoredOntology(URI uri);	
	
	public void setErrorHandler(OWLErrorHandler errHandler);
	
	public OWLCache getCache();
	
	public void setCache(OWLCache cache);
	
	public void setIgnoreFailedImport( boolean ignore );
	
	public boolean getIgnoreFailedImport();

    /**
     * Clear the internal cache
     */
    public void clear();
}
