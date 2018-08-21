package examples;
/*
 * Created on Jan 6, 2005
 */


import impl.owls.service.ServiceImpl;

import java.net.URI;

import org.mindswap.exceptions.ConversionException;
import org.mindswap.owl.EntityFactory;
import org.mindswap.owl.OWLConfig;
import org.mindswap.owl.OWLDataProperty;
import org.mindswap.owl.OWLFactory;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLKnowledgeBase;
import org.mindswap.owl.OWLObject;
import org.mindswap.owl.OWLObjectConverter;
import org.mindswap.owl.OWLOntology;
import org.mindswap.owls.grounding.Grounding;
import org.mindswap.owls.process.AtomicProcess;
import org.mindswap.owls.process.Process;
import org.mindswap.owls.profile.Profile;
import org.mindswap.owls.service.Service;
import org.mindswap.owls.vocabulary.OWLS;
import org.mindswap.utils.URIUtils;

/**
 * Another example demonstrating the extensibility features of the API. 
 * 
 * This example shows how the default Service implementation can be extended to support
 * custom defined extensions of the service. 
 *   
 * @author Michael Daenzer (University of Zurich)
 *
 */
public class ServiceExtension {
	public static final URI baseURI = URI.create("http://www.ifi.unizh.ch/ddis/ont/owl-s/ServiceExtensionTest.owl#");
	
    public static void main(String[] args) throws Exception {
        ServiceExtension test = new ServiceExtension();
        test.runRead();
        System.out.println("");
        test.runWrite();
    }
    
    public void runWrite() throws Exception {
    	// Override the default Service converter to return ExtendedService descriptions    	     	
        OWLConfig.addConverter(Service.class, new ExtendedServiceConverter());
        OWLConfig.addConverter(ExtendedService.class, new ExtendedServiceConverter());
        
        // Create a KB and an ontology
        OWLKnowledgeBase kb = OWLFactory.createKB();
        kb.setReasoner("Pellet");                               
        OWLOntology ont = kb.createOntology();

        // create extended service
        Service s = ont.createService(URIUtils.createURI(baseURI, "TestService"));        
        ExtendedService service = (ExtendedService) s.castTo(ExtendedService.class); 
        AtomicProcess process = ont.createAtomicProcess(URIUtils.createURI(baseURI, "TestProcess"));
        Profile profile = ont.createProfile(URIUtils.createURI(baseURI, "TestProfile"));
        Grounding grounding = ont.createGrounding(URIUtils.createURI(baseURI, "TestGrounding"));
        service.setProfile(profile);
        service.setProcess(process);
        service.setGrounding(grounding);  
        service.setAdditionalProperty("test");
        
        // Print the results
        System.out.println("Wrote and re-read service:");
        System.out.println("Service name: " + service.getLocalName());
        System.out.println("Profile name: " + service.getProfile().getLocalName()); 
        System.out.println("Process name: " + service.getProcess().getLocalName());
        System.out.println("Grounding name: " + service.getGrounding().getLocalName());
        System.out.println("additional property: " + service.getAdditionalProperty());    
    }
        
    public void runRead() throws Exception {
    	// Override the default Profile converter to return ExtendedProfile descriptions    	  
        OWLConfig.addConverter(Service.class, new ExtendedServiceConverter());
        
        // Create a KB
        OWLKnowledgeBase kb = OWLFactory.createKB();
        // use a reasoner that will understand class and property inheritance
        kb.setReasoner("Pellet");                       
                
        // Load an example service description        
        Service s = (Service) kb.readService("http://www.ifi.unizh.ch/ddis/ont/owl-s/ServiceExtensionTest.owl#");
        ExtendedService service = (ExtendedService) s;                    
        Profile profile = service.getProfile();
        Process process = service.getProcess(); 
        Grounding grounding = service.getGrounding();
        String additionalProperty = service.getAdditionalProperty();
               
        // Print the results
        System.out.println("Read service:");
        System.out.println("Service name: " + service.getLocalName());
        System.out.println("Profile name: " + profile.getLocalName()); 
        System.out.println("Process name: " + process.getLocalName());
        System.out.println("Grounding name: " + grounding.getLocalName());
        System.out.println("additional property: " + additionalProperty);                       
    }
    
    
    /**
     * An extension to existing Profile implmentation to return contact information.
     *
     */
    public static class ExtendedService extends ServiceImpl {
    	OWLDataProperty additionalProperty = EntityFactory.createDataProperty(
    			URIUtils.createURI((
    					"http://www.ifi.unizh.ch/ddis/ont/owl-s/ServiceExtensionTest.owl#additionalProperty")));

        public ExtendedService(OWLIndividual ind) {
            super(ind);
        }        
        
        public String getAdditionalProperty() {
        	return getPropertyAsString(additionalProperty);
        }
        
        public void setAdditionalProperty(String value) {
        	setProperty(additionalProperty, value);
        }
    }
    
    /**
     * A simple converter that will wrap existing OWLIndividuals as ExtendedProfile objects.
     * Very similar to the default Profile converter. 
     *
     */
    public static class ExtendedServiceConverter implements OWLObjectConverter {
        public boolean canCast(OWLObject object) {
			return (object instanceof OWLIndividual) &&
			   	   ((OWLIndividual) object).isType(OWLS.Service.Service);
        }

        public OWLObject cast(OWLObject object) {
		    if(!canCast(object))
		        throw new ConversionException("OWLObject " + object + " cannot be cast to Service class");
		
            return new ExtendedService((OWLIndividual) object);
        }

        public OWLObject convert(OWLObject object) {            
            return cast(object);
        }        
    }      
}
