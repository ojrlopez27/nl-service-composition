//The MIT License
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

package org.mindswap.owls.vocabulary;

import java.net.URI;

import org.mindswap.owl.EntityFactory;
import org.mindswap.owl.OWLClass;
import org.mindswap.owl.OWLDataProperty;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLObjectProperty;
import org.mindswap.owl.list.GenericListVocabulary;
import org.mindswap.owl.list.ListVocabulary;
import org.mindswap.utils.URIUtils;

/**
 * 
 * @author Evren Sirin
 */
public class OWLS_1_2 {	
	public static String base = "http://www.daml.org/services/owl-s/";
	public static String version = "1.2";
	public static String URI = base + version + "/";
	
	public static class Service {
		public static String URI = OWLS_1_2.URI + "Service.owl#";	

		public static OWLClass Service;
		public static OWLClass ServiceProfile;
		public static OWLClass ServiceModel;
		public static OWLClass ServiceGrounding;

		public static OWLObjectProperty presentedBy;
		public static OWLObjectProperty presents;
		public static OWLObjectProperty describedBy;
		public static OWLObjectProperty describes;
		public static OWLObjectProperty supportedBy;
		public static OWLObjectProperty supports;
		
		static {
			Service = EntityFactory.createClass(URIUtils.createURI(URI + "Service"));
			ServiceProfile = EntityFactory.createClass(URIUtils.createURI(URI + "ServiceProfile"));
			ServiceModel = EntityFactory.createClass(URIUtils.createURI(URI + "ServiceModel"));
			ServiceGrounding = EntityFactory.createClass(URIUtils.createURI(URI + "ServiceGrounding"));

			presentedBy = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "presentedBy"));
			presents    = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "presents"));
			describedBy = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "describedBy"));
			describes   = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "describes"));
			supportedBy = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "supportedBy"));
			supports    = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "supports"));			
		}		
	}

	public static class Profile {
		public static String URI = OWLS_1_2.URI + "Profile.owl#";
		
		public static OWLClass Profile;

		public static OWLDataProperty serviceName;
		public static OWLDataProperty textDescription;
		
		public static OWLObjectProperty hasProcess;
		
		public static OWLClass ServiceParameter;
		public static OWLObjectProperty serviceParameter;
		public static OWLDataProperty serviceParameterName;
		public static OWLObjectProperty sParameter;

		public static OWLClass ServiceCategory;
		public static OWLObjectProperty serviceCategory;
		public static OWLDataProperty categoryName;
		public static OWLDataProperty taxonomy;
		public static OWLDataProperty value;
		public static OWLDataProperty code;
		
		public static OWLDataProperty serviceClassification;
		public static OWLDataProperty serviceProduct;
		
		public static OWLObjectProperty hasInput;
		public static OWLObjectProperty hasOutput;
		public static OWLObjectProperty hasPrecondition;
		public static OWLObjectProperty hasParameter;
		public static OWLObjectProperty hasResult;

		public static OWLObjectProperty contactInformation;
		
		static {
			Profile         = EntityFactory.createClass(URIUtils.createURI(URI + "Profile"));
			serviceName     = EntityFactory.createDataProperty(URIUtils.createURI(URI + "serviceName"));
			textDescription = EntityFactory.createDataProperty(URIUtils.createURI(URI + "textDescription"));
			
			ServiceParameter = EntityFactory.createClass(URIUtils.createURI(URI + "ServiceParameter"));
			serviceParameter = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "serviceParameter"));
			serviceParameterName = EntityFactory.createDataProperty(URIUtils.createURI(URI + "serviceParameterName"));
			sParameter       = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "sParameter"));
			
			hasProcess      = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "has_process"));
			hasInput        = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "hasInput"));
			hasOutput       = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "hasOutput"));
			hasPrecondition = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "hasPrecondition"));
			hasParameter    = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "hasParameter"));			

			hasResult = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "hasResult"));

			
			ServiceCategory = EntityFactory.createClass(URIUtils.createURI(URI + "ServiceCategory"));
			serviceCategory = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "serviceCategory"));
			categoryName    = EntityFactory.createDataProperty(URIUtils.createURI(URI + "categoryName"));
			taxonomy        = EntityFactory.createDataProperty(URIUtils.createURI(URI + "taxonomy"));
			value           = EntityFactory.createDataProperty(URIUtils.createURI(URI + "value"));
			code            = EntityFactory.createDataProperty(URIUtils.createURI(URI + "code"));	
			
			serviceClassification = EntityFactory.createDataProperty(URIUtils.createURI(URI + "serviceClassification"));
			serviceProduct = EntityFactory.createDataProperty(URIUtils.createURI(URI + "serviceProduct"));
			
			contactInformation = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "contactInformation"));
		}
	}
	
	public static class Process {
        public static String URI = OWLS_1_2.URI + "Process.owl#";	
		
		public static OWLClass Process;
		public static OWLClass AtomicProcess;
		public static OWLClass CompositeProcess;		
		public static OWLClass SimpleProcess;
		public static OWLClass Parameter;
		public static OWLClass Input;
		public static OWLClass Output;
		public static OWLClass Precondition;
		public static OWLClass Effect;
		
		public static OWLDataProperty parameterType;
		public static OWLObjectProperty hasParameter;
		public static OWLObjectProperty hasInput;
		public static OWLObjectProperty hasOutput;
		public static OWLObjectProperty hasPrecondition;		
		
		public static OWLDataProperty name;
		
		public static OWLClass ControlConstruct;
		public static OWLClass ControlConstructList;
		public static OWLClass ControlConstructBag;
		public static OWLClass Sequence;
		public static OWLClass AnyOrder;
		public static OWLClass Choice;
		public static OWLClass IfThenElse;
		public static OWLClass Produce;
		public static OWLClass Split;
		public static OWLClass SplitJoin;
		public static OWLClass Iterate;
		public static OWLClass RepeatUntil;
		public static OWLClass RepeatWhile;
		
		/**
		 * This is not a standard part of OWL-S 1.1
		 */
		public static OWLClass ForEach;
		/**
		 * This is not a standard part of OWL-S 1.1. Supposed to be used with ForEach construct.
		 */
		public static OWLObjectProperty theList;
		/**
		 * This is not a standard part of OWL-S 1.1. Supposed to be used with ForEach construct.
		 */
		public static OWLObjectProperty theLoopVar;
		/**
		 * This is not a standard part of OWL-S 1.1. Supposed to be used with ForEach construct.
		 */
		public static OWLObjectProperty iterateBody;
		
		public static OWLClass ValueOf;		

		public static OWLObjectProperty composedOf;
		public static OWLObjectProperty components;		
		public static OWLObjectProperty ifCondition;
		public static OWLObjectProperty thenP;
		public static OWLObjectProperty elseP;
		public static OWLObjectProperty untilProcess;
		public static OWLObjectProperty untilCondition;
		public static OWLObjectProperty whileProcess;
		public static OWLObjectProperty whileCondition;
		public static OWLObjectProperty producedBinding;
		public static OWLObjectProperty realizedBy;
		public static OWLObjectProperty expandsTo;
		
		public static OWLClass Perform;
		public static OWLObjectProperty process;
		public static OWLObjectProperty hasDataFrom;
		public static OWLClass Binding;
		public static OWLClass InputBinding;
		public static OWLClass OutputBinding;
		
		public static OWLDataProperty parameterValue;
		
		public static OWLObjectProperty hasResult;
		public static OWLClass Result;
		public static OWLClass ResultVar;
		public static OWLObjectProperty hasResultVar;
		
		public static OWLObjectProperty hasLocal;
		public static OWLClass Local;
		
		public static OWLObjectProperty inCondition;
		public static OWLObjectProperty hasEffect;
		public static OWLObjectProperty toParam;
		public static OWLObjectProperty withOutput;
		public static OWLObjectProperty valueSource;
		public static OWLObjectProperty fromProcess;
		public static OWLObjectProperty theVar;
		
		public static OWLDataProperty valueSpecifier;
		public static OWLDataProperty valueFunction;
		public static OWLDataProperty valueForm;
		public static OWLDataProperty valueData;
        public static OWLObjectProperty valueObject;
        
		public static OWLIndividual TheParentPerform;
		public static OWLIndividual ThisPerform;
		
		static {
			Process          = EntityFactory.createClass(URIUtils.createURI(URI + "Process"));
			AtomicProcess    = EntityFactory.createClass(URIUtils.createURI(URI + "AtomicProcess"));
			CompositeProcess = EntityFactory.createClass(URIUtils.createURI(URI + "CompositeProcess"));		
			SimpleProcess    = EntityFactory.createClass(URIUtils.createURI(URI + "SimpleProcess"));
			Parameter        = EntityFactory.createClass(URIUtils.createURI(URI + "Parameter"));
			Input            = EntityFactory.createClass(URIUtils.createURI(URI + "Input"));
			Output           = EntityFactory.createClass(URIUtils.createURI(URI + "Output"));
			Precondition     = EntityFactory.createClass(URIUtils.createURI(URI + "Precondition"));
			Effect           = EntityFactory.createClass(URIUtils.createURI(URI + "Effect"));
			
			parameterType   = EntityFactory.createDataProperty(URIUtils.createURI(URI + "parameterType"));
			hasParameter    = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "hasParameter"));
			hasInput        = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "hasInput"));
			hasOutput       = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "hasOutput"));
			hasPrecondition = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "hasPrecondition"));
			
			name			= EntityFactory.createDataProperty(URIUtils.createURI(URI + "name"));
			
			ControlConstructList = EntityFactory.createClass(URIUtils.createURI(URI + "ControlConstructList"));
			ControlConstructBag = EntityFactory.createClass(URIUtils.createURI(URI + "ControlConstructBag"));
			ControlConstruct = EntityFactory.createClass(URIUtils.createURI(URI + "ControlConstruct"));
			Sequence         = EntityFactory.createClass(URIUtils.createURI(URI + "Sequence"));
			AnyOrder         = EntityFactory.createClass(URIUtils.createURI(URI + "Any-Order"));
			Choice           = EntityFactory.createClass(URIUtils.createURI(URI + "Choice"));
			IfThenElse       = EntityFactory.createClass(URIUtils.createURI(URI + "If-Then-Else"));
			Produce          = EntityFactory.createClass(URIUtils.createURI(URI + "Produce"));
			Split            = EntityFactory.createClass(URIUtils.createURI(URI + "Split"));
			SplitJoin        = EntityFactory.createClass(URIUtils.createURI(URI + "Split-Join"));
			Iterate          = EntityFactory.createClass(URIUtils.createURI(URI + "Iterate"));
			RepeatUntil      = EntityFactory.createClass(URIUtils.createURI(URI + "Repeat-Until"));
			RepeatWhile      = EntityFactory.createClass(URIUtils.createURI(URI + "Repeat-While"));
			
			
			ForEach    = EntityFactory.createClass(URIUtils.createURI(URI + "For-Each"));
			theList = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "theList"));
			theLoopVar = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "theLoopVar"));
			iterateBody = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "iterateBody"));
			
			ValueOf          = EntityFactory.createClass(URIUtils.createURI(URI + "ValueOf"));		

			composedOf   = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "composedOf"));
			components   = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "components"));
			ifCondition  = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "ifCondition"));
			thenP        = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "then"));
			elseP        = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "else"));
			untilProcess   = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "untilProcess"));
			untilCondition = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "untilCondition"));
			whileProcess   = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "whileProcess"));
			whileCondition = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "whileCondition"));
			producedBinding = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "producedBinding"));
			realizedBy = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "realizedBy"));
			expandsTo = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "expandsTo"));			
			
			Perform = EntityFactory.createClass(URIUtils.createURI(URI + "Perform"));
			process = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "process"));
			hasDataFrom = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "hasDataFrom"));
			Binding = EntityFactory.createClass(URIUtils.createURI(URI + "Binding"));
			InputBinding = EntityFactory.createClass(URIUtils.createURI(URI + "InputBinding"));
			OutputBinding = EntityFactory.createClass(URIUtils.createURI(URI + "OutputBinding"));
			
			parameterValue = EntityFactory.createDataProperty(URIUtils.createURI(URI + "parameterValue"));
			
			hasResult = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "hasResult"));
			Result = EntityFactory.createClass(URIUtils.createURI(URI + "Result"));
			ResultVar = EntityFactory.createClass(URIUtils.createURI(URI + "ResultVar"));
			hasResultVar = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "hasResultVar"));
			
			hasLocal = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "hasLocal"));
			Local = EntityFactory.createClass(URIUtils.createURI(URI + "Local"));
			
			inCondition = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "inCondition"));
			hasEffect    = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "hasEffect"));
			toParam = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "toParam"));
			withOutput = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "withOutput"));
			valueSource = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "valueSource"));
			fromProcess = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "fromProcess"));
			theVar = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "theVar"));
			
			valueSpecifier = EntityFactory.createDataProperty(URIUtils.createURI(URI + "valueSpecifier"));
			valueFunction = EntityFactory.createDataProperty(URIUtils.createURI(URI + "valueFunction"));
			valueForm = EntityFactory.createDataProperty(URIUtils.createURI(URI + "valueFrom"));
			valueData = EntityFactory.createDataProperty(URIUtils.createURI(URI + "valueData"));
            valueObject = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "valueObject"));
					
			TheParentPerform = EntityFactory.createIndividual(URIUtils.createURI(URI + "TheParentPerform"));
			ThisPerform = EntityFactory.createIndividual(URIUtils.createURI(URI + "ThisPerform"));
			
			CCList = ObjList.specialize(ControlConstructList);
			CCBag = ObjList.specialize(ControlConstructBag);
		}		
	}

	public static class Grounding {
		public static String URI = OWLS_1_2.URI + "Grounding.owl#";
		
		public static OWLClass WsdlGrounding;

		public static OWLObjectProperty hasAtomicProcessGrounding;
		public static OWLClass WsdlAtomicProcessGrounding;

		public static OWLObjectProperty wsdlOperation;
		public static OWLClass WsdlOperationRef;
		public static OWLDataProperty portType;
		public static OWLDataProperty operation;
		
		public static OWLDataProperty wsdlDocument;
		public static OWLDataProperty wsdlService;
		public static OWLObjectProperty owlsProcess;

		public static OWLDataProperty wsdlInputMessage;
		public static OWLObjectProperty wsdlInput;
		
		public static OWLDataProperty wsdlOutputMessage;
		public static OWLObjectProperty wsdlOutput;
		
		public static OWLClass WsdlMessageMap;
		public static OWLClass WsdlInputMessageMap;
		public static OWLClass WsdlOutputMessageMap;
		public static OWLDataProperty wsdlMessagePart;
		public static OWLObjectProperty owlsParameter;
		public static OWLDataProperty xsltTransformation;
		public static OWLDataProperty xsltTransformationString;
		public static OWLDataProperty xsltTransformationURI;
		
		static {
			WsdlGrounding = EntityFactory.createClass(URIUtils.createURI(URI + "WsdlGrounding"));
			WsdlAtomicProcessGrounding = EntityFactory.createClass(URIUtils.createURI(URI + "WsdlAtomicProcessGrounding"));
			WsdlOperationRef = EntityFactory.createClass(URIUtils.createURI(URI + "WsdlOperationRef"));
			
			hasAtomicProcessGrounding = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "hasAtomicProcessGrounding"));
			wsdlDocument      = EntityFactory.createDataProperty(URIUtils.createURI(URI + "wsdlDocument"));
			wsdlOperation     = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "wsdlOperation"));
			portType          = EntityFactory.createDataProperty(URIUtils.createURI(URI + "portType"));
			operation         = EntityFactory.createDataProperty(URIUtils.createURI(URI + "operation"));
			owlsProcess       = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "owlsProcess"));
			wsdlInputMessage  = EntityFactory.createDataProperty(URIUtils.createURI(URI + "wsdlInputMessage"));
			wsdlOutputMessage = EntityFactory.createDataProperty(URIUtils.createURI(URI + "wsdlOutputMessage"));
			wsdlInput         = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "wsdlInput"));
			wsdlOutput        = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "wsdlOutput"));
			
			WsdlMessageMap    = EntityFactory.createClass(URIUtils.createURI(URI + "WsdlMessageMap"));
			WsdlInputMessageMap    = EntityFactory.createClass(URIUtils.createURI(URI + "WsdlInputMessageMap"));
			WsdlOutputMessageMap    = EntityFactory.createClass(URIUtils.createURI(URI + "WsdlOutputMessageMap"));
			wsdlMessagePart   = EntityFactory.createDataProperty(URIUtils.createURI(URI + "wsdlMessagePart"));
			owlsParameter     = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "owlsParameter"));
			xsltTransformation = EntityFactory.createDataProperty(URIUtils.createURI(URI + "xsltTransformation"));		
			xsltTransformationString = EntityFactory.createDataProperty(URIUtils.createURI(URI + "xsltTransformationString"));		
			xsltTransformationURI = EntityFactory.createDataProperty(URIUtils.createURI(URI + "xsltTransformationURI"));		
		}
	}

	public static class Expression {
		public static String URI = OWLS_1_2.URI + "generic/Expression.owl#";
		
		public static OWLClass LogicLanguage;
		public static OWLClass Expression;	
		public static OWLClass Condition;	
		
		public static OWLIndividual KIF;
		public static OWLIndividual SWRL;	
		public static OWLIndividual DRS;	
		
		public static OWLClass KIF_Condition;	
		public static OWLClass SWRL_Condition;	
		public static OWLClass DRS_Condition;	
		
		public static OWLClass KIF_Expression;	
		public static OWLClass SWRL_Expression;	
		public static OWLClass DRS_Expression;	
		
		public static URI KIFref;	
		public static URI SWRLref;	
		public static URI DRSref;	
		public static URI SPARQLref;
		
		public static OWLDataProperty refURI;
		public static OWLObjectProperty expressionLanguage;
		public static OWLDataProperty expressionBody;
		public static OWLDataProperty expressionData;
        public static OWLObjectProperty expressionObject;
        
        public static OWLIndividual AlwaysTrue;
		
		static {
			LogicLanguage = EntityFactory.createClass(URIUtils.createURI(URI + "LogicLanguage"));
			Expression = EntityFactory.createClass(URIUtils.createURI(URI + "Expression"));	
			Condition = EntityFactory.createClass(URIUtils.createURI(URI + "Condition"));	
			
			expressionLanguage = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "expressionLanguage"));
			expressionBody = EntityFactory.createDataProperty(URIUtils.createURI(URI + "expressionBody"));
			expressionData = EntityFactory.createDataProperty(URIUtils.createURI(URI + "expressionData"));
            expressionObject = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "expressionObject"));
			refURI = EntityFactory.createDataProperty(URIUtils.createURI(URI + "refURI"));
			
			KIF_Condition = EntityFactory.createClass(URIUtils.createURI(URI + "KIF-Condition"));	
			SWRL_Condition = EntityFactory.createClass(URIUtils.createURI(URI +  "SWRL-Condition"));	
			DRS_Condition = EntityFactory.createClass(URIUtils.createURI(URI + "DRS-Condition"));	
			
			KIF_Expression = EntityFactory.createClass(URIUtils.createURI(URI + "KIF-Expression"));	
			SWRL_Expression = EntityFactory.createClass(URIUtils.createURI(URI +  "SWRL-Expression"));	
			DRS_Expression = EntityFactory.createClass(URIUtils.createURI(URI + "DRS-Expression"));	
			
			KIF  = EntityFactory.createIndividual(URIUtils.createURI(URI + "KIF"));	
			SWRL = EntityFactory.createIndividual(URIUtils.createURI(URI + "SWRL"));	
			DRS  = EntityFactory.createIndividual(URIUtils.createURI(URI + "DRS"));			
			
			KIFref  = URIUtils.createURI("http://logic.stanford.edu/kif/kif.html");	
			SWRLref = URIUtils.createURI("http://www.daml.org/rules/proposal/swrl.owl");	
			DRSref  = URIUtils.createURI("http://www.daml.org/services/owl-s/1.1/generic/drs.owl");
			SPARQLref  = URIUtils.createURI("http://www.w3.org/TR/rdf-sparql-query/");
			
			KIF.setProperty(refURI, KIFref);
			SWRL.setProperty(refURI, SWRLref);
			DRS.setProperty(refURI, DRS);
            
            AlwaysTrue = EntityFactory.createIndividual(URIUtils.createURI(URI + "AlwaysTrue"));
		}
	}
	
	/**
	 * Vocabulary for the Actor ontology
	 *
	 */
	public static class Actor {
	    public static String URI = OWLS_1_2.URI + "ActorDefault.owl#";
	        
	    public static OWLClass Actor = EntityFactory.createClass(URIUtils.createURI(URI + "Actor"));
	    public static OWLDataProperty email = EntityFactory.createDataProperty(URIUtils.createURI(URI + "email"));
	    public static OWLDataProperty fax = EntityFactory.createDataProperty(URIUtils.createURI(URI + "fax"));
	    public static OWLDataProperty name = EntityFactory.createDataProperty(URIUtils.createURI(URI + "name"));
	    public static OWLDataProperty phone = EntityFactory.createDataProperty(URIUtils.createURI(URI + "phone"));
	    public static OWLDataProperty physicalAddress = EntityFactory.createDataProperty(URIUtils.createURI(URI + "physicalAddress"));
	    public static OWLDataProperty title = EntityFactory.createDataProperty(URIUtils.createURI(URI + "title"));
	    public static OWLDataProperty webURL = EntityFactory.createDataProperty(URIUtils.createURI(URI +  "webURL"));
	}
	
	public static ListVocabulary ObjList = new GenericListVocabulary(URIUtils.createURI(URI + "generic/ObjectList.owl#"));
	
	public static ListVocabulary CCList;
	public static ListVocabulary CCBag;
}
