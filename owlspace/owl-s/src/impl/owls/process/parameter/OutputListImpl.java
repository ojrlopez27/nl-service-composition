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

import java.net.URI;

import org.mindswap.owl.OWLIndividualList;
import org.mindswap.owls.process.Output;
import org.mindswap.owls.process.OutputList;

/**
 * @author Evren Sirin
 *
 */
public class OutputListImpl extends ParameterListImpl implements OutputList {
    public OutputListImpl() {
        super(Output.class);
    }
    
    public OutputListImpl(OWLIndividualList list) {
        super(list, Output.class);
    }

    /* (non-Javadoc)
	 * @see org.mindswap.owls.process.ParameterList#parameterAt(int)
	 */
	public Output outputAt(int index) {
		return (Output) get(index);
	}

	/* (non-Javadoc)
	 * @see org.mindswap.owls.process.ParameterList#getParameter(java.lang.String)
	 */
	public Output getOutput(URI parameterURI) {
		return (Output) getIndividual(parameterURI);
	}
}
