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
package impl.owls.profile;

import impl.owl.WrappedIndividual;

import java.net.URI;
import java.util.List;

import org.mindswap.owl.OWLDataValueList;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLIndividualList;
import org.mindswap.owl.OWLObjectProperty;
import org.mindswap.owls.OWLSFactory;
import org.mindswap.owls.process.Condition;
import org.mindswap.owls.process.ConditionList;
import org.mindswap.owls.process.Input;
import org.mindswap.owls.process.InputList;
import org.mindswap.owls.process.Output;
import org.mindswap.owls.process.OutputList;
import org.mindswap.owls.process.Process;
import org.mindswap.owls.process.Result;
import org.mindswap.owls.process.ResultList;
import org.mindswap.owls.profile.Actor;
import org.mindswap.owls.profile.Profile;
import org.mindswap.owls.profile.ServiceCategory;
import org.mindswap.owls.profile.ServiceParameter;
import org.mindswap.owls.service.Service;
import org.mindswap.owls.vocabulary.OWLS;

/**
 * @author Evren Sirin
 *
 */
public class ProfileImpl extends WrappedIndividual implements Profile {
	/**
	 * @param resource
	 */
	public ProfileImpl(OWLIndividual ind) {
		super(ind);
	}

	/* (non-Javadoc)
	 * @see org.mindswap.owls.profile.Profile#getService()
	 */
	public Service getService() {
	    return (Service) getPropertyAs(OWLS.Service.presentedBy, Service.class);
	}

	/* (non-Javadoc)
	 * @see org.mindswap.owls.profile.Profile#getTextDescription()
	 */
	public void setTextDescription(String desc) {
		setProperty(OWLS.Profile.textDescription, desc);
	}

	/* (non-Javadoc)
	 * @see org.mindswap.owls.profile.Profile#getTextDescription()
	 */
	public String getTextDescription() {
	    return getPropertyAsString(OWLS.Profile.textDescription);
	}

	/* (non-Javadoc)
	 * @see org.mindswap.owls.profile.Profile#getTextDescription(java.lang.String)
	 */
	public String getTextDescription(String lang) {
	    return getPropertyAsString(OWLS.Profile.textDescription, lang);
	}

	/* (non-Javadoc)
	 * @see org.mindswap.owls.profile.Profile#getTextDescription()
	 */
	public OWLDataValueList getTextDescriptions() {
		return getProperties(OWLS.Profile.textDescription);
	}
	
	public void addInput(Input input) {
	    addProperty(OWLS.Profile.hasInput, input);
	}
	
	public void addOutput(Output output) {
	    addProperty(OWLS.Profile.hasOutput, output);
	}

	public void addInputs(InputList inputs) {
		for (int i = 0; i < inputs.size(); i++)
			addInput(inputs.inputAt(i));
	}
	
	public void addOutputs(OutputList outputs) {
		for (int i = 0; i < outputs.size(); i++)
			addOutput(outputs.outputAt(i));
	}
	
	public void addResult(Result result) {
	    addProperty(OWLS.Profile.hasResult, result);
	}

	public void setResult(Result result) {
	    setProperty(OWLS.Profile.hasResult, result);
	}

	/* (non-Javadoc)
	 * @see org.mindswap.owls.profile.Profile#getInputs()
	 */
	public InputList getInputs() {
		return OWLSFactory.createInputList(getProperties(OWLS.Profile.hasInput));
	}

	/* (non-Javadoc)
	 * @see org.mindswap.owls.profile.Profile#getOutputs()
	 */
	public OutputList getOutputs() {
	    return OWLSFactory.createOutputList(getProperties(OWLS.Profile.hasOutput));
	}

	public Condition getCondition() {
	    return (Condition) getPropertyAs(OWLS.Profile.hasPrecondition, Condition.class);
	}
	
	public ConditionList getConditions() {
	    return OWLSFactory.createConditionList(getProperties(OWLS.Profile.hasPrecondition));
	}	
	
	public void setCondition(Condition condition) {
	    setProperty(OWLS.Profile.hasPrecondition, condition);
	}
		
	public void addCondition(Condition condition) {
	    addProperty(OWLS.Profile.hasPrecondition, condition);
	}

