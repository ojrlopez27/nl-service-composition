package edu.cmu.inmind.demo.orchestrator;

import edu.cmu.inmind.demo.common.DemoConstants;
import edu.cmu.inmind.multiuser.controller.blackboard.Blackboard;
import edu.cmu.inmind.multiuser.controller.blackboard.BlackboardEvent;
import edu.cmu.inmind.multiuser.controller.blackboard.BlackboardListener;
import edu.cmu.inmind.multiuser.controller.blackboard.BlackboardSubscription;
import edu.cmu.inmind.multiuser.controller.common.CommonUtils;
import edu.cmu.inmind.multiuser.controller.common.Constants;
import edu.cmu.inmind.multiuser.controller.common.Utils;
import edu.cmu.inmind.multiuser.controller.communication.SessionMessage;
import edu.cmu.inmind.multiuser.controller.orchestrator.ProcessOrchestratorImpl;
import edu.cmu.inmind.multiuser.controller.plugin.StateType;
import edu.cmu.inmind.multiuser.controller.session.Session;
/**
 * Created for demo : sakoju 10/4/2018
 */
@StateType(state = Constants.STATELESS)
@BlackboardSubscription(messages= {DemoConstants.MSG_SEND_TO_S2V, DemoConstants.MSG_SEND_TO_CLIENT})
public class DemoOrchestrator extends ProcessOrchestratorImpl {
    @Override
    public void process(String input) throws Throwable {
        super.process(input);
        SessionMessage sessionMessage = CommonUtils.fromJson(input,
                SessionMessage.class);
        switch(sessionMessage.getRequestType()) {
            //check if we received userID, then post it on blackboard
            case DemoConstants.MSG_CHECK_USER_ID:
                blackboard.post(this, sessionMessage.getMessageId(),
                    sessionMessage);
                break;
            case DemoConstants.MSG_PROCESS_USER_ACTION:
                break;
            default:
                    break;
        }
    }

    @Override
    public void initialize(Session session) throws Throwable {
        super.initialize(session);
    }

    @Override
    public void onEvent(Blackboard blackboard, BlackboardEvent event) throws Throwable {
        if(event.getId().equals(DemoConstants.MSG_SEND_TO_S2V))
        {
            sendResponse(event.getElement());
        }
        super.onEvent(blackboard, event);
    }
}
