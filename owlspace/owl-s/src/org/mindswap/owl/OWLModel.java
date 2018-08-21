/*
 * Created on Dec 16, 2004
 */
package org.mindswap.owl;

import java.io.OutputStream;
import java.io.Writer;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.mindswap.exceptions.LockNotSupportedException;
import org.mindswap.exceptions.UnboundVariableException;
import org.mindswap.owl.list.ListVocabulary;
import org.mindswap.owl.list.RDFList;
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
import org.mindswap.owls.process.BindingList;
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
import org.mindswap.owls.process.Parameter;
import org.mindswap.owls.process.Perform;
import org.mindswap.owls.process.Process;
import org.mindswap.owls.process.Produce;
import org.mindswap.owls.process.RepeatUntil;
import org.mindswap.owls.process.RepeatWhile;
import org.mindswap.owls.process.Result;
import org.mindswap.owls.process.Sequence;
import org.mindswap.owls.process.Split;
import org.mindswap.owls.process.SplitJoin;
import org.mindswap.owls.process.ValueData;
import org.mindswap.owls.process.ValueOf;
import org.mindswap.owls.profile.Profile;
import org.mindswap.owls.profile.ServiceParameter;
import org.mindswap.owls.service.Service;
import org.mindswap.query.ABoxQuery;
import org.mindswap.query.ValueMap;
import org.mindswap.swrl.AtomList;

/**
 * An abstract view for querying a KnowledgeBase or a single ontology.
 *
 * @author Evren Sirin
 */
public interface OWLModel {
    /**
     * Return the implementation specific object for this model.
     *
     * @return
     */
    public Object getImplementation();

    /**
     * Refresh the data in this model from the original source.
     */
    public void refresh();

    /**
     * Check if the model is consistent or not. This functions will simply return true if
     * there is no reasoner attached or the reasoner used does not support consistency check.
     *
     * @return
     */
    public boolean isConsistent();

    /**
     * Prepare the reasoner associated with this model.
     */
    public void prepare();

    /**
     * Tell the reasoner to compute the subclass relations between all the named classes and find
     * the instances of each class. The results will be cached and subsequent calls to queyr the
     * model will use the cache. This call has no effect if reasoner does not support
     * classification.
     */
    public void classify();

    /**
     * Returns true if the model is classifed.
     */
    public boolean isClassified();

    /**
     * Set the reasoner of this model using a reasner name. See OWLFactory.getReasoners() for
     * available reasoner names.
     *
     * <p>For Jena based implementation available reasoner names are:
     * <ul>
     * 	<li>RDFS</li>
     * 	<li>RDFS-Simple</li>
     * 	<li>Transitive</li>
     * 	<li>OWL</li>
     * 	<li>OWL-Mini</li>
     * 	<li>OWL-Micro</li>
     * 	<li>DIG</li>
     * 	<li>Pellet</li>
     * </ul>
     * </p>
     * @param reasonerName
     */
    public void setReasoner(String reasonerName);

    /**
     * Set the reasoner using an implementation specific reasoner object. The exact type of the
     * parameter is different for Jena and OWL-API.
     *
     * @param reasoner
     */
    public void setReasoner(Object reasoner);

    /**
     * @return
     */
    public Object getReasoner();

//	/**
//	 * Used to enable/disable the attached reasoner for some period of time. Using reasoner to
//	 * answer all the queries may be too expensive especially when the KB is being changed
//	 * frequently. Using this function, attached reasoner can be disabled and enabled back when
//	 * needed.
//	 *
//	 * @param enabled
//	 * @return
//	 */
//	public void enableReasoner( boolean enabled );

    /**
     * The ontology where the changes will go. If this model represents an OWLOntology object
     * then it is the same ontology, if th emodel represents a OWLKnowledgeBase then it is the
     * base ontology of the KB.
     *
     * @return
     */
    public OWLOntology getBaseOntology();

//	/**
//	 * @return
//	 */
//	public OWLSObjListFactory getListFactory();
//
//	/**
//	 * @param vocabulary
//	 * @return
//	 */
//	public OWLSObjListFactory getListFactory(ListVocabulary vocabulary);

