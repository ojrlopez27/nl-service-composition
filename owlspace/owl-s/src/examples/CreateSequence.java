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

package examples;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.mindswap.owl.EntityFactory;
import org.mindswap.owl.OWLFactory;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLKnowledgeBase;
import org.mindswap.owl.OWLOntology;
import org.mindswap.owls.OWLSFactory;
import org.mindswap.owls.grounding.AtomicGrounding;
import org.mindswap.owls.grounding.Grounding;
import org.mindswap.owls.process.AtomicProcess;
import org.mindswap.owls.process.CompositeProcess;
import org.mindswap.owls.process.Input;
import org.mindswap.owls.process.Output;
import org.mindswap.owls.process.Perform;
import org.mindswap.owls.process.Process;
import org.mindswap.owls.process.ProcessList;
import org.mindswap.owls.process.Result;
import org.mindswap.owls.process.Sequence;
import org.mindswap.owls.process.execution.ProcessExecutionEngine;
import org.mindswap.owls.profile.Profile;
import org.mindswap.owls.service.Service;
import org.mindswap.query.ValueMap;
import org.mindswap.utils.URIUtils;
import org.mindswap.utils.Utils;

/**
 * An example to show how service descriptions can be created on the fly, saved and executed.
 *
 * @author Evren Sirin
 */
public class CreateSequence {
    public static final URI baseURI = URI.create("http://www.example.org/BookPrice.owl#");

    OWLOntology ont;

    public CreateSequence() {
    }

    /**
     *
     * Create a new Sequence from the processes of the given services and put them in a new
     * Service object with a automatically generated Profile. This function assumes that
     * each service in the list has exactly one input and one output (except the first and
     * last one) such that in the resulting Service the output of each service will be fed
     * as input to the next one. The first service does not have to have an input and the
     * last one does not need to have an output. The resulting service will have an input
     * (or an output) depending on this.
     *
     * @param services List of Service objects
     * @param baseURI The base URI for the generated service
     * @return The Service which is a Sequence of the given services
     */
    Service createSequenceService(List services) {
        Service service = ont.createService(URIUtils.createURI(baseURI, "TestService"));
        CompositeProcess process = ont.createCompositeProcess(URIUtils.createURI(baseURI, "TestProcess"));
        Profile profile = ont.createProfile(URIUtils.createURI(baseURI, "TestProfile"));
        Grounding grounding = ont.createGrounding(URIUtils.createURI(baseURI, "TestGrounding"));

        System.out.println(ont.getKB().getServices());

        service.setProfile(profile);
        service.setProcess(process);
        service.setGrounding(grounding);

        createSequenceProcess(process, services);
        createProfile(profile, process);

        ProcessList list = process.getComposedOf().getAllProcesses();
        for(int i = 0; i < list.size(); i++) {
            Process pc = list.processAt(i);
            if(pc instanceof AtomicProcess) {
                AtomicGrounding ag = ((AtomicProcess)pc).getGrounding();
                if( ag == null ) continue;
                grounding.addGrounding( ag );
            }
        }


        profile.setLabel(createLabel(services));
        profile.setTextDescription(profile.getLabel());

        service.setProfile(profile);
        service.setProcess(process);
        service.setGrounding(grounding);
        return service;
    }

    /**
     *
     * Create a label for the composite service based on the labels of services. Basically
     * return the string [Service1 + Service2 + ... + ServiceN] as the label
     *
     * @param services List of Servie objects
     * @return
     */
    String createLabel(List services) {
        String label = "[";

        for(int i = 0; i < services.size(); i++) {
            Service s = (Service) services.get(i);

            if(i > 0) label += " + ";

            label += s.getLabel();
        }
        label += "]";

        return label;
    }

    /**
     *
     * Create a Profile for the composite service. We only set the input and output of the profile
     * based on the process.
     *
     * @param profile
     * @param process
     * @return
     */
    Profile createProfile(Profile profile, Process process) {
        for(int i = 0; i < process.getInputs().size(); i++) {
            Input input = process.getInputs().inputAt(i);

            profile.addInput(input);
        }

        for(int i = 0; i < process.getOutputs().size(); i++) {
            Output output = process.getOutputs().outputAt(i);

            profile.addOutput(output);
        }

        return profile;
    }

