package edu.cmu.inmind.services.muf.data;

import edu.cmu.inmind.multiuser.controller.common.CommonUtils;
import edu.cmu.inmind.multiuser.controller.communication.SessionMessage;
import edu.cmu.inmind.services.muf.inputs.ServiceRegistryInput;

import static edu.cmu.inmind.services.muf.commons.Constants.MSG_SR_INPUT_CMD;

public class ServiceRegistryMessage extends SessionMessage {

    public ServiceRegistryMessage() {
        super();
        super.setRequestType(MSG_SR_INPUT_CMD);
    }

    public void setPayload(ServiceRegistryInput serviceRegistryInput) {
        super.setPayload(CommonUtils.toJson(serviceRegistryInput));
    }

    @Override
    public String toString() {
        return "ServiceRegistryMessage{" +
                "sessionId=" + getSessionId() + ", " +
                "messageId=" + getMessageId() + ", " +
                "requestType=" + getRequestType() + ", " +
                "payload=" + getPayload() +
                "} ";
    }
}
