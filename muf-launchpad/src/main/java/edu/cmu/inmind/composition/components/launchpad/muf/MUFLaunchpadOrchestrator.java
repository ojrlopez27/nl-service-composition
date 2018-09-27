package edu.cmu.inmind.composition.components.launchpad.muf;

import edu.cmu.inmind.composition.components.launchpad.core.LaunchpadStarterMain;
import edu.cmu.inmind.multiuser.controller.orchestrator.ProcessOrchestratorImpl;
import edu.cmu.inmind.multiuser.controller.session.Session;

public class MUFLaunchpadOrchestrator extends ProcessOrchestratorImpl {
    //private MUFCommunicationController mufCommunicationController;

    @Override
    public void initialize(Session session) throws Throwable {
        super.initialize(session);
        MUFLaunchpadLog.setFileName("MUF_LAUNCHPAD");

        MUFLaunchpadLog.log(this, "In MUFLaunchpadOrchestrator.initialize()");
      //  mufCommunicationController = new MUFCommunicationController();
        LaunchpadStarterMain.init();
    }

    @Override
    public void process(String message) throws Throwable {
        super.process(message);

        MUFLaunchpadLog.log(this, "Orchestrator: Process");
        System.out.println("Message: " + message);

        /*
        mufCommunicationController.sendLaunchpadStarter(message);
        String receipt = mufCommunicationController.receiveLaunchpadStarter();
        System.out.println(receipt.equals("") ? "NO_RECEIPT" : receipt);
        */

    }

}
