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

package impl.owls.grounding;

import java.net.URL;

import org.mindswap.exceptions.ExecutionException;
import org.mindswap.owl.EntityFactory;
import org.mindswap.owl.OWLFactory;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLKnowledgeBase;
import org.mindswap.owls.OWLSFactory;
import org.mindswap.owls.grounding.AtomicGrounding;
import org.mindswap.owls.grounding.MessageMap;
import org.mindswap.owls.grounding.MessageMapList;
import org.mindswap.owls.grounding.UPnPAtomicGrounding;
import org.mindswap.owls.process.Parameter;
import org.mindswap.owls.vocabulary.FLAServiceOnt;
import org.mindswap.query.ValueMap;
import org.mindswap.utils.Utils;
import org.mindswap.utils.XSLTEngine;
import org.w3c.dom.Node;

import com.fujitsu.fla.upnp.Action;
import com.fujitsu.fla.upnp.Argument;
import com.fujitsu.fla.upnp.ArgumentList;
import com.fujitsu.fla.upnp.ControlPoint;
import com.fujitsu.fla.upnp.Device;
import com.fujitsu.fla.upnp.Service;
import com.fujitsu.fla.upnp.UPnPStatus;


/**
 * @author Evren Sirin
 *
 */
public class UPnPAtomicGroundingImpl extends AtomicGroundingImpl implements UPnPAtomicGrounding {
	public UPnPAtomicGroundingImpl(OWLIndividual ind) {
		super(ind);
	}
	
    public MessageMapList getInputMap() {
        return OWLSFactory.createMessageMapList(getProperties(FLAServiceOnt.upnpInputMapping));
    }

    public MessageMapList getOutputMap() {
        return OWLSFactory.createMessageMapList(getProperties(FLAServiceOnt.upnpOutputMapping));
    }
	
	/* (non-Javadoc)
	 * @see org.mindswap.owls.grounding.UPnPAtomicGrounding#setUPnPService(java.lang.String)
	 */
	public void setUPnPService(String service) {
	    setProperty(FLAServiceOnt.upnpServiceID, service);
	}

	/* (non-Javadoc)
	 * @see org.mindswap.owls.grounding.UPnPAtomicGrounding#getUPnPService()
	 */
	public String getUPnPService() {
		return getPropertyAsString(FLAServiceOnt.upnpServiceID);
	}

	/* (non-Javadoc)
	 * @see org.mindswap.owls.grounding.UPnPAtomicGrounding#setUPnPAction(java.lang.String)
	 */
	public void setUPnPAction(String action) {
	    setProperty(FLAServiceOnt.upnpCommand, action);
	}

	/* (non-Javadoc)
	 * @see org.mindswap.owls.grounding.UPnPAtomicGrounding#getUPnPAction()
	 */
	public String getUPnPAction() {
	    return getPropertyAsString(FLAServiceOnt.upnpCommand);
	}

	/* (non-Javadoc)
	 * @see org.mindswap.owls.grounding.AtomicGrounding#getDescriptionURL()
	 */
	public URL getDescriptionURL() {
		try {
			ControlPoint cp = new ControlPoint();
			Device device = cp.getProxyDevice(getUPnPDescription());
			String url = device.getPresentationURL();
			cp.stop();
			return new URL(url);
        } catch(Exception e) {
            return null;
        }			
	}

	/* (non-Javadoc)
	 * @see org.mindswap.owls.grounding.UPnPAtomicGrounding#setUPnPDescription(java.lang.String)
	 */
	public void setUPnPDescription(String description) {
	    setProperty(FLAServiceOnt.upnpDeviceURL, description);		
	}

	/* (non-Javadoc)
	 * @see org.mindswap.owls.grounding.UPnPAtomicGrounding#getUPnPDescription()
	 */
	public String getUPnPDescription() {
		return getPropertyAsString(FLAServiceOnt.upnpDeviceURL);
	}

	public ValueMap invoke(ValueMap values) {
	    return invoke(values, OWLFactory.createKB());
	}
	
	public ValueMap invoke(ValueMap values, OWLKnowledgeBase kb) {
	    ControlPoint cp = new ControlPoint();
		Device device = cp.getProxyDevice(getUPnPDescription());
		Service service = device.getService(getUPnPService());
		Action action = service.getAction(getUPnPAction()); 
				
		ValueMap results = new ValueMap();
		
		MessageMapList inputMapList = getInputMap();
		ArgumentList inArguments = action.getInputArgumentList();
		for(int i = 0; i < inArguments.size(); i++) {		
			Argument in = inArguments.getArgument(i);
			MessageMap mp = inputMapList.getMessageMap(in.getName());
			Parameter param = mp.getOWLSParameter();
			
			Object value = values.getValue(param);
			Object inputValue = value;
			
			System.out.println("value = " + value);
			if(mp.getTransformation() != null) {
				value = XSLTEngine.transform(value.toString(), mp.getTransformation());
				Node node  = Utils.getAsNode(value.toString());
				inputValue = node.getFirstChild().getNodeValue();
				System.out.println("input value = " + value);
			}				

			action.setArgumentValue(mp.getGroundingParameter().toString(), inputValue.toString());
		}
		
		boolean ctrlRes = action.postControlAction();
		if (ctrlRes == false) {
			UPnPStatus err = action.getControlStatus();
			throw new ExecutionException(err.getDescription() + " (" + Integer.toString(err.getCode()) + ")");
		}
	
		MessageMapList outputMapList = getOutputMap();
		ArgumentList outArguments = action.getOutputArgumentList();
		for(int i = 0; i < outArguments.size(); i++) {
			Argument out = outArguments.getArgument(i);
			MessageMap mp = outputMapList.getMessageMap(out.getName());
			
			if(mp == null) continue;
			
			Parameter param = mp.getOWLSParameter();

			Object outputValue = null;
						
			if(mp.getTransformation() == null) 
				outputValue = out.getValue();
			else 
				outputValue = XSLTEngine.transform(out.getValue().toString(), mp.getTransformation());			
			
			// FIXME UPnP
			if(param.getParamType().isDataType())
			    results.setValue(param, EntityFactory.createDataValue( outputValue ) );
			else 
			    results.setValue(param, kb.parseLiteral( outputValue.toString() ));			
		}
		
		return results;			
	}	
	

    /* (non-Javadoc)
     * @see org.mindswap.owls.grounding.AtomicGrounding#addMessageMap(org.mindswap.owls.process.Parameter, java.lang.String, java.lang.String)
     */
    public void addMessageMap(Parameter owlsParameter, String groundingParameter, String xsltTransformation) {
        MessageMap map = getOntology().createUPnPMessageMap();
        
	    map.setOWLSParameter(owlsParameter);
	    map.setGroundingParameter(groundingParameter);
	    
	    if(xsltTransformation != null && xsltTransformation.length() > 0)
	        map.setTransformation(xsltTransformation);
	    
	    addMessageMap(map);
    }
    
	@Override
	public String getGroundingType() {
		return AtomicGrounding.GROUNDING_UPNP;
	}
}