    /**
     * @param uri
     * @return
     */
    public OWLClass createClass(URI uri);
    /**
     * @param uri
     * @return
     */
    public OWLObjectProperty createObjectProperty(URI uri);
    /**
     * @param uri
     * @return
     */
    public OWLDataProperty createDataProperty(URI uri);
    /**
     *
     * @param uri
     * @return
     */
    public OWLIndividual createIndividual(URI uri);
    /**
     * @param c
     * @return
     */
    public OWLIndividual createInstance(OWLClass c);
    /**
     * @param c
     * @param uri
     * @return
     */
    public OWLIndividual createInstance(OWLClass c, URI uri);
    /**
     * @param value
     * @return
     */
    public OWLDataValue createDataValue(String value);
    /**
     * @param value
     * @param language
     * @return
     */
    public OWLDataValue createDataValue(String value, String language);
    /**
     * @param value
     * @param datatypeURI
     * @return
     */
    public OWLDataValue createDataValue(Object value, URI datatypeURI);
    /**
     * @param value
     * @return
     */
    public OWLDataValue createDataValue(Object value);

    /**
     * @param uri
     * @return
     */
    public OWLClass getClass(URI uri);
    
    public Set getClasses();
    /**
     * @param uri
     * @return
     */
    public OWLIndividual getIndividual(URI uri);

    public OWLIndividualList getIndividuals();

    /**
     * @param uri
     * @return
     */
    public OWLProperty getProperty(URI uri);
    /**
     * @param uri
     * @return
     */
    public OWLObjectProperty getObjectProperty(URI uri);
    /**
     * @param uri
     * @return
     */
    public OWLDataProperty getDataProperty(URI uri);
    /**
     * @param uri
     * @return
     */
    public OWLDataType getDataType(URI uri);
    /**
     * @param uri
     * @return
     */
    public OWLType getType(URI uri);
    /**
     * @param uri
     * @return
     */
    public OWLEntity getEntity(URI uri);

    /**
     * Set the value for the given data property to the given plain literal
     * value (no language identifier). All the existing data values (that has
     * no language identifier) will be removed.
     *
     * @param propURI
     * @param value
     */
    /**
     * @param ind
     * @param prop
     * @param value
     */
    public void setProperty(OWLIndividual ind, OWLDataProperty prop, String value);

    /**
     * Set the value for the given data property. All the existing data values
     * (that has the same language identifier with the given value) will be removed.
     *
     * @param propURI
     * @param value
     */
    /**
     * @param ind
     * @param prop
     * @param value
     */
    public void setProperty(OWLIndividual ind, OWLDataProperty prop, OWLDataValue value);

    /**
     * Set the value for the given data property to the given literal by
     * determining the RDF datatype from Java class. This function is
     * equivalent to <code>setProperty(prop, OWLFactory.createDataValue(value))</code>.
     *
     * @param prop
     * @param value
     */
    /**
     * @param ind
     * @param prop
     * @param value
     */
    public void setProperty(OWLIndividual ind, OWLDataProperty prop, Object value);

    public void addProperty(OWLIndividual ind, OWLProperty prop, OWLValue value);



    /**
     * @param ind
     * @param prop
     * @param value
     */
    public void addProperty(OWLIndividual ind, OWLDataProperty prop, OWLDataValue value);

    public void addProperty(OWLIndividual ind, OWLDataProperty prop, Object value);

    public void addProperty(OWLIndividual ind, OWLDataProperty prop, String value);
    
    /**
     * Removes all RDF statements from the model with the given OWLIndividual in 
     * its object or subject therefore removing each occurrence of the given individual.
     * This is a convenience method for removeIndividuals(ind, false)  
     *  
     * @param ind the individual to remove
     * @see #removeIndividuals(OWLIndividual, boolean)
     */
    public void removeIndividuals(OWLIndividual ind);
    
    /**
     * Removes all RDF statements from the model with the given OWLIndividual in 
     * its object or subject therefore removing each occurrence of the given individual.
     * If the <code>recursive</code> parameter is set, the method iterates recursively 
     * through all properties of the given individual and removes them all.   
     * 
     * ATTENTION: Use the recursive mode only when you are sure that your data is 
     * structured in a tree and not in a graph (as OWL and OWL-S usually is). Otherwise 
     * unexpected behaviour may be observed.
     *  
     * @param ind the individual to remove
     * @param recursive true, if recursive removal is desired. false otherwise
     */
    public void removeIndividuals(OWLIndividual ind, boolean recursive);
    
