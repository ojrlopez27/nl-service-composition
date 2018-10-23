package edu.cmu.inmind.demo.components;

import edu.cmu.inmind.demo.common.DemoConstants;
import edu.cmu.inmind.demo.common.Schedule;
import edu.cmu.inmind.multiuser.controller.blackboard.Blackboard;
import edu.cmu.inmind.multiuser.controller.blackboard.BlackboardEvent;
import edu.cmu.inmind.multiuser.controller.blackboard.BlackboardSubscription;
import edu.cmu.inmind.multiuser.controller.common.Constants;
import edu.cmu.inmind.multiuser.controller.communication.SessionMessage;
import edu.cmu.inmind.multiuser.controller.plugin.PluggableComponent;
import edu.cmu.inmind.multiuser.controller.plugin.StateType;

/**
 * Created for demo : sakoju 10/4/2018
 */
@StateType(state = Constants.STATELESS)
@BlackboardSubscription(messages= {DemoConstants.MSG_CHECK_USER_ID})
public class UserInteractionComponent extends PluggableComponent {


    /***
     * Valid user login
     * @param username
     * @param blackboard
     */
    private void checkUserLogin(String username, Blackboard blackboard){
        String validate = Schedule.validate(username);
        String messageRequest = "";
        if(validate.equals(Schedule.USER_ID_NOT_EXISTS)) {
            validate = "Wrong id, please try again!";
            messageRequest = DemoConstants.MSG_USER_VALIDATION_ERROR;
        }
        else if(validate.equals(Schedule.TOO_EARLY)) {
            validate = "You have connected too early, please come back at your scheduled time!";
            messageRequest = DemoConstants.MSG_USER_VALIDATION_ERROR;
        }
        else if(validate.equals(Schedule.TOO_LATE)) {
            validate = "Sorry, you have connected too late, please request another time slot through the doodle!";
            messageRequest = DemoConstants.MSG_USER_VALIDATION_ERROR;
        }
        else
        {
            validate = "Session "+username + " has been successfully created. Initiate chat to start the user study.";
            messageRequest = DemoConstants.MSG_USER_VALIDATION_SUCCCES;
        }
        blackboard.post(this, messageRequest,
                new SessionMessage(messageRequest, validate));
    }

    @Override
    public Blackboard getBlackBoard(String sessionId) {
        return super.getBlackBoard(sessionId);
    }

    @Override
    public void execute() {
        super.execute();
    }

    @Override
    protected void startUp() {
        super.startUp();
    }

    @Override
    public void shutDown() {
        super.shutDown();
    }

    @Override
    public void onEvent(Blackboard blackboard, BlackboardEvent blackboardEvent) throws Throwable {
        switch(blackboardEvent.getId())
        {
            //first validate user from schedule.json
            case DemoConstants.MSG_CHECK_USER_ID:
                checkUserLogin(blackboardEvent.getSessionId(),
                        blackboard);
                break;
            default:
                break;
        }
    }


}
