package edu.cmu.inmind.composition.orchestrators;

import edu.cmu.inmind.composition.common.Constants;
import edu.cmu.inmind.composition.common.Schedule;
import edu.cmu.inmind.composition.common.Utils;
import edu.cmu.inmind.multiuser.controller.common.CommonUtils;
import edu.cmu.inmind.multiuser.controller.communication.SessionMessage;
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
        String msg = Utils.removeEOS(message.replace("u\'", "\'"));
        String payload = Utils.extractCleanPayload(msg);
        msg = msg.replace(payload, "\'\'");
        SessionMessage sessionMessage = CommonUtils.fromJson( msg, SessionMessage.class );
        switch( sessionMessage.getRequestType() ) {
            case Constants.MSG_CHECK_USER_ID:
                String validate = Schedule.validate(sessionMessage.getPayload());
                if(validate.equals(Schedule.USER_ID_NOT_EXISTS))
                    validate = "Wrong MKT id, please try again!";
                else if(validate.equals(Schedule.TOO_EARLY))
                    validate = "You have connected too early, please come back at your scheduled time!";
                else if(validate.equals(Schedule.TOO_LATE))
                    validate = "Sorry, you have connected too late, please request another time slot through the doodle!";
                else
                    validate = "Thanks! let's start.";
                sendResponse( validate );
                break;
        }
    }
}
