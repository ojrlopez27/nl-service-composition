/*
 * Created on 13.04.2005
 */
package impl.owls.grounding;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;

import org.mindswap.exceptions.ExecutionException;
import org.mindswap.owl.EntityFactory;
import org.mindswap.owl.OWLDataValue;
import org.mindswap.owl.OWLFactory;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLIndividualList;
import org.mindswap.owl.OWLKnowledgeBase;
import org.mindswap.owl.OWLObject;
import org.mindswap.owl.OWLValue;
import org.mindswap.owls.grounding.AtomicGrounding;
import org.mindswap.owls.grounding.JavaAtomicGrounding;
import org.mindswap.owls.grounding.JavaParameter;
import org.mindswap.owls.grounding.JavaVariable;
import org.mindswap.owls.grounding.MessageMapList;
import org.mindswap.owls.process.Input;
import org.mindswap.owls.process.Output;
import org.mindswap.owls.process.Parameter;
import org.mindswap.owls.vocabulary.MoreGroundings;
import org.mindswap.owls.vocabulary.OWLS;
import org.mindswap.query.ValueMap;
import org.mindswap.utils.ReflectionUtils;
import org.mindswap.utils.URIUtils;

/**
 * A JavaAtomicGrounding grounds an OWL-S Service to a Java method invocation. The method call
 * is specified by its method signature in an OWL-S Ontology. The driving parts are:
 * <ul>
 * 	<li>fully qualified class name</li>
 * 	<li>method name</li>
 * 	<li>a map of all input parameters (at the time only primitive datatypes and their adapter classes)</li>
 * 	<li>an output type (at the time only primitive datatypes and their adapter classes)</li>
 * </ul>
 * 
 * @author Michael Daenzer, University of Zürich
 * 
 * @see <a href="http://www.ifi.unizh.ch/ddis/ont/owl_s/MoreGroundings.owl">Grounding Ontology</a>
 * @see org.mindswap.owls.grounding.AtomicGrounding
 * @see org.mindswap.owls.vocabulary.NextOnt
 * @see impl.owls.grounding.AtomicGroundingImpl
 */
public class JavaAtomicGroundingImpl extends AtomicGroundingImpl implements JavaAtomicGrounding {   
    /**
     * @param ind
     */
    public JavaAtomicGroundingImpl(OWLIndividual ind) {
        super(ind);
    }

    /* (non-Javadoc)
     * @see org.mindswap.owls.grounding.JavaAtomicGrounding#setClaz(java.lang.String)
     */
    public void setClaz(String claz) {
        setProperty(MoreGroundings.javaClass, claz);
    }

    /* (non-Javadoc)
     * @see org.mindswap.owls.grounding.JavaAtomicGrounding#getClaz()
     */
    public String getClaz() {
        return getPropertyAsString(MoreGroundings.javaClass);
    }

    /* (non-Javadoc)
     * @see org.mindswap.owls.grounding.JavaAtomicGrounding#setMethod(java.lang.String)
     */
    public void setMethod(String method) { 
        setProperty(MoreGroundings.javaMethod, method);
    }

    /* (non-Javadoc)
     * @see org.mindswap.owls.grounding.JavaAtomicGrounding#getMethod()
     */
    public String getMethod() {
        return getPropertyAsString(MoreGroundings.javaMethod);
    }

    /* (non-Javadoc)
     * @see org.mindswap.owls.grounding.AtomicGrounding#getDescriptionURL()
     */
    public URL getDescriptionURL() {
        return null;
    }

    /* (non-Javadoc)
     * @see org.mindswap.owls.grounding.AtomicGrounding#addMessageMap(org.mindswap.owls.process.Parameter, java.lang.String, java.lang.String)
     */
    public void addMessageMap(Parameter owlsParameter,
            String groundingParameter, String xsltTransformation) {
    }

    /* (non-Javadoc)
     * @see org.mindswap.owls.grounding.AtomicGrounding#getInputMap()
     */
    public MessageMapList getInputMap() {
        return null;
    }

    /* (non-Javadoc)
     * @see org.mindswap.owls.grounding.AtomicGrounding#getOutputMap()
     */
    public MessageMapList getOutputMap() {
        return null;
    }

