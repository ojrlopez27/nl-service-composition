package edu.cmu.inmind.demo.components;

import edu.cmu.inmind.multiuser.controller.blackboard.Blackboard;
import edu.cmu.inmind.multiuser.controller.blackboard.BlackboardEvent;
import edu.cmu.inmind.multiuser.controller.common.Constants;
import edu.cmu.inmind.multiuser.controller.log.Log4J;
import edu.cmu.inmind.multiuser.controller.plugin.PluggableComponent;
import edu.cmu.inmind.multiuser.controller.plugin.StateType;

@StateType(state = Constants.STATEFULL)
public class MiddlewareComponent extends PluggableComponent {

    @Override
    public void startUp() {
        super.startUp();
        Log4J.info(this, "Inside MiddlewareComponent.startUp");
    }

    @Override
    public void shutDown() {
        super.shutDown();
        Log4J.info(this, "Inside MiddlewareComponent.shutDwon");
    }

    @Override
    public void onEvent(Blackboard blackboard, BlackboardEvent blackboardEvent) throws Throwable {
        Log4J.info(this, "Inside MiddlewareComponent.onEvent");
    }
}
