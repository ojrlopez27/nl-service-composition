package edu.cmu.inmind.composition.orchestrators;

import edu.cmu.inmind.composition.common.*;
import edu.cmu.inmind.composition.controllers.CommunicationController;
import edu.cmu.inmind.composition.controllers.CompositionController;
import edu.cmu.inmind.multiuser.controller.common.CommonUtils;
import edu.cmu.inmind.multiuser.controller.communication.SessionMessage;
import edu.cmu.inmind.multiuser.controller.orchestrator.ProcessOrchestratorImpl;
import edu.cmu.inmind.multiuser.controller.session.Session;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by oscarr on 8/19/18.
 */
public class MKTExperimentOrchestrator extends ProcessOrchestratorImpl {

    private List<String> scenarios = Arrays.asList(
            "You want to go on a vacation to Europe next month and need to plan your trip",
                "Your wedding anniversary is the next weekend and you want to plan a romantic night with your spouse",
                "You are planning to have a party at your home this coning weekend");
    private int scenarioIdx = 0;
    private Map<String, ServiceMethod> serviceMap;
    private CompositionController compositionController;
    private CommunicationController communicationController;
    private String stage = Constants.REQUEST_ACTION_STAGE;


    @Override
    public void initialize(Session session) throws Throwable{
        super.initialize( session );
        // we need to map services, put them into a file so sent2vec can use them
        serviceMap = Utils.generateCorporaFromMethods(false);
        // let's initialize all the resources
        compositionController = new CompositionController();
        communicationController = new CommunicationController();
        if( !getSessionId().equals("manager") ) scenarioIdx = 1;
        //compositionController.createRulesForGroundingServices();
    }


    @Override
    public void process(String message) throws Throwable {
        super.process(message);
        SessionMessage sessionMessage = CommonUtils.fromJson( message, SessionMessage.class );
        switch( sessionMessage.getRequestType() ) {
            case Constants.MSG_CHECK_USER_ID:
                checkUserLogin(sessionMessage.getPayload());
                break;
            case Constants.MSG_PROCESS_USER_ACTION:
                processUserAction(sessionMessage.getPayload());
                break;
        }
    }


    private void checkUserLogin(String username){
        String validate = Schedule.validate(username);
        if(validate.equals(Schedule.USER_ID_NOT_EXISTS))
            validate = "Wrong MKT id, please try again!";
        else if(validate.equals(Schedule.TOO_EARLY))
            validate = "You have connected too early, please come back at your scheduled time!";
        else if(validate.equals(Schedule.TOO_LATE))
            validate = "Sorry, you have connected too late, please request another time slot through the doodle!";
        else {
            validate = String.format("Thanks! let's start. Consider this scenario: \"%s\". What is the first thing you " +
                    "would ask your IPA to do?", scenarios.get(scenarioIdx++));
            IPALog.setFileName(username);
        }
        sendResponse( validate );
    }


    private void processUserAction(String userAction){
        IPALog.log(this, "userAction:\t" + userAction);
        if( !userAction.equals(Constants.DONE) ) {
            if( Constants.REQUEST_ACTION_STAGE.equals(stage) ) {
                // let's get the semantic neighbors provided by sent2vec
                communicationController.sendS2V(userAction);
                String absServiceCandidates = communicationController.receiveS2V();
                IPALog.debug(this, "Received from sent2vec: " + absServiceCandidates);
                compositionController.addStepAndRegister(userAction, absServiceCandidates);
                compositionController.fireRulesAS();

                String[] result = compositionController.execute(serviceMap);
                String response = String.format("Your IPA would open this app: [%s] and execute this action: [%s]. " +
                                "Is that what you would want your IPA to do (Y/N)?",
                        Utils.splitByCapitalizedWord(result[0].replace("Service", ""), false),
                        Utils.splitByCapitalizedWord(result[1], true));
                stage = Constants.ASK_FOR_APP_CONFIRMATION_STAGE;
                sendResponse(response);
            }
            else if( Constants.ASK_FOR_APP_CONFIRMATION_STAGE.equals(stage) ) {
                if(userAction.equalsIgnoreCase("Y") || userAction.equalsIgnoreCase("Yes")){
                    compositionController.fireRulesGS();
                    sendResponse("Great, let's continue. What is the next thing you would ask your IPA to do? " +
                            "(type 'DONE' if you are done for this scenario)");
                    stage = Constants.REQUEST_ACTION_STAGE;
                }else{
                    sendResponse("Ok, can you re-phrase your command? your IPA will try to do it better this time...");
                    stage = Constants.REQUEST_ACTION_STAGE;
                }
            }
        }else{
            if(scenarioIdx < scenarios.size()){
                sendResponse(String.format("Perfect, you are doing really well. This is the next scenario: \"%s\". " +
                                "What is the first action you would ask your IPA to do?", scenarios.get(scenarioIdx++)));
                stage = Constants.REQUEST_ACTION_STAGE;
            }else{
                sendResponse("Wonderful, we have finished the scenarios. The last step is to answer a short " +
                        "questionnaire on this link (). Thanks for your collaboration!");
                stage = Constants.DONE_STAGE;
            }
        }
    }
}
