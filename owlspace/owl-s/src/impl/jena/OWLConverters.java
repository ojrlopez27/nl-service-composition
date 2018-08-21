/*
 * Created on Dec 10, 2004
 */
package impl.jena;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.mindswap.exceptions.CastingException;
import org.mindswap.exceptions.ConversionException;
import org.mindswap.exceptions.NotImplementedException;
import org.mindswap.owl.OWLClass;
import org.mindswap.owl.OWLConfig;
import org.mindswap.owl.OWLDataProperty;
import org.mindswap.owl.OWLDataType;
import org.mindswap.owl.OWLEntity;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLObject;
import org.mindswap.owl.OWLObjectConverter;
import org.mindswap.owl.OWLObjectProperty;
import org.mindswap.owl.OWLProperty;
import org.mindswap.owl.OWLType;

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * @author Evren Sirin
 */
public class OWLConverters {
    public static Map getConverters() {
        Map converters = new HashMap();
        
        OWLObjectConverter objPropConverter = new ObjectPropertyConverter();
        OWLObjectConverter dataPropConverter = new DataPropertyConverter();
        OWLObjectConverter propConverter =
            new CombinedOWLConverter(OWLProperty.class, new OWLObjectConverter[] {
                objPropConverter, dataPropConverter});
        OWLObjectConverter classConverter = new ClassConverter();
        OWLObjectConverter dataTypeConverter = new DataTypeConverter();
        OWLObjectConverter typeConverter =
            new CombinedOWLConverter(OWLType.class, new OWLObjectConverter[] {
                classConverter, dataTypeConverter});
        OWLObjectConverter indConverter = new IndividualConverter();
        OWLObjectConverter entityConverter =
            new CombinedOWLConverter(OWLType.class, new OWLObjectConverter[] {
                classConverter, dataTypeConverter, objPropConverter, 
                dataPropConverter, indConverter});
        
        converters.put(OWLIndividual.class, indConverter);
        
        converters.put(OWLObjectProperty.class, objPropConverter);
        converters.put(OWLDataProperty.class, dataPropConverter);
        converters.put(OWLProperty.class, propConverter);
        
        converters.put(OWLClass.class, classConverter);
        converters.put(OWLDataType.class, dataTypeConverter);
        converters.put(OWLType.class, typeConverter);
        
        converters.put(OWLEntity.class, entityConverter);

        return converters;
	}
	
	public static class CombinedOWLConverter implements OWLObjectConverter {
	    OWLObjectConverter[] converters;
	    Class javaClass;
	    
	    public CombinedOWLConverter(Class javaClass, OWLObjectConverter[] converters) {
	        this.converters = converters;
	        this.javaClass = javaClass;
	    }
	    
		public boolean canCast(OWLObject object) {
		    boolean strict = OWLConfig.setStrictConversion(true);
		    for(int i = 0; i < converters.length; i++) {
		        if(converters[i].canCast(object)) {
		            OWLConfig.setStrictConversion(strict);
		            return true;
		        }
		    }
		    OWLConfig.setStrictConversion(strict);
		    
			return false;
		}
		
		public OWLObject cast(OWLObject object) {
		    boolean strict = OWLConfig.setStrictConversion(true);
		    for(int i = 0; i < converters.length; i++) {
		        if(converters[i].canCast(object)) {
		            OWLConfig.setStrictConversion(strict);
		            return converters[i].cast(object);		            
		        }
		    }
		    OWLConfig.setStrictConversion(strict);
		    
		    throw new CastingException("OWLObject " + object + " cannot be cast to " + javaClass);
		}
		
        public OWLObject convert(OWLObject object) {
            if(!canCast(object))
                throw new ConversionException("Cannot convert " + object + " to abstract class " + javaClass);
            
            return cast(object);
        }
	}

	
	public static class ObjectPropertyConverter implements OWLObjectConverter {
        public boolean canCast(OWLObject object) {
            if(object instanceof OWLEntity) {
                OWLEntity entity = (OWLEntity) object;
                URI uri = entity.getURI();
                return (entity.getKB().getObjectProperty(uri) != null) || !OWLConfig.getStrictConversion();
            }
            
            return false;
        }

