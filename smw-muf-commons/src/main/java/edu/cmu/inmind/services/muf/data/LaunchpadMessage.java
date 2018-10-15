package edu.cmu.inmind.services.muf.data;

import edu.cmu.inmind.multiuser.controller.common.CommonUtils;
import edu.cmu.inmind.multiuser.controller.communication.SessionMessage;
import edu.cmu.inmind.services.muf.inputs.LaunchpadInput;

import static edu.cmu.inmind.services.muf.commons.Constants.MSG_LAUNCHPAD;
import static edu.cmu.inmind.services.muf.commons.Constants.MSG_LP_INPUT_CMD;

public class LaunchpadMessage extends SessionMessage {

    public LaunchpadMessage() {
        super();
        super.setRequestType(MSG_LAUNCHPAD);
    }

    public void setPayload(LaunchpadInput launchpadInput) {
        super.setPayload(CommonUtils.toJson(launchpadInput));
    }

    @Override
    public String toString() {
        return "LaunchpadMessage{" +
                "sessionId=" + getSessionId() + ", " +
                "messageId=" + getMessageId() + ", " +
                "requestType=" + getRequestType() + ", " +
                "payload=" + getPayload() +
                "} ";
    }
}
