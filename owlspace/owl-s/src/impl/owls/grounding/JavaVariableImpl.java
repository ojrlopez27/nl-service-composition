package impl.owls.grounding;

import impl.owl.WrappedIndividual;

import org.mindswap.owl.OWLIndividual;
import org.mindswap.owls.grounding.JavaVariable;
import org.mindswap.owls.process.Parameter;
import org.mindswap.owls.vocabulary.MoreGroundings;
import org.mindswap.owls.vocabulary.OWLS;

public class JavaVariableImpl extends WrappedIndividual implements JavaVariable {
	public JavaVariableImpl(OWLIndividual ind) {
		super(ind);
	}

	public String getJavaType() {
		return getPropertyAsString(MoreGroundings.javaType);
	}

	public Parameter getOWLSParameter() {
		return (Parameter) getPropertyAs(MoreGroundings.owlsParameter, Parameter.class);
	}

	public void removeJavaType() {
		if (hasProperty(MoreGroundings.javaType))
			removeProperties(MoreGroundings.javaType);
	}

	public void removeOWLSParameter() {
		if (hasProperty(MoreGroundings.owlsParameter))
			removeProperties(MoreGroundings.owlsParameter);
	}

	public void setJavaType(String type) {
		setProperty(MoreGroundings.javaType, type);
	}

	public void setOWLSParameter(Parameter parameter) {
		setProperty(OWLS.Grounding.owlsParameter, parameter);
	}

	public String getTransformator() {
		if (hasProperty(MoreGroundings.transformatorClass))
			return getProperty(MoreGroundings.transformatorClass).getLexicalValue();
		return "";
	}

	public void removeTransformator() {
		if (hasProperty(MoreGroundings.transformatorClass))
			removeProperties(MoreGroundings.transformatorClass);
	}

	public void setTransformator(String transformator) {
		setProperty(MoreGroundings.transformatorClass, transformator);
	}
	
}
