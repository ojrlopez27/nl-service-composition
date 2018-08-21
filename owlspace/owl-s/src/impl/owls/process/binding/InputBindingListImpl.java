/*
 * Created on Aug 30, 2004
 */
package impl.owls.process.binding;


import org.mindswap.owl.OWLIndividualList;
import org.mindswap.owls.process.InputBinding;
import org.mindswap.owls.process.InputBindingList;

/**
 * @author Evren Sirin
 */
public class InputBindingListImpl extends BindingListImpl implements InputBindingList {
    public InputBindingListImpl() {
    }
    
    public InputBindingListImpl(OWLIndividualList list) {
        super(list);
    }


    public InputBinding inputBindingAt(int index) {
        return (InputBinding) individualAt(index).castTo(InputBinding.class);
    }
}