	/* (non-Javadoc)
	 * @see org.mindswap.owls.profile.Profile#getEffects()
	 */
	public ResultList getResults() {
	    return OWLSFactory.createResultList(getProperties(OWLS.Profile.hasResult));
	}

	public Result getResult() {
		return (Result) getPropertyAs(OWLS.Profile.hasResult, Result.class);
	}

	public Process getProcess() {
		return getService().getProcess();
	}

	/**
	 * @param service
	 */
	public void setService(Service service) {
		if(hasProperty(OWLS.Service.presentedBy, service))
		    return;
		
		setProperty(OWLS.Service.presentedBy, service);
		service.setProfile(this);
	}

	public String debugString() {
		String str = getLabel() + "  " + getURI() + " " + getType()  + "\n";
		for(int i = 0; i < getInputs().size(); i++)
			str += getInputs().inputAt(i).debugString() + "\n";
		for(int i = 0; i < getOutputs().size(); i++)
			str += getOutputs().outputAt(i).debugString() + "\n";
		
		return str;
	}

	/* (non-Javadoc)
	 * @see org.mindswap.owls.profile.Profile#getCategory()
	 */
	public ServiceCategory getCategory() {
		return (ServiceCategory) getPropertyAs(OWLS.Profile.serviceCategory, ServiceCategory.class);
	}
	
	/**
	 * Get the list of all categories defined for this profile.
	 * 
	 * @return
	 */
	public OWLIndividualList getCategories() {
	    return getPropertiesAs(OWLS.Profile.serviceCategory, ServiceCategory.class);
	}
	
	/**
	 * Add a new category for this profile.
	 * 
	 * @param category
	 */
	public void addCategory(ServiceCategory category) {
	    addProperty(OWLS.Profile.serviceCategory, category);	    
	}

	/**
	 * Set the category for this profile (remove any previous cateogry)
	 * @param category
	 */
	public void setCategory(ServiceCategory category) {
	    setProperty(OWLS.Profile.serviceCategory, category);
	}
	

	/* (non-Javadoc)
	 * @see org.mindswap.owls.profile.Profile#getServiceParameters()
	 */
	public OWLIndividualList getServiceParameters() {
	    return getPropertiesAs(OWLS.Profile.serviceParameter, ServiceParameter.class);
	}

	public OWLIndividualList getContactInfos() {
	    return getPropertiesAs(OWLS.Profile.contactInformation, Actor.class);
	}
	
	public Actor getContactInfo() {
	    return (Actor) getPropertyAs(OWLS.Profile.contactInformation, Actor.class);
	}
	
	public void setContactInfo(Actor actor) {
	    setProperty(OWLS.Profile.contactInformation, actor);
	}

	public void addContactInfo(Actor actor) {
	    addProperty(OWLS.Profile.contactInformation, actor);
	}

	public ServiceParameter getServiceParameter(OWLObjectProperty prop) {
		return (ServiceParameter) getPropertyAs(prop, ServiceParameter.class);
	}
	
	public ServiceParameter getServiceParameter(String name) {
	    // TODO make this an RDQL query
	    List serviceParams = getServiceParameters();
	    for(int i = 0; i < serviceParams.size(); i++) {
	        ServiceParameter serviceParam = (ServiceParameter) serviceParams.get(i);
	        String serviceParamName = serviceParam.getName();
	        if(serviceParamName != null && serviceParamName.equals(name))
	            return serviceParam;
	        
	    }
		return null;
	}
	
	public OWLIndividual getServiceParameterValue(OWLObjectProperty prop) {
	    ServiceParameter serviceParam = getServiceParameter(prop);
	    return (serviceParam == null) ? null : serviceParam.getParameter();
	}
	
	public OWLIndividual getServiceParameterValue(String name) {
	    ServiceParameter serviceParam = getServiceParameter(name);
	    return (serviceParam == null) ? null : serviceParam.getParameter();	    
	}
	
    public void addServiceParameter(ServiceParameter serviceParam) {
        addProperty(OWLS.Profile.serviceParameter, serviceParam);
    }
    
	public ServiceParameter createServiceParameter( OWLObjectProperty prop, OWLIndividual sParameter ) {
        ServiceParameter serviceParam = getOntology().createServiceParameter();
        serviceParam.setParameter( sParameter );
        
        addProperty(prop, serviceParam);	    
        
        return serviceParam;
	}

