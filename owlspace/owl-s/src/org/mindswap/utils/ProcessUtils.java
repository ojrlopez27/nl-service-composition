/*
 * Created on May 3, 2005
 */
package org.mindswap.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.mindswap.owl.OWLClass;
import org.mindswap.owl.OWLKnowledgeBase;
import org.mindswap.owl.OWLType;
import org.mindswap.owl.OWLValue;
import org.mindswap.owls.process.Condition;
import org.mindswap.owls.process.ConditionList;
import org.mindswap.owls.process.Input;
import org.mindswap.owls.process.InputList;
import org.mindswap.owls.process.Process;
import org.mindswap.query.ABoxQuery;
import org.mindswap.query.ValueMap;
import org.mindswap.swrl.AtomList;
import org.mindswap.swrl.ClassAtom;
import org.mindswap.swrl.SWRLFactory;
import org.mindswap.swrl.SWRLFactoryCreator;
import org.mindswap.swrl.SWRLIndividualObject;

/**
 * @author Evren Sirin
 *
 */
public class ProcessUtils {
    /**
     * Returns a map from inputs to a set of values which 
     * show the allowed values for that input that are found in the KB.  Only the KB that the 
     * process belongs to is searched (there might be other allowed values that are not in the KB).
     *  
     * @param process
     * @return
     */    
    public static Map getAllowedValues( Process process ) {
        return getAllowedValues( process.getKB(), process, new ValueMap() );
    }
    
    /**
     * Given a partial input binding, returns a map from inputs to a set of values which 
     * show the allowed values for that input that are found in the KB.  Only the KB that the 
     * process belongs to is searched (there might be other allowed values that are not in the KB).
     * There is no entry in the map for the inputs 
     * that are already bound in the given binding.
     *  
     * @param process
     * @param initialBinding     
     * @return
     */    
    public static Map getAllowedValues( Process process, ValueMap initialBinding ) {
        return getAllowedValues( process.getKB(), process, initialBinding );
    }

    /**
     * Given a partial input binding, returns a map from inputs to a set of values which 
     * show the allowed values for that input that are found in the KB (there might be other
     * allowed values that are not in the KB). There is no entry in the map for the inputs 
     * that are already bound in the given binding. 
     *  
     * @param kb
     * @param process
     * @param initialBinding
     * @return
     */
    public static Map getAllowedValues(OWLKnowledgeBase kb, Process process, ValueMap initialBinding) {
        SWRLFactory swrl = null;
        Map allowedValues = new HashMap(); 
        
        ConditionList conditions = process.getConditions();
        
        if( conditions.size() > 1 )
            System.err.println( 
                "getAllowedValues does not support for multiple conditions, " +
            	"checking the first condition only!" );

        AtomList atoms = null;
        
        if( conditions.isEmpty() ) {
            swrl = SWRLFactoryCreator.createFactory( kb.getBaseOntology() );
            atoms = swrl.createList();
        }
        else {
	        Condition condition = conditions.conditionAt( 0 );
	        atoms = condition.getBody().apply( initialBinding );
	        swrl = SWRLFactoryCreator.createFactory( process.getOntology() );
        }

        InputList inputs = process.getInputs();
        for(int i = 0; i < inputs.size(); i++) {
            Input input = inputs.inputAt( i );
            OWLType inputType = input.getParamType();
            if( !initialBinding.hasValue( input ) ) {
                allowedValues.put( input, new HashSet() );
	            if( inputType.isClass() ) {
	                ClassAtom typeAtom = 
	                    swrl.createClassAtom( 
	                        (OWLClass) inputType, 
	                        (SWRLIndividualObject) input.castTo(SWRLIndividualObject.class) );
	                atoms = atoms.insert( typeAtom );
	            }
            }
        }
        
        ABoxQuery query = new ABoxQuery( atoms );
        query.addResultVars( allowedValues.keySet() );
        
        System.out.println( query );
        
        List results = kb.query( query );
        for(Iterator i = results.iterator(); i.hasNext();) {
            ValueMap binding = (ValueMap) i.next();
            for(Iterator j = allowedValues.keySet().iterator(); j.hasNext();) {
                Input input = (Input) j.next();
                OWLValue value = binding.getValue( input );
                if( value != null ) {
	                Set set = (Set) allowedValues.get( input );
	                set.add( value );
                }
            }
        }
        
        
        return allowedValues;
    }

}
