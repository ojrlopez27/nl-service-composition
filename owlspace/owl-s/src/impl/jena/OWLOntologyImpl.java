/*
 * Created on Dec 10, 2004
 */
package impl.jena;

import java.io.Reader;
import java.io.StringReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.mindswap.owl.OWLClass;
import org.mindswap.owl.OWLDataProperty;
import org.mindswap.owl.OWLDataValue;
import org.mindswap.owl.OWLEntity;
import org.mindswap.owl.OWLFactory;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLIndividualList;
import org.mindswap.owl.OWLKnowledgeBase;
import org.mindswap.owl.OWLObjectProperty;
import org.mindswap.owl.OWLOntology;
import org.mindswap.owl.OWLProperty;
import org.mindswap.owl.OWLValue;
import org.mindswap.owls.OWLSFactory;
import org.mindswap.owls.process.Binding;
import org.mindswap.owls.process.BindingList;
import org.mindswap.owls.process.Parameter;
import org.mindswap.owls.service.Service;
import org.mindswap.owls.vocabulary.MoreGroundings;
import org.mindswap.owls.vocabulary.OWLS;
import org.mindswap.owls.vocabulary.OWLS_Extensions;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;

/**
 * @author Evren Sirin
 */
public class OWLOntologyImpl extends OWLModelImpl implements OWLOntology, org.mindswap.owls.OWLSOntology {
    private OWLKnowledgeBase kb;

    private OWLIndividual ontResource;

    private URI uri;
    private URI fileURI;

    private Map imports;

    private OWLOntology sourceOntology;
    
    private Vector owlsNamespaces;

    public OWLOntologyImpl(OWLKnowledgeBase kb, URI uri, URI fileURI, Model jenaModel) {
        super(jenaModel);
        super.setBaseOntology(this);

        this.uri = uri;
        this.fileURI = fileURI;
        this.kb = kb;

        imports = new HashMap();

        sourceOntology = this;


    }

    public OWLKnowledgeBase getKB() {
        return kb;
    }

    public Set getImports() {
        return new HashSet( imports.values() );
    }

    public Set getImports(boolean direct) {
        if(!direct) {
            LinkedHashSet importsClosure = new LinkedHashSet();
            List orderedList = new ArrayList();
            orderedList.add( this );
            for( int i = 0; i < orderedList.size(); i++ ) {
                OWLOntology ont = (OWLOntology) orderedList.get( i );
                if( importsClosure.contains( ont ) )
                    continue;
                else
                    importsClosure.add( ont );

                for(Iterator it = ont.getImports().iterator(); it.hasNext();) {
                    OWLOntology imported = (OWLOntology) it.next();
                    if( importsClosure.contains( imported ) )
                        continue;
                    else
                        orderedList.add( imported );
                }
            }
            return importsClosure;
        }

        return getImports();
    }

    public void addImport(OWLOntology ontology) {
        URI fileURI = ontology.getFileURI();
        if( fileURI != null && !imports.containsKey( fileURI )) {            
            imports.put( fileURI, ontology );
        }
    }


    public void addImports(Set theImports) {
        Iterator aIter = theImports.iterator();
        while (aIter.hasNext()) {
            try {
                OWLOntology aOnt = (OWLOntology)aIter.next();
                addImport(aOnt);
            }
            catch (Exception ex) {
                // probably a cast exception, someone trying to add an ontology that
                // isnt actually an ontology object.  just ignore, try the next one
            }
        }
        //this.imports.addAll(theImports);
    }

    public OWLClass createClass(URI uri) {
        OWLClass c = getClass(uri);

        if(c == null) {
            Resource r = asResource(uri);
            ontModel.add(r, RDF.type, OWL.Class);
            kb.refresh();

            c = new OWLClassImpl(this, r);
        }

        return c;
    }

