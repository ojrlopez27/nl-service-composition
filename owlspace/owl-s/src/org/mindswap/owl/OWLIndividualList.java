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
package org.mindswap.owl;

import java.net.URI;
import java.util.List;

/**
 * Represents a list of <code>OWLIndividual</code> objects. This interface shouldn't be confused with rdf:List structure.
 * The sole purpose of this interface is to wrap a {@link java.util.List List} class so that the code can
 * be written with less casting statements. 
 * 
 * 
 * @author Evren Sirin
 *
 */
public interface OWLIndividualList extends List {
	/**
	 * Return the OWLIndividual at the specified position in this list.
	 * 
	 * @param index
	 * @return
	 */
	public OWLIndividual individualAt(int index);
	
	/**
	 * Return the individual from the list that has the given URI
	 * 
	 * @param resourceURI
	 * @return
	 */
	public OWLIndividual getIndividual(URI resourceURI);
	
	/**
	 * Return the individual from the list that has the given local name. If there is 
	 * more than one individual with the same local name than any one of them is returned 
	 * 
	 * @param localName
	 * @return
	 */
	public OWLIndividual getIndividual(String localName);
}
