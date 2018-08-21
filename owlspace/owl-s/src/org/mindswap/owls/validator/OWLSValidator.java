package org.mindswap.owls.validator;

import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.mindswap.exceptions.CastingException;
import org.mindswap.exceptions.ConversionException;
import org.mindswap.owl.OWLFactory;
import org.mindswap.owl.OWLIndividualList;
import org.mindswap.owl.OWLKnowledgeBase;
import org.mindswap.owl.OWLOntology;
import org.mindswap.owls.grounding.AtomicGrounding;
import org.mindswap.owls.grounding.AtomicGroundingList;
import org.mindswap.owls.grounding.Grounding;
import org.mindswap.owls.grounding.JavaAtomicGrounding;
import org.mindswap.owls.grounding.MessageMap;
import org.mindswap.owls.grounding.MessageMapList;
import org.mindswap.owls.grounding.WSDLAtomicGrounding;
import org.mindswap.owls.process.AtomicProcess;
import org.mindswap.owls.process.CompositeProcess;
import org.mindswap.owls.process.ControlConstruct;
import org.mindswap.owls.process.ControlConstructBag;
import org.mindswap.owls.process.Input;
import org.mindswap.owls.process.InputBinding;
import org.mindswap.owls.process.InputBindingList;
import org.mindswap.owls.process.InputList;
import org.mindswap.owls.process.Local;
import org.mindswap.owls.process.Output;
import org.mindswap.owls.process.OutputBinding;
import org.mindswap.owls.process.OutputBindingList;
import org.mindswap.owls.process.OutputList;
import org.mindswap.owls.process.Parameter;
import org.mindswap.owls.process.ParameterList;
import org.mindswap.owls.process.Perform;
import org.mindswap.owls.process.Process;
import org.mindswap.owls.process.ProcessList;
import org.mindswap.owls.process.Result;
import org.mindswap.owls.process.ResultList;
import org.mindswap.owls.process.SplitJoin;
import org.mindswap.owls.profile.Profile;
import org.mindswap.owls.profile.ServiceCategory;
import org.mindswap.owls.profile.ServiceParameter;
import org.mindswap.owls.service.Service;
import org.mindswap.owls.vocabulary.OWLS;

/*

what about validating precondtions?
  is that neccessary?

warn about using deprecated terms?
  how to find out which are deprecated heh

--Process--

Binding
  1 toParam,
  <= 1 valueSource, valueData, valueSpecifier

ValueOf
  1 theVar
  <= 1 fromProcess

CompositeProcess
  1 composedOf
  <= 1 invocable
  <= 1 computedOutput
  <= 1 computedInput
  <= 1 computedEffect
  <= 1 computedPrecondition

everything in a ControlConstructBag/ControlConstructList should be of type ControlConstruct

Sequence
  1, components -> ControlConstructList

Split
  1, components -> ControlConstructBag

Any-Order
  1, components -> ControlConstructBag

Choice
  1, components -> ControlConstructBag

If-Then-Else
  1, ifCondition
  1, then
  <= 1, else

Repeat-While
  1, whileCondition
  1, whileProcess -> ControlConstruct

Repeat-Until
  1, untilCondition
  1, untilProcess -> ControlConstruct

*/

public class OWLSValidator
{
    private static final int CODE_INVALID_VALUE = 0;
    private static final int CODE_MISSING_VALUE = 1;

    private Map mMessageMap;

    public OWLSValidator()
    {
        mMessageMap = new HashMap();
    }

    public OWLSValidatorReport validate(OWLOntology theOntology) throws OWLSValidationException
    {
        OWLKnowledgeBase aKb = OWLFactory.createKB();
        aKb.setReasoner("Pellet");
        //aKb.setReasoner("RDFS");
        aKb.load(theOntology);

        return validateKb(aKb);
    }

    public OWLSValidatorReport validate(URI theURI) throws OWLSValidationException
    {
        return validate(theURI.toString());
    }

    public OWLSValidatorReport validate(String theURI) throws OWLSValidationException
    {
        mMessageMap = new HashMap();

        OWLKnowledgeBase aKb = OWLFactory.createKB();
        aKb.setReasoner("Pellet");
        //aKb.setReasoner("RDFS");

        try {
            aKb.read(theURI);
        }
        catch (java.net.URISyntaxException ex) {
            ex.printStackTrace();
            throw OWLSValidationException.createParseException("Invalid URI ("+theURI+") specified for description");
        }
        catch (java.io.FileNotFoundException fnfe) {
            throw OWLSValidationException.createFileNotFoundException("File at URI ("+theURI+") not found, cannot validate");
        }
        catch (Exception ex) {
            // failed to parse?
            throw OWLSValidationException.createParseException("Parse Exception: "+ex.getMessage());
        }

        return validateKb(aKb);
    }

