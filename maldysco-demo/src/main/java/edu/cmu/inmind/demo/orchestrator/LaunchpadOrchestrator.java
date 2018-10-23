package edu.cmu.inmind.demo.orchestrator;

import edu.cmu.inmind.demo.common.DemoConstants;
import edu.cmu.inmind.multiuser.controller.blackboard.Blackboard;
import edu.cmu.inmind.multiuser.controller.blackboard.BlackboardEvent;
import edu.cmu.inmind.multiuser.controller.blackboard.BlackboardSubscription;
import edu.cmu.inmind.multiuser.controller.common.CommonUtils;
import edu.cmu.inmind.multiuser.controller.communication.SessionMessage;
import edu.cmu.inmind.multiuser.controller.log.Log4J;
import edu.cmu.inmind.multiuser.controller.orchestrator.ProcessOrchestratorImpl;
import edu.cmu.inmind.multiuser.controller.session.Session;
import edu.cmu.inmind.services.muf.commons.SessionMessageCreator;
import edu.cmu.inmind.services.muf.inputs.LaunchpadInput;
import edu.cmu.inmind.services.muf.inputs.ServiceRegistryInput;

import static edu.cmu.inmind.demo.common.DemoConstants.MSG_LAUNCHPAD;
import static edu.cmu.inmind.services.muf.commons.Constants.MSG_SERVICE_REGISTRY;

@BlackboardSubscription(messages = {DemoConstants.MSG_LP_OUTPUT_CMD})
public class LaunchpadOrchestrator extends ProcessOrchestratorImpl {
    @Override
    public void initialize(Session session) throws Throwable{
        super.initialize( session );
        Log4J.info(this, "Inside MUFOrchestrator.initialize ");

        // let's initialize the resources
        // ...

    }

    @Override
    public void process(String message) throws Throwable {
        super.process(message);
        Log4J.info(this, "Inside MUFOrchestrator.process ");

        // this is important: only then the blackboard keeps objects with it.
        blackboard.setKeepModel(Boolean.TRUE);

        // read the input message coming from the client
        SessionMessage inputSessionMessage = CommonUtils.fromJson(message, SessionMessage.class);
        Log4J.info(this,"Message from Client: " + CommonUtils.toJson(inputSessionMessage));

        // the client may request several types of inputs
        String requestType = inputSessionMessage.getRequestType();
        switch (requestType) {

            // if the request type is for the launchpad component
            case MSG_LAUNCHPAD: {
                LaunchpadInput launchpadInput = CommonUtils.fromJson(inputSessionMessage.getPayload(), LaunchpadInput.class);
                blackboard.post(this, DemoConstants.MSG_LP_INPUT_CMD, launchpadInput);
                break;
            }

            // if the request type is for the service registry component
            case MSG_SERVICE_REGISTRY: {
                ServiceRegistryInput serviceRegistryInput = CommonUtils.fromJson(inputSessionMessage.getPayload(), ServiceRegistryInput.class);
                blackboard.post(this, inputSessionMessage.getMessageId(), serviceRegistryInput);
                break;
            }

            // if the request type is for the sent2vec component
            case DemoConstants.MSG_SEND_TO_S2V: {
                processSent2Vec(inputSessionMessage);
                break;
            }

            // if there is no known component for the given request type
            default: {
                Log4J.info(this, "No known component for message request: " + requestType);
                break;
            }
        }

        // do not send any output to the client from here
        // use the below onEvent() method instead
    }

    @Override
    public void onEvent(Blackboard blackboard, BlackboardEvent event) throws Throwable {
        super.onEvent(blackboard, event);

        // send the response back to the client
        SessionMessage serverResponseMessage =
                new SessionMessageCreator(event.getSessionId())
                        .createServerResponseMessage(
                                DemoConstants.MSG_OSGI_SERVICE_DEPLOYED,
                                event.getElement()
                        );

        sendResponse(CommonUtils.toJson(serverResponseMessage));
    }

    // dummy method only; it will eventually be removed
    private void processSent2Vec(SessionMessage inputMessage) {
        if (inputMessage == null) return;

        String inputPayload = inputMessage.getPayload();
        Log4J.info(this, "Inside MUFOrchestrator.processSent2Vec " + " will eventually post to Sent2VecComponent");
    }

}
