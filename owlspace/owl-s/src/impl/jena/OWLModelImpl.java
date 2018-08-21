/*
 * Created on Dec 16, 2004
 */
package impl.jena;

import impl.owl.XSDDataTypes;
import impl.owl.list.RDFListImpl;
import impl.owls.generic.expression.SWRLExpressionImpl;
import impl.owls.generic.list.OWLSObjListImpl;
import impl.owls.grounding.GroundingImpl;
import impl.owls.grounding.JavaAtomicGroundingImpl;
import impl.owls.grounding.UPnPAtomicGroundingImpl;
import impl.owls.grounding.UPnPMessageMapImpl;
import impl.owls.grounding.WSDLAtomicGroundingImpl;
import impl.owls.grounding.WSDLMessageMapImpl;
import impl.owls.grounding.WSDLOperationRefImpl;
import impl.owls.process.AtomicProcessImpl;
import impl.owls.process.CompositeProcessImpl;
import impl.owls.process.ResultImpl;
import impl.owls.process.ValueDataImpl;
import impl.owls.process.ValueOfImpl;
import impl.owls.process.binding.InputBindingImpl;
import impl.owls.process.binding.OutputBindingImpl;
import impl.owls.process.constructs.AnyOrderImpl;
import impl.owls.process.constructs.ChoiceImpl;
import impl.owls.process.constructs.ForEachImpl;
import impl.owls.process.constructs.IfThenElseImpl;
import impl.owls.process.constructs.PerformImpl;
import impl.owls.process.constructs.ProduceImpl;
import impl.owls.process.constructs.RepeatUntilImpl;
import impl.owls.process.constructs.RepeatWhileImpl;
import impl.owls.process.constructs.SequenceImpl;
import impl.owls.process.constructs.SplitImpl;
import impl.owls.process.constructs.SplitJoinImpl;
import impl.owls.process.parameter.InputImpl;
import impl.owls.process.parameter.LocalImpl;
import impl.owls.process.parameter.OutputImpl;
import impl.owls.profile.ProfileImpl;
import impl.owls.profile.ServiceParameterImpl;
import impl.owls.service.ServiceImpl;

import java.io.OutputStream;
import java.io.Writer;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.mindswap.exceptions.LockNotSupportedException;
import org.mindswap.exceptions.NotImplementedException;
import org.mindswap.exceptions.UnboundVariableException;
import org.mindswap.owl.OWLClass;
import org.mindswap.owl.OWLConfig;
import org.mindswap.owl.OWLDataProperty;
import org.mindswap.owl.OWLDataType;
import org.mindswap.owl.OWLDataValue;
import org.mindswap.owl.OWLDataValueList;
import org.mindswap.owl.OWLEntity;
import org.mindswap.owl.OWLFactory;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLIndividualList;
import org.mindswap.owl.OWLModel;
import org.mindswap.owl.OWLObject;
import org.mindswap.owl.OWLObjectProperty;
import org.mindswap.owl.OWLOntology;
import org.mindswap.owl.OWLProperty;
import org.mindswap.owl.OWLType;
import org.mindswap.owl.OWLValue;
import org.mindswap.owl.OWLWriter;
import org.mindswap.owl.list.ListVocabulary;
import org.mindswap.owl.list.RDFList;
import org.mindswap.owl.vocabulary.SWRL;
import org.mindswap.owl.vocabulary.SWRLB;
import org.mindswap.owls.OWLSFactory;
import org.mindswap.owls.generic.expression.Expression;
import org.mindswap.owls.generic.list.OWLSObjList;
import org.mindswap.owls.grounding.Grounding;
import org.mindswap.owls.grounding.JavaAtomicGrounding;
import org.mindswap.owls.grounding.MessageMap;
import org.mindswap.owls.grounding.UPnPAtomicGrounding;
import org.mindswap.owls.grounding.WSDLAtomicGrounding;
import org.mindswap.owls.grounding.WSDLOperationRef;
import org.mindswap.owls.process.AnyOrder;
import org.mindswap.owls.process.AtomicProcess;
import org.mindswap.owls.process.Choice;
import org.mindswap.owls.process.CompositeProcess;
import org.mindswap.owls.process.Condition;
import org.mindswap.owls.process.ControlConstruct;
import org.mindswap.owls.process.ControlConstructBag;
import org.mindswap.owls.process.ControlConstructList;
import org.mindswap.owls.process.ForEach;
import org.mindswap.owls.process.IfThenElse;
import org.mindswap.owls.process.Input;
import org.mindswap.owls.process.InputBinding;
import org.mindswap.owls.process.Local;
import org.mindswap.owls.process.Output;
import org.mindswap.owls.process.OutputBinding;
import org.mindswap.owls.process.Perform;
import org.mindswap.owls.process.Process;
import org.mindswap.owls.process.Produce;
import org.mindswap.owls.process.RepeatUntil;
import org.mindswap.owls.process.RepeatWhile;
import org.mindswap.owls.process.Result;
import org.mindswap.owls.process.Sequence;
import org.mindswap.owls.process.SimpleProcess;
import org.mindswap.owls.process.Split;
import org.mindswap.owls.process.SplitJoin;
import org.mindswap.owls.process.ValueData;
import org.mindswap.owls.process.ValueOf;
import org.mindswap.owls.profile.Profile;
import org.mindswap.owls.profile.ServiceParameter;
import org.mindswap.owls.service.Service;
import org.mindswap.owls.vocabulary.FLAServiceOnt;
import org.mindswap.owls.vocabulary.MoreGroundings;
import org.mindswap.owls.vocabulary.OWLS;
import org.mindswap.pellet.jena.PelletInfGraph;
import org.mindswap.pellet.jena.PelletReasoner;
import org.mindswap.query.ABoxQuery;
import org.mindswap.query.ValueMap;
import org.mindswap.swrl.Atom;
import org.mindswap.swrl.AtomList;
import org.mindswap.swrl.BuiltinAtom;
import org.mindswap.swrl.ClassAtom;
import org.mindswap.swrl.DataPropertyAtom;
import org.mindswap.swrl.DifferentIndividualsAtom;
import org.mindswap.swrl.IndividualPropertyAtom;
import org.mindswap.swrl.SWRLObject;
import org.mindswap.swrl.SameIndividualAtom;
import org.mindswap.swrl.Variable;
import org.mindswap.utils.URIUtils;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.graph.Graph;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntDocumentManager;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.core.Binding0;
import com.hp.hpl.jena.query.core.Constraint;
import com.hp.hpl.jena.query.core.ElementFilter;
import com.hp.hpl.jena.query.core.ElementGroup;
import com.hp.hpl.jena.query.core.ResultBinding;
import com.hp.hpl.jena.query.engine1.ExecutionContext;
import com.hp.hpl.jena.query.expr.E_Add;
import com.hp.hpl.jena.query.expr.E_Equal;
import com.hp.hpl.jena.query.expr.E_GreaterThan;
import com.hp.hpl.jena.query.expr.E_GreaterThanOrEqual;
import com.hp.hpl.jena.query.expr.E_LessThan;
import com.hp.hpl.jena.query.expr.E_NotEqual;
import com.hp.hpl.jena.query.expr.E_Subtract;
import com.hp.hpl.jena.query.expr.Expr;
import com.hp.hpl.jena.query.expr.NodeValue;
import com.hp.hpl.jena.query.expr.NodeVar;
import com.hp.hpl.jena.query.util.Utils;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.shared.Lock;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.util.iterator.Map1;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;
import com.hp.hpl.jena.vocabulary.ReasonerVocabulary;

/**
 * @author Evren Sirin
 */
public abstract class OWLModelImpl implements OWLModel, org.mindswap.owls.OWLSModel {
    protected OntModel ontModel;

    private   OntModelSpec spec;
    protected Reasoner reasoner;
    protected OWLOntology baseOntology;
    protected OWLWriter writer;

    public OWLModelImpl(Model model) {
        spec = new OntModelSpec( OntModelSpec.OWL_MEM );
        
        OntDocumentManager docManager = new OntDocumentManager();
        docManager.setProcessImports( false );

        spec.setDocumentManager( docManager );
        
        ontModel = ModelFactory.createOntologyModel( spec, model );
    }