        public OWLObject cast(OWLObject object) {
		    if(!canCast(object))
		        throw new ConversionException("OWLObject " + object + " cannot be converted to ObjectProperty");
		
		    OWLEntity entity = (OWLEntity) object;
		    Resource res = (Resource) entity.getImplementation();
		    Property prop = (Property) res.as(Property.class);
            return ((OWLModelImpl) entity.getKB()).wrapObjectProperty(prop, null);
        }

        public OWLObject convert(OWLObject object) {
            OWLEntity entity = (OWLEntity) object;
            URI uri = entity.getURI();
            return entity.getOntology().createObjectProperty(uri);
        }	    
	}
	
	public static class DataPropertyConverter implements OWLObjectConverter {
        public boolean canCast(OWLObject object) {
            if(object instanceof OWLEntity) {
                OWLEntity entity = (OWLEntity) object;
                URI uri = entity.getURI();
                return (entity.getKB().getDataProperty(uri) != null) || !OWLConfig.getStrictConversion();
            }
            
            return false;
        }

        public OWLObject cast(OWLObject object) {
		    if(!canCast(object))
		        throw new ConversionException("OWLObject " + object + " cannot be converted to DatatypeProperty");
		
		    OWLEntity entity = (OWLEntity) object;
		    Resource res = (Resource) entity.getImplementation();
		    Property prop = (Property) res.as(Property.class);
            return ((OWLModelImpl) entity.getKB()).wrapDataProperty(prop, null);
        }

        public OWLObject convert(OWLObject object) {
            OWLEntity entity = (OWLEntity) object;
            URI uri = entity.getURI();
            return entity.getOntology().createDataProperty(uri);
        }	    
	}

	public static class ClassConverter implements OWLObjectConverter {
        public boolean canCast(OWLObject object) {
            if(object instanceof OWLEntity) {
                OWLEntity entity = (OWLEntity) object;
                Resource res = (Resource) entity.getImplementation();
                return 
                	(((OWLModelImpl)entity.getKB()).getClass(res) != null) || 
                	!OWLConfig.getStrictConversion();
            }
            
            return false;
        }

        public OWLObject cast(OWLObject object) {
		    if(!canCast(object))
		        throw new ConversionException("OWLObject " + object + " cannot be converted to OWLClass");
		
		    OWLEntity entity = (OWLEntity) object;
		    Resource res = (Resource) entity.getImplementation();
            return ((OWLModelImpl) entity.getKB()).wrapClass(res, null);
        }

        public OWLObject convert(OWLObject object) {
            OWLEntity entity = (OWLEntity) object;
            URI uri = entity.getURI();
            return entity.getOntology().createClass(uri);
        }	    
	}
	
	public static class IndividualConverter implements OWLObjectConverter {
        public boolean canCast(OWLObject object) {
            if(object instanceof OWLEntity) {
                return true;
            }
            
            return false;
        }

        public OWLObject cast(OWLObject object) {
		    if(!canCast(object))
		        throw new ConversionException("OWLObject " + object + " cannot be converted to OWLIndividual");
		
            OWLEntity entity = (OWLEntity) object;
            URI uri = entity.getURI();
            OWLIndividual ind = entity.getKB().getIndividual(uri);
            if(ind == null)
                ind = entity.getKB().createIndividual( uri );
            return ind;
        }

        public OWLObject convert(OWLObject object) {
            OWLEntity entity = (OWLEntity) object;
            URI uri = entity.getURI();
            return entity.getOntology().createObjectProperty(uri);
        }	    
	}
	
	public static class DataTypeConverter implements OWLObjectConverter {
        public boolean canCast(OWLObject object) {
            if(object instanceof OWLEntity) {
                OWLEntity entity = (OWLEntity) object;
                URI uri = entity.getURI();
                return (entity.getKB().getDataType(uri) != null);
            }
            
            return false;
        }

        public OWLObject cast(OWLObject object) {
		    if(!canCast(object))
		        throw new ConversionException("OWLObject " + object + " cannot be converted to ObjectProperty");
		
            OWLEntity entity = (OWLEntity) object;
            URI uri = entity.getURI();
            return entity.getKB().getDataType(uri);
        }

        public OWLObject convert(OWLObject object) {
		    if(canCast(object))
		        cast(object);
		    
            throw new NotImplementedException();
        }
	}
}

