/*
 * Created on Dec 28, 2004
 */
package impl.swrl;


import impl.owl.list.RDFListImpl;

import java.util.ArrayList;
import java.util.List;

import org.mindswap.exceptions.NotImplementedException;
import org.mindswap.owl.OWLDataValue;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLOntology;
import org.mindswap.owl.OWLValue;
import org.mindswap.owl.list.RDFList;
import org.mindswap.owl.vocabulary.SWRL;
import org.mindswap.owl.vocabulary.SWRLB;
import org.mindswap.query.ABoxQuery;
import org.mindswap.query.ValueMap;
import org.mindswap.swrl.Atom;
import org.mindswap.swrl.AtomList;
import org.mindswap.swrl.BuiltinAtom;
import org.mindswap.swrl.ClassAtom;
import org.mindswap.swrl.DataPropertyAtom;
import org.mindswap.swrl.DifferentIndividualsAtom;
import org.mindswap.swrl.IndividualPropertyAtom;
import org.mindswap.swrl.SWRLDataObject;
import org.mindswap.swrl.SWRLDataValue;
import org.mindswap.swrl.SWRLFactory;
import org.mindswap.swrl.SWRLFactoryCreator;
import org.mindswap.swrl.SWRLIndividual;
import org.mindswap.swrl.SWRLIndividualObject;
import org.mindswap.swrl.SWRLObject;
import org.mindswap.swrl.SameIndividualAtom;
import org.mindswap.swrl.Variable;

/**
 * @author Evren Sirin
 *
 */
public class AtomListImpl extends RDFListImpl implements AtomList {
    public AtomListImpl(OWLIndividual ind) {
    	super(ind);    	
    	
    	setVocabulary( SWRL.AtomListVocabulary );
    }
    
    public AtomList add( Atom item ) {
        if( isEmpty() )
            return insert( item );
        
        RDFList rest = getRest();
        
        if(rest.isEmpty())
            setRest( rest.insert( item ) );
        else
            rest.add(item);
        
        return this;    
    }
    
    public AtomList insert( Atom first ) {
        OWLOntology ont = getOntology();
        AtomListImpl list = new AtomListImpl( ont.createInstance( vocabulary.List() ) );        
        
        list.setFirst( first );
        list.setRest( this );
                
        return list;
    }

    public AtomList insertAt(int index, Atom item) {
        return (AtomList) super.insertAt( index, item).castTo( AtomList.class );
    }
    
    public RDFList insert( OWLValue item ) {
        Atom atom = (Atom) item.castTo( Atom.class );
                
        return add( atom );        
    }
    
    public OWLValue getFirstValue() {
        return (Atom) getProperty(vocabulary.first()).castTo(Atom.class);
    }
    
    public RDFList getRest() {
        return (RDFList) getProperty(vocabulary.rest()).castTo(AtomList.class);
    }

    public Atom atomAt(int index) {
        return (Atom) get(index);
    }

    public ABoxQuery toQuery() {
        return new ABoxQuery(this);
    }

    public ABoxQuery toQuery(List resultVars) {
        return new ABoxQuery(this, resultVars);
    }
    
    public SWRLIndividualObject getIndidividualObject(SWRLIndividualObject arg, ValueMap binding) {
        if( arg.isVariable() && binding.hasValue( (Variable) arg ) ) {
            OWLIndividual ind = (OWLIndividual) binding.getValue( (Variable) arg );
            arg = (SWRLIndividual) ind.castTo(SWRLIndividual.class);
        }
        
        return arg;
    }

    public SWRLDataObject getDataObject(SWRLDataObject arg, ValueMap binding) {
        if( arg.isVariable() && binding.hasValue( (Variable) arg ) ) {
            OWLDataValue dv = (OWLDataValue) binding.getValue( (Variable) arg );
            arg = (SWRLDataValue) dv.castTo(SWRLDataValue.class);                        
        }
        
        return arg;
    }
    
    public void evaluate(ValueMap values) {
    	for (int i = 0; i < size(); i++)
    		atomAt(i).evaluate(values);
    }
    
