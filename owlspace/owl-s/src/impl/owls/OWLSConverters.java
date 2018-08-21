/*
 * Created on Dec 10, 2004
 */
package impl.owls;

import impl.owl.list.RDFListImpl;
import impl.owls.generic.expression.SWRLExpressionImpl;
import impl.owls.generic.list.OWLSObjListImpl;
import impl.owls.grounding.GroundingImpl;
import impl.owls.grounding.JavaAtomicGroundingImpl;
import impl.owls.grounding.JavaParameterImpl;
import impl.owls.grounding.JavaVariableImpl;
import impl.owls.grounding.UPnPAtomicGroundingImpl;
import impl.owls.grounding.UPnPMessageMapImpl;
import impl.owls.grounding.WSDLAtomicGroundingImpl;
import impl.owls.grounding.WSDLMessageMapImpl;
import impl.owls.grounding.WSDLOperationRefImpl;
import impl.owls.process.AtomicProcessImpl;
import impl.owls.process.CompositeProcessImpl;
import impl.owls.process.ResultImpl;
import impl.owls.process.ResultVarImpl;
import impl.owls.process.SimpleProcessImpl;
import impl.owls.process.ValueOfImpl;
import impl.owls.process.binding.BindingImpl;
import impl.owls.process.binding.InputBindingImpl;
import impl.owls.process.binding.OutputBindingImpl;
import impl.owls.process.constructs.AnyOrderImpl;
import impl.owls.process.constructs.ChoiceImpl;
import impl.owls.process.constructs.ControlConstructBagImpl;
import impl.owls.process.constructs.ControlConstructListImpl;
import impl.owls.process.constructs.ForEachImpl;
import impl.owls.process.constructs.IfThenElseImpl;
import impl.owls.process.constructs.PerformImpl;
import impl.owls.process.constructs.ProduceImpl;
import impl.owls.process.constructs.RepeatUntilImpl;
import impl.owls.process.constructs.RepeatWhileImpl;
import impl.owls.process.constructs.SequenceImpl;
import impl.owls.process.constructs.SplitImpl;
import impl.owls.process.constructs.SplitJoinImpl;
import impl.owls.process.parameter.InputImpl;
import impl.owls.process.parameter.LocalImpl;
import impl.owls.process.parameter.OutputImpl;
import impl.owls.profile.ActorImpl;
import impl.owls.profile.ProfileImpl;
import impl.owls.profile.ServiceCategoryImpl;
import impl.owls.profile.ServiceParameterImpl;
import impl.owls.service.ServiceImpl;
import impl.swrl.AtomImpl;
import impl.swrl.AtomListImpl;
import impl.swrl.BuiltinAtomImpl;
import impl.swrl.ClassAtomImpl;
import impl.swrl.DataPropertyAtomImpl;
import impl.swrl.DifferentIndividualsAtomImpl;
import impl.swrl.IndividualPropertyAtomImpl;
import impl.swrl.SWRLDataValueImpl;
import impl.swrl.SWRLDataVariableImpl;
import impl.swrl.SWRLIndividualImpl;
import impl.swrl.SWRLIndividualVariableImpl;
import impl.swrl.SameIndividualAtomImpl;

