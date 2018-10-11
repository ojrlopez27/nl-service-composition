package edu.cmu.inmind.demo.components;

import edu.cmu.inmind.demo.common.DemoConstants;
import edu.cmu.inmind.multiuser.controller.blackboard.Blackboard;
import edu.cmu.inmind.multiuser.controller.blackboard.BlackboardEvent;
import edu.cmu.inmind.multiuser.controller.blackboard.BlackboardSubscription;
import edu.cmu.inmind.multiuser.controller.common.CommonUtils;
import edu.cmu.inmind.multiuser.controller.common.Constants;
import edu.cmu.inmind.multiuser.controller.communication.SessionMessage;
import edu.cmu.inmind.multiuser.controller.plugin.PluggableComponent;
import edu.cmu.inmind.multiuser.controller.plugin.StateType;
import org.zeromq.ZMQ;

/**
 * Created for demo : sakoju 10/4/2018
 */
@StateType(state = Constants.STATELESS)
@BlackboardSubscription(messages= {DemoConstants.MSG_SEND_TO_S2V,
        DemoConstants.MSG_PROCESS_USER_ACTION})
public class S2VComponent extends PluggableComponent {
    private final String TAG = S2VComponent.class.getSimpleName();
    private final static ZMQ.Context context = ZMQ.context(1);
    private ZMQ.Socket clientSent2Vec;
    public SessionMessage sessionMessage;

    public S2VComponent() {
        clientSent2Vec = context.socket(ZMQ.REQ);
        clientSent2Vec.connect("tcp://localhost:" + CommonUtils.getProperty("sent2vec.server.port"));
    }

    public void sendS2V(String message)
    {
        clientSent2Vec.send(message);
    }

    public String receiveS2V()
    {
        CommonUtils.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String message = clientSent2Vec.recvStr();
                    sessionMessage = CommonUtils.fromJson(message, SessionMessage.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        if(sessionMessage!=null &&
                !sessionMessage.getPayload().isEmpty())
            return sessionMessage.getPayload();
        else
            return DemoConstants.EMPTY_S2V;
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
        if(blackboardEvent.getId().equals(DemoConstants.STEP_END) &&
                !blackboardEvent.getId().equals(DemoConstants.MSG_GROUP_CHAT_READY))
        {
            SessionMessage sessionMessage = CommonUtils.fromJson((String) blackboardEvent.getElement(),
                    SessionMessage.class);
            sendS2V(sessionMessage.getPayload());
        }
    }
}
