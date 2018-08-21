//The MIT License
//
//Copyright (c) 2003 Ron Alford, Mike Grove, Bijan Parsia, Evren Sirin
//
//Permission is hereby granted, free of charge, to any person obtaining a copy
//of this software and associated documentation files (the "Software"), to
//deal in the Software without restriction, including without limitation the
//rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
//sell copies of the Software, and to permit persons to whom the Software is
//furnished to do so, subject to the following conditions:
//
//The above copyright notice and this permission notice shall be included in
//all copies or substantial portions of the Software.
//
//THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
//IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
//FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
//AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
//LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
//FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
//IN THE SOFTWARE.

package org.mindswap.utils;


import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;


/*
 * Created on Sep 3, 2003
 *
 */

/**
 * A simple class to ease the the process of printing on console and printing an HTML output. By
 * setting one variable the format of the output printed is changed, e.g. HTML tags will not
 * be printed when the output is being printed to console. 
 * 
 * @author Evren Sirin
 *
 */
public class OutputFormatter {
	PrintWriter out = null;
	QNameProvider qnames;
	boolean formatHTML;

	public OutputFormatter() {
		this(false);
	}
		
	public OutputFormatter(boolean formatHTML) {
		this(System.out, formatHTML);
	}
	
	public OutputFormatter(OutputStream out, boolean formatHTML) {
		this(new PrintWriter(out), formatHTML);
	}
	
	public OutputFormatter(Writer out, boolean formatHTML) {
		this.out = (out instanceof PrintWriter) ? (PrintWriter) out : new PrintWriter(out);
		this.formatHTML = formatHTML;
		this.qnames = new QNameProvider();
	}
	
	public boolean isFormatHTML() {
		return formatHTML;
	}
	
	public void setFormatHTML(boolean formatHTML) {
	    this.formatHTML = formatHTML;
	}
	
	public PrintWriter getWriter() {
		return out;
	}
	public void flush() {
		out.flush();
	}
	public OutputFormatter printTab() {
	    if(formatHTML)
	        out.print("&nbsp;&nbsp;&nbsp;");
	    else
	        out.print("   ");	
		return this;
	}
	public OutputFormatter print(String s) {
		out.print(s);
		return this;
	}
	public OutputFormatter print(Object o) {
		out.print(o);
		return this;
	}
	public OutputFormatter print(boolean b) {
		out.print(b);
		return this;
	}
	public OutputFormatter println(Object o) {
		print(o);
		println();
		return this;
	}	
	public OutputFormatter println(String s) {
		out.print(s);
		println();		
		return this;
	}	
	public OutputFormatter println() {
		printHTML("<br>");
		out.println();	
		return this;
	}
	public OutputFormatter printParagraph() {
		if(formatHTML)
			out.println("<p>");
		else
			out.println();	
		return this;
	}
	public OutputFormatter printURI(String uri) {
	    String label = (qnames != null)
	    	? qnames.shortForm(uri)
	    	: URIUtils.getLocalName(uri);    
		    
	    if(formatHTML)
	        printLink(uri, label);
	    else
	        print(label);
	    
		return this;	
	}
	public OutputFormatter printLink(String uri) {
		if(formatHTML)
			printLink(uri, uri);		
		else
			out.print(uri);	
		
		return this;
	}
	public OutputFormatter printLink(String uri, String label) {
		if(formatHTML) {
			out.print("<a href=\"");
			out.print(uri);
			out.print("\">");
			out.print(label);
			out.print("</a>");
		}
		else
			out.print(label + " (" + uri + ")");
		
		return this;
	}
	public OutputFormatter printBold(String s) {
		return printInsideTag(s, "b");
	}	
	public OutputFormatter printItalic(String s) {
		return printInsideTag(s, "i");
	}	
	public OutputFormatter printInsideTag(String s, String tag) {
		if(formatHTML) {
			out.print("<"); out.print(tag); out.print(">");
			out.print(s);
			out.print("</"); out.print(tag); out.print(">");
		}
		else
			out.print(s);
		return this;	
	}
	/**
	 * Print an HTML tag that will be ignored if the output format is not
	 * HTML.
	 * 
	 * @param tag
	 * @return
	 */
	public OutputFormatter printHTML(String tag) {
		if(formatHTML) 
			out.print(tag);
		return this;	
	}
    /**
     * @return Returns the qnames.
     */
    public QNameProvider getQNames() {
        return qnames;
    }
    /**
     * @param qnames The qnames to set.
     */
    public void setQNames(QNameProvider qnames) {
        this.qnames = qnames;
    }
}
