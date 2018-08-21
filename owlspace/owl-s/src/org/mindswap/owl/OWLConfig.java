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
 * Created on Jan 15, 2004
 */
package org.mindswap.owl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.mindswap.owls.OWLSFactory;

/**
 * @author Evren Sirin
 */
public class OWLConfig {
	/**
	 * Default language is used to determine which literal will be chosen by the 
	 * getProperty(prop) function when there are multiple triples for the given 
	 * property. An empty string means the literals with no language identifier 
	 * will be chosen. A null value means the first found literal will be returned
	 * without looking at the language identifier. Note that even when a default
	 * language is chosen getProperty(prop, lang) can be used to get literal value
	 * for a certain identifier.
	 * 
	 * All the language identifiers in the list will be tried in the order given. 
	 * Original value for the list is
	 *     DEFAULT_LANGS = {"en", "", "en", null};
	 * The ordering here works as follows: 
	 * 1) Try the default language (which is English at the beginning but can be 
	 *    changed any time with setDefaultLanguage() function
	 * 2) Try to find literal with no language identifier
	 * 3) Try English literal
	 * 4) Try literal with any language identifier
	 * 
	 * See http://www.w3.org/TR/REC-xml#sec-lang-tag for language identifier specs  
	 */
	public static String[] DEFAULT_LANGS = {"en", "", "en", null};
	
	public static List DEFAULT_LANG_LIST = Arrays.asList(DEFAULT_LANGS);

	/**
	 * Sets the default language that will be tried first when a literal property
	 * value is searched. Note that this function only sets the first language
	 * but there can be multiple languages defined as DEFAULT_LANGS. 
	 * 
	 * @param lang
	 */
	public static void setDefaultLanguage(String lang) {
		DEFAULT_LANGS[0] = lang;
	}
	
	/**
	 * Sets all the languages that will be tried when a literal property values is 
	 * searched. All the languages in the list wil lbe tried in the order given
	 * 
	 * @param langs
	 */
	public static void setDefaultLanguages(String[] langs) {
		DEFAULT_LANGS = langs;
	}
	
	
	private static Map converters = getDefaultConverters();
	private static boolean strictConversion = true;
	
    private static Map getDefaultConverters() {
        Map converters = new HashMap();
        converters.putAll(OWLFactory.getDefaultConverters());
        converters.putAll(OWLSFactory.getDefaultConverters());
        return converters;
    }
    
	public static void addConverter(Class javaClass, OWLObjectConverter converter) {
	    converters.put(javaClass ,converter);
	}

    public static OWLObjectConverter getConverter(Class javaClass) {
		return (OWLObjectConverter) converters.get(javaClass);
	}
    
    public static boolean setStrictConversion(boolean strict) {
        boolean old = strictConversion;
        strictConversion = strict;
        
        return old;
    }
    
    public static boolean getStrictConversion() {
        return strictConversion;
    }
    
	private static Map transformators = new Hashtable();	
    
	public static void addTransformator(OWLClass owlClass, OWLTransformator oWLTransformator) {
		transformators.put(owlClass, oWLTransformator);
	}

    public static OWLTransformator getTransformator(OWLClass owlClass) {
		return (OWLTransformator) transformators.get(owlClass);
	}
}