    /* (non-Javadoc)
     * @see org.mindswap.owls.grounding.AtomicGrounding#invoke(org.mindswap.query.ValueMap)
     */
    public ValueMap invoke(ValueMap values) throws ExecutionException {
        return invoke(values, OWLFactory.createKB());
    }

    /* (non-Javadoc)
     * @see org.mindswap.owls.grounding.AtomicGrounding#invoke(org.mindswap.query.ValueMap, org.mindswap.owl.OWLKnowledgeBase)
     */
    public ValueMap invoke(ValueMap values, OWLKnowledgeBase kb) throws ExecutionException {       
        Class claz = null;
        Method method = null;
        Class params[] = null;
        Object paramValues[] = null;
        ValueMap results = new ValueMap();
        Object result = null;
        
        // get and check class
        String strClaz = getClaz();
        if ((strClaz == null) || (strClaz.equals("")))
            throw new ExecutionException("JavaAtomicGrounding: No Java Class defined in Grounding!");
        // get and check method
        String strMethod = getMethod();
        if ((strMethod == null) || (strMethod.equals("")))
            throw new ExecutionException("JavaAtomicGrounding: No Java Method defined in Grounding!");        

        // prepare parameters
        int paramSize = getInputParameters().size();
        params = new Class[paramSize];
        for (int i = 0; i < paramSize; i++) 
        	params[i] = getJavaParameterTypeAt(i);
        
        // get class and method reference
        try {
            claz = Class.forName(strClaz);
            method = claz.getDeclaredMethod(strMethod, params);
        } catch (ClassNotFoundException e){
            throw new ExecutionException("JavaAtomicGrounding: Class " + strClaz + " defined in Grounding not found." );
        } catch (NoSuchMethodException e) {
        	throw new ExecutionException("JavaAtomicGrounding: Method " + strMethod + " defined in Grounding not found." );
        } catch (Exception e) {
        	throw new ExecutionException("JavaAtomicGrounding: " + e.getClass().toString() + " ocurred: " + e.getMessage());
        }      
        
        // prepare inputs
        paramValues = new Object[paramSize];
        for (int i = 0; i < params.length; i++) {
        	
        	OWLValue owlValue = values.getValue(getOWLSParameterAt(i));
        	if (owlValue.isDataValue())        		
        		paramValues[i] = ReflectionUtils.getCastedObjectFromStringValue(((OWLDataValue) owlValue).getLexicalValue(), params[i]);
            else if (owlValue.isIndividual()) {
                paramValues[i] = getJavaParameterAt(i, (OWLIndividual) owlValue);
            }
            //System.out.println( "Parameter " + i + " " + paramValues[i] );
        }
        
        // invoke java method
        try {
            Object obj = claz.newInstance();
            if (method.getReturnType().toString().equalsIgnoreCase("void"))
            	method.invoke(obj, paramValues);
            else
            	result = method.invoke(obj, paramValues);
        } catch (InvocationTargetException e) {
        	throw new ExecutionException("JavaAtomicGrounding: Error in executed method\n" + e.getTargetException().toString() + " ocurred: ");
        } catch (Exception e) {
        	throw new ExecutionException("JavaAtomicGrounding: Error while executing method\n" + e.getClass().toString() + " ocurred: ");
        }
        
        // set output
        if (result != null) {
            Parameter param = getOWLSOutput();
            if (param == null) 
            	throw new ExecutionException("JavaAtomicGrounding: Output in Grounding not specified although method provides a return value.");            
            if(param.getParamType().isDataType())
		    	results.setValue(param, EntityFactory.createDataValue(result));
			else 
				results.setValue(param, transformResult(result));
        }
        
        return results;
    }

    private OWLValue transformResult(Object result) {
    	JavaVariable output = getOutput();
    	if (output == null)
    		return null;
    	
    	String transformerName = output.getTransformator().trim();    	
    	if (transformerName != null && !transformerName.trim().equals("")) {
    		try {
				Class transformerClass = Class.forName(transformerName);
				Constructor constructor = transformerClass.getConstructor(new Class[] {OWLKnowledgeBase.class});
				JavaClassTransformator transformer = (JavaClassTransformator) constructor.newInstance(new Object[] {getKB()});
				return transformer.transformToOWL(result);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}    		    		
    	} 
    	return null;
	}