    private OWLSValidatorReport validateKb(OWLKnowledgeBase theKb)
    {
        List aServiceList = theKb.getServices();
        Iterator servIter = aServiceList.iterator();
        while (servIter.hasNext())
        {
            Service aService = (Service)servIter.next();
            validateService(aService);
        }

        return new OWLSValidatorReport(mMessageMap);
    }

    private void validateService(Service theService)
    {
        if (theService.getProperties(OWLS.Service.describedBy).size() > 1)
        {
            // invalid, can only have 0,1 describedBy
            OWLSValidatorMessage msg = new OWLSValidatorMessage(CODE_INVALID_VALUE,"Cannot specify more than one describedBy for a service");
            addMessage(theService,msg);
        }

        validateProfileForService(theService);

        Process aProcess = null;

        try {
            aProcess = theService.getProcess();
        }
        catch (Exception ex) {
            // probably a cast exception, ignore for now, let the process validator handle it
        }
        validateProcess(theService,aProcess);

        validateGroundingForService(theService);
    }

    private void validateProcess(Service theService, Process theProcess)
    {
        if (theProcess == null)
        {
            OWLSValidatorMessage msg = new OWLSValidatorMessage(CODE_MISSING_VALUE,"No process specified for service: "+theService.getLocalName());
            addMessage(theService, msg);
            return;
        }

        try {
            // TODO: do processes really require profiles?, do they require to belong to a service?
            if (theProcess.getProfile() == null)
            {
                OWLSValidatorMessage msg = new OWLSValidatorMessage(CODE_MISSING_VALUE,"No profile specified for process: "+theProcess.getLocalName());
                addMessage(theService, msg);
            }
        }
        catch (ConversionException ce) {
            addMessage(theService, CODE_INVALID_VALUE, ce.getMessage());
        }
        catch (CastingException ex) {
            addMessage(theService, CODE_INVALID_VALUE, ex.getMessage());
        }

        if (theProcess.getNames().size() > 1)
        {
            // process must have a name
            OWLSValidatorMessage msg = new OWLSValidatorMessage(CODE_MISSING_VALUE,"Process '"+theProcess.getLocalName()+"' can specify at most one name");
            addMessage(theService, msg);
        }

//        ParameterList aParamList = aProcess.getParameters();
//        for (int i = 0; i < aParamList.size(); i++)
//        {
//            Parameter aParam = aParamList.parameterAt(i);
//            validateParameter(theService,aParam);
//        }

        ResultList aResultList = theProcess.getResults();
        for (int i = 0; i < aResultList.size(); i++)
        {
            Result aResult = aResultList.resultAt(i);
            validateResult(theService,aResult);
        }

        ParameterList aParamList = theProcess.getLocals();
        for (int i = 0; i < aParamList.size(); i++)
        {
            Local aLocal = (Local)aParamList.parameterAt(i);
            validateLocal(theService,aLocal);
        }

        InputList aInputList = theProcess.getInputs();
        for (int i = 0; i < aParamList.size(); i++)
        {
            Input aInput = aInputList.inputAt(i);
            validateInput(theService,aInput);
        }

        OutputList aOutputList = theProcess.getOutputs();
        for (int i = 0; i < aOutputList.size(); i++)
        {
            Output aOutput = aOutputList.outputAt(i);
            validateOutput(theService,aOutput);
        }

        if (theProcess.canCastTo(CompositeProcess.class))
            validateCompositeProcess((CompositeProcess)theProcess.castTo(CompositeProcess.class));
        else if (theProcess.canCastTo(AtomicProcess.class))
            validateAtomicProcess((AtomicProcess)theProcess.castTo(AtomicProcess.class));
        else
        {
            // TODO: is this an error?
            System.err.println("WTF!");
        }
    }

    private void validateLocal(Service theService, Local theLocal)
    {
        validateParameter(theService,theLocal);
    }

    private void validateInput(Service theService, Input theInput)
    {
        validateParameter(theService,theInput);
    }

