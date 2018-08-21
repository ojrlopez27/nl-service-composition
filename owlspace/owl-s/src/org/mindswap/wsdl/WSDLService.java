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

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.wsdl.Binding;
import javax.wsdl.Message;
import javax.wsdl.Operation;
import javax.wsdl.Port;
import javax.wsdl.Service;
import javax.wsdl.extensions.soap.SOAPAddress;
import javax.xml.namespace.QName;

import org.apache.axis.client.Call;
import org.apache.axis.wsdl.gen.Parser;
import org.apache.axis.wsdl.symbolTable.BindingEntry;
import org.apache.axis.wsdl.symbolTable.Parameter;
import org.apache.axis.wsdl.symbolTable.Parameters;
import org.apache.axis.wsdl.symbolTable.ServiceEntry;
import org.apache.axis.wsdl.symbolTable.SymTabEntry;
import org.apache.axis.wsdl.symbolTable.SymbolTable;
import org.w3c.dom.Node;


/**
 * Represents a WSDL service
 * 
 * @author Evren Sirin
 */
public class WSDLService {
	public static boolean DEBUG = false;
	//private boolean USE_TCPMON = false;

    private Parser wsdlParser = null;
    private URI uri = null;
	
	private Map operations = new HashMap();
		
    public WSDLService(URI wsdlURL) throws Exception {
		uri = wsdlURL;

        // Start by reading in the WSDL using Parser
        wsdlParser = new Parser();
        if(DEBUG) System.out.println("Reading WSDL document from '" + wsdlURL + "'");
        wsdlParser.run(wsdlURL.toString());
        
		readOperations();        
    }

	static public WSDLService createService(String wsdlLoc) throws Exception {
	    return createService(URI.create(wsdlLoc));
	}
	
	static public WSDLService createService(URI wsdlLoc) throws Exception {
		return new WSDLService(wsdlLoc);
	} 	
	
	private String createURI(QName qname) {
	    return qname.getNamespaceURI() + "#" + qname.getLocalPart();
	}
	
	private String createURI(String localName) {
	    return uri + "#" + localName;
	}