    public OWLObjectProperty createObjectProperty(URI uri) {
        OWLObjectProperty c = getObjectProperty(uri);

        if(c == null) {
            Property r = asProperty(uri);
            ontModel.add(r, RDF.type, OWL.ObjectProperty);
            kb.refresh();

            c = new OWLObjectPropertyImpl(this, r);
        }

        return c;
    }

    public OWLDataProperty createDataProperty(URI uri) {
        OWLDataProperty c = getDataProperty(uri);

        if(c == null) {
            Property r = asProperty(uri);
            ontModel.add(r, RDF.type, OWL.DatatypeProperty);
            kb.refresh();

            c = new OWLDataPropertyImpl(this, r);
        }

        return c;
    }

    public OWLIndividual createIndividual(URI uri) {
        OWLIndividual ind = getIndividual(uri);

        if(ind == null) {
            Resource r = asResource(uri);
            ind = new OWLIndividualImpl(this, r);

            kb.refresh();
        }

        return ind;
    }

    public OWLIndividual createInstance(OWLClass c) {
        Resource r = ontModel.createResource();
        ontModel.add(r, RDF.type, (Resource) c.getImplementation());
        OWLIndividual ind = new OWLIndividualImpl(this, r);
        kb.refresh();

        return ind;
    }

    public OWLIndividual createInstance(OWLClass c, URI uri) {
        Resource r = asResource(uri);
        ontModel.add(r, RDF.type, (Resource) c.getImplementation());
        OWLIndividual ind = new OWLIndividualImpl(this, r);
        kb.refresh();

        return ind;
    }

    public OWLDataValue createDataValue(String value) {
        return new OWLDataValueImpl(ontModel.createLiteral(value));
    }

    public OWLDataValue createDataValue(String value, String language) {
        return new OWLDataValueImpl(ontModel.createLiteral(value, language));
    }

    public OWLDataValue createDataValue(Object value, URI datatypeURI) {
        if(datatypeURI.equals(org.mindswap.owl.vocabulary.RDF.XMLLiteral))
            return new OWLDataValueImpl(ontModel.createLiteral(value.toString(), true));
        else
            return new OWLDataValueImpl(ontModel.createTypedLiteral(value, datatypeURI.toString()));
    }

    public OWLDataValue createDataValue(Object value) {
        if(value instanceof OWLDataValue )
            return (OWLDataValue) value;
        else if(value instanceof URI)
            return new OWLDataValueImpl(ontModel.createTypedLiteral(value, XSDDatatype.XSDanyURI));
        else
            return new OWLDataValueImpl(ontModel.createTypedLiteral(value));
    }

    public URI getURI() {
        return uri;
    }

    public URI getFileURI() {
        return fileURI;
    }

    /* (non-Javadoc)
     * @see org.mindswap.owl.OWLObject#debugString()
     */
    public String debugString() {
        return "Ontology (" + uri + ")";
    }

    /* (non-Javadoc)
     * @see org.mindswap.owl.OWLOntology#getOntologyObject()
     */
    public OWLEntity getOntologyObject() {
        return ontResource;
    }

    /* (non-Javadoc)
     * @see org.mindswap.owl.OWLOntology#asSingletonKB()
     */
    public OWLKnowledgeBase asKB() {
        return asKB(true);
    }

    /* (non-Javadoc)
     * @see org.mindswap.owl.OWLOntology#asSingletonKB(boolean)
     */
    public OWLKnowledgeBase asKB(boolean withImports) {
        OWLKnowledgeBase kb = OWLFactory.createKB();
        kb.load(this, withImports);

        return kb;

    }

    protected OWLDataValue wrapDataValue(Literal l, OWLOntology ont) {
        return new OWLDataValueImpl(l);
    }

    protected OWLIndividual wrapIndividual(Resource r, OWLOntology ont) {
        return new OWLIndividualImpl(this, r);
    }

    protected OWLClass wrapClass(Resource r, OWLOntology ont) {
        return new OWLClassImpl(this, r);
    }

