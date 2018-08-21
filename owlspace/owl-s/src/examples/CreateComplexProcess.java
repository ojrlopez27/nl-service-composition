/*
 * Created on Sep 9, 2005
 */

package examples;
import java.net.URI;

import org.mindswap.owl.EntityFactory;
import org.mindswap.owl.OWLClass;
import org.mindswap.owl.OWLDataProperty;
import org.mindswap.owl.OWLDataType;
import org.mindswap.owl.OWLFactory;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLKnowledgeBase;
import org.mindswap.owl.OWLOntology;
import org.mindswap.owl.vocabulary.XSD;
import org.mindswap.owls.OWLSFactory;
import org.mindswap.owls.grounding.Grounding;
import org.mindswap.owls.process.AtomicProcess;
import org.mindswap.owls.process.CompositeProcess;
import org.mindswap.owls.process.Condition;
import org.mindswap.owls.process.IfThenElse;
import org.mindswap.owls.process.Input;
import org.mindswap.owls.process.Local;
import org.mindswap.owls.process.Output;
import org.mindswap.owls.process.Perform;
import org.mindswap.owls.process.Process;
import org.mindswap.owls.process.ProcessList;
import org.mindswap.owls.process.Produce;
import org.mindswap.owls.process.Result;
import org.mindswap.owls.process.Sequence;
import org.mindswap.owls.process.SplitJoin;
import org.mindswap.owls.process.ValueData;
import org.mindswap.owls.process.execution.ProcessExecutionEngine;
import org.mindswap.owls.process.execution.SimpleProcessMonitor;
import org.mindswap.owls.profile.Profile;
import org.mindswap.owls.service.Service;
import org.mindswap.query.ValueMap;
import org.mindswap.swrl.Atom;
import org.mindswap.swrl.AtomList;
import org.mindswap.swrl.SWRLDataObject;
import org.mindswap.swrl.SWRLFactory;
import org.mindswap.swrl.SWRLFactoryCreator;
import org.mindswap.utils.URIUtils;

/**
 * <p>
 * This example shows how to create a complex process model that is shown in the file
 * <a href="http://www.mindswap.org/2004/owl-s/1.1/FindCheaperBook.owl">FindCheaperBook.owl
 * </a>. The process model contains a sequence that starts with a service that gets the 
 * price for a given book title, then has a SplitJoin to get the book price from Amazon and 
 * Barnes&Nobles concurrently, and as a last step has an If-Then-Else construct to compare 
 * the prices and return the cheaper book price.
 * 
 * @author Evren Sirin
 *  
 */
public class CreateComplexProcess {
    public static final URI baseURI = URI.create( "http://www.example.org/FindCheaperBook.owl#" );
    public static final URI concepts = URI.create( "http://www.mindswap.org/2004/owl-s/concepts.owl#" );

    // knowledge base we are going to use
    OWLKnowledgeBase kb;

    // ontology where service descirption is saved
    OWLOntology ont;
    
    // swrl factory to create consitions and expressions
    SWRLFactory swrl;
    
    // processes we will use
    Process BookFinder;
    Process BNPrice;
    Process AmazonPrice;
    
    // commonly used classes, properties, datatypes
    OWLDataType xsdString;
    OWLDataType xsdFloat;
    OWLClass Price;
    OWLDataProperty amount;
    
    // the process we create to do the comparison
    CompositeProcess ComparePricesProcess;
    
    // the inputs of the comparison process
    Input CP_AmazonPrice;        
    Input CP_BNPrice; 
    
    // the outputs of the comparison process
    Output CP_Bookstore;
    Output CP_OutputPrice;

    public CreateComplexProcess() {
    }

    private URI uri( String localName ) {
        return URIUtils.createURI( baseURI, localName );
    }

    private Service createService() throws Exception {
        Service service = ont.createService( uri( "TestService" ) );
        
        CompositeProcess process = createProcess();        
        Profile profile = createProfile( process );
        Grounding grounding = createGrounding( process );

        service.setProfile( profile );
        service.setProcess( process );
        service.setGrounding( grounding );
        
        return service;
    }

    private Profile createProfile( Process process ) {
        Profile profile = ont.createProfile( uri( "TestProfile" ) );

        profile.setLabel( "Cheaper Book Finder" );
        profile.setTextDescription( 
            "Find the price of book in Amazon and Barnes & Nobles " +
            "and return the cheaper price" );
        
        for(int i = 0; i < process.getInputs().size(); i++) {
            Input input = process.getInputs().inputAt( i );

            profile.addInput( input );
        }

        for(int i = 0; i < process.getOutputs().size(); i++) {
            Output output = process.getOutputs().outputAt( i );

            profile.addOutput( output );
        }

        return profile;
    }