    public Object getImplementation() {
        return ontModel;
    }

    public OntModel getOntModel() {
        return ontModel;
    }

    public void prepare() {
        ontModel.prepare();
    }

    public void classify() {
        if(reasoner instanceof PelletReasoner) {
            ((PelletInfGraph)ontModel.getGraph()).classify();
        }
    }

    public boolean isClassified() {
        if(reasoner instanceof PelletReasoner) {
            return ((PelletInfGraph)ontModel.getGraph()).isClassified();
        }

        return true;
    }

    public boolean isConsistent() {
        if(reasoner instanceof PelletReasoner) {
            return ((PelletInfGraph)ontModel.getGraph()).isConsistent();
        }

        return true;

//        ValidityReport validation = ontModel.validate();
//
//        // if there is no validation report assume everything is fine
//        return ( validation == null ) || validation.isValid();
    }

    public void setBaseOntology(OWLOntology ontology) {
        this.baseOntology = ontology;
    }

    public OWLOntology getBaseOntology() {
        return baseOntology;
    }

//    public void enableReasoner(boolean enabled) {
//	    if(reasoner == null)
//	        throw new IllegalArgumentException(
//	            "There is no reasoner attached to enable/disable. First attach a reasoner using setReasoner function!");
//    }

    public void setReasoner(Reasoner reasoner) {
        this.reasoner = reasoner;

        spec.setReasoner(reasoner);

        List subGraphs = ontModel.getSubGraphs();
        Model baseModel = ontModel.getBaseModel();

        ontModel = ModelFactory.createOntologyModel( spec, baseModel );
        for(Iterator i = subGraphs.iterator(); i.hasNext();) {
            Graph graph = (Graph) i.next();
            Model wrap = ModelFactory.createModelForGraph( graph );
            ontModel.addSubModel( wrap, false );
        }

        ontModel.rebind();
    }

    public void setReasoner(String reasonerName) {
        Reasoner reasoner = (Reasoner) OWLFactory.getReasoner(reasonerName);
        if(reasonerName != null && reasoner == null)
            throw new IllegalArgumentException(
                "Reasoner named " + reasonerName + " is not found!");

        setReasoner(reasoner);
    }

    public void setReasoner(Object reasoner) {
        if(reasoner instanceof Reasoner || reasoner == null)
            setReasoner((Reasoner) reasoner);
        else
            throw new IllegalArgumentException(
                "Jena implementation only supports reasoners that implements the " + Reasoner.class);
    }

    public Object getReasoner() {
        return reasoner;
    }

    public OWLSObjList createList() {
        ListVocabulary vocabulary = OWLS.ObjList;
        // TODO cache nil list
        OWLIndividual nil = createIndividual( vocabulary.nil().getURI() );
        OWLSObjListImpl list = new OWLSObjListImpl( nil );
        list.setVocabulary( vocabulary );

        return list;
    }

    public OWLSObjList createList(OWLIndividual item) {
        OWLSObjList list = createList();
        list = (OWLSObjList) list.insert( item );

        return list;
    }

    public OWLSObjList createList(OWLIndividualList items) {
        OWLSObjList list = createList();
        for( int i = items.size(); i >= 0; i++ )
            list = (OWLSObjList) list.insert( items.individualAt( i ) );

        return list;
    }

    public RDFList createList(ListVocabulary vocabulary) {
        // TODO cache nil list
        OWLIndividual nil = createIndividual( vocabulary.nil().getURI() );
        RDFListImpl list = new RDFListImpl( nil );
        list.setVocabulary( vocabulary );

        return list;
    }

    public RDFList createList(ListVocabulary vocabulary, OWLIndividual item) {
        RDFList list = createList( vocabulary );
        list = list.insert( item );

        return list;
    }

    public RDFList createList(ListVocabulary vocabulary, OWLIndividualList items) {
        RDFList list = createList( vocabulary );
        for( int i = items.size(); i >= 0; i++ )
            list = list.insert( items.individualAt( i ) );

        return list;
    }

    protected Resource asResource(URI uri) {
        return ontModel.getResource(uri.toString());
    }

    protected boolean containsResource(Resource r) {
        return ontModel.containsResource(r);
    }

    protected Property asProperty(URI uri) {
        return ontModel.getProperty(uri.toString());
    }

    public OWLType getType(URI uri) {
        OWLType type = getDataType(uri);

        if(type == null)
            type = getClass(uri);

        return type;
    }

    public OWLDataType getDataType(URI uri) {
        return XSDDataTypes.getDataType(uri);
    }

    public OWLObject getObject(URI uri) {
        OWLObject entity = null;

        if(entity == null) entity = getClass(uri);
        if(entity == null) entity = getObjectProperty(uri);
        if(entity == null) entity = getDataProperty(uri);
        if(entity == null) entity = getDataType(uri);
        if(entity == null) entity = getIndividual(uri);

        return entity;
    }

    public OWLEntity getEntity(URI uri) {
        OWLEntity entity = null;

        if(entity == null) entity = getClass(uri);
        if(entity == null) entity = getObjectProperty(uri);
        if(entity == null) entity = getDataProperty(uri);
        if(entity == null) entity = getIndividual(uri);

        return entity;
    }

    public OWLClass getClass(URI uri) {
        return getClass(asResource(uri));
    }

    public OWLClass getClass(Resource res) {
        OWLClass c = null;

        if(res.canAs(OntClass.class)) {
            c = wrapClass(res, null);
        }

        return c;
    }

    public OWLIndividual getIndividual(URI uri) {
        Resource res = ontModel.getIndividual(uri.toString());

        return (res == null) ? null : wrapIndividual(res, null);
    }

    public OWLIndividualList getIndividuals() {
        return getAllIndividuals( ontModel.listSubjects(), null );
    }

    public OWLProperty getProperty(URI uri) {
        OWLProperty prop = getObjectProperty(uri);

        if(prop == null)
            prop = getDataProperty(uri);

        return prop;
    }

    public OWLObjectProperty getObjectProperty(URI uri) {
        OWLObjectProperty p = null;

        Property prop = asProperty(uri);
        if(ontModel.contains(prop, RDF.type, OWL.ObjectProperty)) {
            p = wrapObjectProperty(prop, null);
        }

        return p;
    }

    public OWLDataProperty getDataProperty(URI uri) {
        OWLDataProperty p = null;

        Property prop = asProperty(uri);
        if(ontModel.contains(prop, RDF.type, OWL.DatatypeProperty)) {
            p = wrapDataProperty(prop, null);
        }

        return p;
    }

    public boolean isSameAs(OWLIndividual ind1, OWLIndividual ind2) {
        return ontModel.contains(
            (Resource) ind1.getImplementation(),
            OWL.sameAs,
            (Resource) ind2.getImplementation());
    }

    public OWLIndividualList getSameIndividuals(OWLIndividual ind) {
        return getAllIndividuals(
            ontModel.listSubjectsWithProperty(
                OWL.sameAs,
                (Resource) ind.getImplementation()),
            ind.getOntology());
    }

    public boolean isDifferentFrom(OWLIndividual ind1, OWLIndividual ind2) {
        return ontModel.contains(
            (Resource) ind1.getImplementation(),
            OWL.differentFrom,
            (Resource) ind2.getImplementation());
    }

    public OWLIndividualList getDifferentIndividuals(OWLIndividual ind) {
        return getAllIndividuals(
            ontModel.listSubjectsWithProperty(
                OWL.differentFrom,
                (Resource) ind.getImplementation()),
            ind.getOntology());
    }

    /* (non-Javadoc)
     * @see org.mindswap.owl.OWLModel#isSubClassOf(org.mindswap.owl.OWLClass, org.mindswap.owl.OWLClass)
     */
    public boolean isSubClassOf(OWLClass c1, OWLClass c2) {
        return ontModel.contains(
            (Resource) c1.getImplementation(),
            RDFS.subClassOf,
            (Resource) c2.getImplementation());
    }

    /* (non-Javadoc)
     * @see org.mindswap.owl.OWLModel#isDisjoint(org.mindswap.owl.OWLClass, org.mindswap.owl.OWLClass)
     */
    public boolean isDisjoint(OWLClass c1, OWLClass c2) {
        return ontModel.contains(
            (Resource) c1.getImplementation(),
            OWL.disjointWith,
            (Resource) c2.getImplementation());
    }


