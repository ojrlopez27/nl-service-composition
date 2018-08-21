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
 * Created on Dec 28, 2003
 *
 */
package impl.owls.grounding;

import impl.owls.process.execution.ProcessExecutionEngineImpl;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import org.mindswap.exceptions.ExecutionException;
import org.mindswap.exceptions.ServiceNotAvailableException;
import org.mindswap.owl.EntityFactory;
import org.mindswap.owl.OWLFactory;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLIndividualList;
import org.mindswap.owl.OWLKnowledgeBase;
import org.mindswap.owl.OWLObjectProperty;
import org.mindswap.owl.OWLValue;
import org.mindswap.owls.OWLSFactory;
import org.mindswap.owls.grounding.AtomicGrounding;
import org.mindswap.owls.grounding.MessageMap;
import org.mindswap.owls.grounding.MessageMapList;
import org.mindswap.owls.grounding.WSDLAtomicGrounding;
import org.mindswap.owls.grounding.WSDLOperationRef;
import org.mindswap.owls.process.Input;
import org.mindswap.owls.process.Output;
import org.mindswap.owls.process.OutputList;
import org.mindswap.owls.process.Parameter;
import org.mindswap.owls.process.Process;
import org.mindswap.owls.vocabulary.OWLS;
import org.mindswap.query.ValueMap;
import org.mindswap.utils.URIUtils;
import org.mindswap.utils.Utils;
import org.mindswap.utils.XSLTEngine;
import org.mindswap.wsdl.WSDLOperation;
import org.mindswap.wsdl.WSDLParameter;
import org.mindswap.wsdl.WSDLService;

/**
 * @author Evren Sirin
 *
 */
public class WSDLAtomicGroundingImpl extends AtomicGroundingImpl implements WSDLAtomicGrounding {	
	public WSDLAtomicGroundingImpl(OWLIndividual ind) {
		super(ind);
	}

	/* (non-Javadoc)
	 * @see org.mindswap.owls.grounding.WSDLAtomicProcessGrounding#setWSDL(java.lang.String)
	 */
	public void setWSDL(URI wsdlLoc) {
	    setProperty(OWLS.Grounding.wsdlDocument, wsdlLoc);
	}

	/* (non-Javadoc)
	 * @see org.mindswap.owls.grounding.WSDLAtomicProcessGrounding#getWSDL()
	 */
	public URI getWSDL() {
	    return getPropertyAsURI(OWLS.Grounding.wsdlDocument);
	}

	/* (non-Javadoc)
	 * @see org.mindswap.owls.grounding.WSDLAtomicProcessGrounding#setOperation(java.lang.String)
	 */
	public void setOperation(URI operation) {		
		WSDLOperationRef opRef = getOperationRef();
		if(opRef == null) {
		    opRef = getOntology().createWSDLOperationRef();
		    setOperationRef(opRef);
		}
		opRef.setOperation(operation);
	}

	/* (non-Javadoc)
	 * @see org.mindswap.owls.grounding.WSDLAtomicProcessGrounding#getOperation()
	 */
	public URI getOperation() {
		return getOperationRef().getOperation();
	}

	/* (non-Javadoc)
	 * @see org.mindswap.owls.grounding.WSDLAtomicProcessGrounding#setPortType(java.lang.String)
	 */
	public void setPortType(URI port) {
		WSDLOperationRef opRef = getOperationRef();
		if(opRef == null) {
		    opRef = getOntology().createWSDLOperationRef();
		    setOperationRef(opRef);
		}
		opRef.setPortType(port);		
	}

	/* (non-Javadoc)
	 * @see org.mindswap.owls.grounding.WSDLAtomicProcessGrounding#getPortType()
	 */
	public URI getPortType() {
	    return getOperationRef().getPortType();
	}

	public ValueMap invoke(ValueMap values) throws ExecutionException {
	    return invoke( values, OWLFactory.createKB() );
	}
	
