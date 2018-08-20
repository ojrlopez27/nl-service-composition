package edu.cmu.inmind.composition.components;

import edu.cmu.inmind.multiuser.controller.blackboard.Blackboard;
import edu.cmu.inmind.multiuser.controller.blackboard.BlackboardEvent;
import edu.cmu.inmind.multiuser.controller.common.Constants;
import edu.cmu.inmind.multiuser.controller.log.Log4J;
import edu.cmu.inmind.multiuser.controller.plugin.PluggableComponent;
import edu.cmu.inmind.multiuser.controller.plugin.StateType;

/**
 * Created by oscarr on 8/19/18.
 */
@StateType(state = Constants.STATEFULL)
public class MKTController extends PluggableComponent {

    @Override
    public void startUp(){
        super.startUp();
        Log4J.info(this, "Inside MKTController.startUp");
    }

    @Override
    public void shutDown() {
        super.shutDown();
        Log4J.info(this, "Inside MKTController.shutDwon");
    }

    @Override
    public void onEvent(Blackboard blackboard, BlackboardEvent blackboardEvent) throws Throwable {
        Log4J.info(this, "Inside MKTController.onEvent");
    }
}
