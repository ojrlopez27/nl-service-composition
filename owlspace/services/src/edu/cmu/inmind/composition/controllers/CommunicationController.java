package edu.cmu.inmind.composition.controllers;

import edu.cmu.inmind.multiuser.controller.common.CommonUtils;
import edu.cmu.inmind.multiuser.controller.log.Log4J;
import org.zeromq.ZMQ;

/**
 * Created by oscarr on 8/15/18.
 */
public class CommunicationController {
    private static final String TAG = CommunicationController.class.getSimpleName();
    private static ZMQ.Context context;
    private static ZMQ.Socket clientSent2Vec;
    private static ZMQ.Socket clientPerformance;

    static{
        context = ZMQ.context(1);
        clientSent2Vec = context.socket(ZMQ.REQ);
        clientSent2Vec.connect("tcp://localhost:" + CommonUtils.getProperty("sent2vec.server.port"));
        if( Boolean.parseBoolean(CommonUtils.getProperty("performance.test.enable") ) ) {
            clientPerformance = context.socket(ZMQ.REQ);
            clientPerformance.connect("tcp://localhost:" + CommonUtils.getProperty("performance.server.port"));
        }
    }

    public static void sendS2V(String message){
        clientSent2Vec.send(message);
    }

    public static void sendPer(String message){
        clientPerformance.send(message);
    }

    public static String receiveS2V(){
        try {
            Log4J.debug(TAG, "Waiting for sent2vec server....");
            return clientSent2Vec.recvStr();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String receivePer(){
        try {
            System.out.println("Waiting for Performance server....");
            return clientPerformance.recvStr();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void stopS2V() {
        clientSent2Vec.close();
    }

    public static void stopPer() {
        clientPerformance.close();
    }
}
