package edu.cmu.inmind.services.muf.data;

import edu.cmu.inmind.multiuser.controller.common.CommonUtils;
import edu.cmu.inmind.multiuser.controller.communication.SessionMessage;

import static edu.cmu.inmind.services.muf.commons.Constants.MSG_LAUNCHPAD;

public class LaunchpadMessage extends SessionMessage {

    public LaunchpadMessage() {
        super();
        super.setRequestType(MSG_LAUNCHPAD);
    }

    public void setPayload(OSGiService payload) {
        super.setPayload(CommonUtils.toJson(payload));
    }

    @Override
    public void setSessionId(String sessionId) {
        super.setSessionId(sessionId);
    }

    @Override
    public void setMessageId(String messageId) {
        super.setMessageId(messageId);
    }

    @Override
    public String toString() {
        return "LaunchpadMessage{" +
                "sessionId=" + getSessionId() + ", " +
                "messageId=" + getMessageId() + ", " +
                "requestType=" + getRequestType() + ", " +
                "payload=" + getPayload() + "} ";
    }
}
