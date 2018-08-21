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

package org.mindswap.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.mindswap.owl.vocabulary.RDF;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

public class Utils {
    public static boolean DEBUG = false;
    
    public static Object getHashtableKey(Hashtable h, Object value) {
		Iterator e = h.entrySet().iterator();
		while(e.hasNext()) {
		    Map.Entry entry = (Map.Entry) e.next();
		    
		    if(entry.getValue().equals(value))
		    	return entry.getKey();
		}    	
		
		return null;
    }
	
    
	public static void printTime(String msg) {
		System.out.println("Time: (" + System.currentTimeMillis() + ") " + msg);
	}    
	
	public static String toString(Object[] array) {
		String s = "[Array ";
		if(array != null && array.length > 0) {
			s += array[0];
			for(int i = 1; i < array.length; i++)
				s += "," + array[i];
		}
		s += "]";
		
		return s;
	}    
		
	public static Node getAsNode(String in) {
		try { 
			org.apache.xerces.parsers.DOMParser parser = 
	        	new org.apache.xerces.parsers.DOMParser();
	        
			parser.setErrorHandler(new ErrorHandler() {
				public void warning(SAXParseException exception) {
				    if(DEBUG) System.err.println(exception);
				}

				public void error(SAXParseException exception) {
				    if(DEBUG) System.err.println(exception);
				}

				public void fatalError(SAXParseException exception) {
				    if(DEBUG) System.err.println(exception);
				} 
			});
			
			parser.parse(new org.xml.sax.InputSource(
				new StringReader(in)));
			
			return parser.getDocument().getDocumentElement();		
		} catch(Exception e) {
			//System.out.println("Invalid XML " + in + " " + e);
			//e.printStackTrace();
		}
		
		return null;
	}
	
	public static boolean getBoolean(String str) {
		return (str == null) ? false :
				str.toLowerCase().equals("true") ||
				str.equals("1");
	}
		
	public static String toString(boolean b) {
		return b ? "true" : "false";
	}
		
	public static String readURL(URL fileURL) throws IOException {
		return readAll(new InputStreamReader(fileURL.openStream()));
	}
	
	public static String readFile(String fileName) throws FileNotFoundException, IOException {
		return readAll(new FileReader(fileName));
	}
	
	public static String readAll(Reader reader) throws IOException {
		StringBuffer buffer = new StringBuffer();
				
		BufferedReader in = new BufferedReader(reader);
		int ch;
		while ((ch = in.read()) > -1) {
			buffer.append((char)ch);
		}
		in.close();

		return buffer.toString();
	}		
	
	public static String formatXML( Node node ) {
        try {
	        StringWriter out = new StringWriter();
	        Document doc = node.getOwnerDocument();
	        OutputFormat format  = new OutputFormat( doc ); 
	        format.setIndenting( true );
	        format.setLineWidth( 0 );             
	        format.setPreserveSpace( false );
	        format.setOmitXMLDeclaration( false );
	        
	        XMLSerializer serial = new XMLSerializer( out, format );
            serial.asDOMSerializer();                            

            serial.serialize( doc );
            
            return out.toString();
        }
        catch( IOException e ) {
            System.err.println( "Problem serializing node " + e );
            
            return "Problem serializing node " + e;
        }	    	    
	}
	
	public static String formatRDF(String rdf) {
		Node node = getAsNode(rdf);
		
		if(node == null)
			return rdf;
		else
			return formatNode(node, " ").substring(System.getProperty("line.separator").length());
	}
	
	
	public static String formatNode(Node node, String indent) {
		String str = "";
		int type = node.getNodeType();
        
		if(type == Node.TEXT_NODE) {
			str = node.getNodeValue().trim();
		}
        else if(type == Node.ELEMENT_NODE) {    
        	if(!(node.getParentNode() instanceof org.w3c.dom.Document))    
        		str = System.getProperty("line.separator") + indent + node.getLocalName() + ": ";

            NodeList children = node.getChildNodes();
            int len = (children != null) ? children.getLength() : 0;
            
            for (int i = 0; i < len; i++) {
                str += formatNode(children.item(i), indent + "  ");
            }
            if(len == 0) {
            	Node rdfResource = node.getAttributes().getNamedItemNS(RDF.getURI().toString(), "resource");
            	
            	if(rdfResource instanceof Attr)
	            	str += URIUtils.getLocalName(((Attr)rdfResource).getValue());
            	            	
            }
        }
        
        return str;
	}	
}