    private void validateOutput(Service theService, Output theOutput)
    {
        validateParameter(theService,theOutput);
    }

    private void validateParameter(Service theService, Parameter theParam)
    {
        if (theParam.getParamType() == null)
        {
            OWLSValidatorMessage msg = new OWLSValidatorMessage(CODE_MISSING_VALUE,"A paramType must be specified for Parameter: "+theParam.getLocalName());
            addMessage(theService,msg);
        }

        if (!theParam.getParamType().isDataType())
        {
            OWLSValidatorMessage msg = new OWLSValidatorMessage(CODE_INVALID_VALUE,"The paramType for parameter: '"+theParam.getLocalName()+"' is not specified properly.  It is supposed to be a datatype property, but it is specifed as an Object property.  Please change the declaration.");
            addMessage(theService,msg);
        }

        // what about getConstantValue() ??
    }

    private void validateAtomicProcess(AtomicProcess theAtomicProcess)
    {
        // TODO:  is the AtomicGrounding required?

        // what else needs to be validated on an atomic process
    }

    private void validateCompositeProcess(CompositeProcess theCompositeProcess)
    {
        Service aService = theCompositeProcess.getService();

        if (theCompositeProcess.getComposedOf() == null)
        {
            OWLSValidatorMessage msg = new OWLSValidatorMessage(CODE_MISSING_VALUE,"");
            addMessage(theCompositeProcess.getService(),msg);
        }

        ControlConstruct aCC = theCompositeProcess.getComposedOf();
        List aList = aCC.getConstructs();
        for (int i = 0; i < aList.size(); i++)
        {
            ControlConstruct tempCC = (ControlConstruct)aList.get(i);
            validateControlConstruct(aService,tempCC);
        }

        try {
            ProcessList aProcessList = aCC.getAllProcesses();
            for (int i = 0; i < aProcessList.size(); i++)
            {
 //               Process aProcess = aProcessList.processAt(i);

                // TODO: validate each process?
                //System.err.println("proc: "+aProcess);
            }
        }
        catch (ConversionException ce) {
            addMessage(aService,CODE_INVALID_VALUE,ce.getMessage());
        }
        catch (CastingException ex) {
            addMessage(aService, CODE_INVALID_VALUE, ex.getMessage());
        }

        // maybe just verify that there are at least two processes for the composite
        // and that the perform and process list match up?
    }

    private void validateGroundingForService(Service theService)
    {
        Grounding aGrounding = null;

        try {
            aGrounding = theService.getGrounding();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            // likely a cast exception, we'll ignore the exception, aGrounding will be null
            // and the below error message will be recorded.
        }

        if (aGrounding == null)
        {
            OWLSValidatorMessage msg = new OWLSValidatorMessage(CODE_MISSING_VALUE,"No grounding specified for service: "+theService.getLocalName());
            addMessage(theService, msg);
            return;
        }

        if (aGrounding.getProperties(OWLS.Service.supportedBy).size() > 1)
        {
            // invalid, can only have 0,1 supportedBy
            OWLSValidatorMessage msg = new OWLSValidatorMessage(CODE_INVALID_VALUE,"The grounding '"+aGrounding.getLocalName()+"' cannot have more than one supportedBy property");
            addMessage(theService, msg);
        }

        if (aGrounding.getService() == null)
        {
            OWLSValidatorMessage msg = new OWLSValidatorMessage(CODE_MISSING_VALUE,"The grounding '"+aGrounding.getLocalName()+"' does not specify a service.");
            addMessage(theService, msg);
        }

        // TODO: is at least one grounding required?
        try {
            AtomicGroundingList aGroundingList = aGrounding.getAtomicGroundings();
            for (int i = 0; i < aGroundingList.size(); i++)
            {
                AtomicGrounding aAtomicGrounding = (AtomicGrounding)aGroundingList.groundingAt(i);

                if (aAtomicGrounding.getProcess() == null)
                {
                    OWLSValidatorMessage msg = new OWLSValidatorMessage(CODE_MISSING_VALUE,"The atomic grounding '"+aAtomicGrounding.getLocalName()+"' does not specify its process.");
                    addMessage(theService, msg);
                }

                if (aAtomicGrounding.canCastTo(JavaAtomicGrounding.class))
                {
                }
                else if (aAtomicGrounding.canCastTo(WSDLAtomicGrounding.class))
                {
                    validateWSDLAtomicGrounding(theService,(WSDLAtomicGrounding)aAtomicGrounding.castTo(WSDLAtomicGrounding.class));
                }
            }
        }
        catch (ConversionException ce) {
            addMessage(theService, CODE_INVALID_VALUE, ce.getMessage());
        }
        catch (CastingException ex) {
            addMessage(theService, CODE_INVALID_VALUE, ex.getMessage());
        }
    }