    /* (non-Javadoc)
     * @see org.mindswap.owl.OWLModel#isEquivalent(org.mindswap.owl.OWLType, org.mindswap.owl.OWLType)
     */
    public boolean isEquivalent(OWLType t1, OWLType t2) {
        if(t1.equals(t2))
            return true;
        if(t1 instanceof OWLClass && t2 instanceof OWLClass) {
            return ontModel.contains(
                (Resource) t1.getImplementation(),
                OWL.equivalentClass,
                (Resource) t2.getImplementation());
        }
        // FIXME datatype equivalance
        if(t1 instanceof OWLDataType && t2 instanceof OWLDataType)
            return t1.equals(t2);

        return false;
    }

    /* (non-Javadoc)
     * @see org.mindswap.owl.OWLModel#isDisjoint(org.mindswap.owl.OWLType, org.mindswap.owl.OWLType)
     */
    public boolean isDisjoint(OWLType t1, OWLType t2) {
        if(t1 instanceof OWLClass && t2 instanceof OWLClass)
            return isDisjoint((OWLClass) t1, (OWLClass) t2);
        // FIXME datatype disjointness
        if(t1 instanceof OWLDataType && t2 instanceof OWLDataType)
            return !isEquivalent(t1, t2);

        return true;
    }

    public Set getSubClasses(OWLClass c) {
        return getSubClasses(c, false);
    }

    public Set getSubClasses(OWLClass c, boolean direct) {
        Property subClassOf = direct
            ? ReasonerVocabulary.directSubClassOf
            : RDFS.subClassOf;

        return getAllClasses(
            ontModel.listSubjectsWithProperty(
                subClassOf,
                (Resource) c.getImplementation()),
            c.getOntology());
    }

    public Set getSubProperties(OWLProperty p) {
        if(p instanceof OWLObjectProperty) {
            return getAllObjectProperties(
                ontModel.listSubjectsWithProperty(
                    RDFS.subPropertyOf,
                    (Resource) p.getImplementation()),
                p.getOntology());
        }
        else {
            return getAllDataProperties(
                ontModel.listSubjectsWithProperty(
                    RDFS.subPropertyOf,
                    (Resource) p.getImplementation()),
                p.getOntology());
        }
    }

    public Set getEquivalentProperties(OWLProperty p) {
        if(p instanceof OWLObjectProperty) {
            return getAllObjectProperties(
                ontModel.listSubjectsWithProperty(
                    OWL.equivalentClass,
                    (Resource) p.getImplementation()),
                p.getOntology());
        }
        else {
            return getAllDataProperties(
                ontModel.listSubjectsWithProperty(
                    OWL.equivalentClass,
                    (Resource) p.getImplementation()),
                p.getOntology());
        }
    }

    public Set getSuperProperties(OWLProperty p) {
        if(p instanceof OWLObjectProperty) {
            return getAllObjectProperties(
                ontModel.listObjectsOfProperty(
                    (Resource) p.getImplementation(),
                    RDFS.subPropertyOf),
                p.getOntology());
        }
        else {
            return getAllDataProperties(
                ontModel.listObjectsOfProperty(
                    (Resource) p.getImplementation(),
                    RDFS.subPropertyOf),
                p.getOntology());
        }
    }

    public Set getSuperClasses(OWLClass c) {
        return getSuperClasses(c, false);
    }

    public Set getSuperClasses(OWLClass c, boolean direct) {
        Property subClassOf = direct
            ? ReasonerVocabulary.directSubClassOf
            : RDFS.subClassOf;

        return getAllClasses(
            ontModel.listObjectsOfProperty(
                (Resource) c.getImplementation(),
                subClassOf),
            c.getOntology());
    }

    public Set getEquivalentClasses(OWLClass c) {
        return getAllClasses(
            ontModel.listSubjectsWithProperty(
                OWL.equivalentClass,
                (Resource) c.getImplementation()),
            c.getOntology());
    }

    public OWLIndividualList getInstances(OWLClass c) {
        return getAllIndividuals(
            ontModel.listSubjectsWithProperty(
                RDF.type,
                (Resource) c.getImplementation()),
            c.getOntology());
    }

    /* (non-Javadoc)
     * @see org.mindswap.owl.OWLModel#getTypes(org.mindswap.owl.OWLIndividual)
     */
    public Set getTypes(OWLIndividual ind) {
        return getAllClasses(
            ontModel.listObjectsOfProperty(
                (Resource) ind.getImplementation(),
                RDF.type),
            ind.getOntology());
    }

    /* (non-Javadoc)
     * @see org.mindswap.owl.OWLModel#getProperty(org.mindswap.owl.OWLIndividual, org.mindswap.owl.OWLObjectProperty)
     */
    public OWLIndividual getProperty(OWLIndividual ind, OWLObjectProperty prop) {
        return getFirstIndividual(
            ontModel.listObjectsOfProperty(
                (Resource) ind.getImplementation(),
                (Property) prop.getImplementation()),
            ind.getOntology());
    }

    /* (non-Javadoc)
     * @see org.mindswap.owl.OWLModel#getProperties(org.mindswap.owl.OWLIndividual, org.mindswap.owl.OWLObjectProperty)
     */
    public OWLIndividualList getProperties(OWLIndividual ind, OWLObjectProperty prop) {
        return getAllIndividuals(
            ontModel.listObjectsOfProperty(
                (Resource) ind.getImplementation(),
                (Property) prop.getImplementation()),
            ind.getOntology());
    }

    final public static List SKIP_PROPS = Arrays.asList(new Property[] {
        RDF.type, RDFS.subClassOf,
        ReasonerVocabulary.directSubClassOf, 
        ReasonerVocabulary.directSubPropertyOf,
        ReasonerVocabulary.directRDFType,
        OWL.hasValue, OWL.allValuesFrom, OWL.someValuesFrom,
        OWL.minCardinality, OWL.maxCardinality, OWL.cardinality });
    
    private Map getProperties(Resource resource, OWLOntology ontology) {
    	Map result = new HashMap();
    	
        Iterator i = ontModel.listStatements(
            resource,
            null,
            (RDFNode) null);

        while(i.hasNext()) {
            Statement stmt = (Statement) i.next();
            Property pred = stmt.getPredicate();

            if(SKIP_PROPS.contains(pred))
                continue;


//            OWLProperty prop = getProperty(URI.create(pred.getURI()));
//            if(prop != null) {
//                if(prop instanceof OWLObjectProperty) {
//                    OWLIndividualList list = (OWLIndividualList) result.get(prop);
//                    if(list == null) {
//                        list = OWLFactory.createIndividualList();
//                        result.put(prop, list);
//                    }
//                    list.add(OWLModelImpl.this.wrapIndividual(stmt.getResource()));
//                }
//                else {
//                    OWLIndividualList list = (OWLIndividualList) result.get(prop);
//                    if(list == null) {
//                        list = OWLFactory.createIndividualList();
//                        result.put(prop, list);
//                    }
//                    list.add(OWLModelImpl.this.wrapDataValue(stmt.getLiteral()));
//                }
//            }

            RDFNode value = stmt.getObject();

            if(value instanceof Resource) {
                OWLObjectProperty prop = baseOntology.createObjectProperty(URI.create(pred.getURI()));
                OWLIndividualList list = (OWLIndividualList) result.get(prop);
                if(list == null) {
                    list = OWLFactory.createIndividualList();
                    result.put(prop, list);
                }
                list.add(OWLModelImpl.this.wrapIndividual(stmt.getResource(), ontology));
            }
            else {
                OWLDataProperty prop = baseOntology.createDataProperty(URI.create(pred.getURI()));
                OWLDataValueList list = (OWLDataValueList) result.get(prop);
                if(list == null) {
                    list = OWLFactory.createDataValueList();
                    result.put(prop, list);
                }
                list.add(OWLModelImpl.this.wrapDataValue(stmt.getLiteral(), ontology));
            }
        }

        return result;
    }
    
    public List getDeclaredProperties(OWLClass claz) {
    	return getDeclaredProperties(claz, true);
    }
    
