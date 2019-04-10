/*
 * Created on Oct 13, 2004
 */
package examples;

import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.mindswap.owl.OWLFactory;
import org.mindswap.owl.OWLKnowledgeBase;
import org.mindswap.owl.OWLOntology;
import org.mindswap.owl.OWLType;
import org.mindswap.owls.process.Input;
import org.mindswap.owls.process.Output;
import org.mindswap.owls.service.Service;
import org.mindswap.query.ValueMap;

/**
 * Source: https://www.programcreek.com/java-api-examples/index.php?source_dir=pfyuit.semtools-master/SemTools/src/org/yubuaa/discovery/Matchmaker.java
 *
 * An example that finds service matches for composition. The outputs of services are matched with 
 * the inputs of services using one of EXACT, SUBSUME and RELAXED match criteria. Pellet reasoner is
 * used to find matches but can be replaced with any other reasoner. 
 * 
 * @author Evren Sirin
 */
public class Matchmaker {    
    OWLKnowledgeBase kb;
    
    public static class Match {
        public static String[] MATCHES = {"EXACT", "SUBSUME", "RELAXED", "FAIL"};
        public static int EXACT   = 0;
        public static int SUBSUME = 1;
        public static int RELAXED = 2;
        public static int FAIL    = 3;        
        
        int matchType;
        boolean listMatch;
        Service outputService;
        Output output;
        Service inputService;
        Input input;
        
        public Match(int matchType, Output output, Input input) {
            this.matchType = matchType;
            this.outputService = output.getService();
            this.output = output;
            this.inputService = input.getService();
            this.input = input;
        }
        
        public String toString() {
            String str = "";
            
            str += MATCHES[matchType] + " ";
            if(listMatch)
                str += ".LIST";
            str += outputService.getLocalName() + "." + output.getLocalName();
            str += " -> "; 
            str += inputService.getLocalName() + "." + input.getLocalName();
            
            return str;
        }
    }
    
    public Matchmaker() {
        kb = OWLFactory.createKB();
		
  	    kb.setReasoner("Pellet");
    }

    public void addOntology( String ont )  throws FileNotFoundException, URISyntaxException {
        System.out.println( "Reading " + ont );
        kb.read( new URI( ont ) );
    }
    
    public void addOntology( URI ont )  throws FileNotFoundException {
        System.out.println( "Reading " + ont );
        kb.read( ont );
    }
    
    public List findServices(boolean getProducers) {
        String hasParameter = getProducers ? "process:hasOutput" : "process:hasInput";
        
        String queryString = 
            "SELECT * " +            
            "WHERE " +
            "	   (?process rdf:type process:Process)" +
            "	   (?process " + hasParameter + " ?param)" +
            "USING " +
            "      process FOR <http://www.daml.org/services/owl-s/1.1/Process.owl#>";
 
        System.out.println(queryString);
        
        Set ontologySet = kb.getOntologies();
        for (Object ontology : ontologySet) {
        	OWLOntology owlOntology = (OWLOntology) ontology;
        	//System.out.println("Onto: " + owlOntology);
        	//System.out.println("Onto Individuals: " + owlOntology.getIndividuals());
        }
        System.out.println("Ontologies: " + kb.getOntologies());
        System.out.println("Profiles: " + kb.getProfiles());
        System.out.println("Processes: " + kb.getProcesses());
        
        return kb.query( queryString );
    }

    public List findOutputs() {
        return findServices(true);
    }
    
    public List findInputs() {
        return findServices(false);        
    }
    
    public int getMatchType(OWLType outputType, OWLType inputType) {
        if(outputType.isEquivalent(inputType))
           return Match.EXACT;
        else if(outputType.isSubTypeOf(inputType))
           return Match.SUBSUME;        
        else if(inputType.isSubTypeOf(outputType)) 
            return Match.RELAXED;
        else
            return Match.FAIL;
    }

