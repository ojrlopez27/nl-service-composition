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
package impl.owls;

import org.mindswap.owl.OWLOntology;
import org.mindswap.owls.OWLSVersionTranslator;

/**
 * @author Evren Sirin
 *
 */
public class GenericVersionTranslator implements OWLSVersionTranslator {
	public static boolean DEBUG = false;

	public OWLSVersionTranslator[] translators = {
	    new OWLSTranslator_1_1(),
	    new OWLSTranslator_1_0()
	};

	public GenericVersionTranslator() {
	}
	
	public String getVersion() {
	    return "X.X";
	}
	
	
	public boolean canTranslate(OWLOntology ontology) {
	    return findTranslator(ontology) != null;
	}

	private OWLSVersionTranslator findTranslator(OWLOntology ont) {
		for(int i = 0; i < translators.length; i++) {
			if(translators[i].canTranslate(ont)) {
				return translators[i];
			}
		}			

		// by default do not translate
		return translators[0];
	}

    public OWLOntology translate(OWLOntology ontology) {
        OWLSVersionTranslator translator = findTranslator(ontology);
        
        return translator.translate(ontology);
    }
}