    protected OWLObjectProperty wrapObjectProperty(Property p, OWLOntology ont) {
        return new OWLObjectPropertyImpl(this, p);
    }

    protected OWLDataProperty wrapDataProperty(Property p, OWLOntology ont) {
        return new OWLDataPropertyImpl(this, p);
    }

    public String toString() {
        if(uri == null)
            return "Ontology()";

        return "Ontology(" + uri + ")";
    }

    public void setURI(URI uri) {
        this.uri = uri;

        if(uri != null)
            ontResource = new OWLIndividualImpl(this, ontModel.getResource(uri.toString()));
    }

    public void setFileURI(URI uri) {
        this.fileURI = uri;
    }

//    public void setKB(OWLKnowledgeBase kb) {
//        this.kb = kb;
//    }

//    public void setImports(Set imports) {
//        this.imports = imports;
//    }

    public void refresh() {
        ontModel.rebind();
        kb.refresh();
    }

    public void setProperty(OWLIndividual ind, OWLDataProperty prop, String value) {
        setProperty(ind, prop, createDataValue(value));
    }

    public void setProperty(OWLIndividual ind, OWLDataProperty prop, Object value) {
        setProperty(ind, prop, createDataValue(value));
    }

    public void setProperty(OWLIndividual ind, OWLDataProperty prop, OWLDataValue value) {
        Resource subject = (Resource) ind.getImplementation();
        Property predicate = (Property) prop.getImplementation();
        Literal object = (Literal) value.getImplementation();

        ontModel.remove(ontModel.listStatements(subject, predicate, (Literal) null));
         ontModel.add(subject, predicate, object);
         kb.refresh();
    }

    public void addProperty(OWLIndividual ind, OWLProperty prop, OWLValue value) {
        if( prop instanceof OWLObjectProperty && value instanceof OWLIndividual )
            addProperty( ind, (OWLObjectProperty) prop, (OWLIndividual) value );
        else if( prop instanceof OWLDataProperty && value instanceof OWLDataValue )
            addProperty( ind, (OWLDataProperty) prop, (OWLDataValue) value );
        else
            throw new IllegalArgumentException();
    }

    public void addProperty(OWLIndividual ind, OWLDataProperty prop, OWLDataValue value) {
         ontModel.add(
            (Resource) ind.getImplementation(),
            (Property) prop.getImplementation(),
            (Literal)  value.getImplementation());
         kb.refresh();
    }


    public void addProperty(OWLIndividual ind, OWLDataProperty prop, String value) {
        addProperty(ind, prop, createDataValue(value));
    }

    public void addProperty(OWLIndividual ind, OWLDataProperty prop, Object value) {
        addProperty(ind, prop, createDataValue(value));
    }

    public void removeProperties(OWLIndividual ind, OWLProperty prop) {
        Resource subject = (Resource) ind.getImplementation();
        Property predicate = (Property) prop.getImplementation();

        ontModel.remove(ontModel.listStatements(subject, predicate, (Resource) null));
        kb.refresh();
    }

    public void removeProperty(OWLIndividual ind, OWLProperty prop, OWLValue value)
    {
        Resource subject = (Resource)ind.getImplementation();
        Property predicate = (Property)prop.getImplementation();
        RDFNode object = (RDFNode)value.getImplementation();

        Statement stmt = ontModel.createStatement(subject,predicate,object);

        ontModel.remove(stmt);

        refresh();
    }

    public void addProperty(OWLIndividual ind, OWLObjectProperty prop, OWLIndividual value) {
         ontModel.add(
            (Resource) ind.getImplementation(),
            (Property) prop.getImplementation(),
            (Resource) value.getImplementation());
         kb.refresh();
    }

    public void setProperty(OWLIndividual ind, OWLObjectProperty prop, OWLIndividual value) {
        Resource subject = (Resource) ind.getImplementation();
        Property predicate = (Property) prop.getImplementation();
        Resource object = (Resource) value.getImplementation();

        ontModel.remove(ontModel.listStatements(subject, predicate, (Resource) null));
        ontModel.add(subject, predicate, object);
        kb.refresh();
    }


