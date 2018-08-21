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

import org.mindswap.owl.OWLFactory;
import org.mindswap.owl.OWLKnowledgeBase;
import org.mindswap.owl.OWLType;
import org.mindswap.owls.process.Input;
import org.mindswap.owls.process.Output;
import org.mindswap.owls.service.Service;
import org.mindswap.query.ValueMap;

/**
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
        
        matchmaker.addOntology("http://www.mindswap.org/2004/owl-s/1.1/BNPrice.owl");
        matchmaker.addOntology("http://www.mindswap.org/2004/owl-s/1.1/BookFinder.owl");
        matchmaker.addOntology("http://www.mindswap.org/2004/owl-s/1.1/CurrencyConverter.owl");
        matchmaker.addOntology("http://www.mindswap.org/2004/owl-s/1.1/Dictionary.owl");
        matchmaker.addOntology("http://www.mindswap.org/2004/owl-s/1.1/ZipCodeFinder.owl");
        matchmaker.addOntology("http://www.mindswap.org/2004/owl-s/1.1/FindLatLong.owl");
        matchmaker.addOntology("http://www.mindswap.org/2004/owl-s/1.1/BabelFishTranslator.owl#");
        
        List matches = matchmaker.displayAllMatches();
        System.out.println();
        System.out.println("Matches:");        
        printIterator(matches.iterator());
    }
    
    public static void main(String[] args) throws FileNotFoundException, URISyntaxException {
    	Matchmaker test = new Matchmaker();
    	test.run();
    }
}
