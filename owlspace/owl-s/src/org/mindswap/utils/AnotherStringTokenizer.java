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

public class AnotherStringTokenizer {
	String str;
	String delim;
	int start = 0;
	int stop = -1;
	
	public AnotherStringTokenizer(String s) {
		this(s, " ");
	}
	public AnotherStringTokenizer(String s, String d) {
		str = s;
		delim = d;
	}
	public String nextToken() {
		return nextToken(delim);
	}
	public String nextToken(String delim) {
		String ret = null;
		
		if(str == null)
			System.out.println("what");
						
		stop = str.indexOf(delim, start);
		
		if(stop != -1) {
			ret = str.substring(start, stop);
			start = stop + delim.length();
			if(start > str.length())
				start = str.length() - 1;
		}
		
		return ret;
	}
}
