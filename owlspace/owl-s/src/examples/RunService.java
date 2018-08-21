// The MIT License
//
// Copyright (c) 2004 Evren Sirin
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to
// deal in the Software without restriction, including without limitation the
// rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
// sell copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
// FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
// IN THE SOFTWARE.

/*
 * Created on Mar 19, 2004
 */
package examples;

import java.net.URI;

import org.mindswap.owl.OWLClass;
import org.mindswap.owl.OWLDataProperty;
import org.mindswap.owl.OWLDataValue;
import org.mindswap.owl.OWLFactory;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLKnowledgeBase;
import org.mindswap.owl.OWLObjectProperty;
import org.mindswap.owls.OWLSFactory;
import org.mindswap.owls.process.Process;
import org.mindswap.owls.process.execution.DefaultProcessMonitor;
import org.mindswap.owls.process.execution.ProcessExecutionEngine;
import org.mindswap.owls.process.execution.SimpleThreadedMonitor;
import org.mindswap.owls.process.execution.ThreadedProcessExecutionEngine;
import org.mindswap.owls.profile.Profile;
import org.mindswap.owls.service.Service;
import org.mindswap.query.ValueMap;
import org.mindswap.wsdl.WSDLOperation;
import org.mindswap.wsdl.WSDLService;

/**
 *
 * Examples to show how services can be executed. Some examples of simple execution monitoring is
 * included.
 *
 * @author Evren Sirin
 */
public class RunService {
    Service service;
    Profile profile;
    Process process;
    WSDLService s;
    WSDLOperation op;
    String inValue;
    String outValue;
    ValueMap values;
    ProcessExecutionEngine exec;

    public RunService() {
        // create an execution engine
        exec = OWLSFactory.createExecutionEngine();

        // Attach a listener to the execution engine
        exec.addMonitor(new DefaultProcessMonitor());
    }
    
    public void runThreaded() throws Exception {
    	ThreadedProcessExecutionEngine threadedExec = OWLSFactory.createThreadedExecutionEngine();
    	threadedExec.addMonitor(new SimpleThreadedMonitor());
    	
        OWLKnowledgeBase kb = OWLFactory.createKB();
        kb.setReasoner("Pellet");
        
        // read the service description
        service = kb.readService("http://www.mindswap.org/2004/owl-s/1.1/FrenchDictionary.owl");
        process = service.getProcess();

        // get the parameter using the local name
        values = new ValueMap();
        values.setDataValue(process.getInput("FirstParam"), "2");
        values.setDataValue(process.getInput("SecParam"), "3");
        
        threadedExec.setSleepInterval(3000);
        threadedExec.executeInThread(process, values);
        
        Thread.sleep(1000);
        threadedExec.interruptExecution();
        Thread.sleep(2000);
        threadedExec.continueExecution();
        
//        Service service1 = kb.readService("http://www.mindswap.org/2004/owl-s/1.1/FrenchDictionary.owl");
//        Service service2 = kb.readService("http://www.mindswap.org/2004/owl-s/1.1/FindCheaperBook.owl");
//        Service service3 = kb.readService("http://www.mindswap.org/2004/owl-s/1.1/Dictionary.owl");
//        Service service4 = kb.readService("http://www.mindswap.org/2004/owl-s/1.1/BabelFishTranslator.owl");

//        Process process1 = service1.getProcess();
//        Process process2 = service2.getProcess();
//        Process process3 = service3.getProcess();
//        Process process4 = service4.getProcess();

        // initialize the input values to be empty
//        ValueMap values1 = new ValueMap();
//        ValueMap values2 = new ValueMap();
//        ValueMap values3 = new ValueMap();
//        ValueMap values4 = new ValueMap();

        // set inputs
//        values1.setDataValue(process1.getInput("InputString"), "mere");
        
//        values2.setDataValue(process2.getInput("BookName"), "City of Glass");
//        
//        inValue = "hello";
//        values3.setDataValue(process3.getInput("InputString"), inValue);
//        
//        String langOnt = "http://www.daml.org/2003/09/factbook/languages#";
//        OWLIndividual English = kb.getIndividual(URI.create(langOnt + "English"));
//        OWLIndividual French = kb.getIndividual(URI.create(langOnt + "French"));
//        values4.setDataValue(process4.getInput("InputString"), "Hello world!");
//        values4.setValue(process4.getInput("InputLanguage"), English);
//        values4.setValue(process4.getInput("OutputLanguage"), French);
        
        
//       threadedExec.executeInThread(process1, values1);
//        threadedExec.executeInThread(process2, values2);
//        threadedExec.executeInThread(process3, values3);
//        threadedExec.executeInThread(process4, values4);
    }