    public void addType(OWLIndividual ind, OWLClass c) {
         ontModel.add(
            (Resource) ind.getImplementation(),
            RDF.type,
            (Resource) c.getImplementation());
         kb.refresh();
    }

    public void removeTypes(OWLIndividual ind) {
        Resource subject = (Resource) ind.getImplementation();

        ontModel.remove(ontModel.listStatements(subject, RDF.type, (Resource) null));
        kb.refresh();
    }

    public OWLOntology getTranslationSource() {
        return sourceOntology;
    }

    public void setTranslationSource(OWLOntology ontology) {
        sourceOntology = ontology;
    }

    public Service getService() {
        List list = getServices();

        return list.isEmpty() ? null : (Service) list.get(0);
    }

    public OWLIndividual parseLiteral(String literal) {
        Reader in = new StringReader( literal );
        OWLOntologyImpl ont = (OWLOntologyImpl) kb.read( in, uri );
        OWLIndividual res = null;
        int outCount = 0;

        OWLIndividualList instances = ont.getIndividuals();
        for(int i = 0; i < instances.size(); i++) {
            OWLIndividual ind = (OWLIndividual) instances.get( i );
            if(ont.getIncomingProperties(ind).isEmpty())
                return ind;
            if (res == null)
            {
                res = ind;
                outCount = ont.getProperties(ind).size();
            }
            else
            {
                int newCount = ont.getProperties(ind).size();
                if (newCount > outCount)
                {
                    res = ind;
                    outCount = newCount;
                }
            }
        }

        return res;
    }

    public OWLOntology union( OWLOntology  ont ) {
        // create empty ont
        OWLOntology newOnt = OWLFactory.createOntology();

        // add the contents of both ontologies
        newOnt.add( this );
        newOnt.add( ont );

        return newOnt;
    }

    public void add( OWLOntology  ont ) {
        Model m1 = ((OntModel) this.getImplementation()).getBaseModel();
        Model m2 = ((OntModel) ont.getImplementation()).getBaseModel();
        m1.add( m2 );

        kb.refresh();
    }

	public List getServices() {
		if (kb != null)
			return kb.getServices();
		
		return super.getServices();
	}

	private void removeResourcesForOneLevel(Resource resource, boolean recursive) {		
		if (recursive) {
			StmtIterator propIter = resource.listProperties();
			System.out.println("Processing resource " + resource);
		
			while (propIter.hasNext()) {
				Statement stmt = propIter.nextStatement();
				System.out.println("Processing statement " + stmt);
				if (stmt.getObject().isResource()) 
					removeResourcesForOneLevel((Resource) stmt.getObject(), recursive);			
			}
		}
		
		// removes all statements with the given individual in the subject
		StmtIterator stmtiter = ontModel.listStatements(resource, (Property) null, (RDFNode) null);
		ontModel.remove(stmtiter);
	
		// removes all statements with the given individual in the object		
		stmtiter = ontModel.listStatements((Resource) null, (Property) null, (RDFNode) resource);
		ontModel.remove(stmtiter);
		if (recursive)
			resource.removeProperties();
	}
	
	public void removeIndividuals(OWLIndividual ind) {
		if (!ind.getIncomingProperties().isEmpty())
			return;
		Resource resource = (Resource) ind.getImplementation();
		removeResourcesForOneLevel(resource, false);
	}
	
	public void removeIndividuals(OWLIndividual ind, boolean recursive) {
		if (ind.getIncomingProperties().isEmpty())
			return;
		Resource resource = (Resource) ind.getImplementation();
		removeResourcesForOneLevel(resource, true);
	}
	
	public List getNonLanguageIndividuals() {
		List classes = getNonLanguageClasses();
		List inds = new ArrayList();
		for (int i = 0; i < classes.size(); i++)
			inds.addAll(getInstances((OWLClass) classes.get(i)));
		return inds;
	}
	
