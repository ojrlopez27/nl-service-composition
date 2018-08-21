/*
 * Created on Oct 31, 2004
 */
package impl.owls.process.binding;

import org.mindswap.owl.OWLIndividual;
import org.mindswap.owls.process.Input;
import org.mindswap.owls.process.InputBinding;
import org.mindswap.owls.process.Parameter;
import org.mindswap.owls.vocabulary.OWLS;

/**
 * @author Evren Sirin
 */
public class InputBindingImpl extends BindingImpl implements InputBinding {
    public InputBindingImpl(OWLIndividual ind) {
        super(ind);
    }

    public void setParameter(Parameter param) {
        if( param == null )
            throw new NullPointerException( "Parameter in InputBinding is null!" );
        
        if( param instanceof Input )
            setProperty(OWLS.Process.toParam, param);
        else
            throw new IllegalArgumentException("Input Binding can only have Input parameters!");
    }
    
    public Parameter getParameter() {
         return getInput();
    }
    
    public Input getInput() {
        return (Input) getPropertyAs(OWLS.Process.toParam, Input.class);
    }

}
