package examples;

/**
 * Just calls most of the examples in one go to be used as some sort of 
 * test before updating core parts of the API to ensure (!) correctness and
 * soundness of changes. 
 * 
 * And yes, UnitTests should be written :-)
 * 
 * @author Michael Dänzer, University of Zürich
 */
public class RunAllExamples {
	public static void main(String[] args) {		
		
		// Run CreateComplexProcess.java
		try {
			System.out.println("----------------------------------------------");
			System.out.println("START CreateComplexProcess");
			CreateComplexProcess testComplex = new CreateComplexProcess();
			testComplex.run();
		} catch(Exception e) {
			System.out.println("CreateComplexProcess threw exception: " + e);
		}
		System.out.println("END CreateComplexProcess");
        
        // Run CreateJGrounding.java
		try {
			System.out.println("----------------------------------------------");
			System.out.println("START CreateJGrounding");
			CreateJavaGrounding testJGround = new CreateJavaGrounding();
			testJGround.run();
		} catch(Exception e) {
			System.out.println("CreateJGrounding threw exception: " + e);
		}
		System.out.println("END CreateJGrounding");
		
        // Run CreateSequence.java
		try {
			System.out.println("----------------------------------------------");
			System.out.println("START CreateSequence");
	        CreateSequence test = new CreateSequence();
	        test.runTest();
		} catch(Exception e) {
			System.out.println("CreateSequence threw exception: " + e);
		}
		System.out.println("END CreateSequence");
		
        // Run ForEachExample
		try {
			System.out.println("----------------------------------------------");
			System.out.println("START ForEachExample");
			ForEachExample test = new ForEachExample();			
			test.run();	
		} catch(Exception e) {
			System.out.println("ForEachExample threw exception: " + e);
		}
		System.out.println("END ForEachExample");
		
        // Run MatchMaker
		try {
			System.out.println("----------------------------------------------");
			System.out.println("START MatchMaker");
			Matchmaker test = new Matchmaker();			
			test.run();	
		} catch(Exception e) {
			System.out.println("MatchMaker threw exception: " + e);
		}
		System.out.println("END MatchMaker");
		
        // Run OWLSExtensions
		try {
			System.out.println("----------------------------------------------");
			System.out.println("START OWLSExtensions");
			OWLSExtensions test = new OWLSExtensions();			
			test.run();	
		} catch(Exception e) {
			System.out.println("OWLSExtensions threw exception: " + e);
		}
		System.out.println("END OWLSExtensions");
				
        // Run PreconditionCheck
		try {
			System.out.println("----------------------------------------------");
			System.out.println("START PreconditionCheck");
			PreconditionCheck test = new PreconditionCheck();		
			test.run();	
		} catch(Exception e) {
			System.out.println("PreconditionCheck threw exception: " + e);
		}
		System.out.println("END PreconditionCheck");

        // Run PreconditionUsage
		try {
			System.out.println("----------------------------------------------");
			System.out.println("START PreconditionUsage");
			PreconditionUsage test = new PreconditionUsage();			
			test.run();	
		} catch(Exception e) {
			System.out.println("PreconditionUsage threw exception: " + e);
		}
		System.out.println("END PreconditionUsage");

        // Run RunService
		try {
			System.out.println("----------------------------------------------");
			System.out.println("START RunService");
			RunService test = new RunService();			
			test.runAll();	
		} catch(Exception e) {
			System.out.println("RunService threw exception: " + e);
		}
		System.out.println("END RunService");
		
        // Run ServiceParameterExample
		try {
			System.out.println("----------------------------------------------");
			System.out.println("START ServiceParameterExample");
			ServiceParameterExample test = new ServiceParameterExample();			
			test.run();	
		} catch(Exception e) {
			System.out.println("ServiceParameterExample threw exception: " + e);
		}
		System.out.println("END ServiceParameterExample");
		
        // Run Translator
		try {
			System.out.println("----------------------------------------------");
			System.out.println("START Translator");
			Translator test = new Translator();			
			test.run("http://www.mindswap.org/2004/owl-s/1.1/ZipCodeFinder.owl");	
		} catch(Exception e) {
			System.out.println("Translator threw exception: " + e);
		}
		System.out.println("END Translator");
	}
}