import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.mindswap.exceptions.CastingException;
import org.mindswap.exceptions.ConversionException;
import org.mindswap.exceptions.NotImplementedException;
import org.mindswap.owl.OWLClass;
import org.mindswap.owl.OWLConfig;
import org.mindswap.owl.OWLDataValue;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLObject;
import org.mindswap.owl.OWLObjectConverter;
import org.mindswap.owl.list.ListVocabulary;
import org.mindswap.owl.list.RDFList;
import org.mindswap.owl.vocabulary.RDF;
import org.mindswap.owl.vocabulary.SWRL;
import org.mindswap.owls.generic.expression.Expression;
import org.mindswap.owls.generic.list.OWLSObjList;
import org.mindswap.owls.grounding.AtomicGrounding;
import org.mindswap.owls.grounding.Grounding;
import org.mindswap.owls.grounding.JavaAtomicGrounding;
import org.mindswap.owls.grounding.JavaParameter;
import org.mindswap.owls.grounding.JavaVariable;
import org.mindswap.owls.grounding.MessageMap;
import org.mindswap.owls.grounding.UPnPAtomicGrounding;
import org.mindswap.owls.grounding.WSDLAtomicGrounding;
import org.mindswap.owls.grounding.WSDLOperationRef;
import org.mindswap.owls.process.AnyOrder;
import org.mindswap.owls.process.AtomicProcess;
import org.mindswap.owls.process.Binding;
import org.mindswap.owls.process.Choice;
import org.mindswap.owls.process.CompositeProcess;
import org.mindswap.owls.process.Condition;
import org.mindswap.owls.process.ControlConstruct;
import org.mindswap.owls.process.ControlConstructBag;
import org.mindswap.owls.process.ControlConstructList;
import org.mindswap.owls.process.ForEach;
import org.mindswap.owls.process.IfThenElse;
import org.mindswap.owls.process.Input;
import org.mindswap.owls.process.InputBinding;
import org.mindswap.owls.process.Iterate;
import org.mindswap.owls.process.Local;
import org.mindswap.owls.process.Output;
import org.mindswap.owls.process.OutputBinding;
import org.mindswap.owls.process.Parameter;
import org.mindswap.owls.process.Perform;
import org.mindswap.owls.process.Process;
import org.mindswap.owls.process.Produce;
import org.mindswap.owls.process.RepeatUntil;
import org.mindswap.owls.process.RepeatWhile;
import org.mindswap.owls.process.Result;
import org.mindswap.owls.process.ResultVar;
import org.mindswap.owls.process.Sequence;
import org.mindswap.owls.process.SimpleProcess;
import org.mindswap.owls.process.Split;
import org.mindswap.owls.process.SplitJoin;
import org.mindswap.owls.process.ValueOf;
import org.mindswap.owls.profile.Actor;
import org.mindswap.owls.profile.Profile;
import org.mindswap.owls.profile.ServiceCategory;
import org.mindswap.owls.profile.ServiceParameter;
import org.mindswap.owls.service.Service;
import org.mindswap.owls.vocabulary.FLAServiceOnt;
import org.mindswap.owls.vocabulary.OWLS;
import org.mindswap.owls.vocabulary.MoreGroundings;
import org.mindswap.swrl.Atom;
import org.mindswap.swrl.AtomList;
import org.mindswap.swrl.BuiltinAtom;
import org.mindswap.swrl.ClassAtom;
import org.mindswap.swrl.DataPropertyAtom;
import org.mindswap.swrl.DifferentIndividualsAtom;
import org.mindswap.swrl.IndividualPropertyAtom;
import org.mindswap.swrl.SWRLDataObject;
import org.mindswap.swrl.SWRLDataValue;
import org.mindswap.swrl.SWRLDataVariable;
import org.mindswap.swrl.SWRLIndividual;
import org.mindswap.swrl.SWRLIndividualObject;
import org.mindswap.swrl.SWRLIndividualVariable;
import org.mindswap.swrl.SameIndividualAtom;

/**
 * @author Evren Sirin
 */
