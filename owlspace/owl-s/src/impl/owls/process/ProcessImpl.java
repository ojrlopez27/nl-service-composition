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
package impl.owls.process;

import impl.owl.WrappedIndividual;

import java.net.URI;

import org.mindswap.owl.OWLDataValueList;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLType;
import org.mindswap.owls.OWLSFactory;
import org.mindswap.owls.process.Condition;
import org.mindswap.owls.process.ConditionList;
import org.mindswap.owls.process.Input;
import org.mindswap.owls.process.InputList;
import org.mindswap.owls.process.Local;
import org.mindswap.owls.process.Output;
import org.mindswap.owls.process.OutputList;
import org.mindswap.owls.process.Parameter;
import org.mindswap.owls.process.ParameterList;
import org.mindswap.owls.process.Perform;
import org.mindswap.owls.process.Process;
import org.mindswap.owls.process.Result;
import org.mindswap.owls.process.ResultList;
import org.mindswap.owls.profile.Profile;
import org.mindswap.owls.service.Service;
import org.mindswap.owls.vocabulary.OWLS;
import org.mindswap.owls.vocabulary.OWLS_Extensions;

/**
 * @author Evren Sirin
 *
 */
public abstract class ProcessImpl extends WrappedIndividual implements Process {
	
	public ProcessImpl(OWLIndividual ind) {
		super(ind);	
	}

	/* (non-Javadoc)
	 * @see org.mindswap.owls.process.Process#getService()
	 */
	public Service getService() {
	    return (Service) getPropertyAs(OWLS.Service.describes, Service.class);
	}
	
	public void setService(Service service) {
		if(hasProperty(OWLS.Service.describes, service))
		    return;
		
		setProperty(OWLS.Service.describes, service);
		service.setProcess(this);
	}
	
	public void removeService() {
		if(hasProperty(OWLS.Service.describes, getService()))
			removeProperty(OWLS.Service.describes, getService());
	}

	public Profile getProfile() {
		return getService().getProfile();
	}
		
	public void addInput(Input input) {
	    addProperty(OWLS.Process.hasInput, input);
	}

	public Input createInput(URI uri, OWLType paramType) {
	    Input in = getOntology().createInput( uri );

	    if( paramType != null )
	        in.setParamType( paramType );
	    
	    addInput( in );
	    
	    return in;
	}
	
	public void addInputs(InputList inputs) {
		for (int i = 0; i < inputs.size(); i++)
			addInput(inputs.inputAt(i));
	}
		
	public void addOutput(Output output) {
	    addProperty(OWLS.Process.hasOutput, output);
	}

	public Output createOutput(URI uri, OWLType paramType) {
	    Output out = getOntology().createOutput( uri );

	    if( paramType != null )
	        out.setParamType( paramType );
	    
	    addOutput( out );
	    
	    return out;

	}
	
	public void addOutputs(OutputList outputs) {
		for (int i = 0; i < outputs.size(); i++)
			addOutput(outputs.outputAt(i));
	}
	
	public void addLocal(Local local) {
	    addProperty(OWLS.Process.hasLocal, local);
	}
	
	public Local createLocal(URI uri, OWLType paramType) {
	    Local local = getOntology().createLocal( uri );

	    if( paramType != null )
	        local.setParamType( paramType );
	    
	    addLocal( local );
	    
	    return local;

	}
	
	public void addParameter(Parameter param) {
	    if(param instanceof Input)
	        addInput((Input) param);
	    else if(param instanceof Output)
	        addOutput((Output) param);
	    else if(param instanceof Local)
	        addLocal((Local) param);
	    else
	        addProperty(OWLS.Process.hasParameter, param);
	}

	public void addResult(Result result) {
	    addProperty(OWLS.Process.hasResult, result);
	}

	public void setResult(Result result) {
	    setProperty(OWLS.Process.hasResult, result);
	}

	public Result createResult() {
	    Result result = getOntology().createResult();
	    
	    addResult( result );
	    
	    return result;
	}
	
	public Result createResult( URI uri ) {
	    Result result = getOntology().createResult( uri );
	    
	    addResult( result );
	    
	    return result;
	}
	
	public void removeResult(Result result) {
		if (hasProperty(OWLS.Process.hasResult, result))
			removeProperty(OWLS.Process.hasResult, result);
	}
	
	public void removeResults() {
		ResultList list = getResults();
		for (int i = 0; i < list.size(); i++) 
			removeResult((Result) list.get(i));		
	}
	
	public InputList getInputs() {
	    return OWLSFactory.createInputList(getProperties(OWLS.Process.hasInput));
	}

	public Input getInput() {
	    InputList inputs = getInputs();	    
	    return inputs.isEmpty() ? null : inputs.inputAt(0);
	}
	
	public Input getInput(int i) {
	    InputList inputs = getInputs();	    
	    return inputs.inputAt(i);
	}
	
	public Input getInput(String localName) {
	    InputList inputs = getInputs();	    
	    return (Input) inputs.getParameter(localName);	    
	}
	
	public OutputList getOutputs() {
	    return OWLSFactory.createOutputList(getProperties(OWLS.Process.hasOutput));
	}
	
	public Output getOutput() {
	    OutputList outputs = getOutputs();	    
	    return outputs.isEmpty() ? null : outputs.outputAt(0);
	}

