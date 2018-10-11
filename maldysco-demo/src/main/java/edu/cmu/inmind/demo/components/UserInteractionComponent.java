package edu.cmu.inmind.demo.components;

import edu.cmu.inmind.demo.common.DemoConstants;
import edu.cmu.inmind.demo.common.Schedule;
import edu.cmu.inmind.multiuser.controller.blackboard.Blackboard;
import edu.cmu.inmind.multiuser.controller.blackboard.BlackboardEvent;
import edu.cmu.inmind.multiuser.controller.blackboard.BlackboardSubscription;
import edu.cmu.inmind.multiuser.controller.common.Constants;
import edu.cmu.inmind.multiuser.controller.communication.SessionMessage;
import edu.cmu.inmind.multiuser.controller.log.Log4J;
import edu.cmu.inmind.multiuser.controller.plugin.PluggableComponent;
import edu.cmu.inmind.multiuser.controller.plugin.StateType;
import java.util.Arrays;
import java.util.List;

//TODO: write pseudo rules and pseudo services
//TODO: communication sequence defined : TEST with mock client.
@StateType(state = Constants.STATELESS)
@BlackboardSubscription(messages= {DemoConstants.MSG_CHECK_USER_ID,
        DemoConstants.MSG_GROUP_CHAT_READY, DemoConstants.MSG_PROCESS_USER_ACTION})
public class UserInteractionComponent extends PluggableComponent {
    private List<String> scenarios = Arrays.asList(
            "You want to go on a vacation to Europe next month and need to plan your trip",
            "Your wedding anniversary is the next weekend and you want to plan a romantic night with your spouse",
            "You are planning to have a party at your home this coning weekend");
    private int scenarioIdx = 0;
    private String stage = DemoConstants.REQUEST_ACTION_STAGE;
    private int actionCounter = 0;
    private final int maxActions = 7;
    private final int minLength = 15;

