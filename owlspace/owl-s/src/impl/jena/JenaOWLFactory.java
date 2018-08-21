/*
 * Created on Dec 12, 2004
 */
package impl.jena;

import impl.owl.OWLDataValueListImpl;
import impl.owl.OWLIndividualListImpl;

import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.mindswap.owl.OWLDataValueList;
import org.mindswap.owl.OWLFactory;
import org.mindswap.owl.OWLIndividualList;
import org.mindswap.owl.OWLKnowledgeBase;
import org.mindswap.owl.OWLModel;
import org.mindswap.owl.OWLOntology;
import org.mindswap.owl.OWLReader;
import org.mindswap.owl.OWLWriter;
import org.mindswap.pellet.jena.PelletReasoner;
import org.mindswap.query.ABoxQueryParser;

import com.hp.hpl.jena.reasoner.ReasonerRegistry;

/**
 * @author Evren Sirin
 */
public class JenaOWLFactory implements OWLFactory.Interface {
    private Map reasoners;
    
    private OWLKnowledgeBase kb;
    
    /**
     * 
     */
    public JenaOWLFactory() {
        reasoners = new HashMap();
        
        // register the Jena built-in reasoners
        reasoners.put("RDFS", ReasonerRegistry.getRDFSReasoner());
        reasoners.put("RDFS-Simple", ReasonerRegistry.getRDFSSimpleReasoner());
        reasoners.put("Transitive", ReasonerRegistry.getTransitiveReasoner());
        reasoners.put("OWL", ReasonerRegistry.getOWLReasoner());
        reasoners.put("OWL-Mini", ReasonerRegistry.getOWLMiniReasoner());
        reasoners.put("OWL-Micro", ReasonerRegistry.getOWLMicroReasoner());
        reasoners.put("DIG", ReasonerRegistry.getDIGReasoner());
        
        // register Pellet reasoner
        reasoners.put("Pellet", new PelletReasoner());
    }
    
    public Map getReasoners() {
        return Collections.unmodifiableMap(reasoners);
    }
    
    public Object getReasoner(String reasonerName) {
        return reasoners.get(reasonerName);
    }

    public OWLReader createReader() {
        return new OWLReaderImpl();
    }

    public OWLWriter createWriter() {
        return new OWLWriterImpl();
    }

    private OWLKnowledgeBase getKB() {
        if(kb == null) {
            kb = createKB();
            kb.setAutoTranslate(false);
        }
        return kb;
    }
    
    public OWLOntology createOntology() {
        return getKB().createOntology();
    }
    
    public OWLOntology createOntology(URI uri) {
        return getKB().createOntology(uri);
    }
    
    public OWLKnowledgeBase createKB() {
        return new OWLKnowledgeBaseImpl();
    }

    public OWLDataValueList createDataValueList() {
         return new OWLDataValueListImpl();
    }

    public OWLIndividualList createIndividualList() {
        return new OWLIndividualListImpl();
    }

    public ABoxQueryParser createRDQLParser(OWLModel model) {
        return new RDQLParser(model);
    }

    public Map getDefaultConverters() {
        return OWLConverters.getConverters();
    }
}
