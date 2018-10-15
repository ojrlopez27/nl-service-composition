package edu.cmu.inmind.services.muf.components.services;

import edu.cmu.inmind.multiuser.controller.blackboard.Blackboard;
import edu.cmu.inmind.multiuser.controller.blackboard.BlackboardEvent;
import edu.cmu.inmind.multiuser.controller.blackboard.BlackboardSubscription;
import edu.cmu.inmind.multiuser.controller.common.Constants;
import edu.cmu.inmind.multiuser.controller.log.Log4J;
import edu.cmu.inmind.multiuser.controller.plugin.PluggableComponent;
import edu.cmu.inmind.multiuser.controller.plugin.StateType;
import edu.cmu.inmind.services.muf.components.services.registry.ServiceMapper;
import edu.cmu.inmind.services.muf.components.services.registry.ServiceRegistryController;
import edu.cmu.inmind.services.muf.data.OSGiService;
import edu.cmu.inmind.services.muf.inputs.LaunchpadInput;
import edu.cmu.inmind.services.muf.inputs.ServiceRegistryInput;
import edu.cmu.inmind.services.muf.outputs.LaunchpadOutput;
import java.util.Map;
import org.osgi.framework.ServiceReference;

import static edu.cmu.inmind.services.muf.commons.Constants.MSG_LP_GET_ALL_SERVICES;
import static edu.cmu.inmind.services.muf.commons.Constants.MSG_LP_GET_SERVICE_IMPL;
import static edu.cmu.inmind.services.muf.commons.Constants.MSG_LP_INPUT_CMD;
import static edu.cmu.inmind.services.muf.commons.Constants.MSG_LP_RESP_GET_ALL_SERVICES;
import static edu.cmu.inmind.services.muf.commons.Constants.MSG_LP_RESP_GET_SERVICE_IMPL;
import static edu.cmu.inmind.services.muf.commons.Constants.MSG_SR_INITIALIZE;
import static edu.cmu.inmind.services.muf.commons.Constants.MSG_SR_REGISTER_SERVICE;

@StateType(state = Constants.STATEFULL)
@BlackboardSubscription(messages = {
        MSG_SR_INITIALIZE,                      // SR initializes the service registry by mapping all services from launchpad
        MSG_SR_REGISTER_SERVICE,                // SR maps the given service from launchpad
        MSG_LP_RESP_GET_ALL_SERVICES,           // SR asks for all services, so, it listens to the response from launchpad
        MSG_LP_RESP_GET_SERVICE_IMPL            // SR asks for a service's implementation, so, it listens to the response from launchpad
})
public class ServiceRegistryComponent extends PluggableComponent {

    ServiceRegistryController serviceRegistryController;

    @Override
    public void startUp() {
        super.startUp();
        Log4J.info(this, "Inside ServiceRegistryComponent.startUp");
        serviceRegistryController = new ServiceRegistryController();
    }

    @Override
    public void shutDown() {
        super.shutDown();
        Log4J.info(this, "Inside ServiceRegistryComponent.shutDwon");

        // TODO: should we unregister all services?
    }

    @Override
    public void onEvent(Blackboard blackboard, BlackboardEvent blackboardEvent) throws Throwable {
        Log4J.info(this, "Inside ServiceRegistryComponent.onEvent");

        try {

            // TODO: stopped here for the day -- assuming that the result from launchpad is sent as a ServiceRegistryMessage how would we distinguish between t

            // read the input command
            String serviceRegistryInputCommand = blackboardEvent.getId();
            Log4J.info(this, "ServiceRegistryInputCommand: " + serviceRegistryInputCommand);

            switch (serviceRegistryInputCommand) {
                case MSG_SR_INITIALIZE: {

                    // obtain the list of all services loaded by launchpad
                    LaunchpadInput launchpadInput = new LaunchpadInput.VanillaBuilder(MSG_LP_GET_ALL_SERVICES).build();

                    blackboard.post(this, MSG_LP_INPUT_CMD, launchpadInput);
                    break;

                    // .. now, there has to be a mechanism for launchpad to post the services
                    // as a ServiceRegistryMessage object and then process the response as below..
                }

                case MSG_SR_REGISTER_SERVICE: {

                    ServiceRegistryInput serviceRegistryInput = (ServiceRegistryInput) blackboardEvent.getElement();
                    OSGiService osGiService = serviceRegistryInput.getOsGiService();

                    LaunchpadInput launchpadInput =
                            new LaunchpadInput.GetServiceImplementationBuilder(MSG_LP_GET_SERVICE_IMPL)
                                    .setOSGiService(osGiService)
                                    .build();

                    blackboard.post(this, MSG_LP_INPUT_CMD, launchpadInput);
                    break;
                }

                case MSG_LP_RESP_GET_SERVICE_IMPL: {

                    // retrieve the service references sent by launchpad
                    LaunchpadOutput launchpadOutput = (LaunchpadOutput) blackboardEvent.getElement();
                    ServiceReference serviceReference = launchpadOutput.getServiceReference();
                    Object serviceImplObj = launchpadOutput.getServiceImpl();

                    // map the service reference and the service impl
                    ServiceMapper.map(serviceReference, serviceImplObj);

                    // print the service map to be sure if the services have been mapped
                    ServiceMapper.printServiceMap();
                    break;
                }
                case MSG_LP_RESP_GET_ALL_SERVICES: {

                    // retrieve the service references sent by launchpad
                    LaunchpadOutput launchpadOutput = (LaunchpadOutput) blackboardEvent.getElement();
                    Map<ServiceReference, Object> allServicesMap = launchpadOutput.getAllServices();

                    // map the service references
                    ServiceMapper.map(allServicesMap);

                    // print the service map to be sure if the services have been mapped
                    ServiceMapper.printServiceMap();
                    break;
                }

            }

            // output..

        }
        catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