    /**
     *
     * Create a Sequence process for the processes of given services. Creates the DataFlow asssuming each
     * service has one output and one intput (except first and last one).
     *
     * @param compositeProcess
     * @param grounding
     * @param services
     * @return
     */
    CompositeProcess createSequenceProcess(CompositeProcess compositeProcess, List services) {
        Sequence sequence = ont.createSequence();
        compositeProcess.setComposedOf(sequence);

        Perform[] performs = new Perform[services.size()];
        for(int i = 0; i < services.size(); i++) {
            Service s = (Service) services.get(i);
            Process p = s.getProcess();

            performs[i] = ont.createPerform();
            performs[i].setProcess(p);

            sequence.addComponent(performs[i]);

            if(i > 0) {
                Perform prevPerform = performs[i - 1];
                Input input = p.getInputs().inputAt(0);
                Output output = prevPerform.getProcess().getOutputs().outputAt(0);

                // the value of 'input' is the value of 'output' from 'prevPerform'
                performs[i].addBinding(input, prevPerform, output);
            }
        }

        Perform firstPerform = performs[0];
        Perform lastPerform = performs[services.size()-1];
        boolean createInput = firstPerform.getProcess().getInputs().size() > 0;
        boolean createOutput = lastPerform.getProcess().getOutputs().size() > 0;

        if(createInput) {
            Input input = firstPerform.getProcess().getInputs().inputAt(0);
            Input newInput = ont.createInput(URIUtils.createURI(baseURI, "TestInput"));
            newInput.setLabel(input.getLabel());
            newInput.setParamType(input.getParamType());
            newInput.setProcess(compositeProcess);

            // input of the first perform is directly read from the input of the
            // composite process
            performs[0].addBinding(input, Perform.TheParentPerform, newInput);
        }

        if(createOutput) {
            Output output = lastPerform.getProcess().getOutputs().outputAt(0);
            Output newOutput = ont.createOutput(URIUtils.createURI(baseURI, "TestOutput"));
            newOutput.setLabel(output.toPrettyString());
            newOutput.setParamType(output.getParamType());
            newOutput.setProcess(compositeProcess);

            // the output of the composite process is the output pf last process
            Result result = ont.createResult();
            result.addBinding(newOutput, lastPerform, output);

            compositeProcess.setResult(result);
        }

        return compositeProcess;
    }


    public void runTest() throws Exception {
        // create an OWL-S knowledge base
        OWLKnowledgeBase kb = OWLFactory.createKB();

        kb.getReader().getCache().setLocalCacheDirectory( "C:\\mindswap\\composer\\cache" );
        
        // create an empty ontology in this KB
        ont = kb.createOntology();

        // create an execution engine
        ProcessExecutionEngine exec = OWLSFactory.createExecutionEngine();

        // load two services
        Service s1 = kb.readService("http://www.mindswap.org/2004/owl-s/1.1/BookFinder.owl#");
        Service s2 = kb.readService("http://www.mindswap.org/2004/owl-s/1.1/BNPrice.owl#");

        // put the services in a list
        List services = new ArrayList();
        services.add(s1);
        services.add(s2);

        // create a new service as a sequence of the list
        Service s = createSequenceService(services);

        // print the description of new service to standard output
        ont.write(System.out, baseURI);
        System.out.println();

        // get the process of the new service
        Process process = s.getProcess();
        // initialize the input values to be empty
        ValueMap values = new ValueMap();
        // get the parameter using the local name
        values.setValue(process.getInputs().inputAt(0), EntityFactory.createDataValue("City of Glass"));

        // execute the service
        System.out.print("Executing...");
        values = exec.execute(process, values);
        System.out.println("done");

        // get the output param using the index
        OWLIndividual outValue = values.getIndividualValue(process.getOutput());

        // display the result
        System.out.println("Book Price = ");
        System.out.println(Utils.formatRDF(outValue.toRDF()));
        System.out.println();
    }

    public static void main(String[] args) throws Exception {
        CreateSequence test = new CreateSequence();
        test.runTest();
    }
}
