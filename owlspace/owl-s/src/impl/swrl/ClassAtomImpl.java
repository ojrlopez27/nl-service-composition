/*
 * Created on Dec 28, 2004
 */
package impl.swrl;

import org.mindswap.owl.OWLClass;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.vocabulary.SWRL;
import org.mindswap.owls.vocabulary.OWLS;
import org.mindswap.query.ValueMap;
import org.mindswap.swrl.ClassAtom;
import org.mindswap.swrl.SWRLIndividualObject;
import org.mindswap.swrl.SWRLObject;
import org.mindswap.swrl.Variable;

/**
 * @author Evren Sirin
 *
 */
public class ClassAtomImpl extends AtomImpl implements ClassAtom {
    public ClassAtomImpl(OWLIndividual ind) { 
        super(ind);
    }

    public OWLClass getClassPredicate() {
        return (OWLClass) getPropertyAs(SWRL.classPredicate, OWLClass.class);
    }
    
    public void setClassPredicate(OWLClass c) {
        setProperty(SWRL.classPredicate, (OWLIndividual) c.castTo(OWLIndividual.class));
    }

    public SWRLIndividualObject getArgument1() {
        return (SWRLIndividualObject) getPropertyAs(SWRL.argument1, SWRLIndividualObject.class);
    }

    public int getArgumentCount() {
        return 1;
    }

    public SWRLObject getArgument(int index) {
        if( index > 0 )            
	        throw new IndexOutOfBoundsException("Illegal argument index: "+index+" for a ClassAtom");
        
        return getArgument1();
    }    

    public void setArgument(int index, SWRLObject term) {
        if( index > 0 )            
	        throw new IndexOutOfBoundsException("Illegal argument index: "+index+" for a ClassAtom");

        if(term instanceof SWRLIndividualObject)
            setArgument1((SWRLIndividualObject) term);
        else
            throw new IllegalArgumentException("ClassAtom argument should be a SWRLIndividualObject");
    } 
    
    public void setArgument1(SWRLIndividualObject obj) {
        setProperty(SWRL.argument1, obj);
    } 
    
    public String toString() {
        OWLClass classPredicate = getClassPredicate();
        if(classPredicate == null)
            return "Missing_class_predicate";
        return "(" + getArgument1() + " rdf:type " + classPredicate.getQName() + ")";
    }

	public void evaluate(ValueMap values) {
		OWLIndividual subject = getArgument1();
		if (subject.isType(OWLS.Process.Parameter)) 
			subject = values.getIndividualValue((Variable) subject);
					
		subject.addType(getClassPredicate());
	}
}
