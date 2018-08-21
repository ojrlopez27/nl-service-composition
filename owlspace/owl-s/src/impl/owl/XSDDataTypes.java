/*
 * Created on Dec 14, 2004
 */
package impl.owl;

import impl.jena.OWLDataTypeImpl;

import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.mindswap.owl.OWLDataType;
import org.mindswap.owl.vocabulary.XSD;

/**
 * @author Evren Sirin
 */
// TODO move this class from impl to public api
public class XSDDataTypes {
    private static interface Interface {
	    public Set getDataTypes();

	    public Map getDataTypeMap();
	    
	    public OWLDataType getDataType(URI uri);
    }
   

    private static class Impl implements Interface{
	    private Map datatypes;
	    
	    private Impl() {
	        datatypes = new HashMap();
	        
			// register built-in primitive types
			datatypes.put(XSD.decimal, new OWLDataTypeImpl(XSD.decimal));
			datatypes.put(XSD.xsdString, new OWLDataTypeImpl(XSD.xsdString));
			datatypes.put(XSD.xsdBoolean, new OWLDataTypeImpl(XSD.xsdBoolean));
			datatypes.put(XSD.xsdFloat, new OWLDataTypeImpl(XSD.xsdFloat));
			datatypes.put(XSD.xsdDouble, new OWLDataTypeImpl(XSD.xsdDouble));
			datatypes.put(XSD.dateTime, new OWLDataTypeImpl(XSD.dateTime));
			datatypes.put(XSD.date, new OWLDataTypeImpl(XSD.date));
			datatypes.put(XSD.time, new OWLDataTypeImpl(XSD.time));
			datatypes.put(XSD.gYear, new OWLDataTypeImpl(XSD.gYear));
			datatypes.put(XSD.gMonth, new OWLDataTypeImpl(XSD.gMonth));
			datatypes.put(XSD.gDay, new OWLDataTypeImpl(XSD.gDay));
			datatypes.put(XSD.gYearMonth, new OWLDataTypeImpl(XSD.gYearMonth));
			datatypes.put(XSD.gMonthDay, new OWLDataTypeImpl(XSD.gMonthDay));
			datatypes.put(XSD.duration, new OWLDataTypeImpl(XSD.duration));
			datatypes.put(XSD.anyURI, new OWLDataTypeImpl(XSD.anyURI));

			// register built-in derived types
			datatypes.put(XSD.integer, new OWLDataTypeImpl(XSD.integer));
			datatypes.put(XSD.nonPositiveInteger, new OWLDataTypeImpl(XSD.nonPositiveInteger));
			datatypes.put(XSD.negativeInteger, new OWLDataTypeImpl(XSD.negativeInteger));
			datatypes.put(XSD.nonNegativeInteger, new OWLDataTypeImpl(XSD.nonNegativeInteger));
			datatypes.put(XSD.positiveInteger, new OWLDataTypeImpl(XSD.positiveInteger));
			datatypes.put(XSD.xsdLong, new OWLDataTypeImpl(XSD.xsdLong));
			datatypes.put(XSD.xsdInt, new OWLDataTypeImpl(XSD.xsdInt));
			datatypes.put(XSD.xsdShort, new OWLDataTypeImpl(XSD.xsdShort)); 
			datatypes.put(XSD.xsdByte, new OWLDataTypeImpl(XSD.xsdByte));
			datatypes.put(XSD.unsignedByte, new OWLDataTypeImpl(XSD.unsignedByte));
			datatypes.put(XSD.unsignedShort, new OWLDataTypeImpl(XSD.unsignedShort));
			datatypes.put(XSD.unsignedInt, new OWLDataTypeImpl(XSD.unsignedInt));
			datatypes.put(XSD.unsignedLong, new OWLDataTypeImpl(XSD.unsignedLong));

			datatypes.put(XSD.normalizedString, new OWLDataTypeImpl(XSD.normalizedString));
			datatypes.put(XSD.token, new OWLDataTypeImpl(XSD.token));
			datatypes.put(XSD.language, new OWLDataTypeImpl(XSD.language));
			datatypes.put(XSD.NMTOKEN, new OWLDataTypeImpl(XSD.NMTOKEN));
			datatypes.put(XSD.Name, new OWLDataTypeImpl(XSD.Name));
			datatypes.put(XSD.NCName, new OWLDataTypeImpl(XSD.NCName));	
	    }
	    
	    public Set getDataTypes() {
	        return new HashSet(datatypes.values());
	    }
	    
	    public Map getDataTypeMap() {
	        return Collections.unmodifiableMap(datatypes);
	    }
	    
	    public OWLDataType getDataType(URI uri) {
	        return (OWLDataType) datatypes.get(uri);
	    }
    }
    
    private static Impl instance = new Impl();
    
    public static Set getDataTypes() {
        return instance.getDataTypes();
    }
    
    public static Map getDataTypeMap() {
        return instance.getDataTypeMap();
    }
    
    public static OWLDataType getDataType(URI uri) {
        return instance.getDataType(uri);
    }
    
}
