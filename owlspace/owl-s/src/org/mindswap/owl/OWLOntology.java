/*
 * Created on Nov 20, 2004
 */
package org.mindswap.owl;

import java.net.URI;

import java.util.Set;

import org.mindswap.owls.service.Service;

/**
 * @author Evren Sirin
 */
public interface OWLOntology extends OWLModel {
    public URI getURI();
    public void setURI(URI uri);

    public URI getFileURI();
    public void setFileURI(URI uri);

    public OWLKnowledgeBase getKB();

    public Set getImports();
    public Set getImports(boolean direct);
//    public void setImports(Set imports);
    public void addImport(OWLOntology ontology);


    /**
     * If this OWL-S ontology was translated from an older version of OWL-S
     * (using <code>OWLSVersionTranslator</code>) then this function will return a
     * reference to the original ontology. This way the information that
     * might have been lost during translation, e.g. non-OWL-S descriptions
     * in the original file, can still be accessed. If the ontology
     * originally belongs to the latest version then this function will
     * return a reference to itself.
     *
     * @return
     */
    public OWLOntology getTranslationSource();

    public void setTranslationSource(OWLOntology ontology);

    public Service getService();

    /**
     * Merge the contents of this ontology with <code>ont</code> and return the
     * merged ontology. Neither of the ontologies is changed. The returned
     * ontology is NOT loaded to the KB automatically.
     *
     *
     * @param ont
     * @return
     */
    public OWLOntology union( OWLOntology ont );

    /**
     * Add the contents of <code>ont</code> into this ontology.
     *
     * @param ont
     * @return
     */
    public void add( OWLOntology ont );
}