	private void readOperations() {
		try {
        String serviceNS = null;
        String serviceName = null;
        String operationName = null;
        String portName = null;
        
        Service service = selectService(serviceNS, serviceName);
        org.apache.axis.client.Service dpf = 
        	new org.apache.axis.client.Service(wsdlParser, service.getQName());

        Port port = selectPort(service.getPorts(), portName);
        if (portName == null) {
            portName = port.getName();
        }
        Binding binding = port.getBinding();

        SymbolTable symbolTable = wsdlParser.getSymbolTable();
        BindingEntry bEntry = symbolTable.getBindingEntry(binding.getQName());
        Parameters parameters = null;
        Iterator i = bEntry.getParameters().keySet().iterator();

        while (i.hasNext()) {
            Operation o = (Operation) i.next();
            operationName = o.getName();

        	Call call = (Call) dpf.createCall(QName.valueOf(portName),
								                       QName.valueOf(operationName));
		    WSDLOperation op = new WSDLOperation(call);
		    op.setService(this);
		    
		    operations.put(operationName, op);

		    Message inputMessage = o.getInput().getMessage();
		    Message outputMessage = o.getOutput().getMessage();
		        		    
		    op.setOperationName(createURI(operationName));
		    op.setInputMessageName(createURI(inputMessage.getQName()));
		    op.setOutputMessageName(createURI(outputMessage.getQName()));
		    op.setPortName(createURI(port.getName()));	
		    
		    if(DEBUG) {
		    	System.out.println(" Operation : " + operationName);
		    	System.out.println(" Port      : " + portName + " -> " + op.getPortName());
		    	System.out.println(" Input Msg : " + inputMessage.getQName() + " -> " + op.getInputMessageName());
		    	System.out.println(" Output Msg: " + outputMessage.getQName() + " -> " + op.getOutputMessageName());
		    }		    
	
		    
		    if(o.getDocumentationElement() != null) {
		    	Node doc = o.getDocumentationElement().getFirstChild(); 
		    	if(doc != null)
		    		op.setDocumentation(doc.getNodeValue());
		    }
		    
            parameters = (Parameters) bEntry.getParameters().get(o);

	        // loop over parameters and set up in/out params
	        for (int j = 0; j < parameters.list.size(); ++j) {
	            Parameter p = (Parameter) parameters.list.get(j);
				String name = createURI(p.getName());
				QName type = p.getType().getQName();				
	
	            if (p.getMode() == Parameter.IN) {           // IN	                
					op.addInput(name, type);

					if(DEBUG) 
						System.out.println(" Input     : " + name + " " +  type);
	            } else if (p.getMode() == Parameter.OUT) {    // OUT
					op.addOutput(name, type);

					if(DEBUG) 
						System.out.println(" Output    : " + name + " " +  type);
	            } else if (p.getMode() == Parameter.INOUT) {    // INOUT
					op.addInput(name, type);
					op.addOutput(name, type);
					System.err.println("WARNING: A wsdl parameter is defined as INOUT is not tested yet");
					System.err.println("         Parameter = " + name);
					
					if(DEBUG)
						System.out.println(" InOut     : " + name + " " +  type);
	            }
	            
	        }
	
	        // set output type
	        if (parameters.returnParam != null) {
	        	Parameter p = parameters.returnParam;
				String name = createURI(p.getName());
				QName type = p.getType().getQName();
				
				op.addOutput(name, type);

				if(DEBUG) 
					System.out.println(" Return    : " + name + " " +  type);
	        }
	        
			if(DEBUG) {
				System.out.println(" Inputs    : " + op.getInputs().size());
				System.out.println(" Outputs   : " + op.getOutputs().size());
				System.out.println(" Document  : " + op.getDocumentation());
				System.out.println();
			}	        
        }

		} catch(Exception e) {
			System.err.println(e);
			e.printStackTrace();
		}
    }

	private Service selectService(String serviceNS, String serviceName)
            throws Exception {
        QName serviceQName = (((serviceNS != null)
                && (serviceName != null))
                ? new QName(serviceNS, serviceName)
                : null);
        ServiceEntry serviceEntry = (ServiceEntry) getSymTabEntry(serviceQName,
                                                                  ServiceEntry.class);
        return serviceEntry.getService();
    }

    private SymTabEntry getSymTabEntry(QName qname, Class cls) {
        HashMap map = wsdlParser.getSymbolTable().getHashMap();
        Iterator iterator = map.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            Vector v = (Vector) entry.getValue();

            if ((qname == null) || qname.equals(qname)) {
                for (int i = 0; i < v.size(); ++i) {
                    SymTabEntry symTabEntry = (SymTabEntry) v.elementAt(i);

                    if (cls.isInstance(symTabEntry)) {
                        return symTabEntry;
                    }
                }
            }
        }
        return null;
    }

    private Port selectPort(Map ports, String portName) throws Exception {
        Iterator valueIterator = ports.keySet().iterator();
        while (valueIterator.hasNext()) {
            String name = (String) valueIterator.next();

            if ((portName == null) || (portName.length() == 0)) {
                Port port = (Port) ports.get(name);
                List list = port.getExtensibilityElements();

                for (int i = 0; (list != null) && (i < list.size()); i++) {
                    Object obj = list.get(i);
                    if (obj instanceof SOAPAddress) {
                        return port;
                    }
                }
            } else if ((name != null) && name.equals(portName)) {
                return (Port) ports.get(name);
            }
        }
        return null;
    }
	
	public URI getFileURI() {
		return uri;
	}
	
	public List getOperations() {
		return new ArrayList(operations.values());
	}
	
	public WSDLOperation getOperation(String opName) {
		return (WSDLOperation) operations.get(opName);
	}
}
