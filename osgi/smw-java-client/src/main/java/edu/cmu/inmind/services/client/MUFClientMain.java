package edu.cmu.inmind.services.client;

import edu.cmu.inmind.multiuser.controller.common.CommonUtils;
import edu.cmu.inmind.multiuser.controller.communication.ResponseListener;
import edu.cmu.inmind.multiuser.controller.communication.SessionMessage;
import edu.cmu.inmind.multiuser.controller.log.Log4J;
import edu.cmu.inmind.services.muf.data.LaunchpadMessage;
import edu.cmu.inmind.services.muf.data.OSGiService;

import static edu.cmu.inmind.multiuser.controller.common.Constants.SESSION_INITIATED;
import static edu.cmu.inmind.multiuser.controller.common.Constants.SESSION_RECONNECTED;
import static edu.cmu.inmind.services.muf.commons.Constants.MSG_LAUNCHPAD;
import static edu.cmu.inmind.services.muf.commons.Constants.MSG_LP_LIST_SERVICES;
import static edu.cmu.inmind.services.muf.commons.Constants.MSG_LP_START_SERVICE;
import static edu.cmu.inmind.services.muf.commons.Constants.MSG_OSGI_SERVICE_DEPLOYED;

public class MUFClientMain {

    private static final String TAG = MUFClientMain.class.getSimpleName();
    private static MUFClient mufClient;

    public static void main(String[] args) throws Throwable {

        mufClient = new MUFClient(new ResponseListener() {
            @Override
            public void process(String message) {
                SessionMessage sessionMessage = CommonUtils.fromJson(message, SessionMessage.class);
                switch (sessionMessage.getRequestType()) {
                    case SESSION_INITIATED:
                    case SESSION_RECONNECTED:
                        Log4J.info(TAG, "Connected to server: " + sessionMessage.getPayload());
                        deployServices();
                        listServices();
                        break;
                    case MSG_LAUNCHPAD: {
                        processLaunchpadMessages(sessionMessage, message);
                        break;
                    }
                }
            }
        });
    }

    private static void processLaunchpadMessages(SessionMessage sessionMessage, String message) {
        switch (sessionMessage.getMessageId()) {
            case MSG_OSGI_SERVICE_DEPLOYED:
                Log4J.info(TAG, "OSGi Service Deployed: " + message);
                break;
            default:
                Log4J.info(TAG, "No message from Launchpad.");
                break;
        }
    }

    public static void listServices() {
        LaunchpadMessage launchpadMessage = new LaunchpadMessage();
        launchpadMessage.setSessionId(mufClient.getSessionId());
        launchpadMessage.setMessageId(MSG_LP_LIST_SERVICES);
        mufClient.send(launchpadMessage);
    }

    public static void deployServices() {
        for (String osgiServiceId : OSGiServices.getServiceIDs()) {
            Log4J.info(TAG, "Deploying OSGi Service: " + osgiServiceId);
            OSGiService osGiService = OSGiServices.getService(osgiServiceId);

            LaunchpadMessage launchpadMessage = new LaunchpadMessage();
            launchpadMessage.setSessionId(mufClient.getSessionId());
            launchpadMessage.setMessageId(MSG_LP_START_SERVICE);
            launchpadMessage.setPayload(osGiService);
            mufClient.send(launchpadMessage);
        }
    }
}