    public void runZipCode() throws Exception {
        OWLKnowledgeBase kb = OWLFactory.createKB();

        service = kb.readService("http://www.mindswap.org/2004/owl-s/1.1/ZipCodeFinder.owl");
        process = service.getProcess();

        // initialize the input values to be empty
        values = new ValueMap();

        values.setDataValue(process.getInput("City"), "College Park");
        values.setDataValue(process.getInput("Stadt"), "College Park");
        values.setDataValue(process.getInput("State"), "MD");

        values = exec.execute(process, values);

        // get the result
        OWLIndividual out = values.getIndividualValue(process.getOutput());

        // do something with output
    }

    public void runJGroundingTest() throws Exception {
        OWLKnowledgeBase kb = OWLFactory.createKB();

        // read the service description
        service = kb.readService("http://www.ifi.unizh.ch/ddis/ont/owl-s/JGroundingTest.owl");
        process = service.getProcess();

        // get the parameter using the local name
        values = new ValueMap();
        values.setDataValue(process.getInput("FirstParam"), "2");
        values.setDataValue(process.getInput("SecParam"), "3");

        values = exec.execute(process, values);

        // do something with output
    }

    public void runBookFinder() throws Exception {
        OWLKnowledgeBase kb = OWLFactory.createKB();

        // read the service description
        service = kb.readService("http://www.mindswap.org/2004/owl-s/1.1/BookFinder.owl");
        process = service.getProcess();

        // initialize the input values to be empty
        values = new ValueMap();

        // use any book name
        inValue = "City of Glass";

        // get the parameter using the local name
        values.setDataValue(process.getInput("BookName"), inValue);
        values = exec.execute(process, values);

        // get the output param using the index
        OWLIndividual out = values.getIndividualValue(process.getOutput());

        // do something with output
    }

    public void runBookPrice() throws Exception {
        String currencyOnt = "http://www.daml.ecs.soton.ac.uk/ont/currency.owl#";

        OWLKnowledgeBase kb = OWLFactory.createKB();

        // read the service description
        service = kb.readService("http://www.mindswap.org/2004/owl-s/1.1/BookPrice.owl");
        process = service.getProcess();

        // initialize the input values to be empty
        values = new ValueMap();

        // use an arbitrary book name
        inValue = "City of Glass";
        // get the parameter using the local name
        values.setDataValue(process.getInput("BookName"), inValue);
        values.setValue(process.getInput("Currency"),
            kb.getIndividual(URI.create(currencyOnt + "EUR")));
        values = exec.execute(process, values);

        // get the output param using the index
        OWLIndividual out = values.getIndividualValue(process.getOutput());

        // do something with output
    }

    public void runFindCheaperBook() throws Exception {
        OWLKnowledgeBase kb = OWLFactory.createKB();

        // we need to check preconditions so that local variables will be assigned values
        exec.setPreconditionCheck( true );

        // read the service description
        service = kb.readService("http://www.mindswap.org/2004/owl-s/1.1/FindCheaperBook.owl");
        process = service.getProcess();

        // initialize the input values to be empty
        values = new ValueMap();

        // use an arbitrary book name
        values.setDataValue(process.getInput("BookName"), "City of Glass");
        values = exec.execute(process, values);

        // get the output values
        OWLIndividual price = values.getIndividualValue(process.getOutput("BookPrice"));
        String bookstore = values.getStringValue(process.getOutput("Bookstore"));

        // do something with output
    }

