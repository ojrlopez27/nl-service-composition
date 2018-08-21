//The MIT License
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


package impl.jena;

import impl.owl.OWLCacheImpl;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.mindswap.owl.OWLCache;
import org.mindswap.owl.OWLErrorHandler;
import org.mindswap.owl.OWLFactory;
import org.mindswap.owl.OWLKnowledgeBase;
import org.mindswap.owl.OWLOntology;
import org.mindswap.owl.OWLReader;
import org.mindswap.utils.URIUtils;
import org.xml.sax.InputSource;

import com.hp.hpl.jena.rdf.arp.ParseException;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFErrorHandler;
import com.hp.hpl.jena.rdf.model.RDFReader;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

/**
 * @author Evren Sirin
 *
 */
public class OWLReaderImpl implements OWLReader {
    public static boolean DEBUG = false;
    
	private static Map modelCache = new HashMap();
	
	private static Set ignores = new HashSet();
	
	private OWLCache cache;
	
	private OWLErrorHandler errHandler;
	private RDFReader reader;
	
	private boolean ignoreFailedImport = false;
	
	private RDFErrorHandler jenaErrHandler = new RDFErrorHandler() {
        public void warning(Exception e) {
            errHandler.warning(ParseException.formatMessage(e));
        }

        public void error(Exception e) {
            errHandler.error(ParseException.formatMessage(e));
        }

        public void fatalError(Exception e) {
            errHandler.error(ParseException.formatMessage(e));
			throw e instanceof RuntimeException 
			? (RuntimeException) e
			: new RuntimeException( e );	
        }            
    };
	
	public OWLReaderImpl() {
	    cache = new OWLCacheImpl();
	    
	    addIgnoredOntology(URI.create(OWL.getURI()));
	    addIgnoredOntology(URI.create(RDF.getURI()));
	    addIgnoredOntology(URI.create(RDFS.getURI()));
	    
	    Model m = ModelFactory.createDefaultModel();
	    reader = m.getReader();
	    m = null; // m is no longer needed.
	}
	
	public OWLCache getCache() {
	    return cache;
	}
	
	public void clear() {
	    modelCache.clear();
	}
	
	private InputSource createInputSource(URI uri) throws FileNotFoundException {
	    return createInputSource (uri, false);
	}
	
	private InputSource createInputSource(final URI uri, boolean addToCache) throws FileNotFoundException {
	    InputSource in = null;	
		if(cache.isForced()) {
			in = cache.getCachedFile(uri.toString());
			if(in == null)  {
				try {
					in = new InputSource(uri.toURL().openConnection().getInputStream());
					if (addToCache)
					{
			            cache.updateCachedFile (uri);
					}
				} catch(Exception e) {
					System.err.println("WARNING: Cannot read file " + uri);
					throw new FileNotFoundException("The file " + uri + " cannot be parsed");
				}
			}
			else
			{
	            cache.updateCachedFile (uri);
			}
		}
		else {
			try {
			    in = new InputSource(uri.toURL().openConnection().getInputStream());
			} catch(Exception e) {
				System.err.println("WARNING: Cannot read file " + uri);
				in = cache.getCachedFile(uri.toString());
				if(in == null) 
					throw new FileNotFoundException("The file " + uri + " cannot be parsed");				
			}
		}
				
		return in;
	}

    public void setErrorHandler(OWLErrorHandler errHandler) {
        this.errHandler = errHandler;
        
        if(errHandler == null)
            reader.setErrorHandler(null);
        else
            reader.setErrorHandler(jenaErrHandler);
    }

    public OWLOntology read(URI uri) throws FileNotFoundException {
        return read(OWLFactory.createKB(), uri);
    }
    
    public OWLOntology read(Reader in, URI baseURI) {
        return readInternal(OWLFactory.createKB(), new InputSource(in), null, baseURI);
    }
    
    public OWLOntology read(InputStream in, URI baseURI) {
        return readInternal(OWLFactory.createKB(), new InputSource(in), null, baseURI);
    }

    public OWLOntology read(OWLKnowledgeBase kb, URI uri) throws FileNotFoundException {
        InputSource in = createInputSource(uri);
        return readInternal(kb, in, uri, uri);
    }

