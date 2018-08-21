/*
 * Created on Oct 31, 2004
 */
package impl.owls.process.binding;

import org.mindswap.owl.OWLIndividual;
import org.mindswap.owls.process.Output;
import org.mindswap.owls.process.OutputBinding;
import org.mindswap.owls.process.Parameter;
import org.mindswap.owls.vocabulary.OWLS;

/**
 * @author Evren Sirin
 */
public class OutputBindingImpl extends BindingImpl implements OutputBinding {

    /**
     * @param resource
     */
    public OutputBindingImpl(OWLIndividual ind) {
        super(ind);
    }

    public void setParameter(Parameter param) {
        if(param instanceof Output)
            setProperty(OWLS.Process.toParam, param);
        else
            throw new IllegalArgumentException("Input Binding can only have Input parameters!");
    }
    
    
    public Parameter getParameter() {
         return getOutput();
    }
    
    public Output getOutput() {
        return (Output) getPropertyAs(OWLS.Process.toParam, Output.class);
    }

}
