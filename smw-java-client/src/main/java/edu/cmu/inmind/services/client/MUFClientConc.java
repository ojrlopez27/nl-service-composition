package edu.cmu.inmind.services.client;

// NOTE: older implementation; not being used
public class MUFClientConc { /* implements CommonUtils.NamedRunnable {

    private static final String TAG = MUFClientMain.class.getSimpleName();
    //private AtomicBoolean stop = new AtomicBoolean(true);
    private ConcurrentLinkedQueue<LaunchpadMessage> serviceQueue = new ConcurrentLinkedQueue<>();

    private AtomicInteger numServices = new AtomicInteger(0);
    private AtomicInteger numServicesDeployReqt = new AtomicInteger(0);
    private AtomicInteger numServicesDeployResp = new AtomicInteger(0);
    private AtomicInteger iterTime = new AtomicInteger(0);
    private MUFClient mufClient = new MUFClient(new MUFClientResponseListener());

    public static void main(String[] args) throws Throwable {

        MUFClientConc mufClientConc = new MUFClientConc();
        mufClientConc.run();

        //mufClient = new MUFClient(new MUFClientResponseListener());
    }

    class DeployServiceConsumer implements Runnable {
        ConcurrentLinkedQueue<LaunchpadMessage> serviceQueue;

        DeployServiceConsumer(ConcurrentLinkedQueue<LaunchpadMessage> serviceQueue) {
            this.serviceQueue = serviceQueue;
        }

        public void run() {

            // delay to make sure OSGiServiceProducer starts first
            CommonUtils.sleep(100);

            System.out.println("OSGiServiceConsumer Started");
            LaunchpadMessage launchpadMessage;

            System.out.println("Cons Num of Services: " + numServices.get());
            // for (int i = 0; i < numServices.get(); i++) {
            while (numServicesDeployReqt.get() < numServices.get()) {

                System.out.println(
                        "Reqt: " + numServicesDeployReqt.get() +
                                " Resp: " + numServicesDeployResp.get() +
                                " Queue size: " + serviceQueue.size() +
                                " Iter Time: " + iterTime.incrementAndGet()
                );

                // check if there are more services to deploy
                while ((numServicesDeployReqt.get() - 1) <  numServicesDeployResp.get()
                        && (launchpadMessage = serviceQueue.poll()) != null) {

                    // if we have received a response for the previous deploy service request
                    // only then send a new deploy service request
                    // if numServicesDeployResp.get() == (numServicesDeployReqt.get() - 1)) {

                    String serviceName = CommonUtils.fromJson(
                            launchpadMessage.getPayload(), LaunchpadInput.class)
                            .getOSGiService()
                            .getServiceName();

                    // each time we send a request to MUF to deploy a service
                    // we increment the request counter
                    Log4J.info(TAG, "Consuming OSGi Service from Queue: " + serviceName);
                    mufClient.send(launchpadMessage);
                    numServicesDeployReqt.incrementAndGet();
                    System.out.println("LaunchpadMessage sent to MUF Server: " + launchpadMessage);
                    //}
                    //CommonUtils.sleep(500);
                }
                CommonUtils.sleep(500);
            }
        }
    }

    class DeployServiceProducer implements Runnable {
        ConcurrentLinkedQueue<LaunchpadMessage> serviceQueue;

        DeployServiceProducer(ConcurrentLinkedQueue<LaunchpadMessage> serviceQueue){
            this.serviceQueue = serviceQueue;
        }

        @Override
        public void run() {
            System.out.println("OSGiServiceProducer Started");
            try {
                Set<String> osgiServiceIDs = OSGiServices.getServiceIDs();
                System.out.println(osgiServiceIDs);
                numServices.set(osgiServiceIDs.size());
                System.out.println("Prod: Num of Services: " + numServices.get());
                //prodStarted.set(true);

                for (String osgiServiceId : osgiServiceIDs) {
                    Log4J.info(TAG, "Added OSGi Service to Queue: " + osgiServiceId);
                    OSGiService osGiService = OSGiServices.getService(osgiServiceId);

                    LaunchpadInput launchpadInput =
                            new LaunchpadInput.StartServiceBuilder(MSG_LP_START_SERVICE)
                                    .setOSGiService(osGiService)
                                    .build();

                    LaunchpadMessage launchpadMessage = new LaunchpadMessage();
                    launchpadMessage.setSessionId(mufClient.getSessionId());
                    launchpadMessage.setMessageId(MSG_LP_START_SERVICE);
                    launchpadMessage.setPayload(launchpadInput);
                    serviceQueue.add(launchpadMessage);
                    //mufClient.send(launchpadMessage);
                    //CommonUtils.sleep(200);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }



    @Override
    public String getName() {

        // let the client's session id be the name of the thread
        return mufClient.getSessionId();
    }

    @Override
    public void run() {
        try {
            // let's wait until it connects
            while(stop.get()) {
                CommonUtils.sleep(100);
            }
            while(!stop.get()) {

            }

            // deploy services
//            ConcurrentLinkedQueue<LaunchpadMessage> queue = new ConcurrentLinkedQueue<>();
//            Thread deployServicesProducer = new Thread(new DeployServiceProducer(queue));
//            Thread deployServicesConsumer = new Thread(new DeployServiceConsumer(queue));
//            deployServicesProducer.start();
//            deployServicesConsumer.start();

            CommonUtils.sleep(1000);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    class MUFClientResponseListener implements ResponseListener {

        @Override
        public void process(String message) {
            System.out.println("Message: " + message);

            SessionMessage sessionMessage = CommonUtils.fromJson(message, SessionMessage.class);
            String sessionRequestType = sessionMessage.getRequestType();
            switch (sessionRequestType) {
                case SESSION_INITIATED:
                case SESSION_RECONNECTED: {

                    // wait for 1 second
                    CommonUtils.sleep(1000);

                    Log4J.info(TAG, "Connected to server: " + sessionMessage.getPayload());
//
                    // deploy services
                    ConcurrentLinkedQueue<LaunchpadMessage> queue = new ConcurrentLinkedQueue<>();
                    Thread deployServicesProducer = new Thread(new DeployServiceProducer(queue));
                    Thread deployServicesConsumer = new Thread(new DeployServiceConsumer(queue));
                    deployServicesProducer.start();
                    deployServicesConsumer.start();

                    //listServices();
                    //registerServices();

                    // stop.getAndSet( false );
                    break;
                }
                default: {
                    while (!sessionRequestType.equals(SESSION_CLOSED)) {
                        if (sessionRequestType.equals(MSG_LAUNCHPAD)) {
                            if (sessionMessage.getMessageId().equals(MSG_OSGI_SERVICE_DEPLOYED)) {
                                // each time we get a response from the MUF
                                // stating that the service has been deployed
                                // we then increment the response counter
                                numServicesDeployResp.incrementAndGet();

                                Log4J.info(TAG, "OSGi Service Deployed: " + message);

                                // wait for 2 seconds because launchpad needs to deploy the services before registering it.
                                // CommonUtils.sleep(2000);
                            }
                        }
                        CommonUtils.sleep(500);
                    }
                }
            }
            CommonUtils.sleep(200);
        }
    }
    */
}
