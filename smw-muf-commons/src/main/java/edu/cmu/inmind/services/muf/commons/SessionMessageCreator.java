package edu.cmu.inmind.services.muf.commons;

import edu.cmu.inmind.multiuser.controller.common.CommonUtils;
import edu.cmu.inmind.multiuser.controller.communication.SessionMessage;
import edu.cmu.inmind.services.muf.inputs.LaunchpadInput;
import edu.cmu.inmind.services.muf.inputs.ServiceRegistryInput;

import static edu.cmu.inmind.services.muf.commons.Constants.MSG_LAUNCHPAD;
import static edu.cmu.inmind.services.muf.commons.Constants.MSG_SERVER_RESPONSE;
import static edu.cmu.inmind.services.muf.commons.Constants.MSG_SERVICE_REGISTRY;

public class SessionMessageCreator {
    private String sessionId;

    public SessionMessageCreator(String sessionId) {
        this.sessionId = sessionId;
    }

    public SessionMessage createLaunchpadMessage(String messageId, LaunchpadInput launchpadInput) {
        return createSessionMessage(MSG_LAUNCHPAD, messageId, launchpadInput);
    }

    public SessionMessage createServiceRegistryMessage(String messageId, ServiceRegistryInput serviceRegistryInput) {
        return createSessionMessage(MSG_SERVICE_REGISTRY, messageId, serviceRegistryInput);
    }

    public SessionMessage createServerResponseMessage(String messageId, Object payload) {
        return createSessionMessage(MSG_SERVER_RESPONSE, messageId, payload);
    }

    public SessionMessage createSessionMessage(String requestType, String messageId, Object payload) {
        SessionMessage launchpadMessage = new SessionMessage();
        launchpadMessage.setSessionId(sessionId);
        launchpadMessage.setRequestType(requestType);
        launchpadMessage.setMessageId(messageId);
        launchpadMessage.setPayload(CommonUtils.toJson(payload));
        return launchpadMessage;
    }
}
