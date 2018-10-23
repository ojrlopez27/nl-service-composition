package edu.cmu.inmind.demo.components;

import edu.cmu.inmind.demo.common.DemoConstants;
import edu.cmu.inmind.demo.common.ServiceMethod;
import edu.cmu.inmind.demo.common.Utils;
import edu.cmu.inmind.multiuser.controller.blackboard.Blackboard;
import edu.cmu.inmind.multiuser.controller.blackboard.BlackboardEvent;
import edu.cmu.inmind.multiuser.controller.blackboard.BlackboardSubscription;
import edu.cmu.inmind.multiuser.controller.common.Constants;
import edu.cmu.inmind.multiuser.controller.plugin.PluggableComponent;
import edu.cmu.inmind.multiuser.controller.plugin.StateType;

import java.util.Map;

@StateType(state = Constants.STATELESS)
@BlackboardSubscription(messages =  {DemoConstants.MSG_GET_SERVICE_MAP})
public class ServiceMappingComponent extends PluggableComponent {
    private Map<String, ServiceMethod> serviceMap;

    @Override
    protected void startUp() {
        super.startUp();
        serviceMap = Utils.generateCorporaFromMethods(false);
    }

    @Override
    public void shutDown() {
        super.shutDown();
    }

    @Override
    public void onEvent(Blackboard blackboard, BlackboardEvent blackboardEvent) throws Throwable {
        /***
         * Rule Engine is now ready, so let us send service map
         */
        if(blackboardEvent.getId().equals(DemoConstants.MSG_GET_SERVICE_MAP))
        {
            blackboard.post(this, DemoConstants.MSG_SEND_SERVICE_MAP,
                    serviceMap);
        }
    }
}
