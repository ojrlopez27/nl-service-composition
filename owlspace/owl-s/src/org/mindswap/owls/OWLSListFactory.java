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

package org.mindswap.owls;

import org.mindswap.owl.OWLIndividualList;
import org.mindswap.owls.grounding.AtomicGroundingList;
import org.mindswap.owls.grounding.MessageMapList;
import org.mindswap.owls.process.BindingList;
import org.mindswap.owls.process.ConditionList;
import org.mindswap.owls.process.InputBindingList;
import org.mindswap.owls.process.InputList;
import org.mindswap.owls.process.OutputBindingList;
import org.mindswap.owls.process.OutputList;
import org.mindswap.owls.process.ParameterList;
import org.mindswap.owls.process.ProcessList;
import org.mindswap.owls.process.ResultList;

/**
 * @author Evren Sirin
 *
 */
public class OWLSListFactory {	
    public interface Interface {     
        public AtomicGroundingList createAtomicGroundingList();
        public AtomicGroundingList createAtomicGroundingList(OWLIndividualList properties);
        
        public BindingList createBindingList();
        public BindingList createBindingList(OWLIndividualList list);

        public ConditionList createConditionList();
        public ConditionList createConditionList(OWLIndividualList list);

        public InputBindingList createInputBindingList();
        public InputBindingList createInputBindingList(OWLIndividualList list);
        
        public InputList createInputList();        
        public InputList createInputList(OWLIndividualList list);

        public MessageMapList createMessageMapList();
        public MessageMapList createMessageMapList(OWLIndividualList list);

        public OutputBindingList createOutputBindingList();
        public OutputBindingList createOutputBindingList(OWLIndividualList list);
        
        public OutputList createOutputList();
        public OutputList createOutputList(OWLIndividualList list);

        public ParameterList createParameterList();
        public ParameterList createParameterList(OWLIndividualList list);

        public ProcessList createProcessList();
        public ProcessList createProcessList(OWLIndividualList list);
        
        public ResultList createResultList();
        public ResultList createResultList(OWLIndividualList list);
        
        public OWLIndividualList wrapList(OWLIndividualList list, Class castTarget);        
    }
    
    private static String[] implementations = {"impl.owls.OWLSListFactoryImpl"};
    
    private static OWLSListFactory.Interface factory = createFactory();
    
    private static OWLSListFactory.Interface createFactory() {
        for(int i = 0; (factory == null) && (i < implementations.length); i++) {
            try {
                Class impl = Class.forName(implementations[i]);
                factory = (OWLSListFactory.Interface) impl.newInstance();
            } catch(ClassNotFoundException e) {
            } catch(InstantiationException e) {
            } catch(IllegalAccessException e) {
            }
        }
        
        return factory;
    }

    public static AtomicGroundingList createAtomicGroundingList() {
        return factory.createAtomicGroundingList();
    } 

    public static AtomicGroundingList createAtomicGroundingList(OWLIndividualList list) {
        return factory.createAtomicGroundingList(list);
    } 
    
    public static BindingList createBindingList() {
        return factory.createBindingList();
    }

    public static BindingList createBindingList(OWLIndividualList list) {
        return factory.createBindingList(list);
    }

    public static ConditionList createConditionList() {
        return factory.createConditionList();
    }

    public static ConditionList createConditionList(OWLIndividualList list) {
        return factory.createConditionList(list);
    }

    public static InputBindingList createInputBindingList() {
        return factory.createInputBindingList();
    }    
    
    public static InputBindingList createInputBindingList(OWLIndividualList list) {
        return factory.createInputBindingList(list);
    }

    public static InputList createInputList() {
        return factory.createInputList();
    }
    
    public static InputList createInputList(OWLIndividualList list) {
        return factory.createInputList(list);
    }

    public static MessageMapList createMessageMapList() {
        return factory.createMessageMapList();
    }
    

    public static MessageMapList createMessageMapList(OWLIndividualList list) {
        return factory.createMessageMapList(list);
    }
    
    public static OutputBindingList createOutputBindingList() {
        return factory.createOutputBindingList();
    }    
    
    public static OutputBindingList createOutputBindingList(OWLIndividualList list) {
        return factory.createOutputBindingList(list);
    }
    
    public static OutputList createOutputList() {
        return factory.createOutputList();
    }
    
    public static OutputList createOutputList(OWLIndividualList list) {
        return factory.createOutputList(list);
    }

    public static ParameterList createParameterList() { 
        return factory.createParameterList();	
    }

    public static ParameterList createParameterList(OWLIndividualList list) { 
        return factory.createParameterList(list);	
    }

    public static ProcessList createProcessList() {
        return factory.createProcessList();
    }    

    public static ProcessList createProcessList(OWLIndividualList list) {
        return factory.createProcessList(list);
    }      
    
    public static ResultList createResultList() {
        return factory.createResultList();
    }

    public static ResultList createResultList(OWLIndividualList list) {
        return factory.createResultList(list);
    }    
    
    public static OWLIndividualList wrapList(OWLIndividualList list, Class castTarget) {
        return factory.wrapList(list, castTarget);
    }
}
