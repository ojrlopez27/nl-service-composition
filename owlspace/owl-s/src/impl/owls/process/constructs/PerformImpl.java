/*
 * Created on Aug 26, 2004
 */
package impl.owls.process.constructs;

import impl.owls.process.ProcessListImpl;

import java.util.ArrayList;
import java.util.List;

import org.mindswap.owl.OWLIndividual;
import org.mindswap.owls.OWLSFactory;
import org.mindswap.owls.process.BindingList;
import org.mindswap.owls.process.CompositeProcess;
import org.mindswap.owls.process.ControlConstruct;
import org.mindswap.owls.process.Input;
import org.mindswap.owls.process.InputBinding;
import org.mindswap.owls.process.InputBindingList;
import org.mindswap.owls.process.Parameter;
import org.mindswap.owls.process.ParameterValue;
import org.mindswap.owls.process.Perform;
import org.mindswap.owls.process.Process;
import org.mindswap.owls.process.ProcessList;
import org.mindswap.owls.process.ValueOf;
import org.mindswap.owls.vocabulary.OWLS;

/**
 * @author Evren Sirin
 */
public class PerformImpl extends ControlConstructImpl implements Perform {
	public PerformImpl(OWLIndividual ind) {
		super(ind);
	}
	
	public void addBinding(Input input, ParameterValue paramValue) {
	    InputBinding binding = getOntology().createInputBinding();
	    binding.setParameter(input);
	    binding.setValue(paramValue);
	    
	    addBinding(binding);
	}
	
	public void addBinding(Input input, Perform perform, Parameter param) {
	    ValueOf valueOf = getOntology().createValueOf();
	    valueOf.setPerform(perform);
	    valueOf.setParameter(param);
	    
	    addBinding(input, valueOf);	    
	}
	
	public void addBinding(InputBinding binding) {
	    addProperty(OWLS.Process.hasDataFrom, binding);
	}
	
	public InputBindingList getBindings() {
		return OWLSFactory.createInputBindingList(getProperties(OWLS.Process.hasDataFrom));
	}

	public InputBinding getBindingFor(Input input) {
	    BindingList bindings = getBindings();
	    return (bindings == null) ? null : (InputBinding) bindings.getBindingFor( input );
	}
	
	public Process getProcess() {
		Process process = (Process) getPropertyAs(OWLS.Process.process, Process.class);
		if (process != null)
			process.setPerform(this);
		return process;
	}

    public void setProcess(Process process) {
    	if (process != null)
    		process.setPerform(this);
        setProperty(OWLS.Process.process, process);       
    }

	public List getConstructs() {
	    return new ArrayList();
	}
	
	public ProcessList getAllProcesses( boolean recursive ) {
        Process process = getProcess(); 
		ProcessList list = new ProcessListImpl();
		list.add(process);
        if(recursive) {
            if( process instanceof CompositeProcess ) {
                ControlConstruct cc = ((CompositeProcess)process).getComposedOf();
                ProcessList processes = cc.getAllProcesses(recursive);
                list.addAll( processes );
            }
        }
        
		return list;
	}

    public String getConstructName() {
        return "Perform";
    }
 }
