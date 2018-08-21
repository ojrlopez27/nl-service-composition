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
package org.mindswap.utils;

import java.net.URI;
import java.net.URISyntaxException;

import org.mindswap.exceptions.InvalidURIException;

/**
 * @author Evren Sirin
 *  
 */
public class URIUtils {
    public static String getLocalName( URI uri ) {
        return getLocalName( uri.toString() );
    }

    public static String getLocalName( String uri ) {
        int index = splitPos( uri );

        if( index == -1 ) return null;

        return uri.substring( index + 1 );
    }

    public static String getNameSpace( URI uri ) {
        return getNameSpace( uri.toString() );
    }

    public static String getNameSpace( String uri ) {
        int index = splitPos( uri );

        if( index == -1 ) return null;

        return uri.substring( 0, index + 1 );
    }

    private static int splitPos( String uri ) {
        int pos = uri.indexOf( "#" );

        if( pos == -1 ) pos = QNameProvider.findLastNameIndex( uri ) - 1;

        return pos;
    }

    public static URI createURI( String uri ) throws InvalidURIException {
        try {
            return new URI( uri.replaceAll( " ", "%20" ) );
        }
        catch( URISyntaxException e ) {
            throw new InvalidURIException( e.getMessage() );
        }
    }

    /**
     * Create a URI with the given baseURI and localName
     * 
     * @param baseURI
     * @param localName
     * @return
     * @throws InvalidURIException
     */
    public static URI createURI( URI baseURI, String localName ) throws InvalidURIException {
        try {
            return new URI( baseURI.getScheme(), baseURI.getSchemeSpecificPart(), localName );
        }
        catch( URISyntaxException e ) {
            throw new InvalidURIException( e.getMessage() );
        }
    }

    public static URI standardURI( URI uri ) throws InvalidURIException {
        if( uri.getFragment() != null ) {
            try {
                uri = new URI( uri.getScheme(), uri.getSchemeSpecificPart(), null );
            }
            catch( URISyntaxException e ) {
                throw new InvalidURIException( e.getMessage() );
            }
        }

        return uri;
    }

    public static boolean relaxedMatch( String uri1, String uri2 ) {
        if( uri1 == null || uri2 == null ) return false;

        if( uri1.equals( uri2 ) ) return true;

        String name1 = getLocalName( uri1 );
        String name2 = getLocalName( uri2 );

        if( name1 == null || name2 == null ) return false;

        return name1.equals( name2 );
    }
    
    public static boolean isValidURI(String uri) {
    	try {
    		URI.create(uri);
    		return true;
    	} catch (Exception e) {
    		return false;
    	}
    }
}
