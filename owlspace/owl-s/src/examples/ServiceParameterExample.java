/*
 * Created on Apr 14, 2005
 */
package examples;

import java.util.Iterator;

import org.mindswap.owl.OWLConfig;
import org.mindswap.owl.OWLFactory;
import org.mindswap.owl.OWLIndividualList;
import org.mindswap.owl.OWLKnowledgeBase;
import org.mindswap.owls.profile.Profile;
import org.mindswap.owls.profile.ServiceCategory;
import org.mindswap.owls.profile.ServiceParameter;
import org.mindswap.owls.service.Service;

/**
 * This example shows how to access <code>ServiceParameter</code> and <code>ServiceCategory</code>
 * values.  Different reasoners will provide different information depending on their capabilities.
 * When there is no reasoner associated with the KB, no information about profile can be retrieved
 * because it cannot be verified that profile is actually an instance of profile:Profile concept.
 * If strict type checking is disabled using <code>OWLConfig.setStrictConversion( false )</code>
 * then some information can be retrieved.   
 * 
 * @author Evren Sirin
 *
 */
public class ServiceParameterExample {
    static OWLKnowledgeBase kb = OWLFactory.createKB();

    public static void main(String[] args) throws Exception {       
    	ServiceParameterExample test = new ServiceParameterExample();
    	test.run();
    }
        
    public void run() throws Exception {
        System.out.print("Reading service...");
        Service service = kb.readService("http://www.daml.org/services/owl-s/1.1/BravoAirService.owl");
        System.out.println("done");
                       
        printProfileInfo( service, null );

        printProfileInfo( service, "RDFS" );
                
        printProfileInfo( service, "Pellet" );
        
        OWLConfig.setStrictConversion( false );
        
        printProfileInfo( service, null );
    }
    
    public static void printProfileInfo( Service s, String reasoner ) {
        try {
            String name = reasoner == null ? "no" : reasoner;
            System.out.println( "===================================" ); 
            System.out.println( "Results using " + name + " reasoner" );
            System.out.println( "Strict type checking: " + OWLConfig.getStrictConversion() );
            System.out.println( "===================================" ); 
            
            kb.setReasoner( reasoner );
            
            Profile profile = s.getProfile();
            System.out.println("Service name: " + profile.getServiceName());
            System.out.println();
            
            // Print the ServiceParameters
            System.out.println("Display service parameters");
            System.out.println("--------------------------");        
            OWLIndividualList params = profile.getServiceParameters();
            for(Iterator i = params.iterator(); i.hasNext();) {
                ServiceParameter param = (ServiceParameter) i.next();
                System.out.println("Service Parameter: ");
                System.out.println("  Name  : "  + trimString( param.getName() ));
                System.out.println("  Value : "  + param.getParameter());
            }     
            System.out.println();
            
            // Print the ServiceCategories
            System.out.println("Display service categories");
            System.out.println("--------------------------");        
            OWLIndividualList categories = profile.getCategories();
            for(Iterator i = categories.iterator(); i.hasNext();) {
                ServiceCategory category = (ServiceCategory) i.next();
                System.out.println("Service Category: ");
                System.out.println("  Name  : "  + trimString( category.getName() ));
                System.out.println("  Code : "  + trimString( category.getCode() ));
                System.out.println("  Taxonomy : "  + trimString( category.getTaxonomy() ));
                System.out.println("  Value : "  + trimString( category.getValue() ));
            }
        } catch(RuntimeException e) {
            System.out.println("The following error occurred: ");
            System.out.println(e);
        }         
        System.out.println(); 
    }
    
    public static String trimString( String str ) {
        return str == null ? "<null>" : str.trim();
    }
}