    public AtomList apply( ValueMap binding ) {
        OWLOntology ont = getOntology();
        SWRLFactory swrl = SWRLFactoryCreator.createFactory( ont );
        
        AtomList newList = swrl.createList();
        for( AtomList list = this; !list.isEmpty(); list = (AtomList) list.getRest() ) {
            Atom atom = (Atom) list.getFirst();            
           
            Atom newAtom = null;
            if(atom instanceof ClassAtom) {
                ClassAtom classAtom = (ClassAtom) atom;
                newAtom = swrl.createClassAtom(
                    classAtom.getClassPredicate(), 
                    getIndidividualObject( classAtom.getArgument1(), binding ) );
            }
            else if(atom instanceof IndividualPropertyAtom) {
                IndividualPropertyAtom indPropAtom = (IndividualPropertyAtom) atom;
                newAtom = swrl.createIndividualPropertyAtom(
                    indPropAtom.getPropertyPredicate(), 
                    getIndidividualObject( indPropAtom.getArgument1(), binding ),
                    getIndidividualObject( indPropAtom.getArgument2(), binding ));
            }
            else if(atom instanceof DataPropertyAtom) {
                DataPropertyAtom dataPropAtom = (DataPropertyAtom) atom;
                newAtom = swrl.createDataPropertyAtom(
                    dataPropAtom.getPropertyPredicate(), 
                    getIndidividualObject( dataPropAtom.getArgument1(), binding ),
                    getDataObject( dataPropAtom.getArgument2(), binding ));                
            }
            else if(atom instanceof SameIndividualAtom) {
                SameIndividualAtom sameAtom = (SameIndividualAtom) atom;
                newAtom = swrl.createSameIndividualAtom(
                    getIndidividualObject( sameAtom.getArgument1(), binding ),
                    getIndidividualObject( sameAtom.getArgument2(), binding ));                
            }
            else if(atom instanceof DifferentIndividualsAtom) {
                DifferentIndividualsAtom diffAtom = (DifferentIndividualsAtom) atom;
                newAtom = swrl.createDifferentIndividualsAtom(
                    getIndidividualObject( diffAtom.getArgument1(), binding ),
                    getIndidividualObject( diffAtom.getArgument2(), binding ));                
            }
            else if(atom instanceof BuiltinAtom) {
    			BuiltinAtom builtinAtom = (BuiltinAtom) atom;
    			OWLIndividual builtin = builtinAtom.getBuiltin();
                int argCount = atom.getArgumentCount();
                SWRLDataObject[] args = new SWRLDataObject[ argCount ];
                for( int i = 0; i < argCount; i++ ) {
                    SWRLDataObject arg = (SWRLDataObject) atom.getArgument( i );
                    args[i] = getDataObject( arg, binding );                    
                }
    			
    			if ( builtin.equals( SWRLB.equal ) ) 
    			    newAtom = swrl.createEqual( args[0], args[1] );
    			else if ( builtin.equals( SWRLB.notEqual ) ) 
    			    newAtom = swrl.createNotEqual( args[0], args[1] );
    			else if ( builtin.equals( SWRLB.greaterThan ) )
    			    newAtom = swrl.createGreaterThan( args[0], args[1] );
    			else if ( builtin.equals( SWRLB.greaterThanOrEqual ) )
    			    newAtom = swrl.createGreaterThanOrEqual( args[0], args[1] );
    			else if ( builtin.equals( SWRLB.lessThan ) )
    			    newAtom = swrl.createLessThan( args[0], args[1] );
    			else if ( builtin.equals( SWRLB.lessThanOrEqual ) )
    			    newAtom = swrl.createLessThanOrEqual( args[0], args[1] );
                else if ( builtin.equals( SWRLB.add ) )
                    newAtom = swrl.createAdd( args[0], args[1], args[2] );
                else if ( builtin.equals( SWRLB.subtract ) )
                    newAtom = swrl.createSubtract( args[0], args[1], args[2] );
    			else
    			    throw new NotImplementedException( builtin.toPrettyString() );
            }
            else
                throw new RuntimeException("Unknown atom type " + atom);
            
        	if( newList.isEmpty() )
        	    newList = newList.insert( newAtom );
        	else
        	    newList.add( newAtom );
            
//            newList = newList.add( newAtom );
        }
        
        return newList;
    }


    public List getVars() {
        List vars = new ArrayList();
        
        for( AtomList list = this; !list.isEmpty(); list = (AtomList) list.getRest() ) {
            Atom atom = (Atom) list.getFirst();
            for( int j = 0; j < atom.getArgumentCount(); j++ ) {
                SWRLObject term = atom.getArgument( j );
                if( term instanceof Variable )
                    vars.add( term );                
            }
        }
        return vars;
    }
    
    public String toString() {
        StringBuffer str = new StringBuffer("[");
        for(RDFList list = this; !list.isEmpty(); list = list.getRest()) {
            if(list != this)
                str.append(", ");
            str.append(list.getFirst().toString());
        }        
        str.append("]");
        return str.toString();
    }
    
    public RDFList remove() {
    	AtomList list = (AtomList) getRest();
        
        if (size() > 1) {        	
        	list.setFirst(getRest().getFirstValue());        	
        	list.setRest((AtomList) getRest().getRest());
        } else {   
        	list = (AtomList) vocabulary.nil().castTo(RDFList.class);        	        	        	
        }
        if (hasProperty(vocabulary.first()))
        	removeProperties(vocabulary.first());
        if (hasProperty(vocabulary.rest()))
        	removeProperties(vocabulary.rest());
        individual.delete();
        
        return list;
    }
        
    public RDFList removeAll() {
    	AtomList list = this;
    	while (list.size() > 0) 
    		list = (AtomList) list.remove();
    	return list;
    }
}