    private Grounding createGrounding( CompositeProcess process ) {
        Grounding grounding = ont.createGrounding( uri( "TestGrounding" ) );
        
        ProcessList list = process.getComposedOf().getAllProcesses();
        for(int i = 0; i < list.size(); i++) {
            Process pc = list.processAt( i );
            if( pc instanceof AtomicProcess ) {
                grounding.addGrounding( ((AtomicProcess) pc).getGrounding() );
            }
        }
        
        return grounding;
    }
    
    private CompositeProcess createProcess() {        
        // create the composite process
        CompositeProcess process = ont.createCompositeProcess( uri( "TestProcess") );

        // Create an input that has parameterType xsd:string
        Input inputBookName = process.createInput( uri( "BookName" ), xsdString );
        
        // Create an output that has parameterType xsd:string
        Output outputBookstore = process.createOutput( uri( "Bookstore" ), xsdString );

        // Create an output that has parameterType concepts:Price
        Output outputBookPrice = process.createOutput( uri( "BookPrice" ), Price );
        
        // process is composed of a sequence
        Sequence sequence = ont.createSequence();
        process.setComposedOf(sequence);

        // first element of the sequence is a simple perform
        Perform FindBookInfo = ont.createPerform( uri( "FindBookInfo" ) );
        FindBookInfo.setProcess( BookFinder );
        // the input of the perform is coming from the parent perform
        FindBookInfo.addBinding( BookFinder.getInput(), Perform.TheParentPerform, inputBookName );
        
        // add thid perform as the first element of the sequence
        sequence.addComponent( FindBookInfo );
        
        // second element of the sequence is a Split-Join that has to performs in it
        SplitJoin split = ont.createSplitJoin();
        
        // first perform AmazonPrice
        Perform FindAmazonPrice = ont.createPerform( uri( "FindAmazonPrice" ) );
        FindAmazonPrice.setProcess( AmazonPrice );
        // the input of the perform is coming from FindBookInfo perform
        FindAmazonPrice.addBinding( AmazonPrice.getInput(), FindBookInfo, BookFinder.getOutput() );
        // add it to the split-join
        split.addComponent( FindAmazonPrice );
        
        // then similarly perform BNPrice
        Perform FindBNPrice = ont.createPerform( uri( "FindBNPrice" ) );
        FindBNPrice.setProcess( BNPrice );
        // the input of the perform is coming from FindBookInfo perform
        FindBNPrice.addBinding( BNPrice.getInput(), FindBookInfo, BookFinder.getOutput() );
        // add it to the split-join
        split.addComponent( FindBNPrice );
        
        // finally add the Split-Join to the sequence
        sequence.addComponent( split );
               	   	   
        CompositeProcess ComparePricesProcess = createComparePricesProcess();
        Perform ComparePrices = ont.createPerform( uri( "ComparePrices" ) );
        ComparePrices.setProcess( ComparePricesProcess );
        // feed the input from book previous performs to the comparison process
        ComparePrices.addBinding( CP_AmazonPrice, FindAmazonPrice, AmazonPrice.getOutput() );
        ComparePrices.addBinding( CP_BNPrice, FindBNPrice, BNPrice.getOutput() );
        
        // add the comparison step as the last construct in the sequence
        sequence.addComponent( ComparePrices );
                
        Result result = process.createResult();
        result.addBinding( outputBookstore, ComparePrices, CP_Bookstore );
        result.addBinding( outputBookPrice, ComparePrices, CP_OutputPrice );
        
        return process;
    }    
    
	private CompositeProcess createComparePricesProcess() {
        // we need a new composite process for the last step to do the
        // comparison (we need local variables whihc can only be declared 
        // in conjunction with a process)
        CompositeProcess ComparePricesProcess = 
            ont.createCompositeProcess( uri( "ComparePricesProcess" ) );
        
        // the price from two bookstores as input
        CP_AmazonPrice = ComparePricesProcess.createInput( uri( "CP_AmazonPrice"), Price );        
        CP_BNPrice = ComparePricesProcess.createInput( uri( "CP_BNPrice"), Price );

        // the actual value for each price as locals
        Local CP_AmazonPriceAmount = ComparePricesProcess.createLocal( uri( "CP_AmazonPriceAmount"), xsdFloat );
        Local CP_BNPriceAmount = ComparePricesProcess.createLocal( uri( "CP_BNPriceAmount"), xsdFloat );

        // the minimum price and the associated bookstore as outputs
        CP_OutputPrice = ComparePricesProcess.createOutput( uri( "CP_OutputPrice"), Price );
        CP_Bookstore = ComparePricesProcess.createOutput( uri( "CP_Bookstore"), xsdString );
        
        // the first precondition is just to bind the value of concepts:amount property
        // to the local variable for AmazonPrice
        Condition precondition1 = ont.createSWRLCondition();
        Atom atom1 = swrl.createDataPropertyAtom( amount, CP_AmazonPrice, CP_AmazonPriceAmount );
        AtomList list1 = swrl.createList( atom1 );
        precondition1.setBody( list1 );        
        ComparePricesProcess.addCondition( precondition1 );
        
        // exactly same as the previous one but for BNPrice. note that we could have
        // equivalently create one precondition and add two atoms in it. the operational
        // semantics would be same because multiple preconditions are simply conjuncted
        Condition precondition2 = ont.createSWRLCondition();
        Atom atom2 = swrl.createDataPropertyAtom( amount, CP_BNPrice, CP_BNPriceAmount );
        AtomList list2 = swrl.createList( atom2 );
        precondition2.setBody( list2 );
        ComparePricesProcess.addCondition( precondition2 );
            
        // ComparePricesProcess is simply one If-Then-Else
        IfThenElse ifThenElse = ont.createIfThenElse();
        ComparePricesProcess.setComposedOf( ifThenElse );
        
        // now the condition for If-Then-Else to compare values
	    Condition condition = ont.createSWRLCondition();
	    Atom atom = swrl.createLessThan(
	        (SWRLDataObject) CP_BNPriceAmount.castTo(SWRLDataObject.class),	
	        (SWRLDataObject) CP_AmazonPriceAmount.castTo(SWRLDataObject.class));
	    AtomList list = swrl.createList( atom );
	    condition.setBody( list );
	    
	    // set the condition
	    ifThenElse.setCondition( condition );
	    
	    // create two produce constructs to generate the results
	    ifThenElse.setThen( createProduceSequence( "BN", CP_BNPrice ) );
	    ifThenElse.setElse( createProduceSequence( "Amazon", CP_AmazonPrice ) );	 

	    return ComparePricesProcess;
	}

