//The MIT License
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
 * Created on Jan 7, 2005
 */
package impl.owls.profile;

import impl.owl.WrappedIndividual;

import java.net.URL;

import org.mindswap.owl.OWLIndividual;
import org.mindswap.owls.profile.Actor;
import org.mindswap.owls.vocabulary.OWLS;



/**
 * A wrapper around an OWLIndividual that defines utility functions to access 
 * contact information.
 *
 */
public class ActorImpl extends WrappedIndividual implements Actor {
    public ActorImpl(OWLIndividual ind) {
        super(ind);
    }

    public String getName() {
        return getPropertyAsString(OWLS.Actor.name);
    }
    
    public String getTitle() {
        return getPropertyAsString(OWLS.Actor.title);
    }
            
    public URL getURL() {
        return getPropertyAsURL(OWLS.Actor.webURL);
    }
    
    public String getEMail() {
        return getPropertyAsString(OWLS.Actor.email);
    }
    
    public String getPhone() {
        return getPropertyAsString(OWLS.Actor.phone);
    }
    
    public String getFax() {
        return getPropertyAsString(OWLS.Actor.fax);
    }
    
    public String getAddress() {
        return getPropertyAsString(OWLS.Actor.physicalAddress).trim();
    }       
    
    public void setName(String  name) {
        setProperty(OWLS.Actor.name, name);
    }
            
    public void setURL(URL url) {
        setProperty(OWLS.Actor.webURL, url);
    }
    
    public void setEMail(String email) {
        setProperty(OWLS.Actor.email, email);
    }
    
    public void setPhone(String  phone) {
        setProperty(OWLS.Actor.phone, phone);
    }
    
    public void getFax(String fax) {
        setProperty(OWLS.Actor.fax, fax);
    }
    
    public void setAddress(String address) {
        setProperty(OWLS.Actor.physicalAddress, address);
    } 
}