public class OWLSConverters {
    public static Map getConverters() {
        Map converters = new HashMap();

        OWLObjectConverter serviceConverter =
            new GenericOWLSConverter(ServiceImpl.class, OWLS.Service.Service);
        OWLObjectConverter profileConverter =
            new GenericOWLSConverter(ProfileImpl.class, OWLS.Profile.Profile);
        OWLObjectConverter actorConverter =
            new GenericOWLSConverter(ActorImpl.class, OWLS.Actor.Actor);
        OWLObjectConverter serviceParamConverter =
            new GenericOWLSConverter(ServiceParameterImpl.class, OWLS.Profile.ServiceParameter);
        OWLObjectConverter serviceCategoryConverter =
            new GenericOWLSConverter(ServiceCategoryImpl.class, OWLS.Profile.ServiceCategory);

        OWLObjectConverter atomicProcessConverter =
            new GenericOWLSConverter(AtomicProcessImpl.class, OWLS.Process.AtomicProcess);
        OWLObjectConverter compositeProcessConverter =
            new GenericOWLSConverter(CompositeProcessImpl.class, OWLS.Process.CompositeProcess);
        OWLObjectConverter simpleProcessConverter =
            new GenericOWLSConverter(SimpleProcessImpl.class, OWLS.Process.SimpleProcess);
        OWLObjectConverter processConverter =
            new CombinedOWLSConverter(Process.class, new OWLObjectConverter[] 
                {atomicProcessConverter, compositeProcessConverter, simpleProcessConverter});

        OWLObjectConverter inputConverter =
            new GenericOWLSConverter(InputImpl.class, OWLS.Process.Input);
        OWLObjectConverter outputConverter =
            new GenericOWLSConverter(OutputImpl.class, OWLS.Process.Output);
        OWLObjectConverter localConverter =
            new GenericOWLSConverter(LocalImpl.class, OWLS.Process.Local);
        OWLObjectConverter resultVarConverter =
            new GenericOWLSConverter(ResultVarImpl.class, OWLS.Process.ResultVar);
        OWLObjectConverter parameterConverter =
            new CombinedOWLSConverter(Parameter.class, new OWLObjectConverter[] 
                {inputConverter, outputConverter, localConverter, resultVarConverter});

        OWLObjectConverter conditionConverter = new ExpressionConverter(false);
        OWLObjectConverter expressionConverter = new ExpressionConverter(true);

        Set performs = new HashSet();
        performs.add(Perform.TheParentPerform);
        performs.add(Perform.ThisPerform);
        OWLObjectConverter performConverter =
            new GenericOWLSConverter(PerformImpl.class, OWLS.Process.Perform, performs);
        OWLObjectConverter sequenceConverter =
            new GenericOWLSConverter(SequenceImpl.class, OWLS.Process.Sequence);
        OWLObjectConverter choiceConverter =
            new GenericOWLSConverter(ChoiceImpl.class, OWLS.Process.Choice);
        OWLObjectConverter anyOrderConverter =
            new GenericOWLSConverter(AnyOrderImpl.class, OWLS.Process.AnyOrder);
        OWLObjectConverter ifThenElseConverter =
            new GenericOWLSConverter(IfThenElseImpl.class, OWLS.Process.IfThenElse);
        OWLObjectConverter repeatWhileConverter =
            new GenericOWLSConverter(RepeatWhileImpl.class, OWLS.Process.RepeatWhile);
        OWLObjectConverter repeatUntilConverter =
            new GenericOWLSConverter(RepeatUntilImpl.class, OWLS.Process.RepeatUntil);
        OWLObjectConverter forEachConverter =
            new GenericOWLSConverter(ForEachImpl.class, OWLS.Process.ForEach);
        OWLObjectConverter splitConverter =
            new GenericOWLSConverter(SplitImpl.class, OWLS.Process.Split);
        OWLObjectConverter splitJoinConverter =
            new GenericOWLSConverter(SplitJoinImpl.class, OWLS.Process.SplitJoin);
        OWLObjectConverter produceConverter =
            new GenericOWLSConverter(ProduceImpl.class, OWLS.Process.Produce);

        OWLObjectConverter iterateConverter =
            new CombinedOWLSConverter(Iterate.class, new OWLObjectConverter[] 
                {repeatWhileConverter, repeatUntilConverter, forEachConverter});
        OWLObjectConverter controlConstructConverter =
            new CombinedOWLSConverter(ControlConstruct.class, new OWLObjectConverter[] 
                {performConverter, sequenceConverter, choiceConverter, anyOrderConverter, 
                ifThenElseConverter, repeatWhileConverter, repeatUntilConverter, forEachConverter,
                splitConverter, splitJoinConverter, produceConverter});
        
        OWLObjectConverter objListConverter =
            new ListConverter(OWLSObjListImpl.class, OWLS.ObjList);
        OWLObjectConverter rdfListConverter =
            new ListConverter(RDFListImpl.class, RDF.ListVocabulary);
        OWLObjectConverter listConverter =
            new CombinedOWLSConverter(OWLSObjList.class, new OWLObjectConverter[] {
                objListConverter, rdfListConverter});
        OWLObjectConverter ccListConverter =
            new ListConverter(ControlConstructListImpl.class, OWLS.CCList);
        OWLObjectConverter ccBagConverter =
            new ListConverter(ControlConstructBagImpl.class, OWLS.CCBag);
        
        OWLObjectConverter inputBindingConverter =
            new GenericOWLSConverter(InputBindingImpl.class, OWLS.Process.InputBinding);
        OWLObjectConverter outputBindingConverter =
            new GenericOWLSConverter(OutputBindingImpl.class, OWLS.Process.OutputBinding);
        OWLObjectConverter bindingConverter =
            new CombinedOWLSConverter(BindingImpl.class, new OWLObjectConverter[] {
                inputBindingConverter, outputBindingConverter});
        
        
        OWLObjectConverter resultConverter =
            new GenericOWLSConverter(ResultImpl.class, OWLS.Process.Result);
        OWLObjectConverter valueOfConverter =
            new GenericOWLSConverter(ValueOfImpl.class, OWLS.Process.ValueOf);
//        OWLObjectConverter valueDataConverter =
//            new ValueDataConverter();

        OWLObjectConverter genericGroundingConverter =
            new GenericOWLSConverter(GroundingImpl.class, OWLS.Service.ServiceGrounding);
        OWLObjectConverter wsdlGroundingConverter =
            new GenericOWLSConverter(GroundingImpl.class, OWLS.Grounding.WsdlGrounding);
        // added by Michael Daenzer
        OWLObjectConverter javaGroundingConverter =
            new GenericOWLSConverter(GroundingImpl.class, MoreGroundings.JavaGrounding);
        // end added by Michael Daenzer
        // changed by Michael Daenzer
        OWLObjectConverter groundingConverter =
            new CombinedOWLSConverter(Grounding.class, new OWLObjectConverter[] {
                genericGroundingConverter, wsdlGroundingConverter, javaGroundingConverter
            });
        // end changed by Michael Daenzer        
        OWLObjectConverter wsdlAtomicGroundingConverter =
            new GenericOWLSConverter(WSDLAtomicGroundingImpl.class, OWLS.Grounding.WsdlAtomicProcessGrounding);
        OWLObjectConverter upnpAtomicGroundingConverter =
            new GenericOWLSConverter(UPnPAtomicGroundingImpl.class, FLAServiceOnt.UPnPAtomicProcessGrounding);
        // added by Michael Daenzer
        OWLObjectConverter javaAtomicGroundingConverter =
            new GenericOWLSConverter(JavaAtomicGroundingImpl.class, MoreGroundings.JavaAtomicProcessGrounding);
        // end added by Michael Daenzer   
        // changed by Michael Daenzer
        OWLObjectConverter atomicGroundingConverter =
            new CombinedOWLSConverter(AtomicGrounding.class, new OWLObjectConverter[] {
                upnpAtomicGroundingConverter, javaAtomicGroundingConverter, wsdlAtomicGroundingConverter});
        OWLObjectConverter javaVariableConverter =
        	new GenericOWLSConverter(JavaVariableImpl.class, MoreGroundings.JavaVariable);
        OWLObjectConverter javaParameterConverter =
        	new GenericOWLSConverter(JavaParameterImpl.class, MoreGroundings.JavaParameter);
        // end changed by Michael Daenzer        
        OWLObjectConverter wsdlOperationRefConverter =
            new GenericOWLSConverter(WSDLOperationRefImpl.class, OWLS.Grounding.WsdlOperationRef);
        OWLObjectConverter wsdlInputMessageMapConverter =
            new GenericOWLSConverter(WSDLMessageMapImpl.class, OWLS.Grounding.WsdlInputMessageMap);
        OWLObjectConverter wsdlOutputMessageMapConverter =
            new GenericOWLSConverter(WSDLMessageMapImpl.class, OWLS.Grounding.WsdlOutputMessageMap);
        OWLObjectConverter upnpMessageMapConverter =
            new GenericOWLSConverter(UPnPMessageMapImpl.class, FLAServiceOnt.UPnPMap);
        OWLObjectConverter messageMapConverter =
            new CombinedOWLSConverter(MessageMap.class, new OWLObjectConverter[] {
                wsdlInputMessageMapConverter, wsdlOutputMessageMapConverter, upnpMessageMapConverter});
        
        OWLObjectConverter atomListConverter =
            new ListConverter(AtomListImpl.class, SWRL.AtomListVocabulary);
        OWLObjectConverter classAtomConverter =
            new GenericOWLSConverter(ClassAtomImpl.class, SWRL.ClassAtom);
        OWLObjectConverter indPropAtomConverter =
            new GenericOWLSConverter(IndividualPropertyAtomImpl.class, SWRL.IndividualPropertyAtom);
        OWLObjectConverter dataPropAtomConverter =
            new GenericOWLSConverter(DataPropertyAtomImpl.class, SWRL.DatavaluedPropertyAtom);
        OWLObjectConverter sameIndAtomConverter =
            new GenericOWLSConverter(SameIndividualAtomImpl.class, SWRL.SameIndividualAtom);
        OWLObjectConverter diffIndAtomConverter =
            new GenericOWLSConverter(DifferentIndividualsAtomImpl.class, SWRL.DifferentIndividualsAtom);
        OWLObjectConverter builtinAtomConverter =
            new GenericOWLSConverter(BuiltinAtomImpl.class, SWRL.BuiltinAtom);
        OWLObjectConverter atomConverter =
            new CombinedOWLSConverter(AtomImpl.class, new OWLObjectConverter[] {
                classAtomConverter, indPropAtomConverter, dataPropAtomConverter, 
                sameIndAtomConverter, diffIndAtomConverter, builtinAtomConverter});
        OWLObjectConverter swrlIndVarConverter =
            new CombinedOWLSConverter(SWRLIndividualVariableImpl.class, new OWLObjectConverter[] {
                new GenericOWLSConverter(SWRLIndividualVariableImpl.class, SWRL.Variable), 
                new GenericOWLSConverter(SWRLIndividualVariableImpl.class, OWLS.Process.Input),
                new GenericOWLSConverter(SWRLIndividualVariableImpl.class, OWLS.Process.Output),
                new GenericOWLSConverter(SWRLIndividualVariableImpl.class, OWLS.Process.Local),
                new GenericOWLSConverter(SWRLIndividualVariableImpl.class, OWLS.Process.ResultVar)});            
        OWLObjectConverter swrlIndConverter =
                new SWRLIndividualConverter();                   
        OWLObjectConverter swrlDataVarConverter =
	        new CombinedOWLSConverter(SWRLDataVariableImpl.class, new OWLObjectConverter[] {
	            new GenericOWLSConverter(SWRLDataVariableImpl.class, SWRL.Variable), 
	            new GenericOWLSConverter(SWRLDataVariableImpl.class, OWLS.Process.Input),
	            new GenericOWLSConverter(SWRLDataVariableImpl.class, OWLS.Process.Output),
	            new GenericOWLSConverter(SWRLDataVariableImpl.class, OWLS.Process.Local),
	            new GenericOWLSConverter(SWRLDataVariableImpl.class, OWLS.Process.ResultVar)});            
        OWLObjectConverter swrlDataValueConverter =
            new SWRLDataValueConverter();
        OWLObjectConverter swrlIndObjConverter =
            new CombinedOWLSConverter(SWRLIndividualObject.class, new OWLObjectConverter[] {
                swrlIndVarConverter, swrlIndConverter});
        OWLObjectConverter swrlDataObjConverter =
            new CombinedOWLSConverter(SWRLDataObject.class, new OWLObjectConverter[] {
                swrlDataVarConverter, swrlDataValueConverter});       
        
        converters.put(Service.class, serviceConverter);        
        converters.put(Profile.class, profileConverter);
        converters.put(ServiceParameter.class, serviceParamConverter);
        converters.put(ServiceCategory.class, serviceCategoryConverter);
        converters.put(Actor.class, actorConverter);
        
        converters.put(Grounding.class, groundingConverter);
        converters.put(AtomicGrounding.class, atomicGroundingConverter);
        converters.put(WSDLAtomicGrounding.class, wsdlAtomicGroundingConverter);
        converters.put(UPnPAtomicGrounding.class, upnpAtomicGroundingConverter);
        converters.put(JavaAtomicGrounding.class, javaAtomicGroundingConverter);
        converters.put(JavaVariable.class, javaVariableConverter);
        converters.put(JavaParameter.class, javaParameterConverter);
        converters.put(WSDLOperationRef.class, wsdlOperationRefConverter);
        converters.put(MessageMap.class, messageMapConverter);
        
        converters.put(AtomicProcess.class, atomicProcessConverter);
        converters.put(CompositeProcess.class, compositeProcessConverter);
        converters.put(SimpleProcess.class, simpleProcessConverter);
        converters.put(Process.class, processConverter);
        
        converters.put(Perform.class, performConverter);
        converters.put(Sequence.class, sequenceConverter);
        converters.put(Choice.class, choiceConverter);
        converters.put(AnyOrder.class, anyOrderConverter);
        converters.put(IfThenElse.class, ifThenElseConverter);
        converters.put(Iterate.class, iterateConverter);
        converters.put(RepeatWhile.class, repeatWhileConverter);
        converters.put(RepeatUntil.class, repeatUntilConverter);
        converters.put(ForEach.class, forEachConverter);
        converters.put(Split.class, splitConverter);
        converters.put(SplitJoin.class, splitJoinConverter);
        converters.put(Produce.class, produceConverter);
        converters.put(ControlConstruct.class, controlConstructConverter);
		
        converters.put(Input.class, inputConverter);
        converters.put(Output.class, outputConverter);
        converters.put(Local.class, localConverter);
        converters.put(ResultVar.class, resultVarConverter);
        converters.put(Parameter.class, parameterConverter);
        
        converters.put(InputBinding.class, inputBindingConverter);
        converters.put(OutputBinding.class, outputBindingConverter);
        converters.put(Binding.class, bindingConverter);
        
        converters.put(Result.class, resultConverter);
        converters.put(ValueOf.class, valueOfConverter);
//        converters.put(ValueData.class, valueDataConverter);
        
        converters.put(OWLSObjList.class, listConverter);
        converters.put(RDFList.class, rdfListConverter);
        converters.put(ControlConstructList.class, ccListConverter);
        converters.put(ControlConstructBag.class, ccBagConverter);
        
        converters.put(Condition.class, conditionConverter);
        converters.put(Expression.class, expressionConverter);

        converters.put(AtomList.class, atomListConverter);
        converters.put(Atom.class, atomConverter);
        converters.put(ClassAtom.class, classAtomConverter);
        converters.put(IndividualPropertyAtom.class, indPropAtomConverter);
        converters.put(SameIndividualAtom.class, sameIndAtomConverter);
        converters.put(DifferentIndividualsAtom.class, diffIndAtomConverter);
        converters.put(DataPropertyAtom.class, dataPropAtomConverter);
        converters.put(BuiltinAtom.class, builtinAtomConverter);
        converters.put(SWRLIndividual.class, swrlIndConverter);
        converters.put(SWRLIndividualVariable.class, swrlIndVarConverter);
        converters.put(SWRLIndividualObject.class, swrlIndObjConverter);
        converters.put(SWRLDataVariable.class, swrlDataVarConverter);
        converters.put(SWRLDataValue.class, swrlDataValueConverter);
        converters.put(SWRLDataObject.class, swrlDataObjConverter);

        return converters;
	}