    private void validateWSDLAtomicGrounding(Service theService, WSDLAtomicGrounding theWSDLAtomicGrounding)
    {
        // todo:  are the wsdlInputMessage/wsdlOutputMessage required?  what about getWSDL()?
        if (theWSDLAtomicGrounding.getOperationRef() == null)
            addMessage(theService,CODE_INVALID_VALUE,"The grounding '"+theWSDLAtomicGrounding.getLocalName()+"' has a missing, or invalid operationRef.");
        else {
            if (theWSDLAtomicGrounding.getOperationRef().getPortType() == null)
                addMessage(theService, CODE_MISSING_VALUE,"The grounding '"+theWSDLAtomicGrounding.getLocalName()+"' must specify a portType for its operationRef.");

            if (theWSDLAtomicGrounding.getOperationRef().getOperation() == null)
                addMessage(theService, CODE_MISSING_VALUE,"The grounding '"+theWSDLAtomicGrounding.getLocalName()+"' must specify an operation for its operationRef");
        }

        // TODO: are you required to have input and output maps?

        MessageMapList inputMap = theWSDLAtomicGrounding.getInputMap();
        for (int i = 0; i < inputMap.size(); i++)
        {
            MessageMap aMap = (MessageMap)inputMap.messageMapAt(i);

            if (aMap.getGroundingParameter() == null)
                addMessage(theService,CODE_MISSING_VALUE,"The input map for grounding '"+theWSDLAtomicGrounding.getLocalName()+"' requires a grounding parameter (wsdlMessagePart).");

            if (aMap.getOWLSParameter() == null && aMap.getTransformation() == null)
                addMessage(theService,CODE_MISSING_VALUE,"The input map for grounding '"+theWSDLAtomicGrounding.getLocalName()+"' must specify either an owlsParameter or an xlstTransformation.");
        }

        MessageMapList outputMap = theWSDLAtomicGrounding.getOutputMap();
        for (int i = 0; i < outputMap.size(); i++)
        {
            MessageMap aMap = (MessageMap)outputMap.messageMapAt(i);

            if (aMap.getOWLSParameter() == null)
                addMessage(theService,CODE_MISSING_VALUE,"The output map for grounding '"+theWSDLAtomicGrounding.getLocalName()+"' requires an owlsParameter.");

            if (aMap.getGroundingParameter() == null && aMap.getTransformation() == null)
                addMessage(theService,CODE_MISSING_VALUE,"The output map for grounding '"+theWSDLAtomicGrounding.getLocalName()+"' must specify either an grounding parameter (wsdlMessagePart) or an xlstTransformation.");
        }
    }

    private void validateSplitJoin(Service theService, SplitJoin theSplitJoin)
    {
        ControlConstructBag ccBag = theSplitJoin.getComponents();
        for (int i = 0; i < ccBag.size(); i++)
        {
            ControlConstruct aCC = ccBag.constructAt(i);
            validateControlConstruct(theService,aCC);
        }
    }

    private void validateControlConstruct(Service theService, ControlConstruct theControlConstruct)
    {
        if (theControlConstruct.canCastTo(Perform.class))
            validatePerform(theService,(Perform)theControlConstruct.castTo(Perform.class));
        else if (theControlConstruct.canCastTo(SplitJoin.class))
            validateSplitJoin(theService,(SplitJoin)theControlConstruct.castTo(SplitJoin.class));
        else
        {
            System.err.println("unsupported CC? "+theControlConstruct.getClass());
        }
            //Perform aPerform = (Perform)aList.get(i);
            //validatePerform(aService, aPerform);
    }

