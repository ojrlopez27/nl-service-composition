/*
 * Created on Jan 4, 2005
 */
package impl.owls.process.constructs;

import impl.owls.process.ProcessListImpl;

import java.util.ArrayList;
import java.util.List;

import org.mindswap.owl.OWLIndividual;
import org.mindswap.owls.OWLSFactory;
import org.mindswap.owls.process.Output;
import org.mindswap.owls.process.OutputBinding;
import org.mindswap.owls.process.OutputBindingList;
import org.mindswap.owls.process.Parameter;
import org.mindswap.owls.process.ParameterValue;
import org.mindswap.owls.process.Perform;
import org.mindswap.owls.process.ProcessList;
import org.mindswap.owls.process.Produce;
import org.mindswap.owls.process.ValueOf;
import org.mindswap.owls.vocabulary.OWLS;

/**
 * @author Evren Sirin
 *
 */
public class ProduceImpl extends ControlConstructImpl implements Produce {
    public ProduceImpl(OWLIndividual ind) {
        super(ind);
    }

    public OutputBinding getBinding() {
        return (OutputBinding) getPropertyAs(OWLS.Process.producedBinding, OutputBinding.class);
    }
    
    public OutputBindingList getBindings() {
        return OWLSFactory.createOutputBindingList(getProperties(OWLS.Process.producedBinding));
    }

    public void addBinding(OutputBinding binding) {
        addProperty(OWLS.Process.producedBinding, binding);
    }
    
    public void setBinding(OutputBinding binding) {
        setProperty(OWLS.Process.producedBinding, binding);
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
    
	public List getConstructs() {
	    return new ArrayList();
	}
    
	public ProcessList getAllProcesses(boolean recursive) {
		return new ProcessListImpl();
	}
	
    public String getConstructName() {
        return "Produce";
    }

}
