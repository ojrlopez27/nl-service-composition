package edu.cmu.inmind.demo.components;

import edu.cmu.inmind.demo.common.DemoConstants;
import edu.cmu.inmind.multiuser.controller.blackboard.Blackboard;
import edu.cmu.inmind.multiuser.controller.blackboard.BlackboardEvent;
import edu.cmu.inmind.multiuser.controller.blackboard.BlackboardSubscription;
import edu.cmu.inmind.multiuser.controller.common.CommonUtils;
import edu.cmu.inmind.multiuser.controller.common.Constants;
import edu.cmu.inmind.multiuser.controller.communication.SessionMessage;
import edu.cmu.inmind.multiuser.controller.log.Log4J;
import edu.cmu.inmind.multiuser.controller.plugin.PluggableComponent;
import edu.cmu.inmind.multiuser.controller.plugin.StateType;
import edu.cmu.inmind.multiuser.log.LogC;
import org.zeromq.ZMQ;

import javax.annotation.Nonnull;

/**
 * Created for demo : sakoju 10/4/2018
 */
@StateType(state = Constants.STATELESS)
@BlackboardSubscription(messages= {DemoConstants.MSG_SEND_TO_S2V})
public class S2VComponent extends PluggableComponent {
    private final String TAG = S2VComponent.class.getSimpleName();
    private final static ZMQ.Context context = ZMQ.context(1);
    private ZMQ.Socket clientSent2Vec;
    public String message ="";
    public Blackboard blackboard;

    public S2VComponent() {
        clientSent2Vec = context.socket(ZMQ.REQ);
        clientSent2Vec.connect("tcp://localhost:" + CommonUtils.getProperty("sent2vec.server.port"));
        LogC.info(this, "connected to S2V: " );
    }

    public void sendS2V(String message)
    {
        clientSent2Vec.send(message);
        Log4J.info(S2VComponent.this, "send to S2V: "+message );
    }

    @Nonnull
    public String receiveS2V()
    {
        CommonUtils.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    message = clientSent2Vec.recvStr();
                    Log4J.info(S2VComponent.this, "receive from S2V: "+message );
                    SessionMessage sessionMessageResult = new SessionMessage();
                    sessionMessageResult.setPayload(message);
                    sessionMessageResult.setRequestType(DemoConstants.MSG_RECEIVE_S2V);
                    blackboard.post(S2VComponent.this, DemoConstants.MSG_RECEIVE_S2V,
                            sessionMessageResult);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return message;
    }

    @Override
    public void execute() {
        super.execute();
    }

    @Override
    protected void startUp() {
        super.startUp();
    }

    @Override
    public void shutDown() {
        clientSent2Vec.close();
        super.shutDown();
    }

    @Override
    public void onEvent(Blackboard blackboard, BlackboardEvent blackboardEvent) throws Throwable {
        //send to s2V once you receive a user utterance: this is
        // only example of when we send/forward to S2V which is an external component
        switch(blackboardEvent.getId()) {
            case DemoConstants.MSG_SEND_TO_S2V:
                    Log4J.info(this, blackboardEvent.getId() + " " +
                            blackboardEvent.getElement().toString());
                    SessionMessage message = (SessionMessage) blackboardEvent.getElement();
                    sendS2V(message.getPayload());
                    this.blackboard = blackboard;
                    String response = receiveS2V();
                //TODO: now that entities are identified, process remaining : service mapping, service execution, checkRules
                break;
             default:
                    break;
        }
    }
}