	public List getNonLanguageClasses() {		    	
		return getNonLanguageItems(getOntModel().listClasses());
	}

	public List getNonLanguageDataProperties() {
		return getNonLanguageItems(getOntModel().listDatatypeProperties());
	}

	public List getNonLanguageObjectProperties() {
		return getNonLanguageItems(getOntModel().listObjectProperties());
	}
		
	private List getNonLanguageItems(ExtendedIterator iter) {
		List list = new ArrayList();
		while (iter.hasNext()) {
			Resource resource = (Resource) iter.next();
			if (!isInLanguageNamespace(resource))
				list.add(wrapClass(resource, getBaseOntology()));
		}
		
		return list;
	}
		
	private boolean isInLanguageNamespace(Resource resource) {
		String namespace = resource.getNameSpace();
		
		if ((owlsNamespaces == null) || (owlsNamespaces.isEmpty())) {
			owlsNamespaces = new Vector();
			// OWL-S namespaces
			owlsNamespaces.add(OWLS.Service.Service.getNamespace());
			owlsNamespaces.add(OWLS.Process.Process.getNamespace());
			owlsNamespaces.add(OWLS.Profile.Profile.getNamespace());
			owlsNamespaces.add(OWLS.Grounding.WsdlGrounding.getNamespace());
			owlsNamespaces.add(OWLS.Expression.Expression.getNamespace());
			owlsNamespaces.add(OWLS.Actor.Actor.getNamespace());
			// expression language namespaces
			owlsNamespaces.add(org.mindswap.owl.vocabulary.KIF.ns);
			owlsNamespaces.add(org.mindswap.owl.vocabulary.SWRL.AtomList.getNamespace());
			owlsNamespaces.add(org.mindswap.owl.vocabulary.SWRLB.equal.getNamespace());
			owlsNamespaces.add(org.mindswap.owl.vocabulary.DRS.ns);			
			// OWL+RDF namespaces
			owlsNamespaces.add(org.mindswap.owl.vocabulary.XSD.ns);
			owlsNamespaces.add(org.mindswap.owl.vocabulary.RDF.ns);
			owlsNamespaces.add(org.mindswap.owl.vocabulary.RDFS.ns);
			owlsNamespaces.add(org.mindswap.owl.vocabulary.OWL.ns);
			// Extensions
			owlsNamespaces.add(MoreGroundings.JavaGrounding.getNamespace());
			owlsNamespaces.add(OWLS_Extensions.Process.hasPerform.getNamespace());
			// helpers
			owlsNamespaces.add(org.mindswap.owl.vocabulary.DC.ns);	
			owlsNamespaces.add("http://www.isi.edu/~pan/damltime/time-entry.owl#");
			owlsNamespaces.add("http://www.daml.org/services/owl-s/1.1/generic/swrlx.owl#");
			owlsNamespaces.add("http://www.daml.org/services/owl-s/1.1/generic/ObjectList.owl#");
		}
			
		for (int i = 0; i < owlsNamespaces.size(); i++) {
			if (owlsNamespaces.get(i).equals(namespace))
				return true;
		}

		return false;
	}

	public Set getClasses() {
		return getAllClasses(ontModel.listClasses(), this);
	}

	public BindingList getBindingsFor(Parameter parameter) {
		Property prop = (Property) OWLS.Process.toParam.getImplementation();
		ResIterator iter = getOntModel().listSubjectsWithProperty(prop, parameter.getImplementation());
		BindingList bindings = OWLSFactory.createBindingList();
		while (iter.hasNext()) {
			Resource resource = (Resource) iter.next();
			OWLIndividual ind = new OWLIndividualImpl(this, resource);
			if (ind.isType(OWLS.Process.Binding))
				bindings.add((Binding) ind.castTo(Binding.class));
		}
						
		return bindings;
	}
}