	// returns the OWL-S Output Parameter
	private Parameter getOWLSOutput() {	
		OWLObject owlsParam = getProperty(MoreGroundings.javaOutput).getProperty(MoreGroundings.owlsParameter);
		return (owlsParam == null) ? null : (Parameter) owlsParam.castTo(Parameter.class);
    }
	


    // returns the n-th OWL-S Input Parameter
    private Parameter getOWLSParameterAt(int index) {
    	JavaParameter param = getInputParamter(index);
    	if (param == null)
    		return null;
        
    	return param.getOWLSParameter();
    }
    
    // returns the n-th Parameter of the Java Method specified in the OWL-S JavaAtomicProcessGrounding
    private Class getJavaParameterTypeAt(int index) {                
    	JavaParameter param = getInputParamter(index);
    	if (param == null)
    		return null;
    	
    	String transformerName = param.getTransformator().trim();
    	String javaType = param.getJavaType();
    	if (transformerName != null && !transformerName.trim().equals("")) {
    		try {
				Class transformerClass = Class.forName(transformerName);
				Constructor constructor = transformerClass.getConstructor(new Class[] {OWLKnowledgeBase.class});
				JavaClassTransformator transformer = (JavaClassTransformator) constructor.newInstance(new Object[] {getKB()});				
				return transformer.getJavaClass();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
    		
    		return null;
    	} else {
    		return ReflectionUtils.getClassFromString(javaType);
    	}
    }

    // returns the n-th Parameter of the Java Method specified in the OWL-S JavaAtomicProcessGrounding
    private Object getJavaParameterAt(int index, OWLIndividual owlValue) {                
    	JavaParameter param = getInputParamter(index);
    	if (param == null)
    		return null;
    	
    	String transformerName = param.getTransformator().trim();
    	String javaType = param.getJavaType();
    	if (transformerName != null && !transformerName.trim().equals("")) {
    		try {
				Class transformerClass = Class.forName(transformerName);
				Constructor constructor = transformerClass.getConstructor(new Class[] {OWLKnowledgeBase.class});
				JavaClassTransformator transformer = (JavaClassTransformator) constructor.newInstance(new Object[] {getKB()});
				return transformer.transformFromOWL(owlValue);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
    		
    		return null;
    	} else {
    		return ReflectionUtils.getClassFromString(javaType);
    	}
    }
    
	/* (non-Javadoc)
	 * @see org.mindswap.owls.grounding.JavaAtomicGrounding#setOutputVar(java.lang.String, java.lang.String, org.mindswap.owls.process.Output)
	 */
	public void setOutput(String name, String type, Output owlsParameter) {
		OWLIndividual ind = getOntology().createInstance(MoreGroundings.JavaVariable, URI.create(name));
		ind.setProperty(MoreGroundings.javaType, type);
		ind.setProperty(MoreGroundings.owlsParameter, owlsParameter);
		setProperty(MoreGroundings.javaOutput, ind);
	}

	/* (non-Javadoc)
	 * @see org.mindswap.owls.grounding.JavaAtomicGrounding#setInputVar(java.lang.String, java.lang.String, int, org.mindswap.owls.process.Input)
	 */
	public void setInputParameter(String name, String type, int index, Input owlsParameter) {
		OWLIndividual ind = getOntology().createInstance(MoreGroundings.JavaParameter, URIUtils.createURI(name));
		ind.setProperty(MoreGroundings.javaType, type);
		ind.setProperty(MoreGroundings.owlsParameter, owlsParameter);
		ind.setProperty(MoreGroundings.paramIndex, Integer.toString(index));
		addProperty(MoreGroundings.hasJavaParameter, ind);
	}

	@Override
	public String getGroundingType() {
		return AtomicGrounding.GROUNDING_JAVA;
	}
	
	@Override
	public String toString() {
		return getClaz() + "." + getMethod();
	}

	private void removeAll() {
		if (hasProperty(MoreGroundings.javaClass))
			removeProperties(MoreGroundings.javaClass);
		if (hasProperty(MoreGroundings.javaMethod))
			removeProperties(MoreGroundings.javaMethod);
		
		// TODO an rdf:type property of the related input stays persistent. why????
		if (hasProperty(MoreGroundings.hasJavaParameter)) {
			OWLIndividualList indList = getProperties(MoreGroundings.hasJavaParameter);
			for (int i = 0; i < indList.size(); i++) {
				OWLIndividual ind = indList.individualAt(i);
				if (ind.hasProperty(MoreGroundings.javaType))
					ind.removeProperties(MoreGroundings.javaType);
				if (ind.hasProperty(MoreGroundings.owlsParameter))
					ind.removeProperties(MoreGroundings.owlsParameter);
				if (ind.hasProperty(MoreGroundings.paramIndex))
					ind.removeProperties(MoreGroundings.paramIndex);
				removeProperty(MoreGroundings.hasJavaParameter, ind);
				ind.delete();				
			}
		}
		
		if (hasProperty(MoreGroundings.javaOutput)) {
			OWLIndividual ind = getProperty(MoreGroundings.javaOutput);
			if (ind.hasProperty(MoreGroundings.javaType))
				ind.removeProperties(MoreGroundings.javaType);
			if (ind.hasProperty(MoreGroundings.owlsParameter))
				ind.removeProperties(MoreGroundings.owlsParameter);
			removeProperties(MoreGroundings.javaOutput);
			ind.delete();
		}
		
		if (hasProperty(MoreGroundings.owlsProcess))
			removeProperties(MoreGroundings.owlsProcess);
		if (hasProperty(OWLS.Grounding.owlsProcess))
			removeProperties(OWLS.Grounding.owlsProcess);	
	}
	
	@Override
	public void delete() {		
		removeAll();			
		
		super.delete();
	}

	public JavaParameter getInputParamter(Input input) {
		OWLIndividualList list = getPropertiesAs(MoreGroundings.hasJavaParameter, JavaParameter.class);
		for (int i = 0; i < list.size(); i++) {
			OWLIndividual ind = list.individualAt(i).getProperty(MoreGroundings.owlsParameter); 
			if (ind.getURI().equals(input.getURI()))
				return (JavaParameter) list.individualAt(i);
		}
		return null;
	}
	
	public JavaParameter getInputParamter(int index) {
		OWLIndividualList list = getPropertiesAs(MoreGroundings.hasJavaParameter, JavaParameter.class);
		for (int i = 0; i < list.size(); i++) {
			String curIndex = list.individualAt(i).getProperty(MoreGroundings.paramIndex).getLexicalValue(); 
			if (Integer.valueOf(curIndex).intValue() == index)
				return (JavaParameter) list.individualAt(i);
		}
		return null;
	}

	public JavaVariable getOutput() {
		return (JavaVariable) getPropertyAs(MoreGroundings.javaOutput, JavaVariable.class);
	}

	public OWLIndividualList getInputParameters() {
		OWLIndividualList list = getPropertiesAs(MoreGroundings.hasJavaParameter, JavaParameter.class);
		
		list.add("tempPlaceForSwap");
		// loop through every element
		for (int i = 0; i < list.size() - 2; i++) {
			int element = Integer.parseInt(list.individualAt(i).getProperty(
					MoreGroundings.paramIndex).getLexicalValue());
			int min = Integer.MAX_VALUE;
			int pos = i;
			// find the minimal element in the rest
			for (int j = i + 1; j < list.size() - 1; j++) {
				int temp = Integer.parseInt(list.individualAt(j).getProperty(
						MoreGroundings.paramIndex).getLexicalValue());
				if (temp < min) {
					min = temp;
					pos = j;
				}
			}
			// swap it to the front
			if (min < element) {
				list.set(list.size() -1, list.get(i));
				list.set(i, list.get(pos));
				list.set(pos, list.get(list.size() - 1));
			}
		}
		
		list.remove(list.size() - 1);
		return list;
	}	
	
	public String getTransformator(JavaVariable variable) {
		return getPropertyAsString(MoreGroundings.transformatorClass);
	}

	public void setTransformator(String transformClass) {
		setProperty(MoreGroundings.transformatorClass, transformClass);
	}		
}


