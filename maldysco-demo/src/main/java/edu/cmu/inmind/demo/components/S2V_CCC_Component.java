package edu.cmu.inmind.demo.components;

import edu.cmu.inmind.demo.common.DemoConstants;
import edu.cmu.inmind.demo.common.Utils;
import edu.cmu.inmind.multiuser.communication.ClientCommController;
import edu.cmu.inmind.multiuser.controller.blackboard.Blackboard;
import edu.cmu.inmind.multiuser.controller.blackboard.BlackboardEvent;
import edu.cmu.inmind.multiuser.controller.blackboard.BlackboardListener;
import edu.cmu.inmind.multiuser.controller.blackboard.BlackboardSubscription;
import edu.cmu.inmind.multiuser.controller.common.CommonUtils;
import edu.cmu.inmind.multiuser.controller.common.Constants;
import edu.cmu.inmind.multiuser.controller.communication.ResponseListener;
import edu.cmu.inmind.multiuser.controller.communication.SessionMessage;
import edu.cmu.inmind.multiuser.controller.log.Log4J;
import edu.cmu.inmind.multiuser.controller.plugin.PluggableComponent;
import edu.cmu.inmind.multiuser.controller.plugin.StateType;
import edu.cmu.inmind.multiuser.log.LogC;

/**
 * Created for demo : sakoju 10/4/2018
 */
@StateType(state = Constants.STATELESS)
@BlackboardSubscription(messages= DemoConstants.MSG_SEND_TO_S2V)
public class S2V_CCC_Component extends PluggableComponent {
    //private final static ZMQ.Context context = ZMQ.context(1);
    //private ZMQ.Socket clientSent2Vec;
    private ClientCommController clientCommController;
    private String sessionID = "s2v-partial-ccc";
    private String serverIPAddress="tcp://";
    private static String DEFAULT_IP_ADDRESS = "127.0.0.1:5555";
    private ResponseListener responseListener;
    private BlackboardListener blackboardListener;

    public S2V_CCC_Component() {
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
            clientCommController.send(getSessionId(), CommonUtils.toJson(message));
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
            getBlackBoard( getSessionId()).post(blackboardListener,
                    DemoConstants.MSG_RECEIVE_S2V, sessionMessage);
        }
    }

    @Override
    public void onEvent(Blackboard blackboard, BlackboardEvent blackboardEvent) throws Throwable {
        //send to s2V once you receive a user utterance: this is
        // only example of when we send/forward to S2V which is an external component
        if(blackboardEvent.getId()!= DemoConstants.STEP_END)
        {
            sendS2V((String) blackboardEvent.getElement());
            LogC.info(this,
                    "Client Response Listener"+ blackboardEvent.getElement().toString());
        }
    }
}