    public void runCurrencyConverter() throws Exception {
        String currencyOnt = "http://www.daml.ecs.soton.ac.uk/ont/currency.owl#";
        String conceptsOnt = "http://www.mindswap.org/2004/owl-s/concepts.owl#";

        OWLKnowledgeBase kb = OWLFactory.createKB();
        // read the service description
        service = kb.readService("http://www.mindswap.org/2004/owl-s/1.1/CurrencyConverter.owl");
        process = service.getProcess();

        // initialize the input values to be empty
        values = new ValueMap();


        OWLIndividual EUR = kb.getIndividual(URI.create(currencyOnt + "EUR"));
        values.setValue(process.getInput("OutputCurrency"), EUR);

        OWLIndividual USD = kb.getIndividual(URI.create(currencyOnt + "USD"));

        OWLClass Price = kb.getClass(URI.create(conceptsOnt + "Price"));
        OWLObjectProperty currency = kb.getObjectProperty(URI.create(conceptsOnt + "currency"));
        OWLDataProperty amount = kb.getDataProperty(URI.create(conceptsOnt + "amount"));

        OWLIndividual inputPrice = kb.createInstance(Price);
        inputPrice.addProperty( currency, USD );
        inputPrice.addProperty( amount, "100" );

        // get the parameter using the local name
        values.setValue(process.getInput("InputPrice"), inputPrice);

        values = exec.execute(process, values);

        // get the output param using the index
        OWLIndividual out = values.getIndividualValue(process.getOutput());

        // do something with output
    }

    public void runFrenchDictionary() throws Exception {
        OWLKnowledgeBase kb = OWLFactory.createKB();
        // we need a reasoner that can evaluate the precondition of the translator
        kb.setReasoner("Pellet");

        service = kb.readService("http://www.mindswap.org/2004/owl-s/1.1/FrenchDictionary.owl");
        process = service.getProcess();

        // initialize the input values to be empty
        values = new ValueMap();

        inValue = "mere";
        values.setDataValue(process.getInput("InputString"), inValue);
        values = exec.execute(process, values);

        // get the output using local name
        outValue = values.getValue(process.getOutputs().getParameter("OutputString")).toString();

        // do something with output
    }

    public void runTranslator() throws Exception {
        // language ontology
        String langOnt = "http://www.daml.org/2003/09/factbook/languages#";

        OWLKnowledgeBase kb = OWLFactory.createKB();
        // we at least need RDFS reasoning to evaluate preconditions (to understand
        // that process:Parameter is subclass of swrl:Variable)
        kb.setReasoner("RDFS");

        service = kb.readService("http://www.mindswap.org/2004/owl-s/1.1/BabelFishTranslator.owl");
        process = service.getProcess();

        // get the references for these values
        OWLIndividual English = kb.getIndividual(URI.create(langOnt + "English"));
        OWLIndividual French = kb.getIndividual(URI.create(langOnt + "French"));

        // initialize the input values to be empty
        values = new ValueMap();

        values.setDataValue(process.getInput("InputString"), "Hello world!");
        values.setValue(process.getInput("InputLanguage"), English);
        values.setValue(process.getInput("OutputLanguage"), French);
        values = exec.execute(process, values);

        // get the output using local name
        outValue = values.getValue(process.getOutput()).toString();

        // do something with output
    }

    public void runDictionary() throws Exception {
        OWLKnowledgeBase kb = OWLFactory.createKB();
        service = kb.readService("http://www.mindswap.org/2004/owl-s/1.1/Dictionary.owl");
        process = service.getProcess();

        // initialize the input values to be empty
        values = new ValueMap();

        inValue = "hello";
        values.setDataValue(process.getInput("InputString"), inValue);
        values = exec.execute(process, values);

        // get the output
        OWLDataValue out = (OWLDataValue) values.getValue(process.getOutput());

        // do something with output
    }
    
    public void runAll() throws Exception {
//    	try {
//            runCurrencyConverter();
//        } catch(Exception e) {
//        }

        try {
            runZipCode();
        } catch(Exception e) {
        }
//
//        try {
//            runTranslator();
//        } catch(Exception e) {
//        }
//
//        try {
//            runJGroundingTest();
//        } catch(Exception e) {
//        }
//
//        try {
//            runDictionary();
//        } catch(Exception e) {
//        }
//
//        try {
//            runBookFinder();
//        } catch(Exception e) {
//        }
//
//        try {
//            runFrenchDictionary();
//        } catch(Exception e) {
//        }
//
//        try {
//            runBookPrice();
//        } catch(Exception e) {
//        }
//
//        try {
//            runFindCheaperBook();
//        } catch(Exception e) {
//        }
    }

    public static void main(String[] args) throws Exception {
        RunService test = new RunService();
        test.runAll();
        
    }
}
