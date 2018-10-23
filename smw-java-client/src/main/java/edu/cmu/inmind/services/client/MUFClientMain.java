package edu.cmu.inmind.services.client;

import edu.cmu.inmind.multiuser.controller.common.CommonUtils;
import edu.cmu.inmind.multiuser.controller.communication.ResponseListener;
import edu.cmu.inmind.multiuser.controller.communication.SessionMessage;
import edu.cmu.inmind.multiuser.controller.log.Log4J;
import edu.cmu.inmind.services.muf.commons.SessionMessageCreator;
import edu.cmu.inmind.services.muf.data.OSGiService;
import edu.cmu.inmind.services.muf.inputs.LaunchpadInput;
import edu.cmu.inmind.services.muf.inputs.ServiceRegistryInput;
import java.util.Set;

import static edu.cmu.inmind.multiuser.controller.common.Constants.SESSION_INITIATED;
import static edu.cmu.inmind.multiuser.controller.common.Constants.SESSION_RECONNECTED;
import static edu.cmu.inmind.services.muf.commons.Constants.MSG_LP_LIST_SERVICES;
import static edu.cmu.inmind.services.muf.commons.Constants.MSG_LP_START_SERVICE;
import static edu.cmu.inmind.services.muf.commons.Constants.MSG_OSGI_SERVICE_DEPLOYED;
import static edu.cmu.inmind.services.muf.commons.Constants.MSG_SERVER_RESPONSE;
import static edu.cmu.inmind.services.muf.commons.Constants.MSG_SR_REGISTER_SERVICE;

public class MUFClientMain {

    private static final String TAG = MUFClientMain.class.getSimpleName();
    private static MUFClientMain mufClientMain;

    private MUFClient mufClient;
    private ResponseListener responseListener;

    public MUFClientMain() {
        this.responseListener = new MUFClientResponseListener();
        this.mufClient = new MUFClient(this.responseListener);
    }

    public static void main(String[] args) {
        mufClientMain = new MUFClientMain();
    }

    class MUFClientResponseListener implements ResponseListener {
        private int step = 1;

        @Override
        public void process(String message) {
            Log4J.info(TAG, "Response from Server: " + message);

            // obtain the session message from the server
            SessionMessage sessionMessage = CommonUtils.fromJson(message, SessionMessage.class);
            String sessionRequestType = sessionMessage.getRequestType();

            switch (sessionRequestType) {
                case SESSION_INITIATED:
                case SESSION_RECONNECTED: {
                    Log4J.info(TAG, "Connected to server: " + sessionMessage.getPayload());

                    // wait for the launchpad starter to start completely
                    // that is, when MUF's ServiceManager has initialized all
                    // stateless and statefull components, if there is a different
                    // SESSION_<message> to denote that, we could use that as a
                    // switch-conditional to start the deployment of services
                    // until then, we wait for a few seconds and then deploy
                    // CommonUtils.sleep(12000);        // this doesn't help!

                    deployServices();
                    break;
                }
                case MSG_SERVER_RESPONSE: {
                    processServerResponses(sessionMessage, message);
                    break;
                }
            }
        }

        private void deployServices() {
            Set<String> osgiServiceIDs = OSGiServices.getServiceIDs();
            for (String osgiServiceId : osgiServiceIDs) {
                Log4J.info(TAG, "Deploying OSGi Service: " + osgiServiceId);
                OSGiService osGiService = OSGiServices.getService(osgiServiceId);

                LaunchpadInput lpInput =
                        new LaunchpadInput.StartServiceBuilder(MSG_LP_START_SERVICE)
                                .setOSGiService(osGiService)
                                .build();

                SessionMessage lpMessage =
                        new SessionMessageCreator(mufClient.getSessionId())
                                .createLaunchpadMessage(MSG_LP_START_SERVICE, lpInput);

                mufClient.send(lpMessage);

                CommonUtils.sleep(5000);
            }
        }

        private void processServerResponses(SessionMessage sessionMessage, String message) {
            switch (sessionMessage.getMessageId()) {
                case MSG_OSGI_SERVICE_DEPLOYED: {
                    Log4J.info(TAG, "OSGi Service Deployed: " + message);

                    // wait for 2 seconds because launchpad needs to deploy the services before registering it.
                    // CommonUtils.sleep(2000);

                    break;
                }
                default: {
                    Log4J.info(TAG, "No message from server.");
                    break;
                }
            }
        }

        private void listServices() {
            Log4J.info(TAG, "In listServices()");

            LaunchpadInput lpInput =
                    new LaunchpadInput.VanillaBuilder(MSG_LP_LIST_SERVICES).build();

            SessionMessage lpMessage =
                    new SessionMessageCreator(mufClient.getSessionId())
                            .createLaunchpadMessage(MSG_LP_LIST_SERVICES, lpInput);

            mufClient.send(lpMessage);
        }

        private void registerService(OSGiService osGiService) {
            Log4J.info(TAG, "In registerService() for " + osGiService);

            ServiceRegistryInput srInput =
                    new ServiceRegistryInput.RegisterServiceBuilder(MSG_SR_REGISTER_SERVICE)
                            .setOSGiService(osGiService).build();

            SessionMessage srMessage =
                    new SessionMessageCreator(mufClient.getSessionId())
                            .createServiceRegistryMessage(MSG_SR_REGISTER_SERVICE, srInput);

            mufClient.send(srMessage);
        }
    }
}