	public List displayAllMatches() {
		List matches = new ArrayList();
		
		System.out.println( "Computing matches..." );
		
		List producers = findOutputs();
		List consumers = findInputs();
		
		System.out.println("Producers: " + producers);
		System.out.println("Consumers: " + consumers);
		
		Iterator i = producers.iterator();
		while( i.hasNext() ) {
		    ValueMap binding = (ValueMap) i.next();
		    Output output = (Output) binding.getIndividualValue("param").castTo(Output.class);
		    OWLType outputType = output.getParamType();
		    
		    Iterator j = consumers.iterator();
		    while( j.hasNext() ) {
		        binding = (ValueMap) j.next() ;
			    Input input = (Input) binding.getIndividualValue("param").castTo(Input.class);
			    OWLType inputType = input.getParamType();
			    
//			    System.out.println("Trying " + 
//			        URIUtils.getLocalName(outputType.getURI()) + " " + 
//			        URIUtils.getLocalName(inputType.getURI()) + " " + 
//			        producer.getLocalName() + " " + 
//			        output.getLocalName() + " " + 
//			        consumer.getLocalName() + " " + 
//			        input.getLocalName()
//			    );
			    
			    System.out.println("Checking Match... " + 
			    		outputType + " :: " + inputType + " :: " + output + " :: " + input);
			    
		        int matchType = getMatchType(outputType, inputType);
		        if(matchType != Match.FAIL)
		            matches.add(new Match(matchType, output, input));			        
		    }
		}
		
		return matches;
	}

    public static void printIterator(Iterator i) {
        if(i.hasNext()) {
	        while (i.hasNext()) 
	            System.out.println( i.next() );
        }       
        else
            System.out.println("<EMPTY>");
        
        System.out.println();
    }
	
    public void run() throws FileNotFoundException, URISyntaxException {
    	
        Matchmaker matchmaker = new Matchmaker();

        /* 
         * Original example that doesn't work 
         * because the mindswap.org domain doesn't exist anymore.
		/*
	        matchmaker.addOntology("http://www.mindswap.org/2004/owl-s/1.1/BNPrice.owl");
	        matchmaker.addOntology("http://www.mindswap.org/2004/owl-s/1.1/BookFinder.owl");
	        matchmaker.addOntology("http://www.mindswap.org/2004/owl-s/1.1/CurrencyConverter.owl");
	        matchmaker.addOntology("http://www.mindswap.org/2004/owl-s/1.1/Dictionary.owl");
	        matchmaker.addOntology("http://www.mindswap.org/2004/owl-s/1.1/ZipCodeFinder.owl");
	        matchmaker.addOntology("http://www.mindswap.org/2004/owl-s/1.1/FindLatLong.owl");
	        matchmaker.addOntology("http://www.mindswap.org/2004/owl-s/1.1/BabelFishTranslator.owl#");
        */
        
        /*
         * Example that runs and matches the output of BookFinder with the input of BookPrice.
         * An EXACT matches yields us for: BookFinderService.BookInfo -> BNPriceService.BookInfo.
         * 
         * Note: OWL-S files downloaded from this URL:
         * https://gitlab.com/universAAL/service/tree/e554aeb509fd4c2f62ef6644764cd29dcf5ae961/owls-api/src/examples
         */
		matchmaker.addOntology(ExampleURIs.BN_BOOK_PRICE_OWLS11);
		matchmaker.addOntology(ExampleURIs.BOOK_FINDER_OWLS11);

		/*
		 * Q: What if we add another service that has the same types. Could it lead to inconsistencies?
		 *    That is, if we had more than one service that matches with same input-output type pairs. 
		 * A: Yes, it does output more than one service. There is no weighted or probabilistic match to pick the best one.
		 */
		matchmaker.addOntology(ExampleURIs.AMAZON_BOOK_PRICE_OWLS11);

		/*
		 * Q: Does it consider type hierarchies? 
		 * A: If EXACT match is sought, it does not. Refer getMatchType() in this class. 
		 *    However, for SUBSUME and RELAXED matches, it considers subtypes.
		 */
		
		/*
		 * Example from the OWL-S Walkthrough link:
		 * http://www.ai.sri.com/daml/services/owl-s/1.2/OWL-S-walkthru.html
		 * 
		 * The MatchMaker doesn't work on this because of either of the following reasons:
		 * a) It refers to OWL-S 1.2 whereas the current revision of MatchMaker works with OWL-S 1.1
		 * b) Fails to parse OWL-S file because it depends on another OWL-S file:
		 *    http://www.isi.edu/~pan/damltime/time-entry.owl
		 *    that has inconsistencies: eg: "White spaces are required between publicId and systemId."
		 * c) The Service, Profile, Process and Grounding for a bunch of services such as  
		 * 	  LocateBook, PutInCart, SignIn, etc. are separated by roles in the 4 OWL-S files, 
		 *    whereas, in the examples above these are separated by services and each service has
		 *    its own corresponding role. 
		 */
		/*
			String congoBase = "http://www.ai.sri.com/daml/services/owl-s/1.2/";
			matchmaker.addOntology(congoBase + "CongoService.owl");
			matchmaker.addOntology(congoBase + "CongoProfile.owl");
			matchmaker.addOntology(congoBase + "CongoProcess.owl");
			matchmaker.addOntology(congoBase + "CongoGrounding.owl");
		*/
		
        List matches = matchmaker.displayAllMatches();
        System.out.println();
        System.out.println("Matches:");        
        printIterator(matches.iterator());
        System.out.println();
    }
    

