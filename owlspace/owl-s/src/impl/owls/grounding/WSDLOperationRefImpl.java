/*
 * Created on Dec 21, 2004
 */
package impl.owls.grounding;

import impl.owl.WrappedIndividual;

import java.net.URI;

import org.mindswap.owl.OWLIndividual;
import org.mindswap.owls.grounding.WSDLOperationRef;
import org.mindswap.owls.vocabulary.OWLS;

/**
 * @author evren
 * @author Michael Dänzer (University of Zurich)
 */
public class WSDLOperationRefImpl extends WrappedIndividual implements WSDLOperationRef {
    public WSDLOperationRefImpl(OWLIndividual ind) {
        super(ind);
    }

    public void setOperation(URI op) {
        setProperty(OWLS.Grounding.operation, op);
    }

    public URI getOperation() {
        return getPropertyAsURI(OWLS.Grounding.operation);
    }

    public void setPortType(URI port) {
    /*******************************************
	 ** modified by guang huang @2005-4-8**
	 ********************************************/
        setProperty(OWLS.Grounding.portType, port);
    /*******************************************
	 ** end by guang huang **
	 ********************************************/
    }

    public URI getPortType() {
        return getPropertyAsURI(OWLS.Grounding.portType);
    }

	public void removeOperation() {
		if (hasProperty(OWLS.Grounding.wsdlOperation))
			removeProperties(OWLS.Grounding.wsdlOperation);
	}

	public void removePortType() {
		if (hasProperty(OWLS.Grounding.portType))
			removeProperties(OWLS.Grounding.portType);
	}

	@Override
	public void delete() {
		removeOperation();
		removePortType();
		super.delete();
	}    
}