	private Sequence createProduceSequence( String bookstore, Input price ) {
	    // the produce construct to produce the price
	    Produce producePrice = ont.createProduce( uri( "Produce" + bookstore + "Price" ) );
	    // we directly pass the input value from the parent process as output to the parent process
	    producePrice.addBinding( CP_OutputPrice, Perform.TheParentPerform, price );
	    
	    // the produce construct to produce the name 
	    Produce produceName = ont.createProduce( uri( "Produce" + bookstore + "Name" ) );
	    // we need a constant string value to produce so use process:ValueData
	    ValueData valueData = ont.createValueData( ont.createDataValue( bookstore ) );
	    // add the binding for this output
	    produceName.addBinding( CP_Bookstore, valueData);
	    
	    // now add both produce into this sequence
	    Sequence sequence = ont.createSequence();
	    sequence.addComponent( producePrice );
	    sequence.addComponent( produceName );

	    return sequence;
	}

	public void run() throws Exception {
        // create an OWL-S knowledge base
        kb = OWLFactory.createKB();

        // create an empty ontology in this KB
        ont = kb.createOntology();
        
    	// create SWRLFactory we will use for creating conditions and expressions
    	swrl = SWRLFactoryCreator.createFactory();
    	
        // load three OWL-S files and directly get the processes we want
        BookFinder = kb.readService("http://www.mindswap.org/2004/owl-s/1.1/BookFinder.owl#").getProcess();
        BNPrice = kb.readService("http://www.mindswap.org/2004/owl-s/1.1/BNPrice.owl#").getProcess();
        AmazonPrice = kb.readService("http://www.mindswap.org/2004/owl-s/1.1/AmazonBookPrice.owl#").getProcess();
        
        // also add the imports statement
        ont.addImport( BookFinder.getOntology() );
        ont.addImport( BNPrice.getOntology() );
        ont.addImport( AmazonPrice.getOntology() );

        // get common classes, properties and datatypes we will need
        xsdString = kb.getDataType( XSD.string ); 
        xsdFloat = kb.getDataType( XSD.xsdFloat ); 
        Price = kb.getClass( URIUtils.createURI( concepts, "Price" ) ); 
        amount = kb.getDataProperty( URIUtils.createURI( concepts, "amount" ) );

        // create the service
        Service s = createService();

        // print the description of new service to standard output
        ont.write( System.out, baseURI );
        System.out.println();

        // create an execution engine
        ProcessExecutionEngine exec = OWLSFactory.createExecutionEngine();
        
        exec.addMonitor( new SimpleProcessMonitor() );

        // get the process of the new service
        Process process = s.getProcess();
        // initialize the input values to be empty
        ValueMap values = new ValueMap();
        // get the parameter using the local name
        values.setValue( process.getInput(), 
            EntityFactory.createDataValue( "City of Glass" ) );

        // execute the service
        values = exec.execute( process, values );

        // get the output param using the index
        String bookstore = values.getStringValue( process.getOutput( "Bookstore" ) );
        OWLIndividual price = values.getIndividualValue( process.getOutput( "BookPrice" ) );

        // display the result
        System.out.println( "Cheaper price found in " + bookstore );
        System.out.println( "Price is $" + price.getProperty( amount ));
        System.out.println();
    }

    public static void main( String[] args ) throws Exception {
        CreateComplexProcess test = new CreateComplexProcess();
        test.run();
    }
}
