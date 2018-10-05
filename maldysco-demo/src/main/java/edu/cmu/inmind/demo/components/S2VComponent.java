package edu.cmu.inmind.demo.components;

import edu.cmu.inmind.demo.common.DemoConstants;
import edu.cmu.inmind.multiuser.controller.blackboard.Blackboard;
import edu.cmu.inmind.multiuser.controller.blackboard.BlackboardEvent;
import edu.cmu.inmind.multiuser.controller.common.CommonUtils;
import edu.cmu.inmind.multiuser.controller.plugin.PluggableComponent;
import org.zeromq.ZMQ;
/**
 * Created for demo : sakoju 10/4/2018
 */
public class S2VComponent extends PluggableComponent {
    private final String TAG = S2VComponent.class.getSimpleName();
    private final static ZMQ.Context context = ZMQ.context(1);
    private ZMQ.Socket clientSent2Vec;

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
        try {
            return clientSent2Vec.recvStr();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
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
        if(blackboardEvent.getId()!= DemoConstants.STEP_END)
        {
            sendS2V((String) blackboardEvent.getElement());
        }
    }
}
