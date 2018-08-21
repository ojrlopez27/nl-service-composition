/*
 * Created on Aug 30, 2004
 */
package impl.owls.process;

import impl.owl.WrappedIndividual;

import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLIndividualList;
import org.mindswap.owls.OWLSFactory;
import org.mindswap.owls.generic.expression.Expression;
import org.mindswap.owls.process.Condition;
import org.mindswap.owls.process.ConditionList;
import org.mindswap.owls.process.Output;
import org.mindswap.owls.process.OutputBinding;
import org.mindswap.owls.process.OutputBindingList;
import org.mindswap.owls.process.Parameter;
import org.mindswap.owls.process.ParameterList;
import org.mindswap.owls.process.ParameterValue;
import org.mindswap.owls.process.Perform;
import org.mindswap.owls.process.Result;
import org.mindswap.owls.process.ValueOf;
import org.mindswap.owls.vocabulary.OWLS;

/**
 * @author Evren Sirin
 */
public class ResultImpl extends WrappedIndividual implements Result {
    public ResultImpl(OWLIndividual ind) {
        super(ind);
    }

    public Expression getEffect() {
        return (Expression) getPropertyAs(OWLS.Process.hasEffect, Expression.class);
    }

    public OWLIndividualList getEffects() {
        return getPropertiesAs(OWLS.Process.hasEffect, Expression.class);
    }

    public void setEffect(Expression effect) {
        setProperty(OWLS.Process.hasEffect, effect);
    }

    public void addEffect(Expression effect) {
        addProperty(OWLS.Process.hasEffect, effect);
    }
    	
	public void addBinding(Output output, ParameterValue paramValue) {
	    OutputBinding binding = getOntology().createOutputBinding();
	    binding.setParameter(output);
	    binding.setValue(paramValue);
	    
	    addBinding(binding);
	}
	
	public void addBinding(Output output, Perform perform, Parameter param) {
	    ValueOf valueOf = getOntology().createValueOf();
	    valueOf.setPerform(perform);
	    valueOf.setParameter(param);
	    
	    addBinding(output, valueOf);	    
	}

    public void addBinding(OutputBinding binding) {
        addProperty(OWLS.Process.withOutput, binding);
    }

    public OutputBindingList getBindings() {
        return OWLSFactory.createOutputBindingList(getProperties(OWLS.Process.withOutput));
    }

    public ParameterList getParameters() {
	    return OWLSFactory.createParameterList(getProperties(OWLS.Process.hasResultVar));
    }

    public ConditionList getConditions() {
	    return OWLSFactory.createConditionList(getProperties(OWLS.Process.inCondition));
    }

    public Condition getCondition() {
        return (Condition) getPropertyAs(OWLS.Process.inCondition, Condition.class);
    }
    	
	public void setCondition(Condition condition) {
	    setProperty(OWLS.Process.inCondition, condition);
	}
	
	public void addCondition(Condition condition) {
	    addProperty(OWLS.Process.inCondition, condition);
	}
}
