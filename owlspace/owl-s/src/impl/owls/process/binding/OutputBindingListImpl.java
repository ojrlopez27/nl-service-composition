/*
 * Created on Aug 30, 2004
 */
package impl.owls.process.binding;


import org.mindswap.owl.OWLIndividualList;
import org.mindswap.owls.process.OutputBinding;
import org.mindswap.owls.process.OutputBindingList;

/**
 * @author Evren Sirin
 */
public class OutputBindingListImpl extends BindingListImpl implements OutputBindingList {
    public OutputBindingListImpl() {
    }
    
    public OutputBindingListImpl(OWLIndividualList list) {
        super(list);
    }


    public OutputBinding outputBindingAt(int index) {
         return (OutputBinding) individualAt(index).castTo(OutputBinding.class);
    }
}