    /**
     * @param ind
     * @param prop
     */
    public void removeProperties(OWLIndividual ind, OWLProperty prop);

    /**
     * Removes the specified triple from the Model.
     * @param theInd OWLIndividual
     * @param theProp OWLProperty
     * @param theValue OWLValue
     */
    public void removeProperty(OWLIndividual theInd, OWLProperty theProp, OWLValue theValue);

    /**
     * @param ind
     * @param prop
     * @param value
     */
    public void addProperty(OWLIndividual ind, OWLObjectProperty prop, OWLIndividual value);

    /**
     * @param ind
     * @param prop
     * @param value
     */
    public void setProperty(OWLIndividual ind, OWLObjectProperty prop, OWLIndividual value);

    /**
     * @param ind
     * @param c
     */
    public void addType(OWLIndividual ind, OWLClass c);

    /**
     * @param ind
     */
    public void removeTypes(OWLIndividual ind);

    /**
     * Returns true if  c is an enumerated class (defined with owl:oneOf)
     *
     * @param c
     * @return
     */
    public boolean isEnumerated(OWLClass c);

    /**
     * Returns the enumeration for the class (the list of individuals declared in the
     * owl:oneOf list).
     *
     * @param c
     * @return List of individuals or null if the class is not enumerated
     */
    public OWLIndividualList getEnumerations(OWLClass c);

    /**
     * Check if one class is subclass of another
     *
     * @param c1
     * @param c2
     * @return
     */
    public boolean isSubClassOf(OWLClass c1, OWLClass c2);

    /**
     * Check if one datatype is subtype of another
     *
     * @param uri1
     * @param uri2
     * @return
     */
    /**
     * @param t1
     * @param t2
     * @return
     */
    public boolean isSubTypeOf(OWLDataType t1, OWLDataType t2);

    /**
     * Check if one type is subsumed by another
     *
     * @param uri1
     * @param uri2
     * @return
     */
    /**
     * @param t1
     * @param t2
     * @return
     */
    public boolean isSubTypeOf(OWLType t1, OWLType t2);

    /**
     * @param t1
     * @param t2
     * @return
     */
    public boolean isEquivalent(OWLType t1, OWLType t2);

    /**
     * @param t1
     * @param t2
     * @return
     */
    public boolean isDisjoint(OWLType t1, OWLType t2);


    /**
     * Check if two classes are disjoint. Returns true if there can possibly be no
     * individual that may belong ot both classes.
     *
     * @param uri1
     * @param uri2
     * @return
     */
    /**
     * @param c1
     * @param c2
     * @return
     */
    public boolean isDisjoint(OWLClass c1, OWLClass c2);


    /**
     * Get all the subclasses of the given class
     *
     * @param uri
     * @return
     */
    /**
     * @param c
     * @return
     */
    public Set getSubClasses(OWLClass c);

    /**
     * Get all the (direct) subclasses of the given class
     *
     * @param c
     * @param direct
     * @return
     */
    public Set getSubClasses(OWLClass c, boolean direct);

    /**
     * Get all the superclasses of the given class
     *
     * @param c
     * @return
     */
    public Set getSuperClasses(OWLClass c);

    /**
     * Get all the (direct) subclasses of the given class
     *
     * @param c
     * @param direct
     * @return
     */
    public Set getSuperClasses(OWLClass c, boolean direct);

    /**
     * @param c
     * @return
     */
    public Set getEquivalentClasses(OWLClass c);

    /**
     * @param p
     * @return
     */
    public Set getSubProperties(OWLProperty p);
    /**
     * @param p
     * @return
     */
    public Set getSuperProperties(OWLProperty p);
    /**
     * @param p
     * @return
     */
    public Set getEquivalentProperties(OWLProperty p);

    /**
     * @param c
     * @return
     */
    public OWLIndividualList getInstances(OWLClass c);

    /**
     * @param ind
     * @param c
     * @return
     */
    public boolean isType(OWLIndividual ind, OWLClass c);

    /**
     * @param ind
     * @return
     */
    public OWLClass getType(OWLIndividual ind);

    /**
     * @param ind
     * @return
     */
    public Set getTypes(OWLIndividual ind);

