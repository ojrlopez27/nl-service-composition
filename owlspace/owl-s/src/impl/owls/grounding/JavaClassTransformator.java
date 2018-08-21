package impl.owls.grounding;

import org.mindswap.owl.OWLClass;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLKnowledgeBase;
import org.mindswap.owl.OWLTransformator;
import org.mindswap.owl.OWLValue;

/**
 * 
 * @author Michael Dänzer (University of Zurich)
 * @date 20.03.2007
 */
public abstract class JavaClassTransformator implements OWLTransformator {
	protected OWLClass owlClass;
	protected Class javaClass;
	protected OWLKnowledgeBase kb;	

	public JavaClassTransformator(OWLKnowledgeBase kb) {
		super();		
		setKB(kb);
	}
	
	public void setKB(OWLKnowledgeBase kb) {
		this.kb = kb;
	}
	
	public void setOWLClass(OWLClass owlClass) {
		this.owlClass = owlClass;		
	}
	
	public void setJavaClass(Class javaClass) {
		this.javaClass = javaClass;		
	}
	
	public OWLClass getOWLClass() { 
		return owlClass;
	}
	
	public Class getJavaClass() {
		return javaClass;
	}
	
	public abstract Object transformFromOWL(OWLValue ind);

	public abstract OWLValue transformToOWL(Object object);
	
	protected OWLIndividual getIndividual(OWLValue value) {
		if (value.isDataValue())
			return null;
		else
			return (OWLIndividual) value.castTo(OWLIndividual.class);
	}
}
