package edu.cmu.inmind.composition.controllers;

import org.zeromq.ZMQ;

/**
 * Created by oscarr on 8/15/18.
 */
public class CommunicationController {
    private static ZMQ.Context context;
    private static ZMQ.Socket requester;

    static{
        context = ZMQ.context(1);
        requester = context.socket(ZMQ.REQ);
        requester.connect("tcp://localhost:5555");
    }

    public static void send(String message){
        requester.send(message);
    }

    public static String receive(){
        try {
            System.out.println("Waiting for sent2vec server....");
            return requester.recvStr();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
