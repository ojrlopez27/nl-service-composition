/*
 * Created on Jan 6, 2005
 */
package examples;

import impl.owl.WrappedIndividual;
import impl.owls.profile.ProfileImpl;

import java.util.Iterator;

import org.mindswap.exceptions.ConversionException;
import org.mindswap.owl.OWLConfig;
import org.mindswap.owl.OWLFactory;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLIndividualList;
import org.mindswap.owl.OWLKnowledgeBase;
import org.mindswap.owl.OWLObject;
import org.mindswap.owl.OWLObjectConverter;
import org.mindswap.owls.profile.Profile;
import org.mindswap.owls.profile.ServiceParameter;
import org.mindswap.owls.service.Service;
import org.mindswap.owls.vocabulary.FLAServiceOnt;
import org.mindswap.owls.vocabulary.OWLS;

/**
 * An example demonstrating the extensibility features of the API. 
 * 
 * This example shows how the default Profile implementation can be extended to support
 * custom defined extensions of the . 
 *   
 * 
 * @author Evren Sirin
 *
 */
public class OWLSExtensions {
    public static void main(String[] args) throws Exception {
        OWLSExtensions test = new OWLSExtensions();
        test.run();        
    }
    
    public void run() throws Exception {
    	// register a converter for Owner class
        OWLConfig.addConverter(OwnerEntity.class, new OwnerConverter());
        // Override the default Profile converter to return ExtendedProfile descriptions
        OWLConfig.addConverter(Profile.class, new ExtendedProfileConverter());
        
        // Create a KB
        OWLKnowledgeBase kb = OWLFactory.createKB();
        // use a reasoner that will understand class and property inheritance
        kb.setReasoner("Pellet");
        
        // Load an example service description
        Service s = kb.readService("http://www.mindswap.org/2004/owl-s/1.1/Dictionary.owl");
        // Cast the profile to an ExtendedProfile (since default converter is overridden)
        ExtendedProfile profile = (ExtendedProfile) s.getProfile();
        // Get the owner info
        OwnerEntity owner = profile.getOwner();
        
        // Print the results
        System.out.println("Service name: " + profile.getServiceName());
        System.out.println();
        
        System.out.println("Display service parameters using generic functions");
        System.out.println("--------------------------------------------------");
        OWLIndividualList params = profile.getServiceParameters();
        for(Iterator i = params.iterator(); i.hasNext();) {
            ServiceParameter param = (ServiceParameter) i.next();
            System.out.println("Service Parameter: ");
            System.out.println("  Name  : "  + param.getName());
            System.out.println("  Value : "  + param.getParameter());
        }
        System.out.println();
        
        System.out.println("Display service parameters using custom functions");
        System.out.println("-------------------------------------------------");
        System.out.println("Owner: ");
        System.out.println("  Name : "  + owner.getLabel());
        System.out.println("  ID   : "  + owner.getEntityID());
    }
    
    /**
     * An extension to existing Profile implmentation to return contact information.
     *
     */
    public static class ExtendedProfile extends ProfileImpl {
        /**
         * Wrap the given OWLIndividual as a profile instance.
         * 
         * @param ind
         */
        public ExtendedProfile(OWLIndividual ind) {
            super(ind);
        }
        
        /**
         * Simply query the ontology for contactInformation property and cast the
         * result to an Actor object.
         * 
         * @return
         */
        public OwnerEntity getOwner() {
            OWLIndividual ind = getServiceParameterValue(FLAServiceOnt.ownedBy);
            return (ind == null ) ? null : (OwnerEntity) ind.castTo(OwnerEntity.class);
        }
    }
    
    /**
     * A wrapper around an OWLIndividual that defines utility functions to access 
     * Owner information defined in FLA ontology.
     *
     */
    public static class OwnerEntity extends WrappedIndividual  {
        /**
         * @param ind
         */
        public OwnerEntity(OWLIndividual ind) {
            super(ind);
        }
        
        public String getEntityID() {
            return getPropertyAsString(FLAServiceOnt.ownerEntityID);
        }
    }
    
    /**
     * A simple converter that will wrap an existing OWLIndividual as an Actor objects. The 
     * individual should have rdf:type Actor triple in order the wrapping work. The convert
     * function will automatically add this triple but should onyl be used if the ontology
     * needs to be updated.
     *
     */
    public static class OwnerConverter implements OWLObjectConverter {
        public boolean canCast(OWLObject object) {
			return (object instanceof OWLIndividual) &&
			   	   ((OWLIndividual) object).isType(FLAServiceOnt.OwnerEntity);
        }

        public OWLObject cast(OWLObject object) {
		    if(!canCast(object))
		        throw new ConversionException("OWLObject " + object + " cannot be cast to Actor class");
		
            return new OwnerEntity((OWLIndividual) object);
        }      
    }
    
    /**
     * A simple converter that will wrap existing OWLIndividuals as ExtendedProfile objects.
     * Very similar to the default Profile converter. 
     *
     */
    public static class ExtendedProfileConverter implements OWLObjectConverter {
        public boolean canCast(OWLObject object) {
			return (object instanceof OWLIndividual) &&
			   	   ((OWLIndividual) object).isType(OWLS.Profile.Profile);
        }

        public OWLObject cast(OWLObject object) {
		    if(!canCast(object))
		        throw new ConversionException("OWLObject " + object + " cannot be cast to Profile class");
		
            return new ExtendedProfile((OWLIndividual) object);
        }

        public OWLObject convert(OWLObject object) {
            if(!canCast(object))
                ((OWLIndividual) object).addType(OWLS.Profile.Profile);
            
            return cast(object);
        }        
    }
}
