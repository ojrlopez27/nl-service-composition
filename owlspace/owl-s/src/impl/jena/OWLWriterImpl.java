/*
 * Created on Dec 12, 2004
 */
package impl.jena;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.mindswap.owl.OWLClass;
import org.mindswap.owl.OWLFactory;
import org.mindswap.owl.OWLModel;
import org.mindswap.owl.OWLOntology;
import org.mindswap.owl.OWLWriter;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.Ontology;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFWriter;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFSyntax;

/**
 * @author Evren Sirin
 */
public class OWLWriterImpl implements OWLWriter {
    private List prettyTypes;
    private Map prefixMap;

    private boolean showXmlDeclaration;
    
    private Charset defaultCharset;

    public OWLWriterImpl() {
        prettyTypes = new ArrayList();
        prefixMap = new HashMap();

        showXmlDeclaration = true;
        
        defaultCharset = Charset.forName("UTF8");
    }       
    

    public boolean isShowXmlDeclaration() {
        return showXmlDeclaration;
    }

    public void setShowXmlDeclaration( boolean showXmlDeclaration ) {
        this.showXmlDeclaration = showXmlDeclaration;
    }

    private void writeInternal(OWLModel theOntology, Writer out, URI baseURI) {
        OntModel model = null;

        if (theOntology instanceof OWLKnowledgeBaseImpl)
        {
            // want to serialize a kb, lets create the output model as a union
            // of all the ontologies that make up the kb
            model = ModelFactory.createOntologyModel();

            OWLKnowledgeBaseImpl aKB = (OWLKnowledgeBaseImpl)theOntology;
            Set onts = aKB.ontologies;
            Iterator iter = onts.iterator();

            // create ontology node for imports
            Ontology aOntNode = null;
            if (baseURI == null) {
                Resource aRes = model.createResource();
                aRes.addProperty(RDF.type,OWL.Ontology);
                aOntNode = (Ontology)aRes.as(Ontology.class);
            }
            else aOntNode = model.getOntology(baseURI.toString());

            while (iter.hasNext()) {
                OWLOntology aOnt = (OWLOntology)iter.next();
                // if this was read into the kb and not imported
                // lets add its model to the output model
                if (aKB.notImported.contains(aOnt) || aKB.getBaseOntology().equals(aOnt)) {
                    model.add( (Model) aOnt.getImplementation());
                }
                else {
                    // otherwise, lets just add an imports stmt for the ontology
                    aOntNode.addImport(model.getResource(aOnt.getURI().toString()));
                }
            }
        }
        else {
            // otherwise this is a model/ontology, lets just get its implementation
            // model and use that for the output.
            model = ((OWLModelImpl) theOntology).getOntModel();
        }

        OntModel aOutModel = ModelFactory.createOntologyModel();
        aOutModel.add(model.getBaseModel());

        RDFWriter writer = aOutModel.getWriter("RDF/XML-ABBREV");

        // we don't want literals to be used as attributes
        writer.setProperty("blockRules", new Resource[] {RDFSyntax.propertyAttr, RDFSyntax.sectionListExpand});

        // show xml header <?xml version="..." ...?>
        writer.setProperty("showXmlDeclaration", new Boolean( showXmlDeclaration ));

        writer.setProperty("allowBadURIs", Boolean.TRUE);

        for(Iterator i = prefixMap.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry entry = (Map.Entry) i.next();
            aOutModel.setNsPrefix(entry.getKey().toString(), entry.getValue().toString());
        }

        if(baseURI != null) {
            writer.setProperty("xmlbase", baseURI.toString());
            aOutModel.setNsPrefix("", baseURI.toString());
        }

        writer.setProperty("prettyTypes", prettyTypes.toArray(new Resource[prettyTypes.size()]));

        if (theOntology instanceof OWLOntology)
            handleImports((OWLOntology)theOntology,aOutModel, baseURI);
//        else if (theOntology instanceof OWLKnowledgeBase)
//            handleImports((OWLKnowledgeBase)theOntology,aOutModel, baseURI);

        writer.write(aOutModel.getBaseModel(), out, "");
    }

