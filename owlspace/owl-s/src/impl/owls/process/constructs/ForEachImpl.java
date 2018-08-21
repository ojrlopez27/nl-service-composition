/*
 * Created on Jan 4, 2005
 */
package impl.owls.process.constructs;

import org.mindswap.owl.OWLIndividual;
import org.mindswap.owls.process.ControlConstruct;
import org.mindswap.owls.process.ForEach;
import org.mindswap.owls.process.Local;
import org.mindswap.owls.process.Parameter;
import org.mindswap.owls.process.Perform;
import org.mindswap.owls.process.ValueOf;
import org.mindswap.owls.vocabulary.OWLS;

/**
 * @author Evren Sirin
 *
 */
public class ForEachImpl extends IterateImpl implements ForEach {
    public ForEachImpl(OWLIndividual ind) {
        super(ind);
    }

    public ValueOf getListValue() {
        return (ValueOf) getPropertyAs(OWLS.Process.theList, ValueOf.class);
    }

    public void setListValue(ValueOf value) {
        setProperty(OWLS.Process.theList, value);
    }
    	
    public Local getLoopVar() {
        return (Local) getPropertyAs(OWLS.Process.theLoopVar, Local.class);
    }

    public void setLoopVar(Local var) {
        setProperty(OWLS.Process.theLoopVar, var);
    }
    
	public ControlConstruct getComponent() {
	    return (ControlConstruct) getPropertyAs(OWLS.Process.iterateBody, ControlConstruct.class);	    
	}
	
    public void setComponent(ControlConstruct component) {
        setProperty(OWLS.Process.iterateBody, component);
    }
    
    public void setListValue(Perform perform, Parameter parameter) {
        ValueOf valueOf = getOntology().createValueOf();
        valueOf.setPerform(perform);
        valueOf.setParameter(parameter);
        
        setListValue(valueOf);
    }
	
    public String getConstructName() {
        return "For-Each";
    }
    
	public void removeComponent() {
		if (hasProperty(OWLS.Process.iterateBody))
			removeProperties(OWLS.Process.iterateBody);
	}
}