	public static class GenericOWLSConverter implements OWLObjectConverter {
	    Class javaClass;
	    OWLClass owlClass;
	    Set individuals;
	    Constructor constructor;
	    
	    public GenericOWLSConverter(Class javaClass, OWLClass owlClass) {
	        this(javaClass, owlClass, Collections.EMPTY_SET);
	    }
	    
	    public GenericOWLSConverter(Class javaClass, OWLClass owlClass, Set individuals) {
	        this.javaClass = javaClass;
	        this.owlClass = owlClass;
	        this.individuals = individuals;
	        
	        try {
                constructor = javaClass.getConstructor(new Class[] {OWLIndividual.class});
            } catch(Exception e) {
                throw new RuntimeException(e);
            }
	    }
	    
		public boolean canCast(OWLObject object) {
			return (object instanceof OWLIndividual) &&
				   (!OWLConfig.getStrictConversion() ||
				    ((OWLIndividual) object).isType(owlClass) ||
				    individuals.contains(object));
		}
		
		public OWLObject cast(OWLObject object) {
		    if(!canCast(object))
		        throw new ConversionException("OWLObject " + object + " cannot be cast to " + owlClass);
		
			try {
                return (OWLObject) constructor.newInstance(new Object[] {(OWLIndividual) object});
            } catch(Exception e) {
                e.printStackTrace();
                throw new CastingException("OWLObject " + object + " cannot be cast to " + javaClass);
            }
		}

