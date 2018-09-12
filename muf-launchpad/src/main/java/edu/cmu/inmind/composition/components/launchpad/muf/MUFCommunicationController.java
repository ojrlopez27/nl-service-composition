package edu.cmu.inmind.composition.components.launchpad.muf;

import edu.cmu.inmind.multiuser.controller.common.CommonUtils;
import edu.cmu.inmind.multiuser.controller.log.Log4J;
import org.zeromq.ZMQ;

import static edu.cmu.inmind.composition.components.launchpad.commons.Constants.MUF_SERVER_COMMUNICATION;

public class MUFCommunicationController {

    private final String TAG = MUFCommunicationController.class.getSimpleName();
    private final static ZMQ.Context context = ZMQ.context(1);
    private ZMQ.Socket clientLaunchpadStarter;

    public MUFCommunicationController() {
        System.out.println("in mufcommcontroller..");
        clientLaunchpadStarter = context.socket(ZMQ.REQ);
        clientLaunchpadStarter.connect("tcp://localhost:" + CommonUtils.getProperty(MUF_SERVER_COMMUNICATION));
    }

    public void sendLaunchpadStarter(String message){
        clientLaunchpadStarter.send(message);
    }

    public String receiveLaunchpadStarter() {
        try {
            Log4J.debug(TAG, "Waiting for launchpad server....");
            return clientLaunchpadStarter.recvStr();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return "";
    }

    public void stopLaunchpadStarter() {
        clientLaunchpadStarter.close();
    }
}