    public List getDeclaredProperties(OWLClass claz, boolean direct) {
    	List result = OWLFactory.createIndividualList();
    	
    	OntResource ontRes = ontModel.getOntResource((Resource) claz.getImplementation());
    	OntClass ontClaz = ontRes.asClass(); 
        ExtendedIterator i = ontClaz.listDeclaredProperties(direct);

        while(i.hasNext()) {        	
            OntProperty ontProp = (OntProperty) i.next();            

            if(SKIP_PROPS.contains(ontProp))
                continue;
            
            if (ontProp.isDatatypeProperty()) {
            	OWLDataProperty prop = baseOntology.createDataProperty(URI.create(ontProp.getURI()));
            	result.add(prop);
            } else {
                OWLObjectProperty prop = baseOntology.createObjectProperty(URI.create(ontProp.getURI()));                
                result.add(prop);
            }
        }

        return result;
    }
    
    public Map getProperties(OWLClass claz) {
    	return getProperties((Resource) claz.getImplementation(), claz.getOntology());
    }

    public Map getProperties(OWLIndividual ind) {
    	return getProperties((Resource) ind.getImplementation(), ind.getOntology());
    }

    /* (non-Javadoc)
     * @see org.mindswap.owl.OWLModel#getProperty(org.mindswap.owl.OWLIndividual, org.mindswap.owl.OWLDataProperty)
     */
    public OWLDataValue getProperty(OWLIndividual ind, OWLDataProperty prop) {
        OWLDataValue result = null;
        int maxPriority = -1;
        OWLDataValueList list = getAllDataValues(
            ontModel.listObjectsOfProperty(
                (Resource) ind.getImplementation(),
                (Property) prop.getImplementation()),
            ind.getOntology());

        for(int i = 0; i < list.size(); i++) {
            OWLDataValue value = list.valueAt(i);

            int priority = OWLConfig.DEFAULT_LANG_LIST.indexOf(value.getLanguage());

            if(priority == 0)
                return value;
            else if(priority > maxPriority) {
                result = value;
                maxPriority = priority;
            }
        }

        return result;
    }

    /* (non-Javadoc)
     * @see org.mindswap.owl.OWLModel#getProperty(org.mindswap.owl.OWLIndividual, org.mindswap.owl.OWLDataProperty, java.lang.String)
     */
    public OWLDataValue getProperty(OWLIndividual ind, OWLDataProperty prop, String lang) {
        OWLDataValueList list = getAllDataValues(
            ontModel.listObjectsOfProperty(
                (Resource) ind.getImplementation(),
                (Property) prop.getImplementation()),
            ind.getOntology());

        for(int i = 0; i < list.size(); i++) {
            OWLDataValue value = list.valueAt(i);
            if(lang == null || value.getLanguage().equals(lang))
                return value;
        }

        return null;
    }

    /* (non-Javadoc)
     * @see org.mindswap.owl.OWLModel#getProperties(org.mindswap.owl.OWLIndividual, org.mindswap.owl.OWLDataProperty)
     */
    public OWLDataValueList getProperties(OWLIndividual ind, OWLDataProperty prop) {
        return getAllDataValues(
            ontModel.listObjectsOfProperty(
                (Resource) ind.getImplementation(),
                (Property) prop.getImplementation()),
            ind.getOntology());
    }



    /* (non-Javadoc)
     * @see org.mindswap.owl.OWLModel#getIncomingProperty(org.mindswap.owl.OWLObjectProperty, org.mindswap.owl.OWLIndividual)
     */
    public OWLIndividual getIncomingProperty(OWLObjectProperty prop, OWLIndividual ind) {
        return getFirstIndividual(
            ontModel.listSubjectsWithProperty(
                (Property) prop.getImplementation(),
                (Resource) ind.getImplementation()),
            ind.getOntology());
    }

    public OWLIndividualList getIncomingProperties(OWLObjectProperty prop, OWLIndividual ind) {
        return getAllIndividuals(
            ontModel.listSubjectsWithProperty(
                (Property) prop.getImplementation(),
                (Resource) ind.getImplementation()),
            ind.getOntology());
    }

    public OWLIndividualList getIncomingProperties(OWLIndividual ind) {
        return getAllIndividuals(
            ontModel.listStatements(
                null,
                null,
                (Resource) ind.getImplementation()).mapWith(
                    new Map1(){
                        public Object map1(Object o) { return ((Statement) o).getSubject(); }
                    }),
            ind.getOntology());
    }

    public OWLIndividual getIncomingProperty(OWLDataProperty prop, OWLDataValue value) {
        return getFirstIndividual(
            ontModel.listSubjectsWithProperty(
                (Property) prop.getImplementation(),
                (Literal) value.getImplementation()), prop.getOntology());
    }

    /* (non-Javadoc)
     * @see org.mindswap.owl.OWLModel#getIncomingProperties(org.mindswap.owl.OWLDataProperty, org.mindswap.owl.OWLIndividual)
     */
    public OWLIndividualList getIncomingProperties(OWLDataProperty prop, OWLDataValue value) {
        return getAllIndividuals(
            ontModel.listSubjectsWithProperty(
                (Property) prop.getImplementation(),
                (Literal) value.getImplementation()), prop.getOntology());
    }

    /* (non-Javadoc)
     * @see org.mindswap.owl.OWLModel#hasProperty(org.mindswap.owl.OWLIndividual, org.mindswap.owl.OWLProperty)
     */
    public boolean hasProperty(OWLIndividual ind, OWLProperty prop) {
        return ontModel.contains(
            (Resource) ind.getImplementation(),
            (Property) prop.getImplementation(),
            (RDFNode) null);
    }

    public boolean hasProperty(OWLIndividual ind, OWLProperty prop, OWLValue value) {
        return ontModel.contains(
            (Resource) ind.getImplementation(),
            (Property) prop.getImplementation(),
            (RDFNode)  value.getImplementation());
    }

    /* (non-Javadoc)
     * @see org.mindswap.owl.OWLModel#getType(org.mindswap.owl.OWLIndividual)
     */
    public OWLClass getType(OWLIndividual ind) {
        Resource res = (Resource) ind.getImplementation();
        res = (Resource) res.inModel(ontModel);
        OntResource ontR = (OntResource) res.as(OntResource.class);
        Resource type = ontR.getRDFType(true);

        return (type == null) ? null : OWLModelImpl.this.wrapClass(type, ind.getOntology());
    }

    public boolean isType(OWLIndividual ind, OWLClass c) {
        return ontModel.contains(
            (Resource) ind.getImplementation(),
            RDF.type,
            (Resource)  c.getImplementation());
    }

    public boolean isEnumerated(OWLClass c) {
        return ontModel.contains(
            (Resource) c.getImplementation(),
            OWL.oneOf,
            (Resource)  null);
    }

    public OWLIndividualList getEnumerations(OWLClass c) {
        Statement stmt =
            ontModel.getProperty(
                (Resource) c.getImplementation(),
                OWL.oneOf);
        if(stmt == null)
            return null;

        com.hp.hpl.jena.rdf.model.RDFList oneOf =
            (com.hp.hpl.jena.rdf.model.RDFList)
                stmt.getResource().as(com.hp.hpl.jena.rdf.model.RDFList.class);
        return getAllIndividuals(oneOf.iterator(), c.getOntology());
    }

    private Set getAllObjectProperties(Iterator i, OWLOntology ont) {
        Set set = new HashSet();
        while(i.hasNext()) {
            Resource r = (Resource) i.next();
            set.add(OWLModelImpl.this.wrapObjectProperty((Property) r.as(Property.class), ont));
        }
        return set;
    }

    private Set getAllDataProperties(Iterator i, OWLOntology ont) {
        Set set = new HashSet();
        while(i.hasNext()) {
            Resource r = (Resource) i.next();
            set.add(OWLModelImpl.this.wrapDataProperty((Property) r.as(Property.class), ont));
        }
        return set;
    }

    protected Set getAllClasses(Iterator i, OWLOntology ont) {
        Set set = new HashSet();
        while(i.hasNext()) {
            set.add(OWLModelImpl.this.wrapClass((Resource) i.next(), ont));
        }
        return set;
    }