    private void handleImports(OWLOntology theOntology, OntModel theModel, URI theBaseURI)
    {
        // imports stuff
        Ontology aOntNode = null;

        // search for existing ontology nodes in the model...
        Ontology aBlankOnt = theModel.getOntology("");
        if (aBlankOnt != null) {
            // i dont think jena auto-serializes imports as blanks, we can ignore this case
        }

        // this is likely the case, jena will output the Ontology object
        // named with the URI of the ontology
        if (theOntology.getURI() != null) {
            Ontology aNamedOnt = null;

            if (theBaseURI != null)
                aNamedOnt = theModel.getOntology(theBaseURI.toString());
            else theModel.getOntology(theOntology.getURI().toString());

            if (aNamedOnt != null) {
                aOntNode = aNamedOnt;
            }
        }

        // if we didnt find an ontology tag, create it
        if (aOntNode == null && theOntology.getImports().size() > 0) {
            if (theModel.listSubjectsWithProperty(RDF.type,OWL.Ontology).hasNext())
                aOntNode = (Ontology)theModel.listSubjectsWithProperty(RDF.type,OWL.Ontology).nextResource().as(Ontology.class);
            else
            {
                if (theBaseURI != null)
                    aOntNode = theModel.createOntology(theBaseURI.toString());
                else {
                    if (theOntology.getURI() == null)
                        aOntNode = theModel.createOntology(null);
                    else aOntNode = theModel.createOntology(theOntology.getURI().toString());
                }
            }
        }

        // add imports statements to the output model
        Iterator aIter = theOntology.getImports().iterator();
        while (aIter.hasNext()) {
            OWLOntology aOnt = (OWLOntology)aIter.next();
            if (aOnt.getURI() != null)
                aOntNode.addImport(theModel.getResource(aOnt.getURI().toString()));
        }
    }

//    private void handleImports(OWLKnowledgeBase theKB, OntModel theModel, URI theBaseURI)
//    {
//        Iterator aIter = theKB.getOntologies().iterator();
//        while (aIter.hasNext())
//        {
//            OWLOntology aOntology = (OWLOntology)aIter.next();
//            handleImports(aOntology,theModel, theBaseURI);
//        }
//    }

    /* (non-Javadoc)
     * @see org.mindswap.owl.OWLWriter#setNsPrefix(java.lang.String, java.net.URI)
     */
    public void setNsPrefix(String prefix, String uri) {
        prefixMap.put(prefix, uri.toString());
    }

    /* (non-Javadoc)
     * @see org.mindswap.owl.OWLWriter#addPrettyType(org.mindswap.owl.OWLClass)
     */
    public void addPrettyType(OWLClass c) {
        prettyTypes.add(c.getImplementation());
    }

    /* (non-Javadoc)
     * @see org.mindswap.owl.OWLWriter#write(org.mindswap.owl.OWLModel, java.io.Writer)
     */
    public void write(OWLModel ont, Writer writer) {
        writeInternal(ont, writer, null);
    }

    /* (non-Javadoc)
     * @see org.mindswap.owl.OWLWriter#write(org.mindswap.owl.OWLModel, java.io.Writer, java.net.URI)
     */
    public void write(OWLModel ont, Writer writer, URI baseURI) {
        writeInternal(ont, writer, baseURI);
    }

    /* (non-Javadoc)
     * @see org.mindswap.owl.OWLWriter#write(org.mindswap.owl.OWLModel, java.io.OutputStream)
     */
    public void write(OWLModel ont, OutputStream out) {
        writeInternal(ont, new OutputStreamWriter(out, defaultCharset), null);
    }

    /* (non-Javadoc)
     * @see org.mindswap.owl.OWLWriter#write(org.mindswap.owl.OWLModel, java.io.OutputStream, java.net.URI)
     */
    public void write(OWLModel ont, OutputStream out, URI baseURI) {
        writeInternal(ont, new OutputStreamWriter(out, defaultCharset), baseURI);
    }

    public static void main(String[] args) {

//        URI aURI = URI.create("http://www.mindswap.org/2004/owl-s/1.1/ZipCodeFinder.owl");
//        OWLKnowledgeBase aKb = OWLFactory.createKB();
//        aKb.read(aURI);
//        aKb.getBaseOntology().write(System.err);
//
//        OWLOntology aOnt = aKb.getOntology(aURI);
//        aOnt.addImport(OWLFactory.createOntology(URI.create("http://www.mindswap.org/2004/owl-s/1.1/Process.owl")));
//
//        System.err.println(aKb.getBaseOntology().equals(aURI));
//
//        aOnt.write(System.err);


        OWLOntology aOnt = OWLFactory.createOntology(URI.create("http://www.mindswap.org/2004/owl-s/1.1/ZipCodeFinder.owl"));
        aOnt.addImport(OWLFactory.createOntology(URI.create("http://www.mindswap.org/2004/owl-s/1.1/Process.owl")));
//        aOnt.addImport(OWLFactory.createOntology(URI.create("")));
//System.err.println(aOnt.getIndividuals().size());
//((com.hp.hpl.jena.rdf.model.Model)aOnt.getImplementation()).write(System.err);
        aOnt.write(System.err);

        ((com.hp.hpl.jena.rdf.model.Model)aOnt.getImplementation()).write(System.err);
    }
}
