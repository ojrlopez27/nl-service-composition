package edu.cmu.inmind.composition.launchers;

import edu.cmu.inmind.composition.common.Constants;
import edu.cmu.inmind.composition.common.ServiceMethod;
import edu.cmu.inmind.composition.common.Utils;
import edu.cmu.inmind.composition.controllers.CommunicationController;
import edu.cmu.inmind.composition.controllers.CompositionController;
import edu.cmu.inmind.composition.controllers.InputController;
import edu.cmu.inmind.multiuser.controller.common.CommonUtils;
import edu.cmu.inmind.multiuser.controller.log.Log4J;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by oscarr on 8/8/18.
 */
public class PerformanceTestLauncher {
    private static final String TAG = PerformanceTestLauncher.class.getSimpleName();
    private static int numberOfClients;
    private static boolean performanceTestMode;
    private static AtomicInteger clientDone = new AtomicInteger(0);
    private static AtomicLong averageTime = new AtomicLong(0);
    private static Map<String, ServiceMethod> serviceMap;

    public static void main(String args[]){
        numberOfClients = Integer.parseInt(CommonUtils.getProperty("performance.test.clients"));
        performanceTestMode = Boolean.parseBoolean(CommonUtils.getProperty("performance.test.enable"));
        if( performanceTestMode ) Log4J.turnOn(false);

        // we need to map services, put them into a file so sent2vec can use them
        serviceMap = Utils.generateCorporaFromMethods(false);

        for(int i = 0; i < numberOfClients; i++){
            new Thread( new User(serviceMap) ).start();
            CommonUtils.sleep(200);
        }

        // let's wait for all clients to terminate
        while( clientDone.get() < numberOfClients ){
            CommonUtils.sleep(100);
        }
        System.out.println("Total: " + averageTime.get() + ", average: " + (averageTime.get() / numberOfClients) );
        BufferedWriter output = null;
        try {
            File file = new File("output.txt");
            Files.write(Paths.get(file.getPath()), ("," + averageTime.toString()).getBytes(),
                    StandardOpenOption.APPEND);
        } catch ( IOException e ) {
            e.printStackTrace();
        } finally {
            if ( output != null ) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Log4J.error(TAG, "We are done! Bye bye...");
    }




    static class User implements Runnable{
        private CompositionController compositionController;
        private CommunicationController communicationController;
        private Map<String, ServiceMethod> serviceMap;

        public User(Map<String, ServiceMethod> serviceMap) {
            // let's initialize all the resources
            this.compositionController = new CompositionController();
            this.communicationController = new CommunicationController();
            this.serviceMap = serviceMap;
        }

        @Override
        public void run() {

            // let's get some descriptions of a plan from the console or a file
            InputController inputController = new InputController(true, InputController.PHASE.ABSTRACT);

            Log4J.debug(TAG, "======== HIGH LEVEL PLAN DESCRIPTION ===========");
            Log4J.info(TAG, "Enter your high level goal/plan:");
            compositionController.addGoal( inputController.getNext() );
            String step;

            // PHASE 1: ABSTRACT SERVICES COMPOSITION
            // assuming that we have a plan (a pipeline of steps) that is provided either by console input or
            // by the file 'task-def-script', let's create abstract services.
            Utils.startChrono();
            while( true ){
                Log4J.info(TAG, "Enter one by one each step to accomplish the goal (type 'END' to finish):");
                step = inputController.getNext();

                if( !step.equals(Constants.END) ) {
                    // let's get the semantic neighbors provided by sent2vec
                    communicationController.sendS2V(step);
                    String absServiceCandidates = communicationController.receiveS2V();
                    compositionController.addStep(step, absServiceCandidates);
                }else{
                    break;
                }
            }

            // let's create a composite service using the abstract services
            if(!performanceTestMode) System.out.println("\n\n");
            Log4J.debug(TAG, "======== ABSTRACT SERVICES ===========");
            CompositionController.CompositeService compositeService = compositionController.generateCompositeServiceRequest();
            compositionController.fireRulesAS();


            // PHASE 2: GROUNDING SERVICE COMPOSITION
            // execute services using the serviceMap:
            inputController = new InputController(true, InputController.PHASE.GROUNDING);
            // let's create some rules for grounding specific services based on QoS:
            compositionController.createRulesForGroundingServices();

            if(!performanceTestMode) System.out.println("\n\n");
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
                    compositionController.addPhoneStatusToWM();

                    // let's execute the grounded service:
                    compositionController.execute(concreteAction, serviceMap);

                    numStep++;
                    if(!performanceTestMode) System.out.println("\n");
                    Log4J.debug(TAG, "***********************");
                }else{
                    break;
                }
            }
            averageTime.addAndGet( Utils.stopChrono() );
            int done = clientDone.incrementAndGet();
            if(performanceTestMode) System.out.println("Number of clients done: " + done);
            communicationController.stopS2V();
        }
    }

}