    private OWLIndividualList getAllIndividuals(Iterator i, OWLOntology ont) {
        OWLIndividualList list = OWLFactory.createIndividualList();
        while(i.hasNext()) {
            RDFNode node = (RDFNode) i.next();
            if(node instanceof Resource)
                list.add(wrapIndividual((Resource) node, ont));
        }

        return list;
    }

    private OWLIndividual getFirstIndividual(Iterator i, OWLOntology ont) {
        while(i.hasNext()) {
            RDFNode node = (RDFNode) i.next();
            if(node instanceof Resource) {
                return wrapIndividual((Resource) node, ont);
            }
        }

        return null;
    }

    private OWLDataValueList getAllDataValues(Iterator i, OWLOntology ont) {
        OWLDataValueList list = OWLFactory.createDataValueList();
        while(i.hasNext()) {
            RDFNode node = (RDFNode) i.next();
            if(node instanceof Literal)
                list.add(wrapDataValue((Literal) node, ont));
        }
        return list;
    }

    protected abstract OWLDataValue wrapDataValue(Literal l, OWLOntology ont);

    protected abstract OWLIndividual wrapIndividual(Resource r, OWLOntology ont);

    protected abstract OWLClass wrapClass(Resource r, OWLOntology ont);

    protected abstract OWLObjectProperty wrapObjectProperty(Property p, OWLOntology ont);

    protected abstract OWLDataProperty wrapDataProperty(Property p, OWLOntology ont);

    public void applyGround( AtomList atoms ) {
        apply( atoms, true );
    }
    
    public void apply( AtomList atoms ) throws UnboundVariableException {
        apply( atoms, false );
    }
    
    private void apply( AtomList atoms, boolean skipUnground ) {   
        LOOP:
        for( AtomList list = atoms; !list.isEmpty(); list = (AtomList) list.getRest() ) {
            Atom atom = (Atom) list.getFirst();

            for(int i = 0; i < atom.getArgumentCount(); i++) {
                SWRLObject arg = atom.getArgument( i );
                if( arg.isVariable() ) {
                    if( skipUnground )
                        continue LOOP;
                    else    
                        throw new UnboundVariableException( (Variable) arg );
                }
            }

            if(atom instanceof ClassAtom) {
                ClassAtom classAtom = (ClassAtom) atom;
                OWLClass c = classAtom.getClassPredicate();
                OWLIndividual ind = classAtom.getArgument1();

                addType(ind, c);
            }
            else if(atom instanceof IndividualPropertyAtom) {
                IndividualPropertyAtom propAtom = (IndividualPropertyAtom) atom;
                OWLObjectProperty pred = propAtom.getPropertyPredicate();
                OWLIndividual subj = propAtom.getArgument1();
                OWLIndividual obj = propAtom.getArgument2();

                addProperty(subj, pred, obj);
            }
            else if(atom instanceof DataPropertyAtom)  {
                DataPropertyAtom propAtom = (DataPropertyAtom) atom;
                OWLDataProperty pred = propAtom.getPropertyPredicate();
                OWLIndividual subj = propAtom.getArgument1();
                OWLDataValue obj = (OWLDataValue) propAtom.getArgument2();

                addProperty(subj, pred, obj);
            }
            else if(atom instanceof SameIndividualAtom)  {
                throw new NotImplementedException();
            }
            else if(atom instanceof DifferentIndividualsAtom)   {
                throw new NotImplementedException();
            }
            else
                throw new RuntimeException("Unknown atom type: " + atom);
        }
    }

    public boolean isTrue(ABoxQuery query) {
        return !query( query ).isEmpty();
    }

    public boolean isTrue(Condition condition) {
        return isTrue( condition.getBody().toQuery() );
    }

    public boolean isTrue(Condition condition, ValueMap values) {
        AtomList atoms = condition.getBody();

        // assume that no expression body evaluates to true
        if( atoms == null )
            return true;

        atoms = atoms.apply( values );

        ABoxQuery query = atoms.toQuery();

        return !query( query ).isEmpty();
    }

    private Node makeNode(OWLObject term) {
        if(term instanceof Variable) {
            String varName = ((Variable) term).getLocalName();
            if(varName == null || varName.equals(""))
                throw new IllegalArgumentException(
                    "There is no local name for the variable URI " + ((Variable) term).getURI());
            return Node.createVariable( varName );
        }
        else
            return ((RDFNode) term.getImplementation()).asNode();
    }
    
    private Node makeNode( NodeValue nodeVal ) {
        if ( nodeVal.isBooleanRaw() )
            return Node.createLiteral(nodeVal.getBoolean()?"true":"false", null, XSDDatatype.XSDboolean) ;
        
        if ( nodeVal.isDate() )
            return Node.createLiteral(Utils.calendarToXSDDateTimeString(nodeVal.getDate()), null, XSDDatatype.XSDdateTime) ;
            
        if ( nodeVal.isInteger() )
            return Node.createLiteral(Long.toString(nodeVal.getInteger()), null, XSDDatatype.XSDinteger) ;
            
        if ( nodeVal.isDouble() )
            return Node.createLiteral(Double.toString(nodeVal.getDouble()), null, XSDDatatype.XSDdouble) ;
        
        if ( nodeVal.isString() )
            return Node.createLiteral(nodeVal.getString()) ;
        
        throw new RuntimeException( "Unrecognized node type" );
    }

