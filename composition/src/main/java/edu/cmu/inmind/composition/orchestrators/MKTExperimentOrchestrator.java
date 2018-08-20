package edu.cmu.inmind.composition.orchestrators;

import edu.cmu.inmind.multiuser.controller.orchestrator.ProcessOrchestratorImpl;
import edu.cmu.inmind.multiuser.controller.session.Session;

/**
 * Created by oscarr on 8/19/18.
 */
public class MKTExperimentOrchestrator extends ProcessOrchestratorImpl {
    @Override
    public void initialize(Session session) throws Throwable{
        super.initialize( session );
    }


    @Override
    public void process(String message) throws Throwable {
        super.process(message);
        System.out.println(message);
    }
}
