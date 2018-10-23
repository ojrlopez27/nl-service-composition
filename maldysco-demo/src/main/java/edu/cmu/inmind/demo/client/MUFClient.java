package edu.cmu.inmind.demo.client;


import edu.cmu.inmind.multiuser.communication.ClientCommController;
import edu.cmu.inmind.multiuser.controller.common.CommonUtils;
import edu.cmu.inmind.multiuser.controller.common.Constants;
import edu.cmu.inmind.multiuser.controller.communication.ResponseListener;
import edu.cmu.inmind.multiuser.controller.communication.SessionMessage;
import edu.cmu.inmind.multiuser.controller.log.Log4J;
import edu.cmu.inmind.multiuser.log.LogC;
/***
 * Merging Ankit's changes
 */
public class MUFClient {

    private static final String TAG = MUFClient.class.getSimpleName();

    public static final String MUF_SERVER_PORT          = "muf.server.port";
    private static final String MUF_SERVER_ADDRESS      = "muf.server.address";
    private static final String MUF_OSGI_SESSION_ID     = "muf-osgi-session-id";

    private ClientCommController clientCommController;
    private ResponseListener responseListener;

    private String sessionId = MUF_OSGI_SESSION_ID;
    private String serverAddress =
            CommonUtils.getProperty(MUF_SERVER_ADDRESS) + ":" + CommonUtils.getProperty(MUF_SERVER_PORT);

    public MUFClient() {
        this(null, null, null);
    }

    public MUFClient(String serverAddress, String sessionId, ResponseListener responseListener) {
        if (serverAddress != null) this.serverAddress = serverAddress;
        if (sessionId != null) this.sessionId = sessionId;
        if (responseListener != null) {
            this.responseListener = responseListener;
        } else {
            this.responseListener = new NoResponseListener();
        }
        clientCommController = new ClientCommController.Builder(new LogC())
                .setServerAddress(this.serverAddress)
                .setSessionId(this.sessionId)
                .setRequestType(Constants.REQUEST_CONNECT)
                .setResponseListener(this.responseListener)
                .build();
    }

    public MUFClient(ResponseListener responseListener) {
        this();
        this.clientCommController.setResponseListener(responseListener);
    }

    public void setResponseListener(ResponseListener responseListener) {
        this.clientCommController.setResponseListener(responseListener);
    }

    public String getSessionId() {
        return sessionId;
    }

    public void send(SessionMessage sessionMessage) {
        Log4J.info(TAG, "Request to Server: " + CommonUtils.toJson(sessionMessage));
        clientCommController.send(sessionId, CommonUtils.toJson(sessionMessage));
    }

    /*
    // only for testing MUF 3.0.55
    public void send(SessionMessage sessionMessage, boolean transformString) {
        if (transformString) {
            clientCommController.send(sessionId, CommonUtils.toJson(sessionMessage));
        }
        else send(sessionMessage);
    }
    */

    public void disconnect() {
        clientCommController.disconnect(sessionId);
    }

    public Boolean getIsConnected() {
        return clientCommController.getIsConnected().get();
    }

    class NoResponseListener implements ResponseListener {

        @Override
        public void process(String message) {
            LogC.info(this, "Response from server: " + message);
        }
    }
}