        public OWLObject convert(OWLObject object) {
            if(!canCast(object))
                ((OWLIndividual) object).addType(owlClass);
            
            return cast(object);
        }
        
        public String toString() {
            return "Converter " + owlClass + " -> " + javaClass;
        }
	}
	
	public static class CombinedOWLSConverter implements OWLObjectConverter {
	    OWLObjectConverter[] converters;
	    Class javaClass;
	    
	    public CombinedOWLSConverter(Class javaClass, OWLObjectConverter[] converters) {
	        this.converters = converters;
	        this.javaClass = javaClass;
	    }
	    
		public boolean canCast(OWLObject object) {
		    boolean strict = OWLConfig.setStrictConversion(true);
		    for(int i = 0; i < converters.length; i++) {
		        OWLObjectConverter converter = converters[i];
		        if(converter.canCast(object)) {
		            OWLConfig.setStrictConversion(strict);
		            return true;
		        }
		    }
		    OWLConfig.setStrictConversion(strict);
		    
			return false;
		}
		
		public OWLObject cast(OWLObject object) {
		    boolean strict = OWLConfig.setStrictConversion(true);
		    for(int i = 0; i < converters.length; i++) {
		        OWLObjectConverter converter = converters[i];
		        if(converter.canCast(object)) {
		            OWLConfig.setStrictConversion(strict);
		            return converters[i].cast(object);
		        }
		    }
		    OWLConfig.setStrictConversion(strict);
		    
		    System.err.println(((OWLIndividual)object).toRDF(false));
		    throw new CastingException("OWLObject " + object + " cannot be cast to " + javaClass);
		}
		
