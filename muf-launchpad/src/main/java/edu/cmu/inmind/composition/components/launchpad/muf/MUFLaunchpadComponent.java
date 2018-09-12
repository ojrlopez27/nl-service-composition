package edu.cmu.inmind.composition.components.launchpad.muf;

import edu.cmu.inmind.composition.components.launchpad.core.LaunchpadStarterMain;
import edu.cmu.inmind.multiuser.controller.blackboard.Blackboard;
import edu.cmu.inmind.multiuser.controller.blackboard.BlackboardEvent;
import edu.cmu.inmind.multiuser.controller.common.Constants;
import edu.cmu.inmind.multiuser.controller.log.Log4J;
import edu.cmu.inmind.multiuser.controller.plugin.PluggableComponent;
import edu.cmu.inmind.multiuser.controller.plugin.StateType;

@StateType(state = Constants.STATEFULL)
public class MUFLaunchpadComponent extends PluggableComponent {

    @Override
    public void startUp(){
        super.startUp();
        Log4J.info(this, "Inside MUFLaunchpadController.startUp");
    }

    @Override
    public void execute() {
        Log4J.info(this, "Inside MUFLaunchpadController.execute" + hashCode());
        Blackboard blackboard = getBlackBoard(getSessionId());
        LaunchpadStarterMain.init();
    }

    @Override
    public void shutDown() {
        super.shutDown();
        Log4J.info(this, "Inside MUFLaunchpadController.shutDwon");
    }

    @Override
    public void onEvent(Blackboard blackboard, BlackboardEvent blackboardEvent) throws Throwable {
        Log4J.info(this, "Inside MUFLaunchpadController.onEvent");
    }
}
