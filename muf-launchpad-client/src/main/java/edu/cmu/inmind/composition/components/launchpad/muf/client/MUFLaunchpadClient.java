package edu.cmu.inmind.composition.components.launchpad.muf.client;

import edu.cmu.inmind.multiuser.controller.common.CommonUtils;
import java.io.PrintWriter;
import org.zeromq.ZMQ;

public class MUFLaunchpadClient {

    public static void main(String args[]){

        StringBuffer buffer = new StringBuffer();
        ZMQ.Context context = ZMQ.context(1);
        ZMQ.Socket receiver = context.socket(ZMQ.REP);

        receiver.bind("tcp://*:5556");
        System.out.println("Listening on tcp://*:5556...");

        try {
            while (!Thread.currentThread().isInterrupted()) {
                String message = receiver.recvStr();
                receiver.send("ACK");
                System.out.println("Received: " + message);
                buffer.append(message + "\n");


            }
        }catch (Exception e){
            e.printStackTrace();
        }
        receiver.close ();
        context.term ();
    }
}