	public Output getOutput(int i) {
	    OutputList outputs = getOutputs();	    
	    return outputs.outputAt(i);
	}
	
	public Output getOutput(String localName) {
	    OutputList outputs = getOutputs();	    
	    return (Output) outputs.getParameter(localName);	    
	}
	
	public ParameterList getLocals() {
	    return OWLSFactory.createParameterList(getProperties(OWLS.Process.hasLocal));
	}
	
	public ParameterList getParameters() {
		ParameterList parameters = OWLSFactory.createParameterList(getInputs());
		parameters.addAll(getOutputs());
		parameters.addAll(getLocals());
		
		return parameters;
	}

	/* (non-Javadoc)
	 * @see org.mindswap.owls.process.Process#getParameter(java.net.URI)
	 */
	public Parameter getParameter(URI parameterURI) {
		return getParameters().getParameter(parameterURI);
	}
	
    /* (non-Javadoc)
     * @see org.mindswap.owls.process.Conditional#getCondition()
     */
    public Condition getCondition() {
        return (Condition) getPropertyAs(OWLS.Process.hasPrecondition, Condition.class);
    }
    
    public ConditionList getConditions() {
        return OWLSFactory.createConditionList(getProperties(OWLS.Process.hasPrecondition));
    }
        	
	public void setCondition(Condition condition) {
	    setProperty(OWLS.Process.hasPrecondition, condition);
	}
		
	public void addCondition(Condition condition) {
	    addProperty(OWLS.Process.hasPrecondition, condition);
	}
    
	public ResultList getResults() {
	    return OWLSFactory.createResultList(getProperties(OWLS.Process.hasResult));
	}

	public Result getResult() {
	    return (Result) getPropertyAs(OWLS.Process.hasResult, Result.class);
	}

	
    public String getName() {
        return getPropertyAsString(OWLS.Process.name);
    }
    
    public String getName(String lang) {
        return getPropertyAsString(OWLS.Process.name, lang);
    }
    
    public OWLDataValueList getNames() {
        return getProperties(OWLS.Process.name);
    }
	
	public String debugString() {
	    InputList inputs = getInputs();
	    OutputList outputs = getOutputs();
		String str = getLabel() + "  " + getURI() + "\n";
		for(int i = 0; i < inputs.size(); i++)
			str += inputs.inputAt(i).debugString() + "\n";
		for(int i = 0; i < outputs.size(); i++)
			str += outputs.outputAt(i).debugString() + "\n";
		
		return str;
	}

	public Perform getPerform() {
		return (Perform) getPropertyAs(OWLS_Extensions.Process.hasPerform, Perform.class);
	}

	public void setPerform(Perform perform) {
		if(hasProperty(OWLS_Extensions.Process.hasPerform, perform))
		    return;
		
		setProperty(OWLS_Extensions.Process.hasPerform, perform);
		if (perform != null)
			perform.setProcess(this);
	}

	public void removeInput(Input input) {
		if (hasProperty(OWLS.Process.hasInput, input))
			removeProperty(OWLS.Process.hasInput, input);
	}

	public void removeInputs() {
		InputList list = getInputs();
		for (int i = 0; i < list.size(); i++) 
			removeInput((Input) list.individualAt(i));	
	}

	public void removeLocal(Local local) {
		if (hasProperty(OWLS.Process.hasLocal, local))
			removeProperty(OWLS.Process.hasLocal, local);
	}

	public void removeLocals() {
		ParameterList list = getLocals();
		for (int i = 0; i < list.size(); i++) 
			removeLocal((Local) list.individualAt(i));	
		
	}

	public void removeOutput(Output output) {
		if (hasProperty(OWLS.Process.hasOutput, output))
			removeProperty(OWLS.Process.hasOutput, output);
	}

	public void removeOutputs() {
		OutputList list = getOutputs();
		for (int i = 0; i < list.size(); i++) 
			removeOutput((Output) list.individualAt(i));	
	}
	
	public void deleteInput(Input input) {
		removeInput(input);
		input.delete();
	}

	public void deleteInputs() {			
		InputList inputs = getInputs();
		removeInputs(); 
		for (int index = 0; index < inputs.size(); index++) 
			inputs.individualAt(index).delete();
	}

	public void deleteLocal(Local local) {
		removeLocal(local);
		local.delete();
	}

	public void deleteLocals() {
		ParameterList locals = getLocals();
		removeLocals();
		for (int index = 0; index < locals.size(); index++)  
			locals.individualAt(index).delete();
	}

	public void deleteOutput(Output output) {
		removeOutput(output);
		output.delete(); 
	}

	public void deleteOutputs() {
		OutputList outputs = getOutputs();		
		removeOutputs();				
		for (int index = 0; index < outputs.size(); index++) 
			outputs.individualAt(index).delete();
	}
	
	public void deleteResult(Result result) {
		removeResult(result);		
		result.delete();
	}

	public void deleteResults() {				
		ResultList results = getResults();
		removeResults();
		for (int index = 0; index < results.size(); index++) 
			results.individualAt(index).delete();
	}

	public void deleteAllParameters() {		
		deleteInputs();		
		deleteOutputs();
		deleteLocals();
		deleteResults();
	}
	
	@Override
	public void delete() {
		deleteAllParameters();				
	}		
}
