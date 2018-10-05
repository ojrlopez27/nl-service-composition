package edu.cmu.inmind.demo.client;

import edu.cmu.inmind.demo.common.Utils;
import edu.cmu.inmind.multiuser.communication.ClientCommController;
import edu.cmu.inmind.multiuser.controller.common.CommonUtils;
import edu.cmu.inmind.multiuser.controller.common.Constants;
import edu.cmu.inmind.multiuser.controller.communication.ResponseListener;
import edu.cmu.inmind.multiuser.controller.communication.SessionMessage;
import edu.cmu.inmind.multiuser.log.LogC;

public class MockDemoClient {
    private ClientCommController clientCommController;
    private String sessionID = "demo-sakoju-client";
    private String serverIPAddress="tcp://";
    private static String DEFAULT_IP_ADDRESS = "127.0.0.1/5555";
    private ResponseListener responseListener;

    public MockDemoClient(String serverIPAddress, String sessionID,
                          ResponseListener responseListener)
    {
        String tempIPAddress= Utils.getSystemIPAddress();
        serverIPAddress += tempIPAddress.isEmpty()?DEFAULT_IP_ADDRESS
                            :tempIPAddress;
        this.responseListener =responseListener!=null? responseListener:
            new ClientResponseListener();
        this.sessionID += (!sessionID.isEmpty())?sessionID:"";
        clientCommController = new ClientCommController.Builder(new LogC())
                .setServerAddress(serverIPAddress)
                .setResponseListener(this.responseListener)
                .setSessionId(sessionID)
                .setRequestType(Constants.REQUEST_CONNECT)
                .build();
    }

    public MockDemoClient()
    {
        this(null, null, null);
    }

    /***
     * Send message to active session on MUF server
     */
    public void send(Object message)
    {
        try
        {
            SessionMessage sessionMessage = new SessionMessage();
            sessionMessage.setSessionId(sessionID);
            clientCommController.send(sessionID, CommonUtils.toJson(message));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    /***
     * Disconnect client connection to an active session in MUF server
     */
    public void disconnect()
    {
        clientCommController.disconnect(sessionID);
    }

    /***
     * ResponseListener : custom implementation on receiving a message: see if user has to reply Y/N etc.
     */
    public class ClientResponseListener implements ResponseListener
    {
        @Override
        public void process(String message)
        {
            LogC.info(this, "Client Response Listener"+message);
        }
    }
}
