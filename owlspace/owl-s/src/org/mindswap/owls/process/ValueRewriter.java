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
 * Created on Jan 21, 2004
 */
package org.mindswap.owls.process;

import java.net.URI;

/**
 * @author Evren sirin
 */
public interface ValueRewriter {
	/**
	 * Rewrites an individual which is an instance of the source concept as an 
	 * instance of a target concept (which is possibily defined in a different
	 * ontology) There are a lot of issues related with respect to syntax vs. 
	 * semantics.
	 *  
	 * String
	 * @param individual A string representation of RDF data for an individual
	 * @param sourceC The URI of the source concept
	 * @param targetC The URI of the target concept
	 * @return
	 */
	public String rewrite(String value, URI sourceC, URI targetC);		
}