        public OWLObject convert(OWLObject object) {
            if(!canCast(object))
                throw new ConversionException("Cannot convert " + object + " to abstract class " + javaClass);
            
            return cast(object);
        }
	}

	public static class ListConverter extends GenericOWLSConverter {
	    ListVocabulary vocabulary;
	    
	    public ListConverter(Class javaClass, ListVocabulary vocabulary) {
	        super(javaClass, vocabulary.List());
	        
	        this.vocabulary = vocabulary;
	    }
	    
		public boolean canCast(OWLObject object) {
		    if(object instanceof OWLIndividual) {
		        OWLIndividual ind = (OWLIndividual) object;
		        			    
				return ind.equals(vocabulary.nil()) ||
					   ind.isType(vocabulary.List()) || 
				       ind.hasProperty(vocabulary.first()) ||
				       ind.hasProperty(vocabulary.rest());
		    }

		    return false;
		}
		
        public OWLObject convert(OWLObject object) {
            if(!canCast(object))
                throw new ConversionException("Cannot do conversion for object lists!");
            
            return cast(object);
        }
	}

	public static class ExpressionConverter implements OWLObjectConverter {
	    boolean convertToExpression;
	    OWLClass owlClass;
//	    OWLClass specificClass;
//	    OWLIndividual specificValue;	    
	    
