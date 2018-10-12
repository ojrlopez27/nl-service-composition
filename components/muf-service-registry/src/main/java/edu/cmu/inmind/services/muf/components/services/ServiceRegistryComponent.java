package edu.cmu.inmind.services.muf.components.services;

import edu.cmu.inmind.multiuser.controller.blackboard.Blackboard;
import edu.cmu.inmind.multiuser.controller.blackboard.BlackboardEvent;
import edu.cmu.inmind.multiuser.controller.blackboard.BlackboardSubscription;
import edu.cmu.inmind.multiuser.controller.common.CommonUtils;
import edu.cmu.inmind.multiuser.controller.common.Constants;
import edu.cmu.inmind.multiuser.controller.log.Log4J;
import edu.cmu.inmind.multiuser.controller.plugin.PluggableComponent;
import edu.cmu.inmind.multiuser.controller.plugin.StateType;
import edu.cmu.inmind.services.muf.components.services.registry.ServiceRegistryController;
import edu.cmu.inmind.services.muf.data.ServiceRegistryMessage;
import edu.cmu.inmind.services.muf.inputs.LaunchpadInput;
import edu.cmu.inmind.services.muf.inputs.ServiceRegistryInput;
import java.lang.reflect.Array;
import org.osgi.framework.ServiceReference;

import static edu.cmu.inmind.services.muf.commons.Constants.MSG_LP_GET_ALL_SERVICES;
import static edu.cmu.inmind.services.muf.commons.Constants.MSG_LP_INPUT_CMD;
import static edu.cmu.inmind.services.muf.commons.Constants.MSG_LP_RESP_GET_ALL_SERVICES;
import static edu.cmu.inmind.services.muf.commons.Constants.MSG_LP_RESP_GET_SERVICE_IMPL;
import static edu.cmu.inmind.services.muf.commons.Constants.MSG_SR_INITIALIZE;
import static edu.cmu.inmind.services.muf.commons.Constants.MSG_SR_INPUT_CMD;

@StateType(state = Constants.STATEFULL)
@BlackboardSubscription(messages = {
        MSG_SR_INPUT_CMD,                       // SR processes all invocations to its methods
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
            ServiceRegistryMessage serviceRegistryMessage = (ServiceRegistryMessage) blackboardEvent.getElement();
            String serviceRegistryMessageId = serviceRegistryMessage.getMessageId();

            switch (serviceRegistryMessageId) {
                case MSG_SR_INITIALIZE: {

                    // obtain the list of all services loaded by launchpad
                    LaunchpadInput launchpadInput = new LaunchpadInput.VanillaBuilder(MSG_LP_GET_ALL_SERVICES).build();
                    blackboard.post(this, MSG_LP_INPUT_CMD, launchpadInput);

                    // .. now, there has to be a mechanism for launchpad to post the services
                    // as a ServiceRegistryMessage object and then process the response below..
                }
                case MSG_LP_RESP_GET_ALL_SERVICES: {

                    // what happens if we don't know the dims? or should we sent another request to get the number of services..
                    // or, is there a method in CommonUtils that creates arrays as well, or may be, I should convert to JSON while sending from launchpad..
                    ServiceReference[] serviceRefs = (ServiceReference[]) Array.newInstance(ServiceReference.class);    //, dims);

                }

            }

            // output..

        }
        catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
