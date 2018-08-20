import edu.cmu.inmind.multiuser.controller.common.CommonUtils;
import org.zeromq.ZMQ;

import java.io.File;
import java.io.PrintWriter;

/**
 * Created by oscarr on 8/16/18.
 */
public class PerformanceServer {
    public static void main(String args[]){
        int totalNumProcess = Integer.parseInt(CommonUtils.getProperty("experiment.total.processes"));
        long totalAmountOfTime = 0;
        int numProcess = 0;
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
                totalAmountOfTime += Long.parseLong(message);
                numProcess++;
                if(numProcess == totalNumProcess) {
                    PrintWriter pw = new PrintWriter(new File("results.txt"));
                    pw.write(buffer.toString() + "\n");
                    String averageTime = "Average time: " + (totalAmountOfTime/totalNumProcess);
                    System.out.println(averageTime);
                    pw.write(averageTime);
                    pw.flush();
                    pw.close();
                    break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        receiver.close ();
        context.term ();
    }
}