	public ServiceParameter createServiceParameter( String name, OWLIndividual sParameter ) {
        ServiceParameter serviceParam = getOntology().createServiceParameter();
        serviceParam.setName( name );
        serviceParam.setParameter( sParameter );
        
        addServiceParameter( serviceParam );
        
        return serviceParam;
	}
    
    public void addServiceParameterValue(OWLObjectProperty prop, OWLIndividual value) {
        createServiceParameter(prop, value);
    }

    public void addServiceParameterValue(String name, OWLIndividual value) {
        createServiceParameter( name, value );
    }
    
    public String getServiceName() {
	    return getPropertyAsString(OWLS.Profile.serviceName);
    }

    /* (non-Javadoc)
     * @see org.mindswap.owls.profile.Profile#getServiceName(java.lang.String)
     */
    public String getServiceName(String lang) {
        return getPropertyAsString(OWLS.Profile.serviceName, lang);
    }

    /* (non-Javadoc)
     * @see org.mindswap.owls.profile.Profile#getServiceNames()
     */
    public OWLDataValueList getServiceNames() {
        return getProperties(OWLS.Profile.serviceName);
    }
    
    public void setServiceName(String name) {
        setProperty(OWLS.Profile.serviceName, name);
    }

	public URI getServiceProduct() {
	    return getPropertyAsURI(OWLS.Profile.serviceProduct);
	}
	
	public OWLDataValueList getServiceProducts() {
	    return getProperties(OWLS.Profile.serviceProduct);
	}
	
	public void addServiceProduct(URI product) {
	    addProperty(OWLS.Profile.serviceProduct, product);
	}
	
	public void setServiceProduct(URI product){
	    setProperty(OWLS.Profile.serviceProduct, product);
	}
	
	public URI getServiceClassification() {
	    return getPropertyAsURI(OWLS.Profile.serviceClassification);
	}
	
	public OWLDataValueList getServiceClassifications() {
	    return getProperties(OWLS.Profile.serviceClassification);
	}
	
	public void addServiceClassification(URI classification) {
	    addProperty(OWLS.Profile.serviceClassification, classification);
	}
	
	public void setServiceClassification(URI classification) {
	    setProperty(OWLS.Profile.serviceClassification, classification);
	}

	public void removeService() {
		if (hasProperty(OWLS.Service.presentedBy, getService()))
			removeProperty(OWLS.Service.presentedBy, getService());
	}

	public void removeInput(Input input) {
		if (hasProperty(OWLS.Profile.hasInput, input))
			removeProperty(OWLS.Profile.hasInput, input);
	}

	public void removeInputs() {
		InputList list = getInputs();
		for (int i = 0; i < list.size(); i++) 
			removeInput((Input) list.individualAt(i));	
	}

	public void removeOutput(Output output) {
		if (hasProperty(OWLS.Profile.hasOutput, output))
			removeProperty(OWLS.Profile.hasOutput, output);
	}

	public void removeOutputs() {
		OutputList list = getOutputs();
		for (int i = 0; i < list.size(); i++) 
			removeOutput((Output) list.individualAt(i));	
	}
	
	public void removeResult(Result result) {
		if (hasProperty(OWLS.Profile.hasResult, result))
			removeProperty(OWLS.Profile.hasResult, result);
	}

	public void removeResults() {
		ResultList list = getResults();
		for (int i = 0; i < list.size(); i++) 
			removeResult((Result) list.individualAt(i));	
	}
	
	public void deleteInput(Input input) {
		input.delete();
	}

	public void deleteInputs() {
		InputList inputs = getInputs();
		for (int index = 0; index < inputs.size(); index++) 
			inputs.individualAt(index).delete();
	}

	public void deleteOutput(Output output) {
		output.delete(); 
	}

	public void deleteOutputs() {
		OutputList outputs = getOutputs();
		for (int index = 0; index < outputs.size(); index++) 
			outputs.individualAt(index).delete();
	}

	public void deleteResult(Result result) {
		result.delete();
	}

	public void deleteResults() {
		ResultList results = getResults();
		for (int index = 0; index < results.size(); index++) 
			results.individualAt(index).delete();
	}
}