    private void validateResult(Service theService, Result theResult)
    {
        ParameterList aParamList = theResult.getParameters();
        for (int i = 0; i < aParamList.size(); i++)
        {
            Parameter aParam = aParamList.parameterAt(i);
            validateParameter(theService,aParam);
        }

        OWLIndividualList aList = theResult.getEffects();
        for (int i = 0; i < aList.size(); i++)
        {
//            OWLIndividual aInd = aList.individualAt(i);
            // are these the preconditions? or post conditions?
        }

        OutputBindingList aBindingList = theResult.getBindings();
        for (int i = 0; i < aBindingList.size(); i++)
        {
            OutputBinding aOutputBinding = aBindingList.outputBindingAt(i);
            if (aOutputBinding.getOutput() == null)
                addMessage(theService,CODE_MISSING_VALUE,"Result '"+theResult.getLocalName()+"' must specify an Output for its toParam property.");
        }
    }

    private void validatePerform(Service theService, Perform thePerform)
    {
        try {
            if (thePerform.getProcess() == null)
            {
                OWLSValidatorMessage msg = new OWLSValidatorMessage(CODE_MISSING_VALUE,"Perform '"+thePerform.getLocalName()+"' must specify a process");
                addMessage(theService, msg);
            }
            //else validateProcess(theService, thePerform.getProcess());
        }
        catch (CastingException ex) {
            addMessage(theService,CODE_INVALID_VALUE,ex.getMessage());
        }
        catch (ConversionException ce) {
            addMessage(theService,CODE_INVALID_VALUE,ce.getMessage());
        }

        InputBindingList aBindingList = thePerform.getBindings();
        for (int i = 0; i < aBindingList.size(); i++)
        {
            InputBinding aInputBinding = aBindingList.inputBindingAt(i);

            try {
                if (aInputBinding.getInput() == null)
                    addMessage(theService,CODE_MISSING_VALUE,"Perform '"+thePerform.getLocalName()+"' must specify an Input for the toParam property.");
            }
            catch (CastingException ex) {
                addMessage(theService,CODE_INVALID_VALUE,ex.getMessage());
            }
            catch (ConversionException ex) {
                addMessage(theService,CODE_INVALID_VALUE,ex.getMessage());
            }
        }

        // TODO:  is there any way to validate the valueSource for the perform?
    }

    private void validateProfileForService(Service theService)
    {
        Profile aProfile = null;

        try {
            aProfile = theService.getProfile();
        }
        catch (Exception ex) {
            // cast exception most likely, let the null check below handle the error message
        }

        if (aProfile == null)
        {
            OWLSValidatorMessage msg = new OWLSValidatorMessage(CODE_MISSING_VALUE,"No profile specified for service: "+theService.getLocalName());
            addMessage(theService, msg);
            return;
        }

        if (aProfile.getService() == null)
        {
            OWLSValidatorMessage msg = new OWLSValidatorMessage(CODE_MISSING_VALUE,"No service specified for profile: "+aProfile.getLocalName()+"; double check the presentedBy property on the profile to make sure its correctly specified");
            addMessage(theService, msg);
        }

        if (aProfile.getProperties(OWLS.Profile.serviceName).size() != 1)
        {
            // invalid, must have a service name
            OWLSValidatorMessage msg = new OWLSValidatorMessage(CODE_MISSING_VALUE,"The profile '"+aProfile.getLocalName()+"' must specify only one serviceName. ");
            addMessage(theService, msg);
        }

        if (aProfile.getProperties(OWLS.Profile.textDescription).size() != 1)
        {
            // invalid, must have a text description
            OWLSValidatorMessage msg = new OWLSValidatorMessage(CODE_MISSING_VALUE,"The profile '"+aProfile.getLocalName()+"' must specify only one textDescription.");
            addMessage(theService, msg);
        }

        if (!aProfile.getInputs().equals(theService.getProcess().getInputs()))
        {
            // profile inputs should match process inputs
            OWLSValidatorMessage msg = new OWLSValidatorMessage(CODE_INVALID_VALUE,"Profile inputs for service '"+theService.getLocalName()+"' do not match the process inputs!");
            addMessage(theService, msg);
        }

        if (!aProfile.getOutputs().equals(theService.getProcess().getOutputs()))
        {
            // profile outputs should match process outputs
            OWLSValidatorMessage msg = new OWLSValidatorMessage(CODE_INVALID_VALUE,"Profile outputs for service '"+theService.getLocalName()+"' do not match the process outputs!");
            addMessage(theService, msg);
        }

        OWLIndividualList aList = aProfile.getServiceParameters();
        for (int i = 0; i < aList.size(); i++)
        {
            // TODO, why does this give a cast exception?
            ServiceParameter aServParam = (ServiceParameter)aList.individualAt(i);
            validateServiceParameter(theService, aServParam);
        }

        aList = aProfile.getCategories();
        for (int i = 0; i < aList.size(); i++)
        {
            ServiceCategory aServCategory = (ServiceCategory)aList.individualAt(i);
            validateServiceCategory(theService, aServCategory);
        }

        try {
            if (aProfile.getProcess() == null)
            {
                OWLSValidatorMessage msg = new OWLSValidatorMessage(CODE_MISSING_VALUE,"Profile '"+aProfile.getLocalName()+"' do not specify a process!");
                addMessage(theService, msg);
            }
        }
        catch (ConversionException ce) {
            addMessage(theService,CODE_INVALID_VALUE,ce.getMessage());
        }
        catch (CastingException ex) {
            addMessage(theService, CODE_INVALID_VALUE, ex.getMessage());
        }
        catch (NullPointerException npe) {
            // we'll do nothing in this case for now, the above check should catch this problem
            // you'll get an NPE here if the service for this profile is not properly specified.
            // there is a check above that will print a msg for this, so I think we can
            // ignore it here
        }

        ResultList aResultList = aProfile.getResults();
        for (int i = 0; i < aResultList.size(); i++)
            validateResult(theService,aResultList.resultAt(i));

        // what about Classification, Products??
    }