    /**
     * @param ind
     * @param prop
     * @return
     */
    public OWLIndividual getProperty(OWLIndividual ind, OWLObjectProperty prop);
    /**
     * @param ind
     * @param prop
     * @return
     */
    public OWLIndividualList getProperties(OWLIndividual ind, OWLObjectProperty prop);
    /**
     * @param ind
     * @return
     */
    public Map getProperties(OWLIndividual ind);
    
    /**
     * Returns all properties for a class. Note that a class is an instance in OWL and therefore
     * the properties given to the instance at definition are returned and NOT the
     * properties that are declared for this class. Use 
     * <code>getDeclaredProperties(OWLClass claz)</code> instead.
     *  
     * @param claz the class whose properties will be returned
     * @return all properties (data- and object-properties) of the given class
     * @see #getDeclaredProperties(OWLClass)
     */
    public Map getProperties(OWLClass claz);
    
    /**
     * Returns all declared properties for a class. Use 
     * <code>getDeclaredProperties(OWLClass claz)</code> if you want the properties
     * of the instance at class defintion.
     *  
     * @param claz the class whose properties will be returned
     * @return all properties (data- and object-properties) of the given class
     * @see #getDeclaredProperties(OWLClass)
     */
    public List getDeclaredProperties(OWLClass claz);
    
    /**
     * Returns all declared properties for a class. Use 
     * <code>getDeclaredProperties(OWLClass claz)</code> if you want the properties
     * of the instance at class defintion.
     *  
     * @param claz the class whose properties will be returned
     * @param direct true, if only properties of the given class itself should be returned. false, if properties of superclasses should be returned too 
     * @return all properties (data- and object-properties) of the given class
     * @see #getDeclaredProperties(OWLClass)
     */
    public List getDeclaredProperties(OWLClass claz, boolean direct);
    
    
    /**
     * @param ind
     * @param prop
     * @return
     */
    public OWLDataValue getProperty(OWLIndividual ind, OWLDataProperty prop);
    /**
     * @param ind
     * @param prop
     * @param lang
     * @return
     */
    public OWLDataValue getProperty(OWLIndividual ind, OWLDataProperty prop, String lang);
    /**
     * @param ind
     * @param prop
     * @return
     */
    public OWLDataValueList getProperties(OWLIndividual ind, OWLDataProperty prop);

    /**
     * @param prop
     * @param ind
     * @return
     */
    public OWLIndividual getIncomingProperty(OWLObjectProperty prop, OWLIndividual ind);
    /**
     * @param prop
     * @param ind
     * @return
     */
    public OWLIndividualList getIncomingProperties(OWLObjectProperty prop, OWLIndividual ind);

     public OWLIndividualList getIncomingProperties(OWLIndividual ind);

    /**
     * @param prop
     * @param value
     * @return
     */
    public OWLIndividual getIncomingProperty(OWLDataProperty prop, OWLDataValue value);
    /**
     * @param prop
     * @param value
     * @return
     */
    public OWLIndividualList getIncomingProperties(OWLDataProperty prop, OWLDataValue value);


    /**
     * @param ind
     * @param prop
     * @return
     */
    public boolean hasProperty(OWLIndividual ind, OWLProperty prop);
    /**
     * @param ind
     * @param prop
     * @param value
     * @return
     */
    public boolean hasProperty(OWLIndividual ind, OWLProperty prop, OWLValue value);

    /**
     * Apply the given set of (ground) atoms to the model. Simply adds new assertions to the
     * model.
     *
     * @param atoms
     * @throws UnboundVariableException
     */
    public void apply(AtomList atoms) throws UnboundVariableException;

    /**
     * Similar to apply but simply skips the nonground atoms instead of throwing an
     * exception.
     *  
     * @param atoms
     */
    public void applyGround(AtomList atoms);

    /**
     * Check if the conjunction of given atoms is entailed by the model.
     *
     * @param expr
     * @return
     */
    public boolean isTrue(ABoxQuery query);

    /**
     * Check if the condition is true in this model. The condition is transformed into an
     * <code>ABoxQuery</code> and then it is checked if the conjunction of atoms in the
     * query is entailed by the model. This is equivalent to the call
     * <code>isTrue( condition.getBody().toQuery() ).</code>
     *
     * @param expr
     * @return
     */
    public boolean isTrue(Condition condition);