    public QueryExecution makeJenaQuery( AtomList body, Model model ) {
//        AtomList body = q.getBody();
//        List resultVars = q.getResultVars();
        
        ResultBinding initialBinding = new ResultBinding( model );
        initialBinding.add( "test", OWL.Thing );

        ElementGroup group = new ElementGroup();
        List constraints = new ArrayList();
        for( AtomList list = body; !list.isEmpty(); list = (AtomList) list.getRest() ) {
            Atom atom = (Atom) list.getFirst();
            int argCount = atom.getArgumentCount();
            
            Node s = null, p = null, o = null;

            s = makeNode( atom.getArgument( 0 ) );
            if( argCount > 1 )
                o = makeNode( atom.getArgument( 1 ) );

            if(atom instanceof BuiltinAtom) {
                OWLIndividual builtin = ((BuiltinAtom) atom).getBuiltin();

                Expr arg1 = s.isVariable() ? (Expr) new NodeVar( s.getName() ) : NodeValue.makeNode( s );
                Expr arg2 = o.isVariable() ? (Expr) new NodeVar(o.getName() ) : NodeValue.makeNode( o );

                Expr arg3 = null;
                if(argCount > 2) {
                    Node node3 = makeNode( atom.getArgument( 2 ) );
                    arg3 = node3.isVariable() ? (Expr) new NodeVar(node3.getName() ) : NodeValue.makeNode( node3 );
                }
                
                
                Expr c = null;
                if ( builtin.equals( SWRLB.equal ) ) {
                    if( arg1.isConstant() ) {
                        if( arg2.isConstant() )     
                            c = new E_Equal( arg1, arg2 );
                        else
                            initialBinding.add( o.getName(), ontModel.asRDFNode( makeNode( arg1.getConstant() ) ) );
                    }
                    else {
                        if( arg2.isConstant() )     
                            initialBinding.add( s.getName(), ontModel.asRDFNode( arg2.getConstant().asNode() ) );                        
                        else
                            c = new E_Equal( arg1, arg2 );
                    }
                }
                else if ( builtin.equals( SWRLB.notEqual ) )
                    c = new E_NotEqual( arg1, arg2 );
                else if ( builtin.equals( SWRLB.greaterThan ) )
                    c = new E_GreaterThan( arg1, arg2 );
                else if ( builtin.equals( SWRLB.greaterThanOrEqual ) )
                    c = new E_GreaterThanOrEqual( arg1, arg2 );
                else if ( builtin.equals( SWRLB.lessThan ) )
                    c = new E_LessThan( arg1, arg2 );
                else if ( builtin.equals( SWRLB.lessThanOrEqual ) )
                    c = new E_GreaterThanOrEqual( arg1, arg2 );
                else if ( builtin.equals( SWRLB.add ) ) {
                    if( !arg1.isConstant() && arg2.isConstant() && arg3.isConstant() ) {
                        Expr expr = new E_Add( arg2, arg3 );
                        NodeValue result = expr.eval( new Binding0(), new ExecutionContext() );
                        initialBinding.add( arg1.getVarName(), ontModel.asRDFNode( makeNode( result.getConstant() ) ) );
                    }
                    else if( arg1.isConstant() && !arg2.isConstant() && arg3.isConstant() ) {
                        Expr expr = new E_Subtract( arg1, arg3 );
                        NodeValue result = expr.eval( new Binding0(), new ExecutionContext() );
                        initialBinding.add( arg2.getVarName(), ontModel.asRDFNode( result.getConstant().asNode() ) );
                    }
                    else if( arg1.isConstant() && arg2.isConstant() && !arg3.isConstant() ) {
                        Expr expr = new E_Subtract( arg1, arg2 );
                        NodeValue result = expr.eval( new Binding0(), new ExecutionContext() );
                        initialBinding.add( arg3.getVarName(), ontModel.asRDFNode( result.getConstant().asNode() ) );
                    }                    
                    else
                        c = new E_Equal( arg1, new E_Subtract( arg2, arg3 ) );
                }
                else if ( builtin.equals( SWRLB.subtract ) ) {
                    if( !arg1.isConstant() && arg2.isConstant() && arg3.isConstant() ) {
                        Expr expr = new E_Subtract( arg2, arg3 );
                        NodeValue result = expr.eval( new Binding0(), new ExecutionContext() );
                        initialBinding.add( arg1.getVarName(), ontModel.asRDFNode( makeNode( result.getConstant() ) ) );
                    }
                    else if( arg1.isConstant() && !arg2.isConstant() && arg3.isConstant() ) {
                        Expr expr = new E_Add( arg1, arg3 );
                        NodeValue result = expr.eval( new Binding0(), new ExecutionContext() );
                        initialBinding.add( arg2.getVarName(), ontModel.asRDFNode( result.getConstant().asNode() ) );
                    }
                    else if( arg1.isConstant() && arg2.isConstant() && !arg3.isConstant() ) {
                        Expr expr = new E_Subtract( arg2, arg1 );
                        NodeValue result = expr.eval( new Binding0(), new ExecutionContext() );
                        initialBinding.add( arg3.getVarName(), ontModel.asRDFNode( result.getConstant().asNode() ) );
                    }                    
                    else
                        c = new E_Equal( arg1, new E_Subtract( arg2, arg3 ) );
                }
                else
                    throw new NotImplementedException( builtin.toPrettyString() );

                if( c != null )
                    constraints.add(c);
            }
            else {
                if(atom instanceof ClassAtom) {
                    p = RDF.type.asNode();
                    o = makeNode(((ClassAtom) atom).getClassPredicate());
                }
                else if(atom instanceof IndividualPropertyAtom)
                    p = makeNode(((IndividualPropertyAtom) atom).getPropertyPredicate());
                else if(atom instanceof DataPropertyAtom)
                    p = makeNode(((DataPropertyAtom) atom).getPropertyPredicate());
                else if(atom instanceof SameIndividualAtom)
                    p = OWL.sameAs.asNode();
                else if(atom instanceof DifferentIndividualsAtom)
                    p = OWL.differentFrom.asNode();
                else
                    throw new RuntimeException("Unknown atom type " + atom);

                group.addTriple( Triple.create( s, p, o ) );
            }
        }

        if( !constraints.isEmpty() ) {
            for( Iterator i = constraints.iterator(); i.hasNext(); ) {
                Constraint c = (Constraint) i.next();
                group.addElement( new ElementFilter( c ) );    
            }            
        }

        Query sparql = new Query();
        sparql.setQueryType( Query.QueryTypeSelect );
        sparql.setQueryElement( group );
        // FIXME Set the result vars
        sparql.setQueryResultStar( true );

//        return sparql;
        
        return QueryExecutionFactory.create( sparql, ontModel, initialBinding );
    }


    public List query(String rdql) {
        ABoxQuery query = OWLFactory.createRDQLParser( this ).parse( rdql );

        return query( query );
    }

    public List query(ABoxQuery query, ValueMap values) {
        ABoxQuery q = query.apply( values );

        return query( q );
    }

    public List query(ABoxQuery query) {
      List result = new ArrayList();
      List resultVars = query.getResultVars();
      AtomList atoms = query.getBody();
//      Query sparql = makeJenaQuery( atoms );
//      QueryExecution qe = QueryExecutionFactory.create( sparql, ontModel );

      QueryExecution qe = makeJenaQuery( atoms, ontModel );
          
      ResultSet results = qe.execSelect();
      while(results.hasNext()) {
          QuerySolution binding = results.nextSolution();
          ValueMap map = new ValueMap();
          for(int i = 0; i < resultVars.size(); i++) {
              Variable var = (Variable) resultVars.get(i);
              String rdqlVar = var.getLocalName();
              RDFNode value = binding.get(rdqlVar);

              if( value != null ) {
                  OWLValue owlValue = null;
                  if(value instanceof Literal)
                      owlValue =  new OWLDataValueImpl((Literal) value);
                  else {
                      Resource resource = (Resource) value;
                      owlValue = new OWLIndividualImpl(baseOntology, resource);
                  }
                  map.setValue(var, owlValue);
              }
          }
          result.add(map);
      }

      return result;
    }

//    public List query(ABoxQuery query, ValueMap values) {
//        List result = new ArrayList();
//        List resultVars = query.getResultVars();
////        String rdqlStr = query.toRDQL();
//        Query rdql = makeJenaQuery( query.getBody() );
//        QueryEngine qe = new QueryEngine( rdql );
//        qe.setDataSet( new DataSource1( ontModel ) );
//
//        ResultBindingImpl initBinding = null;
//        if( values != null || !values.isEmpty() ) {
//            initBinding = new ResultBindingImpl();
//
//            for(Iterator i = values.getVariables().iterator(); i.hasNext();) {
//              Variable var = (Variable) i.next();
//              OWLValue value = values.getValue( var );
//
//              initBinding.add( var.getLocalName(), (RDFNode) value.getImplementation() );
//          }
//        }
//
//        QueryResults results = qe.exec( initBinding );
//        while(results.hasNext()) {
//            ResultBinding binding = (ResultBinding) results.next();
//            ValueMap map = new ValueMap();
//            for(int i = 0; i < resultVars.size(); i++) {
//                Variable var = (Variable) resultVars.get(i);
//                String rdqlVar = var.getLocalName();
//                RDFNode value = (RDFNode) binding.get(rdqlVar);
//
//                if( value != null ) {
//  	              OWLValue owlValue = null;
//  	              if(value instanceof Literal)
//  	                  owlValue =  new OWLDataValueImpl((Literal) value);
//  	              else {
//  	                  Resource resource = (Resource) value;
//  	                  owlValue = new OWLIndividualImpl(baseOntology, resource);
//  	              }
//  	              map.setValue(var, owlValue);
//                }
//            }
//            result.add(map);
//        }
//
//        return result;
//      }

    public boolean isSubTypeOf(OWLDataType t1, OWLDataType t2) {
        // FIXME datatype subsumption
        return t1.equals(t2);
    }

    public boolean isSubTypeOf(OWLType t1, OWLType t2) {
        if(t1 instanceof OWLClass && t2 instanceof OWLClass)
            return isSubClassOf((OWLClass) t1, (OWLClass) t2);
        if(t1 instanceof OWLDataType && t2 instanceof OWLDataType)
            return isSubTypeOf((OWLDataType) t1, (OWLDataType) t2);

        return false;
    }

    public AnyOrder createAnyOrder() { return createAnyOrder(createInstance(OWLS.Process.AnyOrder)); }
    public AnyOrder createAnyOrder(URI uri) { return createAnyOrder(createInstance(OWLS.Process.AnyOrder, uri)); }
    public AnyOrder createAnyOrder(OWLIndividual ind) { return new AnyOrderImpl(ind); }

    public AtomicProcess createAtomicProcess() { return new AtomicProcessImpl(createInstance(OWLS.Process.AtomicProcess)); }
    public AtomicProcess createAtomicProcess(URI uri) { return new AtomicProcessImpl(createInstance(OWLS.Process.AtomicProcess, uri)); }
    public AtomicProcess createAtomicProcess(OWLIndividual ind) { return new AtomicProcessImpl(ind); }

