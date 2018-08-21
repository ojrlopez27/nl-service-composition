/*
 * Created on Dec 28, 2004
 */
package impl.swrl;

import org.mindswap.owl.OWLDataProperty;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLValue;
import org.mindswap.owl.vocabulary.SWRL;
import org.mindswap.owls.vocabulary.OWLS;
import org.mindswap.query.ValueMap;
import org.mindswap.swrl.DataPropertyAtom;
import org.mindswap.swrl.SWRLDataObject;
import org.mindswap.swrl.SWRLDataValue;
import org.mindswap.swrl.SWRLDataVariable;
import org.mindswap.swrl.SWRLIndividualObject;
import org.mindswap.swrl.SWRLObject;
import org.mindswap.swrl.Variable;

/**
 * @author Evren Sirin
 *
 */
public class DataPropertyAtomImpl extends AtomImpl implements DataPropertyAtom {
    public DataPropertyAtomImpl(OWLIndividual ind) {
        super(ind); 
    }

    public OWLDataProperty getPropertyPredicate() {
        return (OWLDataProperty) getPropertyAs(SWRL.propertyPredicate, OWLDataProperty.class);
    }
    
    public void setPropertyPredicate(OWLDataProperty p) {
        setProperty(SWRL.propertyPredicate, (OWLIndividual) p.castTo(OWLIndividual.class));
    } 
    
    public SWRLIndividualObject getArgument1() {
        return (SWRLIndividualObject) getPropertyAs(SWRL.argument1, SWRLIndividualObject.class);
    }

    public SWRLDataObject getArgument2() {
        SWRLDataObject arg = (SWRLDataVariable) getPropertyAs(SWRL.argument2, SWRLDataVariable.class);
        if( arg == null )
            arg = (SWRLDataValue) getPropertyAs(SWRL._argument2, SWRLDataValue.class);
        
        return arg;
    }
    
    public int getArgumentCount() {
        return 2;
    }

    public SWRLObject getArgument(int index) {
        if( index == 0 )
            return getArgument1();

        if( index == 1 )
            return getArgument2();

	    throw new IndexOutOfBoundsException("Illegal argument index: "+index+" for a DataPropertyAtom");
    }  
    
    public void setArgument(int index, SWRLObject obj) {
        if( index > 1 )            
	        throw new IndexOutOfBoundsException("Illegal argument index: "+index+" for a DataPropertyAtom");

        
        if( index == 0 ) {
            if(obj instanceof SWRLIndividualObject) 
                setArgument1((SWRLIndividualObject) obj);
            else
                throw new IllegalArgumentException(
                    "First argument of a DataPropertyAtom should be an SWRLIndividiualObject");
        }
        else {
            if(obj instanceof SWRLDataObject) 
                setArgument2((SWRLDataObject) obj);
            else
                throw new IllegalArgumentException(
                    "Second argument of a DataPropertyAtom should be an SWRLDataObject");
        }        
    } 
    
    public void setArgument1(SWRLIndividualObject obj) {
        setProperty(SWRL.argument1, obj);
    } 
    
    public void setArgument2(SWRLDataObject obj) {
        if(obj.isDataValue())
            setProperty(SWRL._argument2, obj);
        else
            setProperty(SWRL.argument2, (SWRLDataVariable) obj);
    }
    
    public String toString() {
        return 
        	"(" + getArgument1() + " " + 
        	getPropertyPredicate().getQName() + 
        	" " + getArgument2() + ")";
    }

	public void evaluate(ValueMap values) {
		OWLIndividual subject = getArgument1();
		OWLValue object = getArgument2();
		if (subject.isType(OWLS.Process.Parameter)) {
			OWLIndividual value = values.getIndividualValue((Variable) subject);
			value.setProperty(getPropertyPredicate(), object);
		}
	}
}
