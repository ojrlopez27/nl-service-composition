/*
 * Created on Nov 20, 2004
 */
package org.mindswap.owl;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Set;

import org.mindswap.owls.service.Service;
import org.mindswap.utils.QNameProvider;

/**
 * @author Evren Sirin
 */
public interface OWLKnowledgeBase extends OWLModel {
    /**
     * Return the set of loaded ontologies including the imported ones.
     * @return
     */
    public Set getOntologies();
    
    /**
     * Return the set of ontologies loaded to this KB with the optino to include/exclude
     * imported ones. An ontology <code>ont</code> is considered to be imported if there
     * was no explicit function call to load the ontology, i.e load(ont) or read(ont.getURI(),
     * but <code>ont</code> was imported by one of the loaded ontologies.
     *  
     * @param all If true return all onotlogies, otherwise return only the explcicitly loaded
     * ones 
     * @return
     */
    public Set getOntologies(boolean all);
    
    /**
     * Return the loaded ontology associated with this URI. Return null if there is not ontology 
     * with this URI.
     * 
     * @param uri URI of the ontology
     * @return Ontology with the given URI
     */
    public OWLOntology getOntology(URI uri);
    
    /**
     * Return the base ontology of this KB. At the creation time of each KB an empty ontology is
     * created to be the base onotlogy of this KB. Any function that adds data to the underlying
     * RDF model (addProperty, setProperty, etc.) will actually add the data to this base ontology. 
     */
    public OWLOntology getBaseOntology();
    
    /**
     * Create an empty ontology which is automatically loaded to the KB.  
     * 
     * @return An empty ontology
     */
    public OWLOntology createOntology();
    
    public OWLOntology createOntology(boolean load);
    
    /**
     * Create an empty ontology with the given logical URI (xml:base). Ontology is automatically 
     * loaded to the KB.  
     * 
     * @param uri
     * @return An empty ontology
     */
    public OWLOntology createOntology(URI uri);
    
    /**
     * Create an empty ontology with the given logical URI (xml:base) and physical URI. Ontology 
     * is automatically loaded to the KB.  
     * 
     * @param uri Logical URI of the onotlogy
     * @param fileURI Physical URI of the onotlogy
     * @return An empty ontology
     */
    public OWLOntology createOntology(URI uri, URI fileURI);
    
    /**
     * Create an empty ontology with the given logical URI (xml:base), physical URI and an
     * implementation specific data (Jena Model or OWL-API OWLOntology). Ontology 
     * is automatically loaded to the KB.  
     * 
     * @param uri
     * @param fileURI
     * @param implementation
     * @return The loaded ontology
     */
    public OWLOntology createOntology(URI uri, URI fileURI, Object implementation);    
    
    /**
     * @deprecated Please use {@link load(OWLOntology)} instead
     *  
     */
    public OWLOntology loadOntology(OWLOntology ontology);
    
    /**
     * Load the ontology to the KB. If the ontology is created using another KB a copy will be
     * created before load. The imports will automatically be loaded. It is guaranteed that the
     * getKB() function on the returned OWLOntology (and any of its import ontologies) will
     * return a reference to this KB.
     * 
     * @param ontology
     * @return The loaded ontology
     */
    public OWLOntology load(OWLOntology ontology);
    
    /**
     * Load the ontology to the KB. If the ontology is created using another KB a copy will be
     * created before load. The imports will automatically be loaded if the second parameter is
     * true. It is guaranteed that the getKB() function on the returned OWLOntology (and any of 
     * its import ontologies) will return a reference to this KB.
     * 
     * @param ontology
     * @param withImports
     * @return  The loaded ontology
     */
    public OWLOntology load(OWLOntology ontology, boolean withImports);
    
    /**
     * Unload the ontology from the KB. The imports of the ontology will automatically be unloaded
     * if there are no other ontologies importing them. Note that ontologies loaded explicitly 
     * using the loadOntology will not be unloaded. This function does nothing if the ontology is 
     * not loaded in this KB.
     * 
     * @param ontology
     */
    public void unload(OWLOntology ontology);
    