	    public ExpressionConverter(boolean convertToExpression) {
	        this.convertToExpression = convertToExpression;

	        if(convertToExpression) {
	            owlClass = OWLS.Expression.Expression;
//	        	specificClass = OWLS.Expression.SWRL_Expression;
	        }
	        else {
	            owlClass = OWLS.Expression.Condition;
//	        	specificClass = OWLS.Expression.SWRL_Condition;
	        }
//	        specificValue = OWLS.Expression.SWRL;
	    }
	    
		public boolean canCast(OWLObject object) {
			if(object instanceof OWLIndividual) {
			    if(!OWLConfig.getStrictConversion())
			        return true;
			    
			    OWLIndividual ind = (OWLIndividual) object; 
				
			    if(ind.equals(OWLS.Expression.AlwaysTrue))
                    return true;
                
                if(convertToExpression) {
                    return  ind.isType(OWLS.Expression.SWRL_Expression) ||
                        ind.isType(OWLS.Expression.SWRL_Condition) ||
                    ind.hasProperty(OWLS.Expression.expressionLanguage, OWLS.Expression.SWRL);

                }
                else {
                    return  ind.isType(OWLS.Expression.SWRL_Condition) ||
                        (ind.isType(OWLS.Expression.Condition) &&
                        ind.hasProperty(OWLS.Expression.expressionLanguage, OWLS.Expression.SWRL));
                    
                }
//                    if(ind.equals(OWLS.Expression.AlwaysTrue))
//                   ind.isType(specificClass) ||
//				  (ind.isType(owlClass) && ind.hasProperty(OWLS.Expression.expressionLanguage, specificValue)))
//				  return true;				   
			}
			
			return false;
		}
		