    public OWLOntology read(OWLKnowledgeBase kb, Reader in, URI baseURI) {
		return readInternal(kb, new InputSource(in), null, baseURI);
    }
    
    public OWLOntology read(OWLKnowledgeBase kb, InputStream in, URI baseURI) {
		return readInternal(kb, new InputSource(in), null, baseURI);
    }
	
	private OWLOntology readInternal(OWLKnowledgeBase kb, InputSource in, URI physicalURI, URI baseURI) {
	    OWLOntology ont = readFile((OWLKnowledgeBaseImpl) kb, in, physicalURI, baseURI);
	    return kb.load(ont);
	}

	private OWLOntology readImport(OWLKnowledgeBaseImpl kb, URI importURI) throws FileNotFoundException {
	    URI fileURI = (importURI == null) ? null : URIUtils.standardURI(importURI);
	    
	    OWLOntology ont = kb.getOntology(fileURI);
	    
		if(ont != null) {
		    if(DEBUG) System.out.println("DEBUG: Use already loaded ontology for the URI " + fileURI);
			return ont;
		}
		
	    return readFile(kb, createInputSource(importURI, true), importURI, importURI);
	}

	private OWLOntology readFile(OWLKnowledgeBaseImpl kb, InputSource in, URI physicalURI, URI baseURI) {
	    URI fileURI = (physicalURI == null) ? null : URIUtils.standardURI(physicalURI);
	    
	    // FIXME check ignores
	    
	    OWLOntology ont = kb.getOntology(fileURI);
	    
		if(ont != null) {
		    if(DEBUG) System.out.println("DEBUG: Use already loaded ontology for the URI " + fileURI);
			return ont;
		}
		
		String base = (baseURI == null) ? null: baseURI.toString();

		ont = (OWLOntology) modelCache.get(fileURI);
		if(ont != null) {
		    if(DEBUG) System.out.println("DEBUG: Use already parsed ontology for the URI " + fileURI);
		}
		else {
		    if(DEBUG) System.out.println("DEBUG: Reading " + fileURI);
			Model jenaModel = ModelFactory.createDefaultModel();
			if(in.getByteStream() != null)
			    reader.read(jenaModel, in.getByteStream(), base);
			else
			    reader.read(jenaModel, in.getCharacterStream(), base);
			
			ont = kb.createOntologyWithoutLoading(fileURI, fileURI, jenaModel);
			
			if(physicalURI != null)
			    modelCache.put(physicalURI, ont);
			StringBuffer out = new StringBuffer();
			if(DEBUG) out.append("--------------------------------\n");
			if(DEBUG) out.append("Handling imports for " + fileURI + "\n");
			StmtIterator i = jenaModel.listStatements(null, OWL.imports, (Resource) null);
			while (i.hasNext()) {
				Statement stmt = i.nextStatement();
				String importFile = stmt.getResource().toString();
				if(DEBUG) out.append("Loading import " + importFile + "\n");
				
				try {
				    URI importURI = new URI(importFile);
				    OWLOntology importOnt = readImport(kb, importURI); 
					ont.addImport(importOnt);
				} catch(URISyntaxException e) {
					System.err.println("WARNING: The import file is not a valid URI: " + importFile);
				} catch(FileNotFoundException e) {
					if(errHandler != null) errHandler.warning(e.toString());
					System.err.println("WARNING: The import file " + importFile + " cannot be parsed");
                }
			}
			if (DEBUG) System.out.println(out.toString());
		}
		
		return ont;
	}


    /* (non-Javadoc)
     * @see org.mindswap.owl.OWLReader#getIgnores()
     */
    public Set getIgnoredOntologies() {
        return ignores;
    }

    /* (non-Javadoc)
     * @see org.mindswap.owl.OWLReader#ignore(java.net.URI)
     */
    public void addIgnoredOntology(URI uri) {
        ignores.add(URIUtils.standardURI(uri));
    }
	
    public void setCache(OWLCache cache) {
        this.cache = cache;
    }
    
	public void setIgnoreFailedImport( boolean ignore ) {
	    ignoreFailedImport = ignore;
	}
	
	public boolean getIgnoreFailedImport() {
	    return ignoreFailedImport;
	}
    
}
