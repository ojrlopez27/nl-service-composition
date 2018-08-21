// The MIT License
//
// Copyright (c) 2004 Evren Sirin
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to
// deal in the Software without restriction, including without limitation the
// rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
// sell copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
// FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
// IN THE SOFTWARE.

/*
 * Created on Dec 27, 2003
 *
 */
package impl.owls.grounding;

import impl.owl.WrappedIndividual;

import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLIndividualList;
import org.mindswap.owls.OWLSFactory;
import org.mindswap.owls.grounding.AtomicGrounding;
import org.mindswap.owls.grounding.AtomicGroundingList;
import org.mindswap.owls.grounding.Grounding;
import org.mindswap.owls.process.AtomicProcess;
import org.mindswap.owls.service.Service;
import org.mindswap.owls.vocabulary.FLAServiceOnt;
import org.mindswap.owls.vocabulary.OWLS;

/**
 * @author Evren Sirin
 *
 */
public class GroundingImpl extends WrappedIndividual implements Grounding {
	public GroundingImpl(OWLIndividual ind) {
		super(ind);
	}

	/* (non-Javadoc)
	 * @see org.mindswap.owls.grounding.Grounding#getGrounding(org.mindswap.owls.process.AtomicProcess)
	 */
	public AtomicGrounding getAtomicGrounding(AtomicProcess process) {
	    // TODO make this a RDQL query
	    OWLIndividualList list = getProperties(OWLS.Grounding.hasAtomicProcessGrounding);
	    for(int i = 0; i < list.size(); i++) {
            OWLIndividual ind = list.individualAt(i);
            if(ind.hasProperty(OWLS.Grounding.owlsProcess, process))
                return (AtomicGrounding) ind.castTo(AtomicGrounding.class);
        }
	    
	    return null;
	}

	/* (non-Javadoc)
	 * @see org.mindswap.owls.grounding.Grounding#getGrounding(org.mindswap.owls.process.AtomicProcess)
	 */
	public AtomicGroundingList getAtomicGroundings() {
	    OWLIndividualList list = getProperties(OWLS.Grounding.hasAtomicProcessGrounding);
	    list.addAll(getProperties(FLAServiceOnt.hasUPnPAtomicProcessGrounding));
//		return OWLSFactory.createAtomicGroundingList(getProperties(OWLS.Grounding.hasAtomicProcessGrounding));
	    return OWLSFactory.createAtomicGroundingList(list);
	}
	
	/* (non-Javadoc)
	 * @see org.mindswap.owls.grounding.Grounding#getGrounding(org.mindswap.owls.process.AtomicProcess)
	 */
	public AtomicGroundingList getAtomicGroundings(AtomicProcess process) {
	    AtomicGroundingList result = OWLSFactory.createAtomicGroundingList();
	    OWLIndividualList list = getProperties(OWLS.Grounding.hasAtomicProcessGrounding);
	    for(int i = 0; i < list.size(); i++) {
            OWLIndividual ind = list.individualAt(i);
            if(ind.hasProperty(OWLS.Grounding.owlsProcess, process))
                result.add(ind);
        }
	    
	    return result;
	}
	
	/* (non-Javadoc)
	 * @see org.mindswap.owls.grounding.Grounding#addGrounding(org.mindswap.owls.grounding.AtomicProcessGrounding)
	 */
	public void addGrounding(AtomicGrounding apg) {
	    addProperty(OWLS.Grounding.hasAtomicProcessGrounding, apg);
	}
	/**
	 * @return Returns the service.
	 */
	public Service getService() {
	    return (Service) getPropertyAs(OWLS.Service.supportedBy, Service.class);
	}

	/**
	 * @param service The service to set.
	 */
	public void setService(Service service) {
		if(hasProperty(OWLS.Service.supportedBy, service))
		    return;
		
		setProperty(OWLS.Service.supportedBy, service);
		service.setGrounding(this);
	}

	public void removeService() {
		if (hasProperty(OWLS.Service.supportedBy, getService()))
			removeProperty(OWLS.Service.supportedBy, getService());
	}

	public void deleteAtomicGroundings(AtomicProcess process) {
		AtomicGroundingList list = getAtomicGroundings();
		for (int index = 0; index < list.size(); index++) {
			if (list.groundingAt(index).hasProperty(OWLS.Grounding.owlsProcess, process))
				list.groundingAt(index).delete();
		}
	}

	public void removeAtomicGroundings(AtomicProcess process) {
		AtomicGroundingList list = getAtomicGroundings();
		for (int index = 0; index < list.size(); index++) {
			if (list.groundingAt(index).hasProperty(OWLS.Grounding.owlsProcess, process))
				removeProperty(OWLS.Grounding.hasAtomicProcessGrounding, list.groundingAt(index));
		}
	}

	public void deleteAtomicGroundings() {
		AtomicGroundingList list = getAtomicGroundings();
		removeAtomicGroundings();
		for (int index = 0; index < list.size(); index++) 
			list.individualAt(index).delete();
	}

	public void removeAtomicGroundings() {
		if (hasProperty(OWLS.Grounding.hasAtomicProcessGrounding))
			removeProperties(OWLS.Grounding.hasAtomicProcessGrounding);
	}

	@Override
	public void delete() {		
		deleteAtomicGroundings();
		super.delete();
	}
}
