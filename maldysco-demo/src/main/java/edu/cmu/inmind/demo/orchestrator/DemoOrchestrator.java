package edu.cmu.inmind.demo.orchestrator;

import edu.cmu.inmind.demo.common.DemoConstants;
import edu.cmu.inmind.demo.data.LaunchpadInput;
import edu.cmu.inmind.demo.data.OSGiService;
import edu.cmu.inmind.multiuser.controller.blackboard.Blackboard;
import edu.cmu.inmind.multiuser.controller.blackboard.BlackboardEvent;
import edu.cmu.inmind.multiuser.controller.blackboard.BlackboardSubscription;
import edu.cmu.inmind.multiuser.controller.common.CommonUtils;
import edu.cmu.inmind.multiuser.controller.common.Constants;
import edu.cmu.inmind.multiuser.controller.communication.SessionMessage;
import edu.cmu.inmind.multiuser.controller.log.Log4J;
import edu.cmu.inmind.multiuser.controller.orchestrator.ProcessOrchestratorImpl;
import edu.cmu.inmind.multiuser.controller.plugin.StateType;
import edu.cmu.inmind.multiuser.controller.session.Session;
import edu.cmu.inmind.multiuser.log.LogC;

/**
 * Created for demo : sakoju 10/4/2018
 */
@StateType(state = Constants.STATELESS)
@BlackboardSubscription(messages= {DemoConstants.MSG_SEND_TO_S2V, DemoConstants.MSG_SEND_TO_CLIENT,
        DemoConstants.MSG_USER_VALIDATION_SUCCCES, DemoConstants.MSG_LP_OUTPUT_CMD})
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
                    sessionMessage.getPayload());
                break;
                //post always this to backboard
            case DemoConstants.MSG_PROCESS_USER_ACTION:
                blackboard.post(this, sessionMessage.getMessageId(),
                        sessionMessage.getPayload());
                break;
            case DemoConstants.MSG_GROUP_CHAT_READY:
                blackboard.post(this, sessionMessage.getMessageId(),
                        sessionMessage.getMessageId());
                break;
            /***
             * Merging Ankit's changes BEGIN
             */
            // if the request type is of the launchpad
            case DemoConstants.MSG_LAUNCHPAD:
                LaunchpadInput launchpadInput =
                        new LaunchpadInput(
                                sessionMessage.getMessageId(),
                                CommonUtils.fromJson(sessionMessage.getPayload(), OSGiService.class)
                        );
                blackboard.post(this, DemoConstants.MSG_LP_INPUT_CMD, launchpadInput);
                break;

            // Merging Ankit's changes ******************END


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
        LogC.info(this,
                "Demo orchestrator"+event.getElement().toString());
        switch(event.getId())
        {
            case DemoConstants.MSG_SEND_TO_S2V:
                    sendResponse(CommonUtils.toJson(event.getElement()));
                    break;
            case DemoConstants.MSG_SEND_TO_CLIENT:
                    sendResponse(CommonUtils.toJson(event.getElement()));
                    break;
            case DemoConstants.MSG_RECEIVE_S2V:
                //ideally call CompositionController to send the results.but for now just print.
                Log4J.info(this, ((SessionMessage)event.getElement()).getPayload());
            case DemoConstants.MSG_USER_VALIDATION_SUCCCES:
                sendResponse(event.getElement());
                break;
            /***
             * Merging Ankit's changes
             */
            case DemoConstants.MSG_LP_OUTPUT_CMD:
                // send the response back to the client
                SessionMessage outputSessionMessage = new SessionMessage();
                outputSessionMessage.setSessionId(event.getSessionId());
                outputSessionMessage.setRequestType(DemoConstants.MSG_LAUNCHPAD);
                outputSessionMessage.setMessageId(DemoConstants.MSG_OSGI_SERVICE_DEPLOYED);
                outputSessionMessage.setPayload(CommonUtils.toJson(event.getElement()));
                sendResponse(outputSessionMessage);
                break;
            // Ankit's changes ***** END
            default:
                break;
        }
        super.onEvent(blackboard, event);
    }
}