    /**
     * Apply the given binding to the condition, thus grounding some of the variables,
     * and then check if this condition is true in this model.
     *
     * @param expr
     * @return
     */
    public boolean isTrue(Condition condition, ValueMap binding);

    /**
     * Answer the given ABoxQuery and return a List of ValueMap's that contains the
     * bindings for the result variables of the query. An empty list of answers
     * indicates there are no answers for the query. If the query is ground, i.e.
     * no variables in the body, then the returned list contains a single empty
     * ValueMap object if the atoms in the query are entailed (again an empty list
     * in case of failure)
     *
     * @param query
     * @return
     */
    public List query(ABoxQuery query);

    /**
     * Answer the given ABoxQuery passing in an initial binding of some of the variables
     * in the query. This function returns a List of ValueMap's that contains the
     * bindings for the result variables of the query. An empty list of answers
     * indicates there are no answers for the query. If the query is ground, i.e.
     * no variables in the body, then the returned list contains a single empty
     * ValueMap object if the atoms in the query are entailed (again an empty list
     * in case of failure)
     *
     * @param query
     * @param values
     * @return
     */
    public List query(ABoxQuery query, ValueMap values);

    /**
     *
     * @param query
     * @return
     */
    public List query(String query);


    /**
     * Checks if this implementation supports locking for concurrent access;
     *
     * @return
     */
    public boolean isLockSupported();

    /**
     * Lock the model for read operations. Multiple read operations are allowed for the same
     * model. When the model is locked for reading no operation that would modify the model
     * should be called. Throws an error if the model does not support concurrency, i.e.
     * isLockSupported function returns false.
     *
     * @throws LockNotSupportedException
     */
    public void lockForRead() throws LockNotSupportedException;

    /**
     * Lock the model for write operations. No other thread can access the model while there is
     * a write lock. Throws an error if the model does not support concurrency, i.e.
     * isLockSupported function returns false.
     *
     * @throws LockNotSupportedException
     */
    public void lockForWrite() throws LockNotSupportedException;

    /**
     * Releases the lock form the matching lockForXXX function.
     */
    public void releaseLock() throws LockNotSupportedException;

    public List getServices();
    public Service getService(URI serviceURI);

    public List getProfiles();
    public Profile getProfile(URI profileURI);

    public List getProcesses();
    public List getProcesses(int type);
    public Process getProcess(URI processURI);

    public AnyOrder createAnyOrder();
    public AnyOrder createAnyOrder(URI uri);

    public AtomicProcess createAtomicProcess();
    public AtomicProcess createAtomicProcess(URI uri);

    public Choice createChoice();
    public Choice createChoice(URI uri);

    public CompositeProcess createCompositeProcess();
    public CompositeProcess createCompositeProcess(URI uri);

    public Condition createSWRLCondition();
    public Condition createSWRLCondition(URI uri);

    public ControlConstructList createControlConstructList(ControlConstruct item);
    public ControlConstructBag createControlConstructBag(ControlConstruct item);

    public Expression createSWRLExpression();
    public Expression createSWRLExpression(URI uri);

    public ForEach createForEach();
    public ForEach createForEach(URI uri);

    public Grounding createGrounding();
    public Grounding createGrounding(URI uri);

    public IfThenElse createIfThenElse();
    public IfThenElse createIfThenElse(URI uri);

    public Input createInput();
    public Input createInput(URI uri);

    public InputBinding createInputBinding();
    public InputBinding createInputBinding(URI uri);

    public Local createLocal();
    public Local createLocal(URI uri);

    public MessageMap createWSDLInputMessageMap();
    public MessageMap createWSDLInputMessageMap(URI uri);

    public MessageMap createWSDLOutputMessageMap();
    public MessageMap createWSDLOutputMessageMap(URI uri);

    public MessageMap createUPnPMessageMap();
    public MessageMap createUPnPMessageMap(URI uri);

    public Output createOutput();
    public Output createOutput(URI uri);

    public OutputBinding createOutputBinding();
    public OutputBinding createOutputBinding(URI uri);

    public Perform createPerform();
    public Perform createPerform(URI uri);
    public Perform createPerform(OWLIndividual individual);

