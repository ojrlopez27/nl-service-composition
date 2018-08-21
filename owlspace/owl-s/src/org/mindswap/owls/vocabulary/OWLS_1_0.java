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

import org.mindswap.owl.OWLClass;
import org.mindswap.owl.OWLDataProperty;
import org.mindswap.owl.EntityFactory;
import org.mindswap.owl.OWLObjectProperty;
import org.mindswap.utils.URIUtils;

/**
 * 
 * @author Evren Sirin
 */
public class OWLS_1_0 {	
	public static String base = "http://www.daml.org/services/owl-s/";
	public static String version = "1.0";
	public static String URI = base + version + "/";
	
	public static class Service {
		public static String URI = OWLS_1_0.URI + "Service.owl#";	

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
		public static String URI = OWLS_1_0.URI + "Profile.owl#";
		
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
		
		public static OWLObjectProperty hasInput;
		public static OWLObjectProperty hasOutput;
		public static OWLObjectProperty hasPrecondition;
		public static OWLObjectProperty hasParameter;
		public static OWLObjectProperty hasResult;

		
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
		}
	}
	
	public static class Process {
		public static String URI = OWLS_1_0.URI + "Process.owl#";	
		
		public static OWLClass ProcessModel;		
        public static OWLObjectProperty hasProcess;

		public static OWLClass Process;
		public static OWLClass AtomicProcess;
		public static OWLClass CompositeProcess;		
		public static OWLClass SimpleProcess;
		public static OWLClass Input;
		public static OWLClass Output;
		public static OWLClass Precondition;
		public static OWLClass Effect;
		
		public static OWLObjectProperty parameterType;
		public static OWLObjectProperty hasParameter;
		public static OWLObjectProperty hasInput;
		public static OWLObjectProperty hasOutput;
		public static OWLObjectProperty hasPrecondition;		
		public static OWLObjectProperty hasEffect;
		
		public static OWLDataProperty name;
		
		public static OWLClass ControlConstruct;
		public static OWLClass ControlConstructList;
		public static OWLClass Sequence;
		public static OWLClass Unordered;
		public static OWLClass Choice;
		public static OWLClass IfThenElse;
		public static OWLClass Split;
		public static OWLClass SplitJoin;
		public static OWLClass Iterate;
		public static OWLClass RepeatUntil;
		public static OWLClass RepeatWhile;
				
		public static OWLObjectProperty sameValues;
		public static OWLClass ValueOf;
		public static OWLObjectProperty atProcess;
		public static OWLObjectProperty theParameter;

		public static OWLObjectProperty composedOf;
		public static OWLObjectProperty components;		
		public static OWLObjectProperty ifCondition;
		public static OWLObjectProperty thenP;
		public static OWLObjectProperty elseP;
		public static OWLObjectProperty untilProcess;
		public static OWLObjectProperty untilCondition;
		public static OWLObjectProperty whileProcess;
		public static OWLObjectProperty whileCondition;
		
		static {
			ProcessModel = EntityFactory.createClass(URIUtils.createURI(URI + "ProcessModel"));
			hasProcess = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "hasProcess"));

			Process          = EntityFactory.createClass(URIUtils.createURI(URI + "Process"));
			AtomicProcess    = EntityFactory.createClass(URIUtils.createURI(URI + "AtomicProcess"));
			CompositeProcess = EntityFactory.createClass(URIUtils.createURI(URI + "CompositeProcess"));		
			SimpleProcess    = EntityFactory.createClass(URIUtils.createURI(URI + "SimpleProcess"));
			Input            = EntityFactory.createClass(URIUtils.createURI(URI + "Input"));
			Output           = EntityFactory.createClass(URIUtils.createURI(URI + "Output"));
			Precondition     = EntityFactory.createClass(URIUtils.createURI(URI + "Precondition"));
			Effect           = EntityFactory.createClass(URIUtils.createURI(URI + "Effect"));
			
			parameterType   = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "parameterType"));
			hasParameter    = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "hasParameter"));
			hasInput        = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "hasInput"));
			hasOutput       = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "hasOutput"));
			hasPrecondition = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "hasPrecondition"));
			
			name			= EntityFactory.createDataProperty(URIUtils.createURI(URI + "name"));
			
			ControlConstructList = EntityFactory.createClass(URIUtils.createURI(URI + "ControlConstructList"));
			ControlConstruct = EntityFactory.createClass(URIUtils.createURI(URI + "ControlConstruct"));
			Sequence         = EntityFactory.createClass(URIUtils.createURI(URI + "Sequence"));
			Unordered         = EntityFactory.createClass(URIUtils.createURI(URI + "Unordered"));
			Choice           = EntityFactory.createClass(URIUtils.createURI(URI + "Choice"));
			IfThenElse       = EntityFactory.createClass(URIUtils.createURI(URI + "If-Then-Else"));
			Split            = EntityFactory.createClass(URIUtils.createURI(URI + "Split"));
			SplitJoin        = EntityFactory.createClass(URIUtils.createURI(URI + "Split-Join"));
			Iterate          = EntityFactory.createClass(URIUtils.createURI(URI + "Iterate"));
			RepeatUntil      = EntityFactory.createClass(URIUtils.createURI(URI + "RepeatUntil"));
			RepeatWhile      = EntityFactory.createClass(URIUtils.createURI(URI + "RepeatWhile"));
			

			composedOf   = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "composedOf"));
			components   = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "components"));
			ifCondition  = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "ifCondition"));
			thenP        = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "then"));
			elseP        = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "else"));
			untilProcess   = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "untilProcess"));
			untilCondition = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "untilCondition"));
			whileProcess   = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "whileProcess"));
			whileCondition = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "whileCondition"));
			
			ValueOf = EntityFactory.createClass(URIUtils.createURI(URI + "ValueOf"));		
			sameValues = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "sameValues"));
			atProcess = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "atProcess"));
			theParameter = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "theParameter"));
		}		
	}

	public static class Grounding {
		public static String URI = OWLS_1_0.URI + "Grounding.owl#";
		
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
		public static OWLObjectProperty wsdlInputs;
		
		public static OWLDataProperty wsdlOutputMessage;
		public static OWLObjectProperty wsdlOutputs;
		
		/**
		 * this is deprecated as of OWL-S 1.0 but for keeping it here for historical reasons
		 */
		public static OWLObjectProperty wsdlInputMessageParts;
		/**
		 * this is deprecated as of OWL-S 1.0 but for keeping it here for historical reasons
		 */
		public static OWLObjectProperty wsdlOutputMessageParts;

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
			wsdlInputs         = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "wsdlInputs"));
			wsdlOutputs        = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "wsdlOutputs"));
			
			wsdlInputMessageParts = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "wsdlInputMessageParts"));
			wsdlOutputMessageParts = EntityFactory.createObjectProperty(URIUtils.createURI(URI + "wsdlOutputMessageParts"));
			
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
}
