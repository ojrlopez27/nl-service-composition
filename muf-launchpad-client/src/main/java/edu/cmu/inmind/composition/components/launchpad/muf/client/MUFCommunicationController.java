package edu.cmu.inmind.composition.components.launchpad.muf.client;

import edu.cmu.inmind.multiuser.controller.log.Log4J;
import org.zeromq.ZMQ;

public class MUFCommunicationController {

    private final String TAG = MUFCommunicationController.class.getSimpleName();
    private final static ZMQ.Context context = ZMQ.context(1);


    private static final String URL_SERVER_ADDRESS = "tcp://127.0.0.1";
    private static final String URL_SERVER_PORT = "5555";
    private static final String URL_SERVER = URL_SERVER_ADDRESS + ":" + URL_SERVER_PORT;

    private ZMQ.Socket clientLaunchpadStarter;

    public MUFCommunicationController() {
        System.out.println("in MUFCommunicationController..");
        clientLaunchpadStarter = context.socket(ZMQ.REQ);
        clientLaunchpadStarter.connect(URL_SERVER);
    }

    public void sendLaunchpadStarter(String message){
        Log4J.debug(TAG, "Sending message to launchpad server at: " + URL_SERVER);
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