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

import javax.xml.namespace.QName;

import org.apache.axis.message.PrefixedQName;

public interface WSDLConsts {
	public static final String xsdURI = "http://www.w3.org/2001/XMLSchema";
	public static final String xsiURI = "http://www.w3.org/2001/XMLSchema-instance";
	public static final String xslURI = "http://www.w3.org/1999/XSL/Transform";
	public static final String soapURI = "http://schemas.xmlsoap.org/soap/envelope/";
	public static final String soapEnc = "http://schemas.xmlsoap.org/soap/encoding/";
	
	public static final QName soapEncArray = new QName(soapEnc, "Array");
	
	// Axis 1.1 has a bug when prefix is specified so last param is empty string
	public static final PrefixedQName xsiType  = new PrefixedQName(xsiURI, "type", "xsi");
	public static final PrefixedQName xsiNull  = new PrefixedQName(xsiURI, "null", "xsi");
	public static final PrefixedQName arrType  = new PrefixedQName(soapEnc, "arrayType", "soapenv");					
	    	
}
