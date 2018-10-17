package edu.cmu.inmind.demo.client;

import edu.cmu.inmind.demo.common.DemoConstants;
import edu.cmu.inmind.multiuser.controller.common.CommonUtils;
import edu.cmu.inmind.multiuser.controller.log.Log4J;
import org.zeromq.ZMQ;

import java.util.Scanner;

public class S2VTestClient {

    public void stopS2V() {
        clientSent2Vec.close();
    }

    public static void main(String args[]) throws Throwable
    {
        Scanner scanner = new Scanner(System.in);
        String input = "";
        S2VTestClient s2VTestClient=null;
        while (!input.equals("stop") && !input.equals("disconnect"))
        {
            System.out.println("Enter a command:");
            input = scanner.nextLine();
            if(input.equals("start")) {
                s2VTestClient = new S2VTestClient();
            }
            if (input.equals("stop")) {
                Log4J.info(s2VTestClient, "Do you want to end the session? " +
                        "Please type \"disconnect\"");
            }
            else if(input.equals("login"))
            {
                Log4J.info(s2VTestClient, "login");
                s2VTestClient.sendS2V(input);
                String result= s2VTestClient.receiveS2V();
                Log4J.info(s2VTestClient, result);

            }
            else if(input.contains("ready"))
            {
                Log4J.info(s2VTestClient, "ready");
                s2VTestClient.sendS2V(input);
                String result= s2VTestClient.receiveS2V();
                Log4J.info(s2VTestClient, result);
            }
            else
            {
                Log4J.info(s2VTestClient, input);
                s2VTestClient.sendS2V(input);
                String result= s2VTestClient.receiveS2V();
                Log4J.info(s2VTestClient, result);
            }
        }
        s2VTestClient.stopS2V();
        System.exit(0);
    }

    private final static ZMQ.Context context = ZMQ.context(1);
    private ZMQ.Socket clientSent2Vec;
    String message;

    public S2VTestClient() {
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
             message = clientSent2Vec.recvStr();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(message.isEmpty()) {
            return DemoConstants.EMPTY_S2V;
        }else
        {
            return message;
        }
    }
}