    /**
     * Unload the ontology with the given URI from the KB. The imports of the ontology will 
     * automatically be unloaded if there are no other ontologies importing them. Note that 
     * ontologies loaded explicitly using the loadOntology will not be unloaded.
     * 
     * @param uri
     */
    public void unload(URI uri);   
   	
	/**
	 * Return the reader assocaited with this KB.
	 * 
	 * @return
	 */
	public OWLReader getReader();
	
	/**
	 * Set the reader assocaited with this KB.
	 * 
	 * @param reader
	 */
	public void setReader(OWLReader reader);
	
	/**
	 * Read the ontology from the given URI, load it to this KB and return a reference to the
	 * ontology object created. Note that ontologies imported by the given ontology are also
	 * loaded to the KB.
	 * 
	 * <p>This function is equivalent to kb.read(new URI(uri)). For loading local files use
	 * kb.read(file.toURI()) instead. If the input string is known to be a URI the
	 * function call kb.read(URI.create(uri)) can be used to avoid URISyntaxException.  
	 * 
	 * @param uri
	 * @return The loaded ontology
	 * @throws URISyntaxException When the given string is not a well-formed URI
	 * @throws FileNotFoundException
	 */
	public OWLOntology read(String uri) throws URISyntaxException, FileNotFoundException;	
	
	/**
	 * Read the ontology from the given URI, load it to this KB and return a reference to the
	 * ontology object created. Note that ontologies imported by the given ontology are also
	 * loaded to the KB.
	 * 
	 * For loading local files use kb.read(file.toURI())
	 * 
	 * @param uri
	 * @return The loaded ontology
	 * @throws FileNotFoundException
	 */
	public OWLOntology read(URI uri) throws FileNotFoundException;	
	
	/**
	 * Read the ontology using the given Reader and the base URI, load it to this KB and return 
	 * a reference to the ontology object created. Note that ontologies imported by the given 
	 * ontology are also loaded to the KB.	 
	 * 
	 * @param in
	 * @param baseURI
	 * @return The loaded ontology
	 */
	public OWLOntology read(Reader in, URI baseURI);
	
	/**
	 * Read the ontology using the given InputStream and the base URI, load it to this KB and return 
	 * a reference to the ontology object created. Note that ontologies imported by the given 
	 * ontology are also loaded to the KB.	
	 * 
	 * @param in
	 * @param baseURI
	 * @return The loaded ontology
	 */
	public OWLOntology read(InputStream in, URI baseURI);
    
    /**
     * @return
     */
    public QNameProvider getQNames();
    
    /**
     * Returns true if the KB checks consistency automatically after each ontology is loaded and
     * rejects to load the ontology if it causes inconsistency.
     */
    public boolean getAutoConsistency();
    
    /**
     * Set/clear the behavior to check consistency automatically after each ontology is loaded. 
     * When AutoConsistency is turned on an ontology that causes inconsistency will not be loaded.
     * If turned off no consistency check will be done automatically.
     */
    public void setAutoConsistency(boolean auto);
    
    public boolean getAutoTranslate();
    public void setAutoTranslate(boolean auto);
    
    public OWLKnowledgeBase getTranslationSource();
    
    /**
     * @deprecated Use createOntology() instead
     */
    public org.mindswap.owls.OWLSOntology createOWLSOntology();
    
    /**
     * @deprecated Use createOntology(URI) instead
     */    
    public org.mindswap.owls.OWLSOntology createOWLSOntology(URI uri);
    
    public Service readService(String uri) throws URISyntaxException, FileNotFoundException;
	public Service readService(URI uri) throws FileNotFoundException;
	public Service readService(Reader in, URI baseURI);
	public Service readService(InputStream in, URI baseURI);
	
	public List readAllServices(String uri) throws URISyntaxException, FileNotFoundException;
	public List readAllServices(URI uri) throws FileNotFoundException;
	public List readAllServices(Reader in, URI baseURI);
	public List readAllServices(InputStream in, URI baseURI);
}
