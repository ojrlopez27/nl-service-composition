/*
 * Created on May 13, 2004
 */
package org.mindswap.utils;

import java.net.URI;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import org.mindswap.owl.vocabulary.OWL;
import org.mindswap.owl.vocabulary.RDF;
import org.mindswap.owl.vocabulary.RDFS;
import org.mindswap.owl.vocabulary.XSD;

/**
 * A class to convert URI's to QNames. Borrowed from SWOOP code.
 * 
 * @author Evren Sirin
 */
public class QNameProvider {

    // stores a map of uri -> prefix
    Map uriToPrefix;

    Map prefixToUri;

    /**
     *  
     */
    public QNameProvider() {
        uriToPrefix = new Hashtable();
        prefixToUri = new Hashtable();

        // initialize it with standard stuff
        setMapping("owl", OWL.ns);
        setMapping("rdf", RDF.ns);
        setMapping("rdfs", RDFS.ns);
        setMapping("xsd", XSD.ns);
    }

    public static boolean isNameStartChar(char ch) {
        return (Character.isLetter(ch) || ch == '_');
    }

    public static boolean isNameChar(char ch) {
        return (isNameStartChar(ch) || Character.isDigit(ch) || ch == '.' || ch == '-');
    }

    public static int findNameStartIndex(String str) {
        char[] strChars = str.toCharArray();
        int nameStartIndex = -1;
        boolean foundNameChar = false;

        for(int strIndex = strChars.length - 1; strIndex >= 0; strIndex--) {
            char letter = strChars[strIndex];

            if(isNameStartChar(letter)) {
                nameStartIndex = strIndex;
                foundNameChar = true;
            }
            else if(foundNameChar && !isNameChar(letter)) {
                break;
            }
        }
        return nameStartIndex;
    }

    public static int findLastNameIndex(String str) {
        char[] strChars = str.toCharArray();
        int nameIndex = -1;

        for(int strIndex = strChars.length - 1; strIndex >= 0; strIndex--) {
            char letter = strChars[strIndex];
            if(isNameChar(letter)) {
                nameIndex = strIndex;
            }
            else {
                break;
            }
        }
        return nameIndex;
    }

    public static int findNextNonNameIndex(String str, int startIndex) {
        char[] strChars = str.toCharArray();
        int nameIndex = startIndex;
        for(nameIndex = startIndex; nameIndex < strChars.length; nameIndex++) {
            char letter = strChars[nameIndex];
            if(!isNameChar(letter)) {
                break;
            }
        }
        return nameIndex;
    }

    protected static String[] splitURI(String uriString) {
        int nameStart, prefixStart, prefixEnd;
        String base, prefix, name;
        String[] bpn = new String[3];

        nameStart = findLastNameIndex(uriString);
        if(nameStart < 0) {
            //System.out.println("Couldn't find name for "+uriString);
            return null;
        }
        name = uriString.substring(nameStart);
        if(nameStart == 0) {
            //System.out.println("Name starts at beginning");
            base = "";
            prefix = "a"; // Pick a unique prefix later
        }
        else {
            base = uriString.substring(0, nameStart);
            //System.out.println("Uri: "+ uri + " Base: " +base);
            prefixStart = findNameStartIndex(base);
            if(prefixStart < 0) {
                //System.out.println("Prefix < 0");
                prefix = "b"; // Pick a uniqe prefix later
            }
            else {
                prefixEnd = findNextNonNameIndex(base, prefixStart + 1);
                prefix = uriString.substring(prefixStart, prefixEnd);
            }
        }
        bpn[0] = base;
        bpn[1] = prefix;
        bpn[2] = name;

        return bpn;
    }

    public String getPrefix(String uri) {
        return (String) uriToPrefix.get(uri);
    }

    public String getURI(String prefix) {
        return (String) prefixToUri.get(prefix);
    }

    public void removePrefix(String prefix) {
        String uri = (String) prefixToUri.remove(prefix);
        if(uri != null)
            uriToPrefix.remove(uri);
    }
    
    public boolean setMapping(String prefix, String uri) {
        String currentUri = getURI(prefix);
        if(currentUri == null) {
            //System.out.println("Setting prefix "+prefix+": "+uri);
            prefixToUri.put(prefix, uri);
            uriToPrefix.put(uri, prefix);
            return true;
        }
        else if(currentUri == uri) {
            return true;
        }
        else {
            return false;
        }
    }

    public Set getPrefixSet() {
        return prefixToUri.keySet();
    }

    public Set getURISet() {
        return uriToPrefix.keySet();
    }

    public String shortForm(URI uri) {
        return shortForm(uri.toString());
    }

    public String shortForm(String uri) {
        //System.out.println("Shortform for " + uri);
        return shortForm(uri, true);
    }

    public String shortForm(String uri, boolean default_to_uri) {
        String[] bpn = splitURI(uri);
        String base, possible_prefix, prefix, name;
        String qname;

        if(bpn == null) {
            if(default_to_uri) {
                return uri;
            }
            else {
                return null;
            }
        }

        base = bpn[0];
        possible_prefix = bpn[1];
        name = bpn[2];

        if(possible_prefix.endsWith(".owl") || possible_prefix.endsWith(".rdf")
            || possible_prefix.endsWith(".xml"))
            possible_prefix = possible_prefix.substring(0, possible_prefix
                .length() - 4);

        if(Character.isLowerCase(possible_prefix.charAt(1)))
            possible_prefix = Character.toLowerCase(possible_prefix.charAt(0))
                + possible_prefix.substring(1, possible_prefix.length());

        prefix = getPrefix(base);
        if(prefix == null) {
            // Check prefix for uniqueness
            prefix = possible_prefix;
            int mod = 0;
            while(!setMapping(prefix, base)) {
                prefix = possible_prefix + mod;
                mod++;
            }
        }

        qname = prefix.length() == 0 ? name : prefix + ":" + name;
        return qname;
    }

    public String longForm(String qname) {
        String[] str = qname.split(":");
        String prefix = str[0];
        String localName = str[1];
        String uri = getURI(prefix);
        
        if(uri == null)
            throw new IllegalArgumentException("Prefix " + prefix + " is not known!");
        
        return  uri + localName;
    }
}