    public Choice createChoice() { return createChoice(createInstance(OWLS.Process.Choice)); }
    public Choice createChoice(URI uri) { return createChoice(createInstance(OWLS.Process.Choice, uri)); }
    public Choice createChoice(OWLIndividual ind) { return new ChoiceImpl(ind); }

    public CompositeProcess createCompositeProcess() { return createCompositeProcess(createInstance(OWLS.Process.CompositeProcess)); }
    public CompositeProcess createCompositeProcess(URI uri) { return createCompositeProcess(createInstance(OWLS.Process.CompositeProcess, uri)); }
    public CompositeProcess createCompositeProcess(OWLIndividual ind) {
        return new CompositeProcessImpl(ind);
    }

    public Condition createSWRLCondition() { return new SWRLExpressionImpl(createInstance(OWLS.Expression.SWRL_Condition)); }
    public Condition createSWRLCondition(URI uri) { return new SWRLExpressionImpl(createInstance(OWLS.Expression.SWRL_Condition, uri)); }
    public Condition createSWRLCondition(OWLIndividual ind) { return new SWRLExpressionImpl(ind);	}

    public Expression createSWRLExpression() { return new SWRLExpressionImpl(createInstance(OWLS.Expression.SWRL_Expression)); }
    public Expression createSWRLExpression(URI uri) { return new SWRLExpressionImpl(createInstance(OWLS.Expression.SWRL_Expression, uri)); }
    public Expression createSWRLExpression(OWLIndividual ind) { return new SWRLExpressionImpl(ind);	}

    public ForEach createForEach() { return createForEach(createInstance(OWLS.Process.ForEach)); }
    public ForEach createForEach(URI uri) { return createForEach(createInstance(OWLS.Process.ForEach, uri)); }
    public ForEach createForEach(OWLIndividual ind) {
        return new ForEachImpl(ind);
    }
    public Grounding createGrounding() { return createGrounding(createInstance(OWLS.Grounding.WsdlGrounding)); }
    public Grounding createGrounding(URI uri) { return createGrounding(createInstance(OWLS.Grounding.WsdlGrounding, uri)); }
    public Grounding createGrounding(OWLIndividual ind) {
        return new GroundingImpl(ind);
    }

    public IfThenElse createIfThenElse() { return new IfThenElseImpl(createInstance(OWLS.Process.IfThenElse)); }
    public IfThenElse createIfThenElse(URI uri) { return new IfThenElseImpl(createInstance(OWLS.Process.IfThenElse, uri)); }
    public IfThenElse createIfThenElse(OWLIndividual ind) {
        return new IfThenElseImpl(ind);
    }

    public Input createInput() { return new InputImpl(createInstance(OWLS.Process.Input)); }
    public Input createInput(URI uri) { return new InputImpl(createInstance(OWLS.Process.Input, uri)); }
    public Input createInput(OWLIndividual ind) { return new InputImpl(ind); }

    public InputBinding createInputBinding() { return new InputBindingImpl(createInstance(OWLS.Process.InputBinding));  }
    public InputBinding createInputBinding(URI uri) { return new InputBindingImpl(createInstance(OWLS.Process.InputBinding, uri));  }
    public InputBinding createInputBinding(OWLIndividual ind) { return new InputBindingImpl(ind);  }

    public Local createLocal() { return createLocal(createInstance(OWLS.Process.Local));  }
    public Local createLocal(URI uri) { return createLocal(createInstance(OWLS.Process.Local, uri));  }
    public Local createLocal(OWLIndividual ind) { return new LocalImpl(ind);  }

    public MessageMap createWSDLMessageMap(OWLIndividual ind) {
        return new WSDLMessageMapImpl(ind);
    }

    public MessageMap createWSDLInputMessageMap() { return createWSDLMessageMap(createInstance(OWLS.Grounding.WsdlInputMessageMap)); }
    public MessageMap createWSDLInputMessageMap(URI uri) {
        return createWSDLMessageMap(createInstance(OWLS.Grounding.WsdlInputMessageMap, uri));
    }

    public MessageMap createWSDLOutputMessageMap() { return createWSDLMessageMap(createInstance(OWLS.Grounding.WsdlOutputMessageMap)); }
    public MessageMap createWSDLOutputMessageMap(URI uri) {
        return createWSDLMessageMap(createInstance(OWLS.Grounding.WsdlOutputMessageMap, uri));
    }

    public MessageMap createUPnPMessageMap() { return createUPnPMessageMap(createInstance(FLAServiceOnt.UPnPMap)); }
    public MessageMap createUPnPMessageMap(URI uri) {
        return createUPnPMessageMap(createInstance(FLAServiceOnt.UPnPMap, uri));
    }
    public MessageMap createUPnPMessageMap(OWLIndividual ind) {
        return new UPnPMessageMapImpl(ind);
    }

    public Output createOutput() { return new OutputImpl(createInstance(OWLS.Process.Output)); }
    public Output createOutput(URI uri) { return new OutputImpl(createInstance(OWLS.Process.Output, uri)); }
    public Output createOutput(OWLIndividual ind) { return new OutputImpl(ind); }

    public OutputBinding createOutputBinding() { return new OutputBindingImpl(createInstance(OWLS.Process.OutputBinding));  }
    public OutputBinding createOutputBinding(URI uri) { return new OutputBindingImpl(createInstance(OWLS.Process.OutputBinding, uri));  }
    public OutputBinding createOutputBinding(OWLIndividual ind) { return new OutputBindingImpl(ind);  }

    public Perform createPerform() { return new PerformImpl(createInstance(OWLS.Process.Perform));	}
    public Perform createPerform(URI uri) { return new PerformImpl(createInstance(OWLS.Process.Perform, uri));	}
    public Perform createPerform(OWLIndividual ind) { return new PerformImpl(ind);	}

    public Produce createProduce() { return createProduce(createInstance(OWLS.Process.Produce)); }
    public Produce createProduce(URI uri) { return createProduce(createInstance(OWLS.Process.Produce, uri)); }
    public Produce createProduce(OWLIndividual ind) {
        return new ProduceImpl(ind);
    }
    public Profile createProfile() { return createProfile(createInstance(OWLS.Profile.Profile)); }
    public Profile createProfile(URI uri) { return createProfile(createInstance(OWLS.Profile.Profile, uri)); }
    public Profile createProfile(OWLIndividual ind) {
        return new ProfileImpl(ind);
    }

    public RepeatUntil createRepeatUntil() { return new RepeatUntilImpl(createInstance(OWLS.Process.RepeatUntil)); }
    public RepeatUntil createRepeatUntil(URI uri) { return new RepeatUntilImpl(createInstance(OWLS.Process.RepeatUntil, uri)); }
    public RepeatUntil createRepeatUntil(OWLIndividual ind) {
        return new RepeatUntilImpl(ind);
    }

    public RepeatWhile createRepeatWhile() { return new RepeatWhileImpl(createInstance(OWLS.Process.RepeatWhile)); }
    public RepeatWhile createRepeatWhile(URI uri) { return new RepeatWhileImpl(createInstance(OWLS.Process.RepeatWhile, uri)); }
    public RepeatWhile createRepeatWhile(OWLIndividual ind) {
        return new RepeatWhileImpl(ind);
    }

    public Result createResult() { return new ResultImpl(createInstance(OWLS.Process.Result));	}
    public Result createResult(URI uri) { return new ResultImpl(createInstance(OWLS.Process.Result, uri));	}
    public Result createResult(OWLIndividual ind) { return new ResultImpl(ind);	}

    public Sequence createSequence() { return createSequence(createInstance(OWLS.Process.Sequence)); }
    public Sequence createSequence(URI uri) { return createSequence(createInstance(OWLS.Process.Sequence, uri)); }
    public Sequence createSequence(OWLIndividual ind) {
        return new SequenceImpl(ind);
    }

    public Service createService() { return createService(createInstance(OWLS.Service.Service)); }
    public Service createService(URI uri) { return createService(createInstance(OWLS.Service.Service, uri)); }
    public Service createService(OWLIndividual ind) {
        return new ServiceImpl(ind);
    }

