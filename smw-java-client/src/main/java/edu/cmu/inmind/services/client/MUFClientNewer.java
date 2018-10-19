package edu.cmu.inmind.services.client;

import edu.cmu.inmind.multiuser.controller.common.CommonUtils;
import edu.cmu.inmind.multiuser.controller.communication.ResponseListener;
import edu.cmu.inmind.multiuser.controller.communication.SessionMessage;
import edu.cmu.inmind.multiuser.controller.log.Log4J;
import edu.cmu.inmind.services.muf.data.OSGiService;
import edu.cmu.inmind.services.muf.inputs.LaunchpadInput;
import java.util.Random;
import java.util.Set;

import static edu.cmu.inmind.multiuser.controller.common.Constants.SESSION_INITIATED;
import static edu.cmu.inmind.multiuser.controller.common.Constants.SESSION_RECONNECTED;
import static edu.cmu.inmind.services.muf.commons.Constants.MSG_LAUNCHPAD;
import static edu.cmu.inmind.services.muf.commons.Constants.MSG_LP_START_SERVICE;
import static edu.cmu.inmind.services.muf.commons.Constants.MSG_OSGI_SERVICE_DEPLOYED;

/**
 *  only for testing MUF 3.0.55
 */
class MUFClientNewer {
    private static final String TAG = MUFClientNewer.class.getSimpleName();

    MUFClient mufClient;
    private ResponseListener responseListener;
    private static MUFClientNewer mufClientNewer;

    public MUFClientNewer() {
        this.responseListener = new MyResponseListener();
        mufClient = new MUFClient(this.responseListener);
    }

    public void send(Object message){
        try {
            SessionMessage sessionMessage = new SessionMessage();
            sessionMessage.setPayload(CommonUtils.toJson(message));
            sessionMessage.setSessionId(mufClient.getSessionId());
            sessionMessage.setRequestType(String.class.getSimpleName());
            sessionMessage.setRequestType(String.class.getSimpleName());
            mufClient.send(sessionMessage, Boolean.TRUE);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void disconnect() {
        mufClient.disconnect();
    }

    class MyResponseListener implements ResponseListener {
        private int step = 1;

        @Override
        public void process(String message) {

            System.out.println("Message: " + message);

            SessionMessage sessionMessage = CommonUtils.fromJson(message, SessionMessage.class);
            String sessionRequestType = sessionMessage.getRequestType();
            System.out.println("SessionRequest: " + sessionRequestType);
            switch (sessionRequestType) {
                case SESSION_INITIATED:
                case SESSION_RECONNECTED: {
                    Log4J.info(TAG, "Connected to server: " + sessionMessage.getPayload());
                    //deployServices();


                    // scenario 1: send msgs directly from process()
                    Set<String> osgiServiceIDs = OSGiServices.getServiceIDs();
                    for (String osgiServiceId : osgiServiceIDs) {
                        Log4J.info(TAG, "Sending OSGi Service w/o. thread to MUF: " + osgiServiceId);
                        OSGiService osGiService = OSGiServices.getService(osgiServiceId);

                        LaunchpadInput launchpadInput =
                                new LaunchpadInput.StartServiceBuilder(MSG_LP_START_SERVICE)
                                        .setOSGiService(osGiService)
                                        .build();

                        SessionMessage launchpadMessage = new SessionMessage();
                        launchpadMessage.setRequestType(MSG_LAUNCHPAD);
                        launchpadMessage.setSessionId(mufClientNewer.mufClient.getSessionId());
                        launchpadMessage.setMessageId(MSG_LP_START_SERVICE);
                        launchpadMessage.setPayload(CommonUtils.toJson(launchpadInput));

                        mufClientNewer.mufClient.send(launchpadMessage, Boolean.TRUE);
                    }


                    break;
                }
                case MSG_LAUNCHPAD: {
                    processLaunchpadMessages(sessionMessage, message);
                    break;
                }
            }
            /*
            LogC.info(this, "Response from server: " + message);
            for(int i = 1; i < 5; i++){
                send(String.format("message from process: %s.%s", step, i));
            }
            step++;
            */
        }

        private void processLaunchpadMessages(SessionMessage sessionMessage, String message) {
            switch (sessionMessage.getMessageId()) {
                case MSG_OSGI_SERVICE_DEPLOYED:
                    Log4J.info(TAG, "OSGi Service Deployed: " + message);

                    // wait for 2 seconds because launchpad needs to deploy the services before registering it.
                    // CommonUtils.sleep(2000);

                    break;
                case "String": {
                    Log4J.info(TAG, "String message: " + message);
                    break;
                }
                default:
                    Log4J.info(TAG, "No message from Launchpad.");
                    break;
            }
        }
    }

    // scenario 2: send msgs by spawning a thread; called in process()
    public static void deployServices() {

        // let's deploy services in parallel
        CommonUtils.execute(new Runnable() {

            @Override
            public void run() {

                Set<String> osgiServiceIDs = OSGiServices.getServiceIDs();
                for (String osgiServiceId : osgiServiceIDs) {
                    Log4J.info(TAG, "Sending OSGi Service to MUF: " + osgiServiceId);
                    OSGiService osGiService = OSGiServices.getService(osgiServiceId);

                    LaunchpadInput launchpadInput =
                            new LaunchpadInput.StartServiceBuilder(MSG_LP_START_SERVICE)
                                    .setOSGiService(osGiService)
                                    .build();

                    SessionMessage launchpadMessage = new SessionMessage();
                    launchpadMessage.setRequestType(MSG_LAUNCHPAD);
                    launchpadMessage.setSessionId(mufClientNewer.mufClient.getSessionId());
                    launchpadMessage.setMessageId(MSG_LP_START_SERVICE);
                    launchpadMessage.setPayload(CommonUtils.toJson(launchpadInput));

                    mufClientNewer.mufClient.send(launchpadMessage, Boolean.TRUE);
                }
            }
        });
    }

    public static void main(String args[]){

        mufClientNewer = new MUFClientNewer();

        // scenario 3: send msgs directly in the main thread
        CommonUtils.execute(new Runnable() {

            @Override
            public void run() {

                int userMessage = 1;
                int maxMessages = 20;
                //while( !Thread.currentThread().isInterrupted() ){
                while (userMessage <= maxMessages) {
                    Random random = new Random();
                    mufClientNewer.send("message from user: " + userMessage);
                    userMessage++;
                    CommonUtils.sleep(random.nextInt(20));
                }
            }
        });
    }
}
