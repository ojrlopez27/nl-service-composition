package edu.cmu.inmind.composition;

import edu.cmu.inmind.composition.common.Constants;
import edu.cmu.inmind.composition.common.ServiceMethod;
import edu.cmu.inmind.composition.common.Utils;
import edu.cmu.inmind.composition.controllers.CommunicationController;
import edu.cmu.inmind.composition.controllers.CompositionController;
import edu.cmu.inmind.composition.controllers.InputController;
import edu.cmu.inmind.multiuser.controller.log.Log4J;
import org.zeromq.ZMQ;

import java.io.File;
import java.util.*;

/**
 * Created by oscarr on 8/8/18.
 */
public class CompositionLauncher {
    private static final String TAG = CompositionLauncher.class.getSimpleName();

    public static void main(String args[]){
        // let's initialize all the resources
        CompositionController.init();

        // let's generate the corpora using service method descriptions:
        Map<String, ServiceMethod> serviceMap = Utils.generateCorporaFromMethods();

        // let's get some descriptions of a plan from the console or a file
        InputController inputController = new InputController(true, InputController.PHASE.ABSTRACT);

        Log4J.info(TAG, "Enter your high level goal/plan:");
        CompositionController.addGoal( inputController.getNext() );
        String step;

        // PHASE 1: ABSTRACT SERVICES COMPOSITION
        // assuming that we have a plan (a pipeline of steps) that is provided either by console input or
        // by the file 'task-def-script', let's create abstract services.
        while( true ){
            Log4J.info(TAG, "Enter one by one each step to accomplish the goal (type 'END' to finish):");
            step = inputController.getNext();

            if( !step.equals(Constants.END) ) {
                // let's get the semantic neighbors provided by sent2vec
                CommunicationController.send(step);
                String absServiceCandidates = CommunicationController.receive();
                CompositionController.addStep(step, absServiceCandidates);
            }else{
                break;
            }
        }

        // let's create a composite service using the abstract services
        System.out.println("\n\n");
        Log4J.debug(TAG, "======== ABSTRACT SERVICES ===========");
        CompositionController.CompositeService compositeService = CompositionController.generateCompositeServiceRequest();
        CompositionController.fireRulesAS();


        // PHASE 2: GROUNDING SERVICE COMPOSITION
        // execute services using the serviceMap:
        inputController = new InputController(true, InputController.PHASE.GROUNDING);
        // let's create some rules for grounding specific services based on QoS:
        CompositionController.createRulesForGroundingServices();

        System.out.println("\n\n");
        Log4J.debug(TAG, "======== GROUNDED SERVICES ===========");
        Log4J.trace(TAG, "Let's execute the plan (if not reading from a file, type the concrete action and goal)....");
        Log4J.info(TAG, "Please type, what is the concrete Goal? ==>   " + inputController.getNext());
        String concreteAction;
        int numStep = 1;
        while( true ){
            Log4J.trace(TAG, numStep + ". Abstract action (step): " + compositeService.getNext());
            Log4J.info(TAG, numStep + ". Type Concrete action (step): " + (concreteAction = inputController.getNext()));

            if( !concreteAction.equals(Constants.END ) ){
                // let's simulate changes on QoS conditions
                CompositionController.addPhoneStatusToWM();

                // let's execute the grounded service:
                CompositionController.execute(concreteAction, serviceMap);

                numStep++;
                System.out.println("\n");
                Log4J.debug(TAG, "***********************");
            }else{
                break;
            }
        }

        CommunicationController.stop();
        Log4J.error(TAG, "We are done! Bye bye...");
    }

}