    public ServiceParameter createServiceParameter() { return createServiceParameter(createInstance(OWLS.Profile.ServiceParameter)); }
    public ServiceParameter createServiceParameter(URI uri) { return createServiceParameter(createInstance(OWLS.Profile.ServiceParameter, uri)); }
    public ServiceParameter createServiceParameter(OWLIndividual ind) {
        return new ServiceParameterImpl(ind);
    }

    public Split createSplit() { return createSplit(createInstance(OWLS.Process.Split)); }
    public Split createSplit(URI uri) { return createSplit(createInstance(OWLS.Process.Split, uri)); }
    public Split createSplit(OWLIndividual ind) {
        return new SplitImpl(ind);
    }

    public SplitJoin createSplitJoin() { return createSplitJoin(createInstance(OWLS.Process.SplitJoin)); }
    public SplitJoin createSplitJoin(URI uri) { return createSplitJoin(createInstance(OWLS.Process.SplitJoin, uri)); }
    public SplitJoin createSplitJoin(OWLIndividual ind) {
        return new SplitJoinImpl(ind);
    }

    public UPnPAtomicGrounding createUPnPAtomicGrounding() {
        return createUPnPAtomicGrounding(createInstance(FLAServiceOnt.UPnPAtomicProcessGrounding));
    }
    public UPnPAtomicGrounding createUPnPAtomicGrounding(URI uri) {
        return createUPnPAtomicGrounding(createInstance(FLAServiceOnt.UPnPAtomicProcessGrounding, uri));
    }
    public UPnPAtomicGrounding createUPnPAtomicGrounding(OWLIndividual ind) {
        return new UPnPAtomicGroundingImpl(ind);
    }
    // added 15.4.2005 by Michael Daenzer
    public JavaAtomicGrounding createJavaAtomicGrounding() {
        return createJavaAtomicGrounding(createInstance(MoreGroundings.JavaAtomicProcessGrounding));
    }
    public JavaAtomicGrounding createJavaAtomicGrounding(URI uri) {
        return createJavaAtomicGrounding(createInstance(MoreGroundings.JavaAtomicProcessGrounding, uri));
    }
    public JavaAtomicGrounding createJavaAtomicGrounding(OWLIndividual ind) {
        return new JavaAtomicGroundingImpl(ind);
    }

    public Grounding createJavaGrounding() { return createJavaGrounding(createInstance(MoreGroundings.JavaGrounding)); }
    public Grounding createJavaGrounding(URI uri) { return createJavaGrounding(createInstance(MoreGroundings.JavaGrounding, uri)); }
    public Grounding createJavaGrounding(OWLIndividual ind) {
        return new GroundingImpl(ind);
    }
    // end added by Michael Daenzer

    public ValueOf createValueOf() { return new ValueOfImpl(createInstance(OWLS.Process.ValueOf));  }
    public ValueOf createValueOf(URI uri) { return new ValueOfImpl(createInstance(OWLS.Process.ValueOf, uri));  }
    public ValueOf createValueOf(OWLIndividual ind) { return new ValueOfImpl(ind);  }

    public ValueData createValueData(OWLValue dataValue) {
        return new ValueDataImpl(dataValue);
    }

    public WSDLAtomicGrounding createWSDLAtomicGrounding() {
        return createWSDLAtomicGrounding(createInstance(OWLS.Grounding.WsdlAtomicProcessGrounding));
    }
    public WSDLAtomicGrounding createWSDLAtomicGrounding(URI uri) {
        return createWSDLAtomicGrounding(createInstance(OWLS.Grounding.WsdlAtomicProcessGrounding, uri));
    }
    public WSDLAtomicGrounding createWSDLAtomicGrounding(OWLIndividual ind) {
        return new WSDLAtomicGroundingImpl(ind);
    }

    public WSDLOperationRef createWSDLOperationRef() {
        return new WSDLOperationRefImpl(createInstance(OWLS.Grounding.WsdlOperationRef));
    }
    public WSDLOperationRef createWSDLOperationRef(URI uri) {
        return new WSDLOperationRefImpl(createInstance(OWLS.Grounding.WsdlOperationRef, uri));
    }

    public List getServices() {
        return OWLSFactory.wrapList(getInstances(OWLS.Service.Service), Service.class);
    }

    public Service getService(URI serviceURI) {
        OWLIndividual ind = getIndividual(serviceURI);

        return (ind == null) ? null : (Service) ind.castTo(Service.class);
    }

    public List getProfiles() {
        return OWLSFactory.wrapList(getInstances(OWLS.Profile.Profile), Service.class);
    }

    public Profile getProfile(URI profileURI) {
        OWLIndividual ind = getIndividual(profileURI);

        return (ind == null) ? null : (Profile) ind.castTo( Profile.class );
    }

    public List getProcesses() {
        return OWLSFactory.wrapList(getInstances(OWLS.Process.Process), Process.class);
    }

    public List getProcesses(int type) {
        switch(type) {
            case Process.ANY:
                return OWLSFactory.wrapList(getInstances(OWLS.Process.Process), Process.class);
            case Process.ATOMIC:
                return OWLSFactory.wrapList(getInstances(OWLS.Process.AtomicProcess), AtomicProcess.class);
            case Process.COMPOSITE:
                return OWLSFactory.wrapList(getInstances(OWLS.Process.CompositeProcess), CompositeProcess.class);
            case Process.SIMPLE:
                return OWLSFactory.wrapList(getInstances(OWLS.Process.SimpleProcess), SimpleProcess.class);
            default:
                break;
        }

        return null;
    }

    public Process getProcess(URI processURI) {
        OWLIndividual ind = getIndividual(processURI);

        return (ind == null) ? null : (Process) ind.castTo( Process.class );
    }



    public ControlConstructList createControlConstructList(ControlConstruct cc) {
        RDFList list = createList( OWLS.CCList, cc );
        return (ControlConstructList) list.castTo(ControlConstructList.class);
    }

    public ControlConstructBag createControlConstructBag(ControlConstruct cc) {
        RDFList list = createList( OWLS.CCBag, cc );
        return (ControlConstructBag) list.castTo(ControlConstructBag.class);
    }

    public boolean isLockSupported() {
        return true;
    }

    public void lockForRead() throws LockNotSupportedException {
        ontModel.enterCriticalSection(Lock.READ);
    }

    public void lockForWrite() throws LockNotSupportedException {
        ontModel.enterCriticalSection(Lock.WRITE);
    }

    public void releaseLock() throws LockNotSupportedException {
        ontModel.leaveCriticalSection();
    }

    public OWLWriter getWriter() {
        if( writer == null ){
            writer = OWLFactory.createWriter();
            writer.setNsPrefix("service", OWLS.Service.URI);
            writer.setNsPrefix("profile", OWLS.Profile.URI);
            writer.setNsPrefix("process", OWLS.Process.URI);
            writer.setNsPrefix("grounding", OWLS.Grounding.URI);
            writer.setNsPrefix("expression", OWLS.Expression.URI);
            writer.setNsPrefix("swrl", SWRL.URI);
            writer.setNsPrefix("list", URIUtils.getNameSpace(OWLS.ObjList.List().getURI().toString()));

            writer.addPrettyType(OWLS.Service.Service);
            writer.addPrettyType(OWLS.Service.ServiceGrounding);
            writer.addPrettyType(OWLS.Profile.Profile);
            writer.addPrettyType(OWLS.Process.Process);
            writer.addPrettyType(OWLS.Process.AtomicProcess);
            writer.addPrettyType(OWLS.Process.CompositeProcess);
            writer.addPrettyType(OWLS.Process.SimpleProcess);
            writer.addPrettyType(OWLS.Process.Perform);
            writer.addPrettyType(OWLS.Grounding.WsdlGrounding);
            writer.addPrettyType(OWLS.Grounding.WsdlAtomicProcessGrounding);
        }

        return writer;
    }

    public void write(Writer out) {
        getWriter().write(this, out);
    }

    public void write(Writer out, URI baseURI) {
        getWriter().write(this, out, baseURI);
    }

    public void write(OutputStream out) {
        getWriter().write(this, out);
    }

    public void write(OutputStream out, URI baseURI) {
        getWriter().write(this, out, baseURI);
    }
}
