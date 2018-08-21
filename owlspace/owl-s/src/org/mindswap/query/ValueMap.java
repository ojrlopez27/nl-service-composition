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
package org.mindswap.query;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.mindswap.owl.EntityFactory;
import org.mindswap.owl.OWLDataValue;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLValue;
import org.mindswap.swrl.Variable;

/**
 * This interface provides a way to assign values to variables. When a process needs to be
 * executed the values for the input parameters are specified with this interface. The result of
 * the execution is also given with this interface
 * 
 * @author Evren Sirin
 *
 */
public class ValueMap {
    private Map map;
    
	/**
	 * 
	 */
	public ValueMap() {
		map = new HashMap();
	}
	
	/**
	 * Set the value of the given variable to a plain string. This is equivalent to
	 * <br>
	 * <code>setValue( var, EntityFactory.createDataValue( value ) )</code>.
	 * 
	 * @param var
	 * @param value
	 */
	public void setDataValue(Variable var, String value) {
	    setValue( var, EntityFactory.createDataValue( value ) );
	}
	
	/**
	 * Set the value of the given variable
	 * 
	 * @param var
	 * @param value
	 */
	public void setValue(Variable var, OWLValue value) {
		if(var == null) throw new NullPointerException("ValueMap cannot set a value for null variable");
		if(value == null) throw new NullPointerException("Value of variable '" + var + "' cannot be set to null");
		
		map.put( var, value);
	}
	
	/**
	 * Get the value of the given variable
	 * 
	 * @param var
	 * @return
	 */
	public OWLValue getValue(Variable var) {
		return (OWLValue) map.get( var );
	}
	
	/**
	 * Get the value of the given variable cast to an OWLDataValue. An exception occurs if 
	 * the variable is bound to an OWLIndividual.
	 * 
	 * @param var
	 * @return
	 */
	public OWLDataValue getDataValue(Variable var) {
		return (OWLDataValue) map.get( var );
	}

	public OWLDataValue getDataValue(String var) {
		return (OWLDataValue) getValue( var );
	}
	
	public String getStringValue(Variable var) {
		return map.get( var ).toString();
	}
	
	public String getStringValue(String var) {
		return getValue( var ).toString();
	}
	
	/**
	 * Get the value of the given variable cast to an OWLIndividual. An exception occurs if 
	 * the variable is bound to an OWLDataValue.
	 * 
	 * @param var
	 * @return
	 */
	public OWLIndividual getIndividualValue(Variable var) {
		return (OWLIndividual) map.get( var );
	}
	
	public OWLIndividual getIndividualValue(String var) {
		return (OWLIndividual) getValue( var );
	}
	
	/**
	 * Get the value of the variable with the given local name. If there are more than 
	 * one such variables a random one is returned; 
	 * 
	 * @param var
	 * @return
	 */
	public OWLValue getValue(String varName) {
	    for( Iterator i = getVariables().iterator(); i.hasNext(); ) {
	        Variable var = (Variable) i.next();
			if(!var.isAnon() && var.getLocalName().equals(varName))
				return getValue( var );
	    }
	    
		return null;
	}
	
	/**
	 * Return true if the given parameter has been assigned a value
	 * 
	 * @param var
	 * @return
	 */
	public boolean hasValue(Variable var) {
		return map.containsKey( var );
	}
	
	/**
	 * Clear the value that has been assigned to the given parameter
	 * 
	 * @param p
	 */
	public void clearValue(Variable var) {
	    map.remove( var );
	}

	/**
	 * Clear all the values
	 * 
	 * @param p
	 */
	public void clear() {
	    map.clear();
	}
	
	/**
	 * Add all the value bindings that has been defined in the given ValueMap.
	 * 
	 * @param valueMap
	 */	
	public void addMap(ValueMap valueMap) {
	    map.putAll(valueMap.map);
	}
		
	public Set getVariables() {
	    return map.keySet();
	}
	
	public Collection getValues() {
	    return map.values();
	}
	
	public boolean isEmpty() {
	    return map.isEmpty();
	}
	
	public int size() {
	    return map.size();
	}
	
	public String toString()  {
	    return map.toString();
	}
	
    public String debugString()  {
        StringBuffer sb = new StringBuffer ();

        for( Iterator i = getVariables().iterator(); i.hasNext(); ) {
            Variable var = (Variable) i.next();
            OWLValue val = getValue( var );
            
            String value = val.isDataValue() ? val.toString() : ((OWLIndividual)val).toRDF(false);
                
            sb.append("(");
            sb.append(var.getURI());
            sb.append("=");
            sb.append(value);
            sb.append(")");
            
            if( i.hasNext() ) sb.append ("&");
        }
        
        return sb.toString();
    }
}
