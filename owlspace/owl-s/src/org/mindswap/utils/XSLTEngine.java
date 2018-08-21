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

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Iterator;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.mindswap.owls.process.Parameter;
import org.mindswap.query.ValueMap;

public class XSLTEngine {
	final static boolean DEBUG = false;
	final static String header = "<?xml";
	
	public static String transform(String input, String xsltStylesheet) {
	    return transform(input, xsltStylesheet, new ValueMap() );
	}
	
	public static String transform(String input, String xsltStylesheet, ValueMap parameters) {
		String output = null;
		
		try { 
			StringWriter result = new StringWriter();
		
			if(DEBUG) {
				System.out.println("Input " + input);
				System.out.println("XSLT  " + (xsltStylesheet==null) + " " + 
					xsltStylesheet.length() + " " + xsltStylesheet);
			}
					
			if(xsltStylesheet == null)
				return input;
			
			xsltStylesheet = xsltStylesheet.trim();
			
			TransformerFactory tFactory = TransformerFactory.newInstance();

			Transformer transformer = tFactory.newTransformer(
				new StreamSource(new StringReader(xsltStylesheet)));
			for(Iterator i = parameters.getVariables().iterator(); i.hasNext();) {
                Parameter param = (Parameter) i.next();
                String value = parameters.getStringValue( param );
                transformer.setParameter( param.getLocalName(), value );
                transformer.setParameter( param.getURI().toString(), value );   
            }			
			
			transformer.transform(
				new StreamSource(new StringReader(input)), 
				new StreamResult(result));	
			
			output = result.toString().trim();
			
			if(output.startsWith(header)) {
				int split = output.indexOf('>') + 1;
				output = output.substring(split);
			}
				
			if(DEBUG)	
				System.out.println("Output " + output);	
		} catch(Exception e) {
			System.out.println("XSLT Engine cannot apply transformation " + e);
			System.out.println("Input " + input);
			System.out.println("XSLT  " + xsltStylesheet);
		}
		
		return output;
	}
}
