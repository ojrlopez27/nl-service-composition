package edu.cmu.inmind.services.muf.core;

import edu.cmu.inmind.multiuser.controller.blackboard.Blackboard;
import edu.cmu.inmind.multiuser.controller.blackboard.BlackboardEvent;
import edu.cmu.inmind.multiuser.controller.blackboard.BlackboardSubscription;
import edu.cmu.inmind.multiuser.controller.common.CommonUtils;
import edu.cmu.inmind.multiuser.controller.communication.SessionMessage;
import edu.cmu.inmind.multiuser.controller.log.Log4J;
import edu.cmu.inmind.multiuser.controller.orchestrator.ProcessOrchestratorImpl;
import edu.cmu.inmind.multiuser.controller.session.Session;
import edu.cmu.inmind.services.muf.inputs.LaunchpadInput;

import static edu.cmu.inmind.services.muf.commons.Constants.MSG_LAUNCHPAD;
import static edu.cmu.inmind.services.muf.commons.Constants.MSG_LP_OUTPUT_CMD;
import static edu.cmu.inmind.services.muf.commons.Constants.MSG_OSGI_SERVICE_DEPLOYED;

@BlackboardSubscription(messages = {MSG_LP_OUTPUT_CMD})
public class MUFOrchestrator extends ProcessOrchestratorImpl {

    // only for testing MUF 3.0.55
    public final static String STRING = "String";

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

        // this is important: only then the blackboard keeps objects with it.
        blackboard.setKeepModel(Boolean.TRUE);


        // read the input message coming from the client
        SessionMessage inputSessionMessage = CommonUtils.fromJson(message, SessionMessage.class);
        Log4J.info(this, "Inside MUFOrchestrator.process ");

        switch (inputSessionMessage.getRequestType()) {
            case MSG_LAUNCHPAD: {
                LaunchpadInput launchpadInput = CommonUtils.fromJson(inputSessionMessage.getPayload(), LaunchpadInput.class);
                String serviceName = launchpadInput.getOSGiService().getServiceName();

                SessionMessage outputSessionMessage = new SessionMessage();
                outputSessionMessage.setSessionId(inputSessionMessage.getSessionId());
                outputSessionMessage.setRequestType(MSG_LAUNCHPAD);
                outputSessionMessage.setMessageId(MSG_OSGI_SERVICE_DEPLOYED);
                outputSessionMessage.setPayload("From Server: " + serviceName);
                sendResponse(outputSessionMessage);
                break;
            }
            // only for testing MUF 3.0.55
            case STRING: {
                SessionMessage opSessionMessage = new SessionMessage();
                opSessionMessage.setSessionId(inputSessionMessage.getSessionId());
                opSessionMessage.setRequestType(MSG_LAUNCHPAD);
                opSessionMessage.setMessageId(STRING);
                opSessionMessage.setPayload("String Resp.. " + inputSessionMessage.getPayload());
                sendResponse(opSessionMessage);
                break;
            }


        }

        /*
        // the client may request several types of inputs
        switch(inputSessionMessage.getRequestType()) {

            // if the request type is of the launchpad
            case MSG_LAUNCHPAD:
                LaunchpadInput launchpadInput = CommonUtils.fromJson(inputSessionMessage.getPayload(), LaunchpadInput.class);
                System.out.println("In MSG_LAUNCHPAD: LaunchpadInput before post(): " + launchpadInput);
                blackboard.post(this, MSG_LP_INPUT_CMD, launchpadInput);
                break;

            case MSG_SERVICE_REGISTRY:
                ServiceRegistryInput serviceRegistryInput = CommonUtils.fromJson(inputSessionMessage.getPayload(), ServiceRegistryInput.class);
                System.out.println("In MSG_SERVICE_REGISTRY: ServiceRegistryInput before post(): " + serviceRegistryInput);
                blackboard.post(this, inputSessionMessage.getMessageId(), serviceRegistryInput);
                break;

            // if the request type is of sent2vec
            case MSG_SENT2VEC:
                processSent2Vec(inputSessionMessage);
                break;
        }
        */

        // do not send any output to the client from here
        // use the below onEvent() method instead

    }

    @Override
    public void onEvent(Blackboard blackboard, BlackboardEvent event) throws Throwable {
        super.onEvent(blackboard, event);

        // send the response back to the client
        /* SessionMessage outputSessionMessage = new SessionMessage();
        outputSessionMessage.setSessionId(event.getSessionId());
        outputSessionMessage.setRequestType(MSG_LAUNCHPAD);
        outputSessionMessage.setMessageId(MSG_OSGI_SERVICE_DEPLOYED);
        outputSessionMessage.setPayload(CommonUtils.toJson(event.getElement()));
        sendResponse(outputSessionMessage);
        */
    }

    // dummy method only; it will eventually be removed
    private void processSent2Vec(SessionMessage inputMessage) {
        if (inputMessage == null) return;

        String inputPayload = inputMessage.getPayload();
        Log4J.info(this, "Inside MUFOrchestrator.processSent2Vec " + " will eventually post to Sent2VecComponent");
    }

}
