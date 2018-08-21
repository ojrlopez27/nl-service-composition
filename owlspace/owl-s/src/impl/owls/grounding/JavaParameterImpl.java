package impl.owls.grounding;

import org.mindswap.owl.OWLIndividual;
import org.mindswap.owls.grounding.JavaParameter;
import org.mindswap.owls.vocabulary.MoreGroundings;

public class JavaParameterImpl extends JavaVariableImpl implements
		JavaParameter {

	public JavaParameterImpl(OWLIndividual ind) {
		super(ind);
	}

	public String getParameterIndex() {
		if (hasProperty(MoreGroundings.paramIndex))
			return getProperty(MoreGroundings.paramIndex).getLexicalValue();
		return "";
	}

	public void removeParameterIndex() {
		if (hasProperty(MoreGroundings.paramIndex))
			removeProperties(MoreGroundings.paramIndex);
	}

	public void setParameterIndex(int index) {
		setProperty(MoreGroundings.paramIndex, index);
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
