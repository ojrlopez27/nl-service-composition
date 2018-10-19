package edu.cmu.inmind.demo.components;

import edu.cmu.inmind.demo.common.DemoConstants;
import edu.cmu.inmind.demo.controllers.NERController;
import edu.cmu.inmind.demo.pojos.NERPojo;
import edu.cmu.inmind.multiuser.controller.blackboard.Blackboard;
import edu.cmu.inmind.multiuser.controller.blackboard.BlackboardEvent;
import edu.cmu.inmind.multiuser.controller.blackboard.BlackboardSubscription;
import edu.cmu.inmind.multiuser.controller.common.Constants;
import edu.cmu.inmind.multiuser.controller.communication.SessionMessage;
import edu.cmu.inmind.multiuser.controller.log.Log4J;
import edu.cmu.inmind.multiuser.controller.plugin.PluggableComponent;
import edu.cmu.inmind.multiuser.controller.plugin.StateType;
import edu.cmu.inmind.multiuser.controller.session.Session;

import java.util.List;

/**
 * Created for demo : sakoju 10/4/2018
 */
@StateType(state = Constants.STATELESS)
@BlackboardSubscription(messages= DemoConstants.MSG_SEND_TO_NER)
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
            case DemoConstants.MSG_SEND_TO_NER:
                SessionMessage message = (SessionMessage) blackboardEvent.getElement();
                Log4J.info(this, message.getPayload());
                List<NERPojo> nerPojoList = NERController.extractEntities(message.getPayload());
                Log4J.info(this, "Entities (annotations) identified "+nerPojoList.size());
                //TODO: now that entities are identified, process remaining : service mapping, service execution,
        }
    }

    @Override
    public String getSessionId() {
        return super.getSessionId();
    }


}
