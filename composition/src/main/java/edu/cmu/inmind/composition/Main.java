package edu.cmu.inmind.composition;

import edu.cmu.inmind.composition.common.ServiceMethod;
import edu.cmu.inmind.composition.common.Utils;
import edu.cmu.inmind.composition.controllers.CommunicationController;
import edu.cmu.inmind.composition.controllers.CompositionController;
import edu.cmu.inmind.composition.controllers.InputController;
import org.zeromq.ZMQ;

import java.io.File;
import java.util.*;

/**
 * Created by oscarr on 8/8/18.
 */
public class Main {

    public static void main(String args[]){
        // let's initialize all the resources
        CompositionController.init();

        // let's generate the corpora using service method descriptions:
        Map<String, ServiceMethod> serviceMap = Utils.generateCorporaFromMethods();

        // let's get some descriptions of a plan from the console or a file
        InputController inputController = new InputController(true, InputController.PHASE.ABSTRACT);

        System.out.println("*** Enter your high level goal/plan:");
        CompositionController.addGoal( inputController.getNext() );
        String step = "";

        // PHASE 1: ABSTRACT SERVICES COMPOSITION
        // assuming that we have a plan (a pipeline of steps) that is provided either by console input or
        // by the file 'task-def-script', let's create abstract services.
        while( !step.equals("END") ){
            System.out.println("*** Enter one by one each step to accomplish the goal (type 'END' to finish):");
            step = inputController.getNext();

            // let's get the semantic neighbors provided by sent2vec
            CommunicationController.send(step);
            String absServiceCandidates = CommunicationController.receive();
            CompositionController.addStep(step, absServiceCandidates);
        }

        // let's create a composite service using the abstract services
        System.out.println("\n\n======== ABSTRACT SERVICES ===========");
        CompositionController.CompositeService compositeService = CompositionController.generateCompositeServiceRequest();
        CompositionController.fireRulesAS();


        // PHASE 2: GROUNDING SERVICE COMPOSITION
        // execute services using the serviceMap:
        inputController = new InputController(true, InputController.PHASE.GROUNDING);
        // let's create some rules for grounding specific services based on QoS:
        CompositionController.createRulesForGroundingServices();

        System.out.println("\n\n======== GROUNDED SERVICES ===========");
        System.out.println("Let's execute the plan (if not reading from a file, type the concrete action and goal)....");
        System.out.println("*** What is the concrete Goal? " + inputController.getNext());
        String concreteAction = "";
        int numStep = 1;
        while( !concreteAction.equals("END") ){
            System.out.println(numStep + ". Abstract action (step): " + compositeService.getNext());
            System.out.println(numStep + ". Concrete action (step): " + (concreteAction = inputController.getNext()));

            // let's simulate changes on QoS conditions
            CompositionController.addPhoneStatusToWM();

            // let's execute the grounded service:
            CompositionController.execute(concreteAction, serviceMap);

            numStep++;
            System.out.println("\n******************\n");
        }
    }

}
