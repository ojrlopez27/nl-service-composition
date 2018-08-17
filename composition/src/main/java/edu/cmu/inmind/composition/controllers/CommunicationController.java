package edu.cmu.inmind.composition.controllers;

import edu.cmu.inmind.multiuser.controller.common.CommonUtils;
import edu.cmu.inmind.multiuser.controller.log.Log4J;
import org.zeromq.ZMQ;

/**
 * Created by oscarr on 8/15/18.
 */
public class CommunicationController {
    private final String TAG = CommunicationController.class.getSimpleName();
    private final static ZMQ.Context context = ZMQ.context(1);
    private ZMQ.Socket clientSent2Vec;

    public CommunicationController(){
        clientSent2Vec = context.socket(ZMQ.REQ);
        clientSent2Vec.connect("tcp://localhost:" + CommonUtils.getProperty("sent2vec.server.port"));
    }

    public void sendS2V(String message){
        clientSent2Vec.send(message);
    }

    public String receiveS2V(){
        try {
            Log4J.debug(TAG, "Waiting for sent2vec server....");
            return clientSent2Vec.recvStr();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public void stopS2V() {
        clientSent2Vec.close();
    }
}
