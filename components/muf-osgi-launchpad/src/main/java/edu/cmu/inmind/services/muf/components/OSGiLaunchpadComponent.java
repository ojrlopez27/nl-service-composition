package edu.cmu.inmind.services.muf.components;

import edu.cmu.inmind.multiuser.controller.blackboard.Blackboard;
import edu.cmu.inmind.multiuser.controller.blackboard.BlackboardEvent;
import edu.cmu.inmind.multiuser.controller.blackboard.BlackboardSubscription;
import edu.cmu.inmind.multiuser.controller.common.Constants;
import edu.cmu.inmind.multiuser.controller.log.Log4J;
import edu.cmu.inmind.multiuser.controller.plugin.PluggableComponent;
import edu.cmu.inmind.multiuser.controller.plugin.StateType;
import edu.cmu.inmind.services.muf.components.osgi.LaunchpadStarterController;
import edu.cmu.inmind.services.muf.inputs.LaunchpadInput;
import edu.cmu.inmind.services.muf.outputs.LaunchpadOutput;
import java.util.Map;
import org.osgi.framework.ServiceReference;

import static edu.cmu.inmind.services.muf.commons.Constants.MSG_LP_GET_ALL_SERVICES;
import static edu.cmu.inmind.services.muf.commons.Constants.MSG_LP_GET_SERVICE_IMPL;
import static edu.cmu.inmind.services.muf.commons.Constants.MSG_LP_INPUT_CMD;
import static edu.cmu.inmind.services.muf.commons.Constants.MSG_LP_LIST_SERVICES;
import static edu.cmu.inmind.services.muf.commons.Constants.MSG_LP_OUTPUT_CMD;
import static edu.cmu.inmind.services.muf.commons.Constants.MSG_LP_RESP_GET_ALL_SERVICES;
import static edu.cmu.inmind.services.muf.commons.Constants.MSG_LP_RESP_GET_SERVICE_IMPL;
import static edu.cmu.inmind.services.muf.commons.Constants.MSG_LP_START_SERVICE;

@StateType(state = Constants.STATEFULL)
@BlackboardSubscription(messages = {MSG_LP_INPUT_CMD})
public class OSGiLaunchpadComponent extends PluggableComponent {

    LaunchpadStarterController launchpadStarterController;

    @Override
    public void startUp(){
        super.startUp();
        Log4J.info(this, "Inside OSGiLaunchpadComponent.startUp");
        launchpadStarterController = new LaunchpadStarterController();
        launchpadStarterController.initFramework();
    }

    @Override
    public void shutDown() {
        super.shutDown();
        Log4J.info(this, "Inside OSGiLaunchpadComponent.shutDwon");
        launchpadStarterController.stopFramework();
    }

    /*
    @Override
    public void execute() {
        Log4J.info(this, "Inside OSGiLaunchpadComponent.execute: " + hashCode());
        handleService(getBlackBoard(getSessionId()));
    }
    */

    @Override
    public void onEvent(Blackboard blackboard, BlackboardEvent blackboardEvent) throws Throwable {
        Log4J.info(this, "Inside OSGiLaunchpadComponent.onEvent");
        try {
            // read the input command
            LaunchpadInput launchpadInput = (LaunchpadInput) blackboardEvent.getElement();
            String launchpadInputCommand = launchpadInput.getCommand();
            System.out.println("LaunchpadInputCommand: " + launchpadInputCommand);

            // process the input command
            switch (launchpadInputCommand) {

                case MSG_LP_START_SERVICE: {

                    // start the service using launchpad
                    launchpadStarterController.startService(launchpadInput.getOSGiService());

                    // update the blackboard with the output
                    blackboard.post(this, MSG_LP_OUTPUT_CMD, launchpadInput.getOSGiService());

                    break;
                }

                case MSG_LP_GET_SERVICE_IMPL: {

                    ServiceReference[] serviceReferences = launchpadStarterController.getServices(launchpadInput.getOSGiService());
                    if (serviceReferences != null && serviceReferences.length > 0) {
                        for (ServiceReference serviceReference : serviceReferences) {
                            Object serviceImplObj = launchpadStarterController.getImplementation(serviceReference);

                            LaunchpadOutput launchpadOutput =
                                    new LaunchpadOutput.GetServiceImplementationRespBuilder(MSG_LP_RESP_GET_SERVICE_IMPL)
                                            .setServiceReference(serviceReference)
                                            .setServiceImpl(serviceImplObj)
                                            .build();

                            // update the blackboard with the service registry message output
                            blackboard.post(this, MSG_LP_RESP_GET_SERVICE_IMPL, launchpadOutput);
                        }
                    }

                    break;
                }
                case MSG_LP_LIST_SERVICES: {

                    // list the services using launchpad
                    launchpadStarterController.listServices();

                    // output
                    // .. do nothing ..

                    break;
                }
                case MSG_LP_GET_ALL_SERVICES: {

                    Log4J.debug(this, "In LP Component: MSG_LP_GET_ALL_SERVICES..");

                    // get all service implementations from launchpad
                    Map<ServiceReference, Object> serviceImplMap = launchpadStarterController.getAllServiceImplementations();

                    // construct a launchpad output with the services
                    LaunchpadOutput launchpadOutput =
                            new LaunchpadOutput.GetAllServicesRespBuilder(MSG_LP_RESP_GET_ALL_SERVICES)
                                    .setAllServices(serviceImplMap)
                                    .build();

                    // update the blackboard with the service registry message output
                    blackboard.post(this, MSG_LP_RESP_GET_ALL_SERVICES, launchpadOutput);
                    break;
                }
                default:
                    Log4J.info(this, "Inside OSGiLaunchpadComponent.handleService " + " -- not sure what to do.");
            }

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

}
