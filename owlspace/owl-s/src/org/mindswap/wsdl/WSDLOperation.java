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

package org.mindswap.wsdl;


import java.util.Iterator;
import java.util.Vector;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;

import org.apache.axis.client.Call;
import org.apache.axis.constants.Style;
import org.apache.axis.message.SOAPBodyElement;
import org.apache.axis.message.SOAPEnvelope;
import org.mindswap.utils.URIUtils;
import org.mindswap.utils.Utils;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class WSDLOperation {
	public static boolean DEBUG = false;
		
	private WSDLService service = null;
	private Call call = null;
	private Vector inputs = new Vector();
	private Vector outputs = new Vector();
	private String operationName;
	private String inputMessageName;
	private String outputMessageName;
	private String portName;
	private String documentation;
	
	public WSDLOperation(Call c) {
		this.call = c;
	}  
	
	public WSDLParameter getInput(int i) { 
		return (WSDLParameter) getInputs().elementAt(i);
	}
	public WSDLParameter getInput(String name) { 
	    for(int i = 0; i < inputs.size(); i++) {
	        WSDLParameter in = (WSDLParameter) inputs.get(i);
	        String paramName = in.getName();
	        if(paramName.equals(name) || URIUtils.getLocalName(paramName).equals(name))
	            return in;
	    }
		
		return null;
	}
	public WSDLParameter getOutput(int i) { 
		return (WSDLParameter) getOutputs().elementAt(i);
	}	
	public WSDLParameter getOutput(String name) { 
	    for(int i = 0; i < outputs.size(); i++) {
	        WSDLParameter out = (WSDLParameter) outputs.get(i);
	        String paramName = out.getName();
	        if( URIUtils.relaxedMatch(paramName, name) )
	            return out;
	    }
		
		return null;
	}
	public Vector getInputs() {	return inputs;	}
	public Vector getOutputs() { return outputs; }
	void addInput(String name, QName type) { inputs.add(new WSDLParameter(name, type)); }
	void addOutput(String name, QName type) { outputs.add(new WSDLParameter(name, type)); }	
	
	public String getName() { return call.getOperationName().getLocalPart(); }
	
	public String getOperationName() { return operationName; }
	public void setOperationName(String s) { operationName = s; }
	public String getPortName() { return portName; }
	public void setPortName(String s) { portName = s; }
	public String getInputMessageName() { return inputMessageName; }
	public void setInputMessageName(String s) { inputMessageName = s; }
	public String getOutputMessageName() { return outputMessageName; }
	public void setOutputMessageName(String s) { outputMessageName = s; }
	public WSDLService getService() { return service; }
	public void setService(WSDLService s) { service = s; }
	public String getDocumentation() { return documentation; }
	public void setDocumentation(String s) { documentation = s; }	

	public String toString() {
		return getName();
	}
	
	public String getDescription() {
		String s = getName() + "(";
		
		for(Iterator i = inputs./*values().*/iterator(); i.hasNext(); ) {
			WSDLParameter param = (WSDLParameter) i.next();
			s += param.getName() + ":" + param.getType().getLocalPart();
			if(i.hasNext())	s += ", ";
		}					
		s += ") -> (";
			
		for(Iterator i = outputs./*values().*/iterator(); i.hasNext(); ) {
			WSDLParameter param = (WSDLParameter) i.next();
			s += param.getName() + ":" + param.getType().getLocalPart();
			if(i.hasNext())	s += ", ";
		}		
		s += ")";

		return s;
	}
	
	
	public void invoke() throws Exception {
		SOAPEnvelope request = createRequest();
							
		if(DEBUG) {	
			System.out.println("Invoke operation " + getDescription());
			System.out.println(request);
		}
			
		SOAPEnvelope reply = call.invoke(request);

		if(DEBUG)	
			System.out.println(Utils.formatXML(reply));
		
		// TODO Handle SOAPFault message
		
		
		processResult(reply);		
	}	
	
	private void processResult(SOAPEnvelope reply) throws SOAPException {	
		SOAPElement soapBody = reply.getBody();
		SOAPElement response = (SOAPElement) soapBody.getChildElements().next();
		Iterator messageParts = response.getChildElements();
		
		for(int i = 0; i < outputs.size(); i++) {
			WSDLParameter output = (WSDLParameter) outputs.elementAt(i);
			
			SOAPElement e = (SOAPElement) messageParts.next();	
			output.setTextValue(e.toString());
			
			if(DEBUG) {
				System.out.println("processResult " + e);
				System.out.println("getValue " + e.getValue());
				System.out.println("getType " + (e.getNodeType() == Node.ELEMENT_NODE));
				System.out.println("getValue is null? " + (e.getValue() == null));
				System.out.println("result has children? " + e.getChildElements().hasNext());
				if(e.getChildElements().hasNext()) {
				    Node child = (Node) e.getChildElements().next();
					System.out.println("result first child " + child);
				}
			}	 
						
			Iterator children = e.getChildElements();
			if( children.hasNext() ) {
			    Node child = (Node) e.getChildElements().next();
			    if( child.getNodeType() == Node.TEXT_NODE ) 
					output.setValue( child.toString() );
				else
					output.setValue( e.toString() );    
			}
			else
			    output.setValue( e.toString() );
		}			
	}

	
	private SOAPEnvelope createRequest() throws SOAPException {		
		String targetNamespace = call.getOperationName().getNamespaceURI();
		String opName = call.getOperationName().getLocalPart();		
		SOAPEnvelope envelope = new SOAPEnvelope();
		
		if(DEBUG) {
			System.out.println("SOAP Action = " +call.getSOAPActionURI());
			System.out.println("SOAP Action used = " + call.useSOAPAction());
		}
		
		envelope.addNamespaceDeclaration("xsi", WSDLConsts.xsiURI);
		envelope.addNamespaceDeclaration("xsd",WSDLConsts. xsdURI);
		//envelope.addNamespaceDeclaration("ns0", targetNamespace);
		
		String inputEncodingStyle = "http://schemas.xmlsoap.org/soap/encoding/";
		if(inputEncodingStyle != null) {
		  envelope.setEncodingStyle(inputEncodingStyle);
		  envelope.addAttribute(WSDLConsts.soapURI, "encodingStyle", inputEncodingStyle);
		}
		  
		// FIXME test this feature
		String nsOp = call.getOperationStyle().equals(Style.RPC) ? "u" : "";
		SOAPBodyElement soapBody = new SOAPBodyElement(
				envelope.createName(opName, nsOp, targetNamespace));
		envelope.addBodyElement(soapBody);
		
		for(Iterator i = inputs.iterator(); i.hasNext(); ) {
			WSDLParameter param = (WSDLParameter) i.next();
			Object paramValue = param.getValue();
			
			if(paramValue == null) continue;
					
			SOAPElement soapElement = soapBody.addChildElement(URIUtils.getLocalName(param.getName()), "");
			
			// TODO treat string, int, float differently
		   	if(paramValue instanceof Node) {
		   		if(DEBUG)
					System.out.println("Case 1");
		   		createSOAPElement(soapElement, (Node) paramValue);
		   		if(soapElement.getAttributeValue(WSDLConsts.xsiType) == null) {
		   			if(DEBUG) System.out.println("Case 1a");
			   		soapElement.addAttribute(WSDLConsts.xsiType, 
			   			"u:" + param.getType().getLocalPart());
			   	}
		   	}
		   	else {
		   		if(DEBUG)
					System.out.println("Case 2 " + param.getType());				
				soapElement.addAttribute(WSDLConsts.xsiType, "xsd:" + param.getType().getLocalPart());
		   		soapElement.addTextNode(paramValue.toString());
		   	}
			   				
		}
		
		return envelope;	
	}
	
	public void createSOAPElement(SOAPElement parent, Node node) throws SOAPException {
		int type = node.getNodeType();
        
		if(type == Node.TEXT_NODE) {
		   		if(DEBUG)
					System.out.println("Case 3");
			
			parent.addAttribute(WSDLConsts.xsiType, "xsd:string");
			parent.addTextNode(node.getNodeValue());        		
		}
        else if(type == Node.ELEMENT_NODE) {        
        	SOAPElement soapElement;
        	
        	if(!(node.getParentNode() instanceof org.w3c.dom.Document)) {
		   		if(DEBUG)
					System.out.println("Case 4");
        		
				soapElement = parent.addChildElement(node.getNodeName()); 
			}
			else {
		   		if(DEBUG)
					System.out.println("Case 5");
					
				soapElement = parent;				
			}

            //NamedNodeMap attrs = node.getAttributes();
                        
            NodeList children = node.getChildNodes();
            if (children != null) {
                int len = children.getLength();
                for (int i = 0; i < len; i++) {
                    createSOAPElement(soapElement, children.item(i));                    
                }
            }
        }
	}	
}
