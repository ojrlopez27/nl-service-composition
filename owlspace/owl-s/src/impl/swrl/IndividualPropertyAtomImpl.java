/*
 * Created on Dec 28, 2004
 */
package impl.swrl;

import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLObjectProperty;
import org.mindswap.owl.vocabulary.SWRL;
import org.mindswap.owls.vocabulary.OWLS;
import org.mindswap.query.ValueMap;
import org.mindswap.swrl.IndividualPropertyAtom;
import org.mindswap.swrl.SWRLIndividualObject;
import org.mindswap.swrl.SWRLObject;
import org.mindswap.swrl.Variable;

/**
 * @author Evren Sirin
 *
 */
public class IndividualPropertyAtomImpl extends AtomImpl implements IndividualPropertyAtom {
    public IndividualPropertyAtomImpl(OWLIndividual ind) {
        super(ind); 
    }

    public OWLObjectProperty getPropertyPredicate() {
        return (OWLObjectProperty) getPropertyAs(SWRL.propertyPredicate, OWLObjectProperty.class);
    }
    
    public void setPropertyPredicate(OWLObjectProperty p) {
        setProperty(SWRL.propertyPredicate, (OWLIndividual) p.castTo(OWLIndividual.class));
    }    

    public SWRLIndividualObject getArgument1() {
        return (SWRLIndividualObject) getPropertyAs(SWRL.argument1, SWRLIndividualObject.class);
    }

    public SWRLIndividualObject getArgument2() {
        return (SWRLIndividualObject) getPropertyAs(SWRL.argument2, SWRLIndividualObject.class);
    }    

    public int getArgumentCount() {
        return 2;
    }

    public SWRLObject getArgument(int index) {
        if( index == 0 )
            return getArgument1();

        if( index == 1 )
            return getArgument2();

	    throw new IndexOutOfBoundsException("Illegal argument index: "+index+" for a IndividualPropertyAtom");
    }    
    
    public void setArgument(int index, SWRLObject obj) {
        if( index > 1 )            
	        throw new IndexOutOfBoundsException("Illegal argument index: "+index+" for a IndividualPropertyAtom");

        if(obj instanceof SWRLIndividualObject) {
            if( index == 0 )
                setArgument1((SWRLIndividualObject) obj);
            else
                setArgument2((SWRLIndividualObject) obj);
        }
        else
            throw new IllegalArgumentException("IndividualPropertyAtom argument should be a SWRLIndividualObject");
    } 
    
    public void setArgument1(SWRLIndividualObject obj) {
        setProperty(SWRL.argument1, obj);
    } 
    
    public void setArgument2(SWRLIndividualObject obj) {
        setProperty(SWRL.argument2, obj);
    }
    
    public String toString() {        
        return 
	    	"(" + getArgument1() + " " + 
	    	getPropertyPredicate().getQName() + 
	    	" " + getArgument2() + ")";        
    }

	public void evaluate(ValueMap values) {				
		OWLIndividual subject = getArgument1();
		OWLIndividual object = getArgument2();
		if (subject.isType(OWLS.Process.Parameter)) {
			OWLIndividual value = values.getIndividualValue((Variable) subject);
			value.setProperty(getPropertyPredicate(), object);
		}
	}
}
