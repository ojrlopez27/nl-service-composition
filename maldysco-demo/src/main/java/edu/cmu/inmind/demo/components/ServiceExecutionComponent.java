package edu.cmu.inmind.demo.components;

import edu.cmu.inmind.demo.common.DemoConstants;
import edu.cmu.inmind.demo.common.ServiceMethod;
import edu.cmu.inmind.demo.controllers.CompositionController;
import edu.cmu.inmind.multiuser.controller.blackboard.Blackboard;
import edu.cmu.inmind.multiuser.controller.blackboard.BlackboardEvent;
import edu.cmu.inmind.multiuser.controller.blackboard.BlackboardSubscription;
import edu.cmu.inmind.multiuser.controller.common.Constants;
import edu.cmu.inmind.multiuser.controller.plugin.PluggableComponent;
import edu.cmu.inmind.multiuser.controller.plugin.StateType;

import java.util.Map;

@StateType(state = Constants.STATELESS)
@BlackboardSubscription(messages =  {DemoConstants.MSG_SEND_SERVICE_MAP})
public class ServiceExecutionComponent extends PluggableComponent {
    private CompositionController compositionController;
    private Map<String, ServiceMethod> serviceMap;

    @Override
    protected void startUp() {
        super.startUp();
        compositionController = CompositionController.getInstance();
    }

    @Override
    public void shutDown() {
        super.shutDown();
    }

    @Override
    public void onEvent(Blackboard blackboard, BlackboardEvent blackboardEvent) throws Throwable {
        /***
         * Service mapping is completed, so let us send the results for Service Execution
         */
        if(blackboardEvent.getId().equals(DemoConstants.MSG_SEND_SERVICE_MAP)) {
            serviceMap = (Map<String, ServiceMethod>) blackboardEvent.getElement();
            // let's simulate changes on QoS conditions
            compositionController.addPhoneStatusToWM();
            String[] result = compositionController.execute(serviceMap);
            blackboard.post(this, DemoConstants.MSG_SERVICE_EXECUTION,
                    result);
        }
    }
}
