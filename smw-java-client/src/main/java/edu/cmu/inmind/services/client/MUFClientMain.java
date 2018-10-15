package edu.cmu.inmind.services.client;

import edu.cmu.inmind.multiuser.controller.common.CommonUtils;
import edu.cmu.inmind.multiuser.controller.communication.ResponseListener;
import edu.cmu.inmind.multiuser.controller.communication.SessionMessage;
import edu.cmu.inmind.multiuser.controller.log.Log4J;
import edu.cmu.inmind.services.muf.data.LaunchpadMessage;
import edu.cmu.inmind.services.muf.data.OSGiService;
import edu.cmu.inmind.services.muf.data.ServiceRegistryMessage;
import edu.cmu.inmind.services.muf.inputs.LaunchpadInput;
import edu.cmu.inmind.services.muf.inputs.ServiceRegistryInput;

import static edu.cmu.inmind.multiuser.controller.common.Constants.SESSION_INITIATED;
import static edu.cmu.inmind.multiuser.controller.common.Constants.SESSION_RECONNECTED;
import static edu.cmu.inmind.services.muf.commons.Constants.MSG_LAUNCHPAD;
import static edu.cmu.inmind.services.muf.commons.Constants.MSG_LP_LIST_SERVICES;
import static edu.cmu.inmind.services.muf.commons.Constants.MSG_LP_START_SERVICE;
import static edu.cmu.inmind.services.muf.commons.Constants.MSG_OSGI_SERVICE_DEPLOYED;
import static edu.cmu.inmind.services.muf.commons.Constants.MSG_SR_INITIALIZE;
import static edu.cmu.inmind.services.muf.commons.Constants.MSG_SR_REGISTER_SERVICE;

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
                        //listServices();
                        //registerServices();
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

                // wait for 2 seconds because launchpad needs to deploy the services before registering it.
                CommonUtils.sleep(2000);

                OSGiService osGiService = CommonUtils.fromJson(sessionMessage.getPayload(), OSGiService.class);
                registerService(osGiService);


                break;
            default:
                Log4J.info(TAG, "No message from Launchpad.");
                break;
        }
    }

    public static void listServices() {

        LaunchpadInput launchpadInput =
                new LaunchpadInput.VanillaBuilder(MSG_LP_LIST_SERVICES).build();

        LaunchpadMessage launchpadMessage = new LaunchpadMessage();
        launchpadMessage.setSessionId(mufClient.getSessionId());
        launchpadMessage.setMessageId(MSG_LP_LIST_SERVICES);
        launchpadMessage.setPayload(launchpadInput);
        mufClient.send(launchpadMessage);
    }

    public static void deployServices() {
        for (String osgiServiceId : OSGiServices.getServiceIDs()) {
            Log4J.info(TAG, "Deploying OSGi Service: " + osgiServiceId);
            OSGiService osGiService = OSGiServices.getService(osgiServiceId);

            LaunchpadInput launchpadInput =
                    new LaunchpadInput.StartServiceBuilder(MSG_LP_START_SERVICE)
                    .setOSGiService(osGiService)
                    .build();

            LaunchpadMessage launchpadMessage = new LaunchpadMessage();
            launchpadMessage.setSessionId(mufClient.getSessionId());
            launchpadMessage.setMessageId(MSG_LP_START_SERVICE);
            launchpadMessage.setPayload(launchpadInput);
            mufClient.send(launchpadMessage);
        }
    }

    public static void registerService(OSGiService osGiService) {
        System.out.println("In registerService() for " + osGiService);
        ServiceRegistryInput serviceRegistryInput =
                new ServiceRegistryInput.RegisterServiceBuilder(MSG_SR_REGISTER_SERVICE)
                        .setOSGiService(osGiService).build();

        ServiceRegistryMessage serviceRegistryMessage = new ServiceRegistryMessage();
        serviceRegistryMessage.setSessionId(mufClient.getSessionId());
        serviceRegistryMessage.setMessageId(MSG_SR_REGISTER_SERVICE);
        serviceRegistryMessage.setPayload(serviceRegistryInput);
        mufClient.send(serviceRegistryMessage);
    }

    public static void registerServices() {
        ServiceRegistryMessage serviceRegistryMessage = new ServiceRegistryMessage();
        serviceRegistryMessage.setSessionId(mufClient.getSessionId());
        serviceRegistryMessage.setMessageId(MSG_SR_INITIALIZE);
        mufClient.send(serviceRegistryMessage);
    }
}
