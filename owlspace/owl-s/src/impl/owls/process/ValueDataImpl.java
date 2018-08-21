/*
 * Created on Dec 30, 2004
 */
package impl.owls.process;

import impl.owl.OWLObjectImpl;
import impl.owls.process.binding.InputBindingImpl;
import impl.owls.process.binding.OutputBindingImpl;

import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLValue;
import org.mindswap.owls.process.Binding;
import org.mindswap.owls.process.InputBinding;
import org.mindswap.owls.process.ValueData;
import org.mindswap.owls.vocabulary.OWLS;

/**
 * @author Evren Sirin
 *
 */
public class ValueDataImpl extends OWLObjectImpl implements ValueData {
    private OWLValue value;
    
    public ValueDataImpl( OWLValue value ) {
        this.value = value;
    }
    
    public OWLValue getData() {
        return value;
    }

    public Object getImplementation() {
        return getData().getImplementation();
    }

	public Binding getEnclosingBinding() {	
		Binding binding = null;
		if (this instanceof OWLIndividual) {
			OWLIndividual ind = (OWLIndividual) this;
			OWLIndividual uncastedBinding = ind.getIncomingProperty(OWLS.Process.valueSource);
			
			if (uncastedBinding instanceof InputBinding)
				binding = new InputBindingImpl(ind.getIncomingProperty(OWLS.Process.valueSource));
			else
				binding = new OutputBindingImpl(ind.getIncomingProperty(OWLS.Process.valueSource));
		}
		return binding;
	}
}