		public OWLObject cast(OWLObject object) {
		    if(!canCast(object))
		        throw new ConversionException("OWLObject " + object + " cannot be converted to " + owlClass);
		
			try {
			    if(convertToExpression)
			        return new SWRLExpressionImpl((OWLIndividual) object);
			    else
			        return new SWRLExpressionImpl((OWLIndividual) object);
            } catch(Exception e) {
                e.printStackTrace();
                throw new CastingException("OWLObject " + object + " cannot be converted to " + owlClass);
            }
		}

        public OWLObject convert(OWLObject object) {
            if(!canCast(object))
                ((OWLIndividual) object).addType(owlClass);
            
            return cast(object);
        }
	}

	public static class SWRLIndividualConverter implements OWLObjectConverter {
        public boolean canCast(OWLObject object) {
            return (object instanceof OWLIndividual);
        }

        public OWLObject cast(OWLObject object) {
		    if(!canCast(object))
		        throw new ConversionException("OWLObject " + object + " cannot be converted to " + SWRLIndividualObject.class);
		
		    return new SWRLIndividualImpl((OWLIndividual) object);
        }

        public OWLObject convert(OWLObject object) {
		    if(canCast(object))
		        cast(object);
		    
            throw new NotImplementedException();
        }	    
	}

	public static class SWRLDataValueConverter implements OWLObjectConverter {
	    public SWRLDataValueConverter() {
	    }
	    
        public boolean canCast(OWLObject object) {
            return (object instanceof OWLDataValue);
        }

        public OWLObject cast(OWLObject object) {
		    if(!canCast(object))
		        throw new ConversionException("OWLObject " + object + " cannot be converted to SWRLDataValue");

		    
		    return new SWRLDataValueImpl((OWLDataValue) object);
        }

        public OWLObject convert(OWLObject object) {
		    if(canCast(object))
		        cast(object);
		    
            throw new NotImplementedException();
        }	    
	}
	
//	public static class ValueDataConverter implements OWLObjectConverter {
//	    public ValueDataConverter() {
//	    }
//	    
//        public boolean canCast(OWLObject object) {
//            return (object instanceof OWLDataValue);
//        }
//
//        public OWLObject cast(OWLObject object) {
//		    if(!canCast(object))
//		        throw new ConversionException("OWLObject " + object + " cannot be converted to ValueData");
//		    
//		    OWLDataValue literal = (OWLDataValue) object;
//	        OWLOntology test = OWLFactory.createKB().parseLiteral(literal.getLexicalValue(), null);
//	        OWLValue value = test.findTheIndividual(OWL.Thing);	        
//	        if(value == null)
//	            value = literal;
//	        
//	        return new ValueDataImpl(value);
//        }
//
//        public OWLObject convert(OWLObject object) {
//		    if(canCast(object))
//		        cast(object);
//		    
//            throw new NotImplementedException();
//        }	    
//	}
}

