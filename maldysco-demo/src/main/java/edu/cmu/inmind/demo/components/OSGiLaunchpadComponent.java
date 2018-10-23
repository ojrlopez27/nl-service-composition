package edu.cmu.inmind.demo.components;

import edu.cmu.inmind.demo.common.DemoConstants;
import edu.cmu.inmind.demo.osgi.LaunchpadStarter;
import edu.cmu.inmind.demo.osgi.LaunchpadStarterController;
import edu.cmu.inmind.multiuser.controller.blackboard.Blackboard;
import edu.cmu.inmind.multiuser.controller.blackboard.BlackboardEvent;
import edu.cmu.inmind.multiuser.controller.blackboard.BlackboardSubscription;
import edu.cmu.inmind.multiuser.controller.common.CommonUtils;
import edu.cmu.inmind.multiuser.controller.common.Constants;
import edu.cmu.inmind.multiuser.controller.log.Log4J;
import edu.cmu.inmind.multiuser.controller.plugin.PluggableComponent;
import edu.cmu.inmind.multiuser.controller.plugin.StateType;
import edu.cmu.inmind.services.muf.data.OSGiService;
import edu.cmu.inmind.services.muf.inputs.LaunchpadInput;
import edu.cmu.inmind.services.muf.inputs.ServiceRegistryInput;
import org.osgi.framework.ServiceReference;
import edu.cmu.inmind.services.muf.outputs.LaunchpadOutput;

import java.util.Map;

import static edu.cmu.inmind.demo.common.DemoConstants.MSG_LP_OUTPUT_CMD;
import static edu.cmu.inmind.services.muf.commons.Constants.*;

/***
 * Merging Ankit's changes
 */
@StateType(state = Constants.STATEFULL)
@BlackboardSubscription(messages = {DemoConstants.MSG_LP_INPUT_CMD, DemoConstants.MSG_LP_LIST_SERVICES
, DemoConstants.MSG_LP_START_SERVICE}) // MSG_LP_START_SERVICE, MSG_LP_LIST_SERVICES})
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
            //System.out.println("LaunchpadInputCommand: " + launchpadInputCommand);

            // process the input command
            switch (launchpadInputCommand) {

                case MSG_LP_START_SERVICE: {

                    OSGiService osGiService = launchpadInput.getOSGiService();

                    // start the service using launchpad
                    launchpadStarterController.startService(osGiService);

                    // Note: if the service has not started, then, the service mapping
                    // into the registry will fail.
                    // So, sleep for a while because the service may not have been started
                    // or we could sync call the service registration component
                    CommonUtils.sleep(2000);

                    // register the service with the service registry (service mapping)
                    ServiceRegistryInput serviceRegistryInput =
                            new ServiceRegistryInput.RegisterServiceBuilder(MSG_SR_REGISTER_SERVICE)
                                    .setOSGiService(osGiService).build();

                    // update the blackboard with the register service request
                    blackboard.post(this, MSG_SR_REGISTER_SERVICE, serviceRegistryInput);

                    break;
                }

                case DemoConstants.MSG_SR_RESP_REGISTER_SERVICE: {

                    // update the blackboard with the output: no output object required, hence the object is the same
                    blackboard.post(this, MSG_LP_OUTPUT_CMD, "OSGiService deployed: " + launchpadInput.getOSGiService());

                    break;
                }

                case MSG_LP_GET_SERVICE_IMPL: {

                    ServiceReference[] serviceReferences = launchpadStarterController.getServices(launchpadInput.getOSGiService());

                    // map the services that exist in the bundle
                    if (LaunchpadStarter.bundleHasServices(serviceReferences)) {

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

                    // there are no services in this bundle to map/register
                    else {

                        // so, update the blackboard by saying that the OSGiService has been deployed
                        // update the blackboard with the output: no output object required, hence the object is the same
                        blackboard.post(this, MSG_LP_OUTPUT_CMD, "OSGiService deployed: " + launchpadInput.getOSGiService());
                    }

                    break;
                }
                case DemoConstants.MSG_LP_LIST_SERVICES: {

                    // list the services using launchpad
                    launchpadStarterController.listServices();

                    // output
                    // .. do nothing ..

                    break;
                }
                case DemoConstants.MSG_LP_GET_ALL_SERVICES: {

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
                default: {
                    Log4J.info(this, "Inside OSGiLaunchpadComponent.handleService " + " -- not sure what to do.");
                    break;
                }
            }

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