	public ValueMap invoke(ValueMap values, OWLKnowledgeBase env) throws ExecutionException {
		ValueMap results = new ValueMap();
		
		String operation = getOperation().toString();
		
		WSDLService s = null;
		try {
            URI wsdlLoc = getWSDL();
            s = WSDLService.createService( wsdlLoc );
        } catch(Exception e1) {
            throw new ServiceNotAvailableException( getProcess(), e1 );
        }
		WSDLOperation op = s.getOperation(operation);
		
		if(op == null) 
		    op = s.getOperation(URIUtils.getLocalName(operation));
		
		if(op == null) 
		    throw new ExecutionException("Operation " + operation + " cannot be found in the WSDL description");
		
		MessageMapList inputMapList = getInputMap();
		for(int i = 0; i < op.getInputs().size(); i++) {		
			WSDLParameter in = op.getInput(i);
			MessageMap mp = inputMapList.getMessageMap(in.getName());
			
			if(mp == null) continue;
			
			Parameter param = mp.getOWLSParameter();
					
			OWLValue value = values.getValue(param);
			if(value == null) {
			    value = param.getConstantValue();
			}
			if(value == null) 
			    throw new ExecutionException("Value of input parameter '" + param + "' is not set!");

			Object inputValue = value;	
			String xslt = mp.getTransformation();
			if(xslt != null) {
                if( value instanceof OWLIndividual ) {
    			    String rdf = ((OWLIndividual)value).toRDF();
    				String xsltResult = XSLTEngine.transform(rdf, xslt, values);
    				inputValue = Utils.getAsNode(xsltResult);
    				if(inputValue == null) 
    				    inputValue = xsltResult.trim();
                }
                else {
                    throw new ExecutionException("Cannot apply XSLT transformation to data value '" + value + "' for input parameter '" + param + "'");
                }
			}				

			in.setValue(inputValue);
		}
									
		try {
            op.invoke();
        } catch(Exception e) {
            e.printStackTrace();
            throw new ExecutionException( e.toString() );
        }

        
		Process process = getProcess();
		OutputList outputs = process.getOutputs();		    
        MessageMapList outputMapList = getOutputMap();
		for(int i = 0; i < outputs.size(); i++) {		
		    Parameter param = outputs.outputAt(i);
			MessageMap mp = outputMapList.getMessageMap(param);
			WSDLParameter out = op.getOutput(mp.getGroundingParameter());
			String xslt = mp.getTransformation();
			
			if( out == null )
				throw new ExecutionException("Parameter '" + mp.getGroundingParameter() + "' is not found in the WSDL file!");

			Object outputValue = out.getValue();

			if(outputValue == null) 
				throw new ExecutionException("Value of output parameter '" + out + "' is not set by the WSDL operation!");
			else if(xslt != null) { 
			    String xml = out.getTextValue();
                if( xml.startsWith( "<dwmlOut" ) )
                    xml = outputValue.toString();
                if( ProcessExecutionEngineImpl.DEBUG ) {
                    System.out.println( "XML: \n" + xml);
                    System.out.println( "XSL: \n" + xslt);
                }
				outputValue = XSLTEngine.transform(xml, xslt);
                if( ProcessExecutionEngineImpl.DEBUG )
                    System.out.println( "RDF: \n" + outputValue);
				if(outputValue == null) 
					throw new ExecutionException(
					    "An error occurred when applying xsltTransformtion to output parameter " +
					    "'" + param + "'");			
			}
			
			if(param.getParamType().isDataType())
			    results.setValue(param, EntityFactory.createDataValue( outputValue ) );
			else 
			    results.setValue(param, env.getBaseOntology().parseLiteral( outputValue.toString() ));
		}
		
		return results;
	}

	/* (non-Javadoc)
	 * @see org.mindswap.owls.grounding.AtomicProcessGrounding#getDescriptionURL()
	 */
	public URL getDescriptionURL() {
		try {
            return getWSDL().toURL();
        } catch(MalformedURLException e) {
            return null;
        }
	}

	/**
	 * @return Returns the inputMessage.
	 */
	public URI getInputMessage() {
	    return getPropertyAsURI(OWLS.Grounding.wsdlInputMessage);
	}

	/**
	 * @param inputMessage The inputMessage to set.
	 */
	public void setInputMessage(URI inputMessage) {
	    setProperty(OWLS.Grounding.wsdlInputMessage, inputMessage);
	}

	/**
	 * @return Returns the outputMessage.
	 */
	public URI getOutputMessage() {
	    return getPropertyAsURI(OWLS.Grounding.wsdlOutputMessage);
	}

	/**
	 * @param outputMessage The outputMessage to set.
	 */
	public void setOutputMessage(URI outputMessage) {
	    setProperty(OWLS.Grounding.wsdlOutputMessage, outputMessage);
	}

    /* (non-Javadoc)
     * @see org.mindswap.owls.grounding.AtomicGrounding#getInputMap()
     */
    public MessageMapList getInputMap() {
        return OWLSFactory.createMessageMapList(getProperties(OWLS.Grounding.wsdlInput));
    }

