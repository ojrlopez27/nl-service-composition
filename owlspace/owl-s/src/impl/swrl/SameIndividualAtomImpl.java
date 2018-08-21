/*
 * Created on Dec 28, 2004
 */
package impl.swrl;

import org.mindswap.owl.EntityFactory;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.vocabulary.OWL;
import org.mindswap.owl.vocabulary.SWRL;
import org.mindswap.owls.vocabulary.OWLS;
import org.mindswap.query.ValueMap;
import org.mindswap.swrl.SWRLIndividualObject;
import org.mindswap.swrl.SWRLObject;
import org.mindswap.swrl.SameIndividualAtom;
import org.mindswap.swrl.Variable;

/**
 * @author Evren Sirin
 *
 */
public class SameIndividualAtomImpl extends AtomImpl implements SameIndividualAtom {
    public SameIndividualAtomImpl(OWLIndividual ind) {
        super(ind); 
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

	    throw new IndexOutOfBoundsException("Illegal argument index: "+index+" for a SameIndividualAtom");
    }    
    
    public void setArgument(int index, SWRLObject obj) {
        if( index > 1 )            
	        throw new IndexOutOfBoundsException("Illegal argument index: "+index+" for a SameIndividualAtom");

        if(obj instanceof SWRLIndividualObject) {
            if( index == 0 )
                setArgument1((SWRLIndividualObject) obj);
            else
                setArgument2((SWRLIndividualObject) obj);
        }
        else
            throw new IllegalArgumentException("SameIndividualAtom argument should be a SWRLIndivdiualObject");
    } 
    
    public void setArgument1(SWRLIndividualObject obj) {
        setProperty(SWRL.argument1, obj);
    } 
    
    public void setArgument2(SWRLIndividualObject obj) {
        setProperty(SWRL.argument2, obj);
    }
    
    public String toString() {
        return "sameAs(" + getArgument1().debugString() + ", " + getArgument2().debugString() + ")";
    }

	public void evaluate(ValueMap values) {
		OWLIndividual ind1 = getArgument1();
		if (ind1.isType(OWLS.Process.Parameter)) 
			ind1 = values.getIndividualValue((Variable) ind1);
		
		OWLIndividual ind2 = getArgument2();
		if (ind2.isType(OWLS.Process.Parameter)) 
			ind2 = values.getIndividualValue((Variable) ind2);
					
		ind1.setProperty(EntityFactory.createObjectProperty(OWL.sameAs), ind2);
	}
}
