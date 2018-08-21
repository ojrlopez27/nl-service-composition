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
 * Created on Jan 23, 2004
 */
package examples;

import java.io.PrintWriter;
import java.io.Writer;
import java.net.URI;

import org.mindswap.owl.OWLFactory;
import org.mindswap.owl.OWLKnowledgeBase;
import org.mindswap.owl.OWLOntology;

/**
 * 
 * Read a service description written in one version and print the description in another
 * version. 
 * 
 * 
 * @author Evren Sirin
 */
public class Translator {	
	public Translator() {		
	}
	
	public void translate(String fileURI, Writer out) throws Exception {
		URI uri = new URI(fileURI);
		
		OWLKnowledgeBase kb = OWLFactory.createKB();
		kb.setAutoTranslate(true);
		OWLOntology ont = kb.read(uri);
		
		ont.write(out);
	}

	public void run(String service) throws Exception {
		Translator translator = new Translator();		
		translator.translate(service, new PrintWriter(System.out));
	}
	
	public static void main(String[] args) throws Exception {
		if(args.length != 1) {
			if(args.length < 1)
				System.err.println("Not enough parameters");
			if(args.length > 1)
				System.err.println("Too many parameters");			
			System.err.println("usage: java OWLSVersionTranslator <serviceURI>");
			System.exit(0);		
		}
		
		Translator translator = new Translator();		
		translator.translate(args[0], new PrintWriter(System.out));
	}	
}
