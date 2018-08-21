/*
 * Created on Aug 30, 2004
 */
package impl.owls.process.constructs;

import org.mindswap.owl.OWLIndividual;
import org.mindswap.owls.process.Condition;
import org.mindswap.owls.process.ControlConstruct;
import org.mindswap.owls.process.RepeatWhile;
import org.mindswap.owls.vocabulary.OWLS;

/**
 * @author Evren Sirin
 * @author Michael Dänzer (University of Zurich)
 */
public class RepeatWhileImpl extends IterateImpl implements RepeatWhile {  
    public RepeatWhileImpl(OWLIndividual ind) {
        super(ind);
    }
    
	public Condition getCondition() {
	    return (Condition) getPropertyAs(OWLS.Process.whileCondition, Condition.class);
	}
		
	public void setCondition(Condition condition) {
	    setProperty(OWLS.Process.whileCondition, condition);
	}
	
	public ControlConstruct getComponent() {
	    return (ControlConstruct) getPropertyAs(OWLS.Process.whileProcess, ControlConstruct.class);	    
	}
	
    public void setComponent(ControlConstruct component) {
        setProperty(OWLS.Process.whileProcess, component);
    }
	
    public String getConstructName() {
        return "Repeat-While";
    }
    
	public void removeComponent() {
		if (hasProperty(OWLS.Process.whileProcess))
			removeProperties(OWLS.Process.whileProcess);
	}
}