    private void validateServiceParameter(Service theService, ServiceParameter theServiceParameter)
    {
        if (theServiceParameter.getName() == null)
        {
            OWLSValidatorMessage msg = new OWLSValidatorMessage(CODE_MISSING_VALUE,"Must specify a name for serviceParameter: "+theServiceParameter.getLocalName());
            addMessage(theService, msg);
        }

        if (theServiceParameter.getParameter() == null)
        {
            OWLSValidatorMessage msg = new OWLSValidatorMessage(CODE_MISSING_VALUE,"Must specify a parameter for serviceParameter: "+theServiceParameter.getLocalName());
            addMessage(theService, msg);
        }
    }

    private void validateServiceCategory(Service theService, ServiceCategory theServiceCategory)
    {
        if (theServiceCategory.getCode() == null)
        {
            OWLSValidatorMessage msg = new OWLSValidatorMessage(CODE_MISSING_VALUE,"Must specify a code for serviceCategory: "+theServiceCategory.getLocalName());
            addMessage(theService, msg);
        }

        if (theServiceCategory.getValue() == null)
        {
            OWLSValidatorMessage msg = new OWLSValidatorMessage(CODE_MISSING_VALUE,"Must specify a value for serviceCategory "+theServiceCategory.getLocalName());
            addMessage(theService, msg);
        }

        if (theServiceCategory.getTaxonomy() == null)
        {
            OWLSValidatorMessage msg = new OWLSValidatorMessage(CODE_MISSING_VALUE,"Must specify a taxonomy for serviceCategory: "+theServiceCategory.getLocalName());
            addMessage(theService, msg);
        }

        if (theServiceCategory.getName() == null)
        {
            OWLSValidatorMessage msg = new OWLSValidatorMessage(CODE_MISSING_VALUE,"Must specify a name for serviceCategory: "+theServiceCategory.getLocalName());
            addMessage(theService, msg);
        }
    }

    private void addMessage(Service theService, int theCode, String theMsg)
    {
        addMessage(theService,new OWLSValidatorMessage(theCode,theMsg));
    }

    private void addMessage(Service theService, OWLSValidatorMessage theMsg)
    {
        Set aSet = (Set)mMessageMap.get(theService);

        if (aSet == null)
            aSet = new HashSet();

        aSet.add(theMsg);

        mMessageMap.put(theService,aSet);
    }

