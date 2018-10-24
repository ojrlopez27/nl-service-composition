package edu.cmu.inmind.demo.client;

import edu.cmu.inmind.demo.common.DemoConstants;
import edu.cmu.inmind.demo.common.Utils;
import edu.cmu.inmind.demo.components.S2V_CCC_Component;
import edu.cmu.inmind.multiuser.communication.ClientCommController;
import edu.cmu.inmind.multiuser.controller.blackboard.Blackboard;
import edu.cmu.inmind.multiuser.controller.blackboard.BlackboardEvent;
import edu.cmu.inmind.multiuser.controller.blackboard.BlackboardListener;
import edu.cmu.inmind.multiuser.controller.common.CommonUtils;
import edu.cmu.inmind.multiuser.controller.communication.ResponseListener;
import edu.cmu.inmind.multiuser.controller.communication.SessionMessage;
import edu.cmu.inmind.multiuser.controller.log.Log4J;
import edu.cmu.inmind.multiuser.log.LogC;

import java.util.Scanner;

public class S2V_CCC_Client {

    //private final static ZMQ.Context context = ZMQ.context(1);
    //private ZMQ.Socket clientSent2Vec;
    private ClientCommController clientCommController;
    private String sessionID = "s2v-partial-ccc";
    private String serverIPAddress="tcp://";
    private static String DEFAULT_IP_ADDRESS = "127.0.0.1:5555";
    private ResponseListener responseListener;
    private BlackboardListener blackboardListener;

    public S2V_CCC_Client() {
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
                : tempIPAddress+":5555";
        this.responseListener = responseListener != null ? responseListener :
                new S2V_CCC_Client.ClientResponseListener();
        this.sessionID += (!sessionID.isEmpty()) ? sessionID : this.sessionID;
        clientCommController = new ClientCommController.Builder(new LogC())
                .setServerAddress("tcp://"+DEFAULT_IP_ADDRESS)
                .setResponseListener(this.responseListener)
                .setSessionId(sessionID)
                .setRequestType(edu.cmu.inmind.multiuser.controller.common.Constants.REQUEST_CONNECT)
                .build();
    }
    public void sendS2V(String message){

        //clientSent2Vec.send(message);
        try
        {
            SessionMessage sessionMessage = new SessionMessage();
            sessionMessage.setRequestType(DemoConstants.MSG_SEND_TO_S2V);
            clientCommController.send(sessionID, CommonUtils.toJson(message));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public String receiveS2V(){
        try {
            Log4J.debug(this, "Waiting for sent2vec server....");
            //return;
            //clientSent2Vec.recvStr();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public void stopS2V() {
        //clientSent2Vec.close();
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
            SessionMessage sessionMessage = new SessionMessage();
            sessionMessage.setRequestType(DemoConstants.MSG_RECEIVE_S2V);
            sessionMessage.setPayload(message);

        }
    }

    public static void main(String args[]) throws Throwable
    {
        Scanner scanner = new Scanner(System.in);
        String input = "";
        S2V_CCC_Client s2VCccClient=null;
        while (!input.equals("stop") && !input.equals("disconnect"))
        {
            System.out.println("Enter a command:");
            input = scanner.nextLine();
            if(input.equals("start")) {
                s2VCccClient = new S2V_CCC_Client();
            }
            if (input.equals("stop")) {
                Log4J.info(s2VCccClient, "Do you want to end the session? " +
                        "Please type \"disconnect\"");
            }
            else if(input.equals("login"))
            {
                Log4J.info(s2VCccClient, "login");
                s2VCccClient.sendS2V(input);
                String result= s2VCccClient.receiveS2V();
                Log4J.info(s2VCccClient, result);

            }
            else if(input.contains("ready"))
            {
                Log4J.info(s2VCccClient, "ready");
                s2VCccClient.sendS2V(input);
                String result= s2VCccClient.receiveS2V();
                Log4J.info(s2VCccClient, result);
            }
            else
            {
                Log4J.info(s2VCccClient, input);
                s2VCccClient.sendS2V(input);
                String result= s2VCccClient.receiveS2V();
                Log4J.info(s2VCccClient, result);
            }
        }
        s2VCccClient.stopS2V();
        System.exit(0);
    }

}
