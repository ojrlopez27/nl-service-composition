/*
 * Created on Aug 30, 2004
 */
package impl.owls.process.binding;

import impl.owl.WrappedIndividual;

import org.mindswap.exceptions.NotImplementedException;
import org.mindswap.owl.OWLDataValue;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLValue;
import org.mindswap.owls.process.Binding;
import org.mindswap.owls.process.Parameter;
import org.mindswap.owls.process.ParameterValue;
import org.mindswap.owls.process.ValueData;
import org.mindswap.owls.process.ValueOf;
import org.mindswap.owls.vocabulary.OWLS;

/**
 * @author Evren Sirin
 */
public abstract class BindingImpl extends WrappedIndividual implements Binding {
    public BindingImpl(OWLIndividual ind) {
        super(ind);
    }

    public ParameterValue getValue() {
        ParameterValue value = 
            (ParameterValue) getPropertyAs(OWLS.Process.valueSource, ValueOf.class);
        
        if(value == null) {
    	    OWLDataValue dataValue = getProperty(OWLS.Process.valueData);	    
    	    if(dataValue != null) {
//    	        Parameter param = getParameter();
//    	        OWLType paramType = (param == null) ? null : param.getParamType();
//    	        OWLValue owlValue = null;
//				if((paramType == null) || paramType.isDataType())
//				    owlValue = dataValue;
//				else {
//                    String literalValue = dataValue.getLexicalValue().trim();
//				    String rdf = literalValue.indexOf("rdf:RDF") == -1
//                        ? RDFUtils.addRDFTag( literalValue )
//                        : literalValue;
//				    owlValue = getOntology().parseLiteral( rdf );
//				}
//				
//				return getKB().createValueData(owlValue);
                return getKB().createValueData(dataValue);
    	    }
        }
        
        OWLIndividual owlValue = getProperty(OWLS.Process.valueObject);       
        if(owlValue != null) {
            return getKB().createValueData(owlValue);
        }

//        if(value == null)
//            value = (ParameterValue) getProperty(OWLS.Process.valueForm);
//        
//        if(value == null)
//            value = (ParameterValue) getPropertyAs(OWLS.Process.valueType, ParameterValue.class);
//        
//        if(value == null)
//            value = (ParameterValue) getPropertyAs(OWLS.Process.valueSpecifier, ValueOf.class);
//        
//        if(value == null)
//            value = (ParameterValue) getPropertyAs(OWLS.Process.valueFunction, ValueFunction.class);
        
        
        return value;
    }

    /* (non-Javadoc)
     * @see org.mindswap.owls.process.Binding#setValue(org.mindswap.owls.process.ParameterValue)
     */
    public void setValue(ParameterValue paramValue) {
        if(paramValue instanceof ValueOf)
            setProperty(OWLS.Process.valueSource, (ValueOf) paramValue);
        else if (paramValue instanceof ValueData) {
            OWLValue value = ((ValueData) paramValue).getData();
            if( value.isDataValue() )
                setProperty(OWLS.Process.valueData, (OWLDataValue) value);
            else {
//                String str = ((OWLIndividual) value).toRDF(true);
//                setProperty(OWLS.Process.valueData, EntityFactory.createDataValue( str, RDF.XMLLiteral ) );
                setProperty(OWLS.Process.valueObject, (OWLIndividual) value);
            }
        }
        else
            throw new NotImplementedException("Only ValueOf parameter values are implemented!");
    }
    
    public Parameter getParameter() {
    	return (Parameter) getPropertyAs(OWLS.Process.toParam, Parameter.class);
   }

}
