package edu.cmu.inmind.services.muf.components;

import edu.cmu.inmind.multiuser.controller.blackboard.Blackboard;
import edu.cmu.inmind.multiuser.controller.blackboard.BlackboardEvent;
import edu.cmu.inmind.multiuser.controller.common.Constants;
import edu.cmu.inmind.multiuser.controller.log.Log4J;
import edu.cmu.inmind.multiuser.controller.plugin.PluggableComponent;
import edu.cmu.inmind.multiuser.controller.plugin.StateType;

@StateType(state = Constants.STATELESS)
public class OSGiDeployerComponent extends PluggableComponent {

    @Override
    public void startUp(){
        super.startUp();
        Log4J.info(this, "Inside OSGiDeployerComponent.startUp");
    }

    @Override
    public void shutDown() {
        super.shutDown();
        Log4J.info(this, "Inside OSGiDeployerComponent.shutDwon");
    }

    @Override
    public void onEvent(Blackboard blackboard, BlackboardEvent blackboardEvent) throws Throwable {
        Log4J.info(this, "Inside OSGiDeployerComponent.onEvent");
    }
}