    public static void main(String[] args)
    {
        System.err.println("validator main");

//        OWLSValidator aValidator = new OWLSValidator();

        try {

//        OWLOntology theSourceOntology = OWLFactory.createOntology();
//        OWLOntology theDestinationOntology = OWLFactory.createOntology();
//
//        java.net.URI theIndToRemoveURI = java.net.URI.create("<some uri>");
//        java.net.URI thePredicateURI = java.net.URI.create("<some property uri>");
//
//        OWLIndividual aInd = theSourceOntology.getIndividual(theIndToRemoveURI);
//        OWLObjectProperty aProp = theSourceOntology.getObjectProperty(thePredicateURI);
//        OWLIndividual aValue = aInd.getProperty(aProp);
//
//        aInd.removeProperty(aProp,aValue);
//        aInd = theDestinationOntology.getIndividual(theIndToRemoveURI);
//        aInd.addProperty(aProp,aValue);


//            OWLKnowledgeBase kb = OWLFactory.createKB();
//            kb.setReasoner("RDFS");
//            Service aService = kb.readService("http://bai-hu.ethz.ch/next/ont/comp/NMRCompSampleSetup.owl");
//            aService.removeProperty(org.mindswap.owls.vocabulary.OWLS_1_1.Service.supports,aService.getGrounding());
//            System.err.println(aService.getProperty(org.mindswap.owls.vocabulary.OWLS_1_1.Service.supports));
//            System.err.println(aService.getGrounding());

            //new OWLSValidator().validate("http://bai-hu.ethz.ch/next/ont/comp/NMRCompSampleSetup.owl").print(System.err);

//org.mindswap.owl.OWLConfig.setStrictConversion(false);
            //new OWLSValidator().validate(new java.io.File("BookPrice.owl").toURL().toExternalForm().replaceAll(" ","%20")).print(System.err);
            //new OWLSValidator().validate("http://www.kellyjoe.com/services/commissioning/CommissioningService.owl").print(System.err);
            new OWLSValidator().validate(new java.io.File("composite.owl").toURL().toExternalForm().replaceAll(" ","%20")).print(System.err);

            //OWLKnowledgeBase kb = OWLFactory.createKB();
            //kb.setReasoner("Pellet");
            //Service aService = kb.readService("http://www.kellyjoe.com/services/commissioning/CommissioningService.owl");

        // execution engine error test code
//            OWLKnowledgeBase kb = OWLFactory.createKB();
//            kb.setReasoner("Pellet");
//            Service aService = kb.readService("http://bai-hu.ethz.ch/next/ont/comp/NMRCompSampleSetup.owl");
//
//            Process aProcess = aService.getProcess();
//            try {
//                //values = exec.execute(aProcess, values);
//                ValueMap aValueMap = new ValueMap();
//
//                ProcessExecutionEngine exec = org.mindswap.owls.OWLSFactory.createExecutionEngine();
//                impl.owls.process.execution.ProcessExecutionEngineImpl.DEBUG = true;
//
//                aValueMap = exec.execute(aProcess,aValueMap);
//            } catch (Exception e) {
//                //System.err.println(e.getMessage());
//                e.printStackTrace();
//            }

            // this code illustrates the cast exception that arises when getting the service parameters for a profile
//            String aURI = "http://www.mindswap.org/2004/owl-s/1.1/sensor/GetData2.owl";
//            OWLKnowledgeBase kb = OWLFactory.createKB();
//            kb.setReasoner("Pellet");
//            Service aService = kb.readService(aURI);
//            OWLIndividualList aList = aService.getProfile().getServiceParameters();
//            for (int i = 0; i < aList.size(); i++)
//                System.err.println(aList.individualAt(i));

            //String aURI = "http://www.mindswap.org/2004/owl-s/1.1/sensor/GetData2.owl";
            //String aURI = "http://www.mindswap.org/2004/owl-s/1.1/BNPrice.owl";
            //String aURI = "http://www.mindswap.org/2004/owl-s/1.1/BookPrice.owl";
            //String aURI = "http://www.mindswap.org/2004/owl-s/1.1/AmazonBookPrice.owl";
            //String aURI = "http://www.mindswap.org/2004/owl-s/1.1/BookFinder.owl";
            //String aURI = "http://www.mindswap.org/2004/owl-s/1.1/BabelFishTranslator.owl";
//String aURI = "http://www.mindswap.org/2004/owl-s/1.1/CheaperBookFinder.owl";
            //String aURI = "http://www.mindswap.org/2004/owl-s/1.1/Dictionary.owl";
            //String aURI = "http://www.mindswap.org/2004/owl-s/1.1/CurrencyConverter.owl";
//String aURI = "http://www.mindswap.org/2004/owl-s/1.1/FindCheaperBook.owl";
            //String aURI = "http://www.mindswap.org/2004/owl-s/1.1/FindLatLong.owl";
//String aURI = "http://www.mindswap.org/2004/owl-s/1.1/FrenchDictionary.owl";
            //String aURI = "http://www.mindswap.org/2004/owl-s/1.1/ZipCodeFinder.owl";

//            OWLSValidatorReport aReport = aValidator.validate(aURI);
//            aReport.print(System.err);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
