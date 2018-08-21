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
package impl.owls.process.parameter;

import impl.owl.WrappedIndividual;

import java.net.URI;

import org.mindswap.owl.EntityFactory;
import org.mindswap.owl.OWLDataValue;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLType;
import org.mindswap.owl.OWLValue;
import org.mindswap.owls.process.Parameter;
import org.mindswap.owls.process.Process;
import org.mindswap.owls.profile.Profile;
import org.mindswap.owls.service.Service;
import org.mindswap.owls.vocabulary.OWLS;
import org.mindswap.utils.RDFUtils;

/**
 * @author Evren Sirin
 *
 */
public abstract class ParameterImpl extends WrappedIndividual implements Parameter {
	public ParameterImpl(OWLIndividual ind) {
		super(ind);
	}

	public OWLType getParamType() {
	    URI typeURI = getPropertyAsURI(OWLS.Process.parameterType);
	    
		OWLType type = null;
		
		if(typeURI != null) {
		    type = getKB().getType(typeURI);
		
		    // if it is an unknown entity assume it is a OWLClass
			if(type == null)
			    type = EntityFactory.createClass(typeURI);
		}
		
		return type;
	}

	public void setParamType(OWLType type) {
		setProperty(OWLS.Process.parameterType, type.getURI());
	}

	/* (non-Javadoc)
	 * @see org.mindswap.owls.process.Parameter#getProcess()
	 */
	public Process getProcess() {
	    OWLIndividual ind = getIncomingProperty(OWLS.Process.hasParameter);
		return (ind == null) ? null : (Process) ind.castTo(Process.class);
	}

	/* (non-Javadoc)
	 * @see org.mindswap.owls.process.Parameter#getProcess(org.mindswap.owls.process.Process)
	 */
	public void setProcess(Process process) {
		process.addParameter(this);	
	}

	/* (non-Javadoc)
	 * @see org.mindswap.owls.process.Parameter#getService()
	 */
	public Service getService() {
	    Process process = getProcess();
		return (process == null) ? null : process.getService();
	}

	/* (non-Javadoc)
	 * @see org.mindswap.owls.process.Parameter#getProfile()
	 */
	public Profile getProfile() {		
	    OWLIndividual ind = getIncomingProperty(OWLS.Profile.hasInput);
		return (ind == null) ? null : (Profile) ind.castTo(Profile.class);
	}

	public String debugString() {
		String str = getLabel() + " " + getParamType() + "  " + getURI();
		
		return str;
	}
	
    /**
     * @return Returns the constantValue.
     */
    public OWLValue getConstantValue() {
	    OWLDataValue dataValue = getProperty(OWLS.Process.parameterValue);	    
		if(dataValue != null) {
	        OWLType paramType = getParamType();
	        OWLValue owlValue = null;
			if((paramType == null) || paramType.isDataType())
			    owlValue = dataValue;
			else {
			    String rdf = RDFUtils.addRDFTag( dataValue.getLexicalValue() );
			    owlValue = getOntology().parseLiteral( rdf );
			}
			
			return owlValue;
	    }
		
        return null;
    }
    
    /**
     * @param constantValue The constantValue to set.
     */
    public void setConstantValue(OWLValue value) {
        if( value.isDataValue() )
            setProperty(OWLS.Process.valueData, (OWLDataValue) value);
        else
            setProperty(OWLS.Process.valueData, ((OWLIndividual) value).toRDF(false));  
    }
}
