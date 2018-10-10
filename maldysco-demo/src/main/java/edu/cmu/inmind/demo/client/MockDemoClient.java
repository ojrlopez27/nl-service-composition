package edu.cmu.inmind.demo.client;

import edu.cmu.inmind.demo.common.DemoConstants;
import edu.cmu.inmind.demo.common.Utils;
import edu.cmu.inmind.multiuser.communication.ClientCommController;
import edu.cmu.inmind.multiuser.controller.common.CommonUtils;
import edu.cmu.inmind.multiuser.controller.common.Constants;
import edu.cmu.inmind.multiuser.controller.communication.ResponseListener;
import edu.cmu.inmind.multiuser.controller.communication.SessionMessage;
import edu.cmu.inmind.multiuser.controller.log.Log4J;
import edu.cmu.inmind.multiuser.log.LogC;

import java.util.Scanner;


public class MockDemoClient {
    private ClientCommController clientCommController;
    private String sessionID = "demo-sakoju-client";
    private String serverIPAddress="tcp://";
    private static String DEFAULT_IP_ADDRESS = "127.0.0.1/5555";
    private ResponseListener responseListener;

    public MockDemoClient(String serverIPAddress, String sessionID,
                          ResponseListener responseListener)
    {
        if(!serverIPAddress.equals("") && !sessionID.equals("")) {
            connect(serverIPAddress, sessionID, responseListener);
        }
        else
        {
            connect("",this.sessionID,null);
        }
    }

    public void connect(String serverIPAddress, String sessionID,
                        ResponseListener responseListener)
    {
        String tempIPAddress = Utils.getSystemIPAddress();
        Log4J.info(this, tempIPAddress);
        serverIPAddress += tempIPAddress.isEmpty() ? DEFAULT_IP_ADDRESS
                : tempIPAddress;
        this.responseListener = responseListener != null ? responseListener :
                new ClientResponseListener();
        this.sessionID += (!sessionID.isEmpty()) ? sessionID : this.sessionID;
        clientCommController = new ClientCommController.Builder(new LogC())
                .setServerAddress("tcp://"+serverIPAddress+":5556")
                .setResponseListener(this.responseListener)
                .setSessionId(sessionID)
                .setRequestType(Constants.REQUEST_CONNECT)
                .build();
    }

    /***
     * by default : send no values for CCC
     */
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
     * ResponseListener : custom implementation on receiving a message: eg: if user has to reply Y/N etc.
     */
    public class ClientResponseListener implements ResponseListener
    {
        @Override
        public void process(String message)
        {
            LogC.info(this,
                    "Client Response Listener"+message);
        }
    }

    public static void main(String args[]) throws Throwable
    {
        Scanner scanner = new Scanner(System.in);
        String input = "";
        MockDemoClient mockDemoClient=null;
        while (!input.equals("stop") && !input.equals("disconnect"))
        {
            System.out.println("Enter a command:");
            input = scanner.nextLine();
            if(input.equals("start")) {
                mockDemoClient = new MockDemoClient("", "",
                        null);
            }
            if (input.equals("stop")) {
                Log4J.info(mockDemoClient, "Do you want to end the session? " +
                        "Please type \"disconnect\"");
            }
            else if(input.contains("login"))
            {
                mockDemoClient.clientCommController.send(mockDemoClient.sessionID,
                    new SessionMessage(DemoConstants.MSG_CHECK_USER_ID,
                            mockDemoClient.sessionID));
            }
            else if (input.equals("disconnect")) {
                mockDemoClient.clientCommController.disconnect(mockDemoClient.sessionID);
            }
            else
            {
                mockDemoClient.clientCommController.send(mockDemoClient.sessionID,
                        new SessionMessage(DemoConstants.MSG_PROCESS_USER_ACTION, input));
            }
        }
        System.exit(0);
    }
}
