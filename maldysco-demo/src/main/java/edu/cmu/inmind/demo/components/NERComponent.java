package edu.cmu.inmind.demo.components;

import edu.cmu.inmind.demo.common.DemoConstants;
import edu.cmu.inmind.demo.controllers.NERController;
import edu.cmu.inmind.multiuser.controller.blackboard.Blackboard;
import edu.cmu.inmind.multiuser.controller.blackboard.BlackboardEvent;
import edu.cmu.inmind.multiuser.controller.blackboard.BlackboardSubscription;
import edu.cmu.inmind.multiuser.controller.common.Constants;
import edu.cmu.inmind.multiuser.controller.log.Log4J;
import edu.cmu.inmind.multiuser.controller.plugin.PluggableComponent;
import edu.cmu.inmind.multiuser.controller.plugin.StateType;
import edu.cmu.inmind.multiuser.controller.session.Session;
/**
 * Created for demo : sakoju 10/4/2018
 */
@StateType(state = Constants.STATELESS)
@BlackboardSubscription(messages= DemoConstants.MSG_PROCESS_USER_ACTION)
public class NERComponent extends PluggableComponent {
    @Override
    public Session getSession() throws Throwable {
        return super.getSession();
    }

    @Override
    public void postCreate() {
        super.postCreate();
    }

    @Override
    public void execute() {
        super.execute();
    }

    @Override
    protected void startUp() {
        NERController.init();
        NERController.extractEntities(
                "First, Check ten times (1, 2, 3, four) the availability on calendar from August 3 to August 10 at 10:30am");
        super.startUp();
    }

    @Override
    public void shutDown() {
        super.shutDown();
    }

    @Override
    public void onEvent(Blackboard blackboard, BlackboardEvent blackboardEvent) throws Throwable {
        switch(blackboardEvent.getId())
        {
            case DemoConstants.MSG_PROCESS_USER_ACTION:
                Log4J.info(this, (String) blackboardEvent.getElement());
        }
    }

    @Override
    public String getSessionId() {
        return super.getSessionId();
    }


}