    public Produce createProduce();
    public Produce createProduce(URI uri);

    public Profile createProfile();
    public Profile createProfile(URI uri);

    public RepeatUntil createRepeatUntil();
    public RepeatUntil createRepeatUntil(URI uri);

    public RepeatWhile createRepeatWhile();
    public RepeatWhile createRepeatWhile(URI uri);

    public Result createResult();
    public Result createResult(URI uri);

    public Sequence createSequence();
    public Sequence createSequence(URI uri);

    public Service createService();
    public Service createService(URI uri);

    public ServiceParameter createServiceParameter();
    public ServiceParameter createServiceParameter(URI uri);

    public Split createSplit();
    public Split createSplit(URI uri);

    public SplitJoin createSplitJoin();
    public SplitJoin createSplitJoin(URI uri);

    //added 15.4.2005 by Michael Daenzer for Java-Grounding
    public JavaAtomicGrounding createJavaAtomicGrounding();
    public JavaAtomicGrounding createJavaAtomicGrounding(URI uri);
    public Grounding createJavaGrounding();
    public Grounding createJavaGrounding(URI uri);
    // end added

    public UPnPAtomicGrounding createUPnPAtomicGrounding();
    public UPnPAtomicGrounding createUPnPAtomicGrounding(URI uri);

    public ValueOf createValueOf();
    public ValueOf createValueOf(URI uri);

    public WSDLAtomicGrounding createWSDLAtomicGrounding();
    public WSDLAtomicGrounding createWSDLAtomicGrounding(URI uri);

    public WSDLOperationRef createWSDLOperationRef();
    public WSDLOperationRef createWSDLOperationRef(URI uri);

    public ValueData createValueData(OWLValue dataValue);

    public OWLSObjList createList();

    public OWLSObjList createList(OWLIndividual item);

    public OWLSObjList createList(OWLIndividualList items);

    public RDFList createList(ListVocabulary vocabulary);

    public RDFList createList(ListVocabulary vocabulary, OWLIndividual item);

    public RDFList createList(ListVocabulary vocabulary, OWLIndividualList items);

    /**
     * Turn the RDF/XML representation into an OWLIndividual
     * @param literal
     * @return
     */
    public OWLIndividual parseLiteral(String literal);

    /**
     * Return true if given two individuals are same as each other as dictated by the
     * semantics of owl:sameAs.
     *
     * @param ind1
     * @param ind2
     * @return
     */
    public boolean isSameAs(OWLIndividual ind1, OWLIndividual ind2);

    public OWLIndividualList getSameIndividuals(OWLIndividual ind);

    /**
     * Return true if given two individuals are different from each other as
     * dictated by the semantics of owl:differentFrom.
     *
     * @param ind1
     * @param ind2
     * @return
     */
    public boolean isDifferentFrom(OWLIndividual ind1, OWLIndividual ind2);

    public OWLIndividualList getDifferentIndividuals(OWLIndividual ind);

    public OWLWriter getWriter();
    public void write(Writer writer);
    public void write(Writer writer, URI baseURI);
    public void write(OutputStream out);
    public void write(OutputStream out, URI baseURI);
    
    /**
     * Returns a list with all individuals that do not belong to the OWL-S language 
     * definition and its base and helpers languages such as OWL, RDF or SWRL.
     * 
     * @return all individuals do not belong to the OWL-S language definition
     */
    public List getNonLanguageIndividuals();
    
    /**
     * Returns a list with all classes that do not belong to the OWL-S language 
     * definition and its base and helpers languages such as OWL, RDF or SWRL.
     * 
     * @return all classes do not belong to the OWL-S language definition
     */
    public List getNonLanguageClasses();
    /**
     * Returns a list with all object properties that do not belong to the OWL-S language 
     * definition and its base and helpers languages such as OWL, RDF or SWRL.
     * 
     * @return all object properties do not belong to the OWL-S language definition
     */
    public List getNonLanguageObjectProperties();
    /**
     * Returns a list with all datatype properties that do not belong to the OWL-S language 
     * definition and its base and helpers languages such as OWL, RDF or SWRL.
     * 
     * @return all datatype do not belong to the OWL-S language definition
     */
    public List getNonLanguageDataProperties();
    
    public BindingList getBindingsFor(Parameter parameter);
}
