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

package impl.owls.service;

import impl.owl.WrappedIndividual;

import org.mindswap.owl.OWLDataValueList;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owls.grounding.Grounding;
import org.mindswap.owls.process.Process;
import org.mindswap.owls.profile.Profile;
import org.mindswap.owls.service.Service;
import org.mindswap.owls.vocabulary.OWLS;

public class ServiceImpl extends WrappedIndividual implements Service {
	public ServiceImpl(OWLIndividual ind) {
		super(ind);
	}
	
	/**
	 * @return
	 */
	public Process getProcess() {
		return (Process) getPropertyAs(OWLS.Service.describedBy, Process.class);
	}

	/**
	 * @return
	 */
	public Profile getProfile() {
		return (Profile) getPropertyAs(OWLS.Service.presents, Profile.class);
	}

	/**
	 * @param process
	 */
	public void setProcess(Process process) {
		if(hasProperty(OWLS.Service.describedBy, process))
		    return;
		
		setProperty(OWLS.Service.describedBy, process);
		process.setService(this);
	}

	/**
	 * @param profile
	 */
	public void setProfile(Profile profile) {
		if(hasProperty(OWLS.Service.presents, profile))
		    return;
		
		setProperty(OWLS.Service.presents, profile);
		profile.setService(this);
	}
		
	/* (non-Javadoc)
	 * @see org.mindswap.owls.service.Service#getGrounding()
	 */
	public Grounding getGrounding() {
		return (Grounding) getPropertyAs(OWLS.Service.supports, Grounding.class);
	}
	
	/**
	 * @param grounding
	 */
	public void setGrounding(Grounding grounding) {
		if(hasProperty(OWLS.Service.supports, grounding))
		    return;
		
		setProperty(OWLS.Service.supports, grounding);
		grounding.setService(this);
	}


	public String debugString() {
		String str = "Service " + getLabel() + " " + getURI() + "\n";
		str += "Profile " + getProfile().debugString() + "\n";
		str += "Process " + getProcess().debugString() + "\n";
		
		return str;
	}

    /* (non-Javadoc)
     * @see org.mindswap.owls.service.Service#getName()
     */
    public String getName() {
        Profile profile = getProfile();
        return profile == null ? null : profile.getServiceName();
    }

    /* (non-Javadoc)
     * @see org.mindswap.owls.service.Service#getName(java.lang.String)
     */
    public String getName(String lang) {
        Profile profile = getProfile();
        return profile == null ? null : profile.getServiceName(lang);
    }

    public void setName(String name) {
        Profile profile = getProfile();
        if(profile != null) 
            profile.setServiceName(name);
    }
    
    /* (non-Javadoc)
     * @see org.mindswap.owls.service.Service#getNames()
     */
    public OWLDataValueList getNames() {
        Profile profile = getProfile();
        return profile == null ? null : profile.getServiceNames();
    }

	public void removeGrounding(Grounding grounding) {		
		if (hasProperty(OWLS.Service.supports, grounding))
			removeProperty(OWLS.Service.supports, grounding);			
	}

	public void removeProcess() {
		getProcess().removeService();
		
		if (hasProperty(OWLS.Service.describedBy))
			removeProperties(OWLS.Service.describedBy);		
	}

	public void removeProfile(Profile profile) {
		if (hasProperty(OWLS.Service.presents, profile))
			removeProperty(OWLS.Service.presents, profile);
		
		profile.removeService();
	}

	public void deleteGrounding(Grounding grounding) {		
		removeGrounding(grounding);
		grounding.delete();	
	}

	public void deleteProcess() {
		Process process = getProcess();
		removeProcess();
		process.delete();
	}

	public void deleteProfile(Profile profile) {
		removeProfile(profile);
		profile.delete();
	}


} 