    private void checkUserLogin(String username, Blackboard blackboard){
        String validate = Schedule.validate(username);
        if(validate.equals(Schedule.USER_ID_NOT_EXISTS))
            validate = "Wrong MKT id, please try again!";
        else if(validate.equals(Schedule.TOO_EARLY))
            validate = "You have connected too early, please come back at your scheduled time!";
        else if(validate.equals(Schedule.TOO_LATE))
            validate = "Sorry, you have connected too late, please request another time slot through the doodle!";
        else
        {
            validate = "Session "+username + " has been successfully created. Initiate chat to start the user study.";
            blackboard.post(this, DemoConstants.MSG_USER_VALIDATION_SUCCCES,
                    validate);
        }
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
            //Android Phone app's GroupChannel chat is ready ?
            case DemoConstants.MSG_GROUP_CHAT_READY :
                String response = String.format("Thanks! let's start. Consider this scenario: %s. What is the first thing you " +
                        "would ask your IPA to do?", scenarios.get(scenarioIdx++));
                //IPALog.setFileName(sessionMessage.getPayload());
                Log4J.info(this, (String) response);
                SessionMessage sessionMessage = new SessionMessage();
                sessionMessage.setPayload(response);
                sessionMessage.setMessageId(DemoConstants.MSG_SEND_TO_CLIENT);
                sessionMessage.setRequestType(DemoConstants.MSG_SEND_TO_CLIENT);
                blackboard.post(this, DemoConstants.MSG_SEND_TO_CLIENT,
                        sessionMessage);
                break;
            case DemoConstants.MSG_PROCESS_USER_ACTION:
                    processUserAction((String)blackboardEvent.getElement(), blackboard);
            default:
                break;
        }
    }

    /***
     * process user action
     * @param userAction
     * @param blackboard
     */
    private void processUserAction(String userAction,Blackboard blackboard){
        Log4J.info(this, "orchestrator processUserAction: "+userAction);
        Log4J.info(this, String.format("%s%s\t%s", DemoConstants.USER, DemoConstants.LEVEL0, userAction));
        if( !userAction.equalsIgnoreCase(DemoConstants.DONE) ) {
            Log4J.info(this, "userAction is NOT DONE: processUserAction: "+userAction);
            if( DemoConstants.ASK_FOR_APP_CONFIRMATION_STAGE.equals(stage) ) {
                if(userAction.equalsIgnoreCase("Y") || userAction.equalsIgnoreCase("Yes")
                        || userAction.contains("Yes ") || userAction.equalsIgnoreCase("yes ")
                        || userAction.equalsIgnoreCase("YES ")){
                    //TODO: psedo rules for testing: W.I.P
                    //compositionController.fireRulesGS();
                    String response = "Great, let's continue. What is the next thing you would ask your IPA to do? " +
                            "(type 'DONE' if you are done for this scenario -- but at least 7 actions/steps are required)";
                    SessionMessage clientSessionMessage = new SessionMessage();
                    clientSessionMessage.setMessageId(DemoConstants.MSG_SEND_TO_CLIENT);
                    clientSessionMessage.setRequestType(DemoConstants.MSG_SEND_TO_CLIENT);
                    clientSessionMessage.setPayload(response);
                    blackboard.post(this, DemoConstants.MSG_SEND_TO_CLIENT,
                            clientSessionMessage);                    Log4J.info(this, String.format("%s%s\t%s", DemoConstants.IPA, DemoConstants.LEVEL1, response));
                    stage = DemoConstants.REQUEST_ACTION_STAGE;
                    actionCounter++;
                }else{
                    String response = "Ok, can you re-phrase your command? your IPA will try to do it better this time...";
                    SessionMessage clientSessionMessage = new SessionMessage();
                    clientSessionMessage.setMessageId(DemoConstants.MSG_SEND_TO_CLIENT);
                    clientSessionMessage.setRequestType(DemoConstants.MSG_SEND_TO_CLIENT);
                    clientSessionMessage.setPayload(response);
                    blackboard.post(this, DemoConstants.MSG_SEND_TO_CLIENT,
                            clientSessionMessage);                    Log4J.info(this, String.format("%s%s\t%s", DemoConstants.IPA, DemoConstants.LEVEL2, response));
                    stage = DemoConstants.REQUEST_ACTION_STAGE;
                }
            }
            else if( DemoConstants.REQUEST_ACTION_STAGE.equals(stage) )
            {
                if( userAction.length() < minLength ){
                    String response = String.format("Your sentence is empty or too short (only %s characters). " +
                            "Please re-enter a sentence with at least %s-characters length", userAction.length(), minLength);
                    Log4J.info(this, String.format("%s%s\t%s", DemoConstants.IPA, DemoConstants.LEVEL5, response));
                    SessionMessage sessionMessage = new SessionMessage();
                    sessionMessage.setMessageId(DemoConstants.MSG_SEND_TO_CLIENT);
                    sessionMessage.setRequestType(DemoConstants.MSG_SEND_TO_CLIENT);
                    sessionMessage.setPayload(response);
                    blackboard.post(this, DemoConstants.MSG_SEND_TO_CLIENT,
                            sessionMessage);
                }
                else
                {
                    // let's get the semantic neighbors provided by sent2vec
                    SessionMessage sessionMessage = new SessionMessage();
                    sessionMessage.setRequestType(DemoConstants.MSG_SEND_TO_S2V);
                    sessionMessage.setPayload(userAction);
                    blackboard.post(this, DemoConstants.MSG_SEND_TO_S2V,
                            sessionMessage);
                    //TODO: To update S2VComponent so instead post on BB so
                    //TODO: S2VComponents sends to S2V using CCC instead
                    //String absServiceCandidates = communicationController.receiveS2V();
                    //Log4J.info(this, String.format("%s%s\t%s", DemoConstants.S2V, DemoConstants.LEVEL0, absServiceCandidates));
                    //TODO: psedo rules for testing: W.I.P
                    //compositionController.addStepAndRegister(userAction, absServiceCandidates);

                    //compositionController.fireRulesAS();

                    //TODO: have written pseudo rules and serviceMaps (string), W.I.P
                    //String[] result = compositionController.execute(serviceMap);
                    //String response = String.format("Your IPA would open this app: [%s] and execute this action: [%s]. " +
                      //      "Is that what you would want your IPA to do (Y/N)?");
                    String response = String.format("Your IPA would open this app: and execute this action: . " +
                                    "Is that what you would want your IPA to do (Y/N)?");
                            //Utils.splitByCapitalizedWord(result[0].replace("Service", ""), false),
                            //Utils.splitByCapitalizedWord(result[1], true));
                    Log4J.info(this, String.format("%s%s\t%s", DemoConstants.IPA, DemoConstants.LEVEL0, response));
                            stage = DemoConstants.ASK_FOR_APP_CONFIRMATION_STAGE;
                    SessionMessage clientSessionMessage = new SessionMessage();
                    clientSessionMessage.setMessageId(DemoConstants.MSG_SEND_TO_CLIENT);
                    clientSessionMessage.setRequestType(DemoConstants.MSG_SEND_TO_CLIENT);
                    clientSessionMessage.setPayload(response);
                    blackboard.post(this, DemoConstants.MSG_SEND_TO_CLIENT,
                            clientSessionMessage);
                }
            }
        }
        else{
            if(scenarioIdx < scenarios.size()){
                if( actionCounter < maxActions){
                    String response = String.format("You have identified %s actions/steps so far, please identify %s " +
                            "more....", actionCounter, (maxActions - actionCounter));
                    SessionMessage clientSessionMessage = new SessionMessage();
                    clientSessionMessage.setMessageId(DemoConstants.MSG_SEND_TO_CLIENT);
                    clientSessionMessage.setRequestType(DemoConstants.MSG_SEND_TO_CLIENT);
                    clientSessionMessage.setPayload(response);
                    blackboard.post(this, DemoConstants.MSG_SEND_TO_CLIENT,
                            clientSessionMessage);
                    Log4J.info(this, String.format("%s%s\t%s", DemoConstants.IPA, DemoConstants.LEVEL3, response));
                }else {
                    String response = String.format("Perfect, you are doing really well. This is the next scenario: \"%s\". " +
                            "What is the first action you would ask your IPA to do?", scenarios.get(scenarioIdx++));
                    SessionMessage clientSessionMessage = new SessionMessage();
                    clientSessionMessage.setMessageId(DemoConstants.MSG_SEND_TO_CLIENT);
                    clientSessionMessage.setRequestType(DemoConstants.MSG_SEND_TO_CLIENT);
                    clientSessionMessage.setPayload(response);
                    blackboard.post(this, DemoConstants.MSG_SEND_TO_CLIENT,
                            clientSessionMessage);                    actionCounter = 0;
                    Log4J.info(this, String.format("%s%s\t%s", DemoConstants.IPA, DemoConstants.LEVEL4, response));
                }
                stage = DemoConstants.REQUEST_ACTION_STAGE;
            }else{
                String response = "Wonderful, we have finished the scenarios. The last step is to answer a short " +
                        "questionnaire on this link (). Thanks for your collaboration!";
                SessionMessage clientSessionMessage = new SessionMessage();
                clientSessionMessage.setMessageId(DemoConstants.MSG_SEND_TO_CLIENT);
                clientSessionMessage.setRequestType(DemoConstants.MSG_SEND_TO_CLIENT);
                clientSessionMessage.setPayload(response);
                blackboard.post(this, DemoConstants.MSG_SEND_TO_CLIENT,
                        clientSessionMessage);
                Log4J.info(this, String.format("%s%s\t%s", DemoConstants.IPA, DemoConstants.LEVEL5, response));
                stage = DemoConstants.DONE_STAGE;
            }
        }
    }
}