    /* (non-Javadoc)
     * @see org.mindswap.owls.grounding.AtomicGrounding#getOutputMap()
     */
    public MessageMapList getOutputMap() {
        return OWLSFactory.createMessageMapList(getProperties(OWLS.Grounding.wsdlOutput));
    }

    /* (non-Javadoc)
     * @see org.mindswap.owls.grounding.WSDLAtomicGrounding#getOperationRef()
     */
    public WSDLOperationRef getOperationRef() {
        return (WSDLOperationRef) getPropertyAs(OWLS.Grounding.wsdlOperation, WSDLOperationRef.class);
    }

    /* (non-Javadoc)
     * @see org.mindswap.owls.grounding.WSDLAtomicGrounding#setOperationRef(org.mindswap.owls.grounding.WSDLOperationRef)
     */
    public void setOperationRef(WSDLOperationRef operationRef) {
        setProperty(OWLS.Grounding.wsdlOperation, operationRef);
    }


    /* (non-Javadoc)
     * @see org.mindswap.owls.grounding.AtomicGrounding#addMessageMap(org.mindswap.owls.process.Parameter, java.lang.String, java.lang.String)
     */
    public void addMessageMap(Parameter owlsParameter, String groundingParameter, String xsltTransformation) {
        MessageMap map = null;
            
	    if(owlsParameter instanceof Input)
	        map = getOntology().createWSDLInputMessageMap();
	    else if(owlsParameter instanceof Output)
	        map = getOntology().createWSDLOutputMessageMap();
	    else
	        throw new IllegalArgumentException("OWL-S parameter in the MessageMap is neither an Input nor Output!");

	    map.setOWLSParameter(owlsParameter);
	    map.setGroundingParameter(groundingParameter);
	    
	    if(xsltTransformation != null && xsltTransformation.length() > 0)
	        map.setTransformation(xsltTransformation);	 
	    
	    addMessageMap(map);
    }
    
	@Override
	public String getGroundingType() {
		return AtomicGrounding.GROUNDING_WSDL;
	}
	
	public void removeOperationRef() {
		if (hasProperty(OWLS.Grounding.wsdlOperation)) {			
			WSDLOperationRef opRef = getOperationRef();
			opRef.removeOperation();
			opRef.removePortType();
			removeProperties(OWLS.Grounding.wsdlOperation);
			opRef.delete();
		}
	}
	
	public void removeMessageMaps(OWLObjectProperty prop) {
		if (hasProperty(prop)) {
			OWLIndividualList indList = getProperties(prop);
			for (int i = 0; i < indList.size(); i++) {
				OWLIndividual ind = indList.individualAt(i);
				if (ind.hasProperty(OWLS.Grounding.owlsParameter))
					ind.removeProperties(OWLS.Grounding.owlsParameter);
				if (ind.hasProperty(OWLS.Grounding.wsdlMessagePart))
					ind.removeProperties(OWLS.Grounding.wsdlMessagePart);
				
				removeProperty(prop, ind);
				ind.delete();				
			}
		}
	}
	
	private void removeAll() {		
		if (hasProperty(OWLS.Grounding.wsdlDocument))
			removeProperties(OWLS.Grounding.wsdlDocument);
		removeOperationRef();
		
		if (hasProperty(OWLS.Grounding.wsdlInputMessage))
			removeProperties(OWLS.Grounding.wsdlInputMessage);
		
		removeMessageMaps(OWLS.Grounding.wsdlInput);
		removeMessageMaps(OWLS.Grounding.wsdlOutput);
		
		if (hasProperty(OWLS.Grounding.wsdlOutputMessage))
			removeProperties(OWLS.Grounding.wsdlOutputMessage);	
	}
	
	@Override
	public void delete() {		
		removeAll();			
		
		super.delete();
	}
	
	public URI getWSDLParameter(Parameter parameter) {
		URI uri = getWSDLParameter(parameter, getInputMap());
		if (uri == null)
			uri = getWSDLParameter(parameter, getOutputMap());
		return uri;	
	}
	
	private URI getWSDLParameter(Parameter parameter, MessageMapList list) {
		for (int i = 0; i < list.size(); i++) {
			if (list.messageMapAt(i).getOWLSParameter().equals(parameter))
				return list.messageMapAt(i).getGroundingParameterAsURI();
		}
		return null;	
	}
}