    public void runSC() throws FileNotFoundException, URISyntaxException {
    	
        Matchmaker matchmaker = new Matchmaker();

        /*
         * Examples from semantic middleware's services. This doesn't work because 
         * it doesn't have the owl:Ontology tag that other services in run() have. 
         * Note: 1) the xml:base and xmlns attributes of rdf:RDF tag need to be renamed.
         *  	 2) this example has custom POJO types.
         */
	    /*
        	final String baseLoc = "http://dangiankit.ddns.net/~dangiankit/owls/";
	        matchmaker.addOntology(baseLoc + "WeatherChannelService/checkWeatherConditions.owl");
	        matchmaker.addOntology(baseLoc + "GoogleCalendarService/checkAvailability.owl");
	        matchmaker.addOntology(baseLoc + "GoogleCalendarService/createEvent.owl");
	        matchmaker.addOntology(baseLoc + "GoogleFlightsService/bookFlight.owl");
	        matchmaker.addOntology(baseLoc + "GoogleFlightsService/searchFlight.owl");
	        matchmaker.addOntology(baseLoc + "SkyScannerService/bookFlight.owl");
	        matchmaker.addOntology(baseLoc + "SkyScannerService/searchFlight.owl");
	        matchmaker.addOntology(baseLoc + "YahooNewsService/getNewsFeed.owl");
	     */
        
       /*
        * Example of service composition where the user would like to 
        * search for a hotel and obtain the price of the hotel 
        * from two groundings: ExpediaPrice and KayakPrice  
        * and assuming this service is for European users, then, the price may be 
        * required to be presented to the user in EUROs as compared to USD. 
        * 
        * The matches returned are based on types and hence, 
        * it matched arbitrary parameters with return types.
        */
        final String baseLoc = "http://dangiankit.ddns.net/~dangiankit/owls/";
        matchmaker.addOntology(baseLoc + "ABookingsService/bookHotels.owl");
        matchmaker.addOntology(baseLoc + "ABookingsService/searchHotels.owl");
        matchmaker.addOntology(baseLoc + "ABookingsService/searchHotelsByID.owl");
        matchmaker.addOntology(baseLoc + "AExpediaPriceService/hotelPrices.owl");
        matchmaker.addOntology(baseLoc + "AKayakPriceService/hotelPrices.owl");
        matchmaker.addOntology(baseLoc + "ACurrencyConverterService/convertToEuros.owl");

        List matches = matchmaker.displayAllMatches();
        System.out.println();
        System.out.println("Matches:");        
        printIterator(matches.iterator());
        System.out.println();
    }
    public static void main(String[] args) throws FileNotFoundException, URISyntaxException {
    	Matchmaker test = new Matchmaker();
    	test.run();
    	test.runSC();
    }
}
