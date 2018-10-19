package edu.cmu.inmind.demo.components;

import edu.cmu.inmind.demo.common.DemoConstants;
import edu.cmu.inmind.demo.common.Node;
import edu.cmu.inmind.demo.common.ServiceMethod;
import edu.cmu.inmind.demo.common.Utils;
import edu.cmu.inmind.demo.controllers.CompositionController;
import edu.cmu.inmind.multiuser.controller.blackboard.Blackboard;
import edu.cmu.inmind.multiuser.controller.blackboard.BlackboardEvent;
import edu.cmu.inmind.multiuser.controller.blackboard.BlackboardSubscription;
import edu.cmu.inmind.multiuser.controller.common.Constants;
import edu.cmu.inmind.multiuser.controller.communication.SessionMessage;
import edu.cmu.inmind.multiuser.controller.log.Log4J;
import edu.cmu.inmind.multiuser.controller.plugin.PluggableComponent;
import edu.cmu.inmind.multiuser.controller.plugin.StateType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created for demo : sakoju 10/4/2018
 */
@StateType(state = Constants.STATELESS)
@BlackboardSubscription(messages =  {DemoConstants.MSG_GROUP_CHAT_READY,
        DemoConstants.MSG_PROCESS_USER_ACTION, DemoConstants.MSG_RECEIVE_S2V})
public class RuleEngineComponent extends PluggableComponent {
    private CompositionController compositionController;
    private Map<String, ServiceMethod> serviceMap;

    // scenarios
    private List<String> scenarios = Arrays.asList(
            "You want to go on a vacation to Europe next month and need to plan your trip",
            "Your wedding anniversary is the next weekend and you want to plan a romantic night with your spouse",
            "You are planning to have a party at your home this coning weekend");
    private int scenarioIdx = 0;
    private String stage = DemoConstants.REQUEST_ACTION_STAGE;
    private int actionCounter = 0;
    private final int maxActions = 7;
    private final int minLength = 15;
    private String absServiceCandidates;
    private String userAction ="";
    private Blackboard blackboard;

    // private ServiceExecutor serviceExecutor;
    @Override
    public void onEvent(Blackboard blackboard, BlackboardEvent blackboardEvent) throws Throwable {
        this.blackboard = blackboard;
        switch(blackboardEvent.getId()) {
            //Android Phone app's GroupChannel chat is ready ?
            case DemoConstants.MSG_GROUP_CHAT_READY :
                String response = String.format("Thanks! let's start. Consider this scenario: %s. What is the first thing you " +
                        "would ask your IPA to do?", scenarios.get(scenarioIdx++));
                //IPALog.setFileName(sessionMessage.getPayload());
                Log4J.info(this, response);
                postToBlackboard(DemoConstants.MSG_SEND_TO_CLIENT, response);
                break;
            case DemoConstants.MSG_PROCESS_USER_ACTION:
                processUserAction((String) blackboardEvent.getElement());
                break;
            case DemoConstants.MSG_RECEIVE_S2V:
                Log4J.info(this, "S2V received "+blackboardEvent.getElement().toString());
                SessionMessage sessionMessage = (SessionMessage)  blackboardEvent.getElement();
                absServiceCandidates = sessionMessage.getPayload();
                // TODO: Now that service mapping, service execution are done,
                // TODO: check Rules, identify Sequence and send result to User
                processUserActionOnS2V();
            default:
                break;
        }

    }

    @Override
    protected void startUp() {
        super.startUp();
        compositionController = new CompositionController();
        serviceMap = Utils.generateCorporaFromMethods(false);
    }

    @Override
    public void shutDown() {
        super.shutDown();
    }

    public class CompositeService {
        private List<Node> nodes = new ArrayList();
        private int idx = 0;

        private CompositeService(CompositeServiceBuilder builder){
            this.nodes = builder.nodes;
        }

        public List<Node> getNodes() {
            return nodes;
        }

        public String getNext() {
            if (idx < nodes.size()) {
                return nodes.get(idx++).getName();
            }
            return "No more services to process!";
        }
    }

    public class CompositeServiceBuilder{
        private List<Node> nodes = new ArrayList();

        private CompositeServiceBuilder(String request){
            and( request );
        }

        public CompositeServiceBuilder and(String request) {
            nodes.add(new Node(request, Node.NodeType.PLAIN));
            return this;
        }

        public CompositeService build(){
            return new CompositeService(this);
        }
    }


    /***
     * process user action
     * @param userAction
     */
    private void processUserAction(String userAction)
    {
        Log4J.info(this, "orchestrator processUserAction: "+userAction);
        Log4J.info(this, String.format("%s%s\t%s", DemoConstants.USER, DemoConstants.LEVEL0, userAction));
        if( !userAction.equalsIgnoreCase(DemoConstants.DONE) ) {
            Log4J.info(this, "userAction is NOT DONE: processUserAction: "+userAction);
            if( DemoConstants.ASK_FOR_APP_CONFIRMATION_STAGE.equals(stage) ) {
                //user said Yes (confirmation)
                if(userAction.equalsIgnoreCase("Y") || userAction.equalsIgnoreCase("Yes")
                        || userAction.contains("Yes ") || userAction.equalsIgnoreCase("yes ")
                        || userAction.equalsIgnoreCase("YES "))
                {
                    continueWithCurrentScenario();
                }
                //user said other than Yes/Y/yes//y
                else
                    {
                        rephraseYourStep();
                    }
            }
            else if( DemoConstants.REQUEST_ACTION_STAGE.equals(stage) )
            {
                //user sentence is too short ?
                if( userAction.length() < minLength ){
                    userSentenceIsTooShort();
                }
                // let's get the semantic neighbors provided by sent2vec
                else
                {
                    postToBlackboard(DemoConstants.MSG_SEND_TO_S2V, userAction);
                    postToBlackboard(DemoConstants.MSG_SEND_TO_NER, userAction);
                }
            }
        }
        else{
            if(scenarioIdx < scenarios.size()){
                Log4J.info(this, "scenarios "+scenarioIdx);
                //user typed DONE, but scenario is NOT DONE
                if( actionCounter < maxActions){
                    askUserToEnterNextStep();
                }
                //scenario is DONE so proceed to next scenario
                else
                {
                    String response = String.format("Perfect, you are doing really well. This is the next scenario: %s. " +
                            "What is the first action you would ask your IPA to do?", scenarios.get(scenarioIdx++));
                    postToBlackboard(DemoConstants.MSG_SEND_TO_CLIENT , response);
                    actionCounter = 0;
                    Log4J.info(this, String.format("%s%s\t%s", DemoConstants.IPA, DemoConstants.LEVEL4, response));
                }
                stage = DemoConstants.REQUEST_ACTION_STAGE;
            }
            //3 scenarios are DONE, so end the iteration for user
            else
                {
                String response = "Wonderful, we have finished the scenarios. Thanks for your collaboration!";
                postToBlackboard(DemoConstants.MSG_SEND_TO_CLIENT , response);
                Log4J.info(this, String.format("%s%s\t%s", DemoConstants.IPA, DemoConstants.LEVEL5, response));
                stage = DemoConstants.DONE_STAGE;
            }
        }
    }

    /***
     * Received results from S2V, so now process userAction + S2V response : composition...
     */
    private void processUserActionOnS2V()
    {
        Log4J.info(this, String.format("%s%s\t%s",
                DemoConstants.S2V, DemoConstants.LEVEL0, absServiceCandidates));
        compositionController.addStepAndRegister(userAction, absServiceCandidates);
        compositionController.fireRulesAS();
        resetAbstractServiceCandidates();

        String[] result = compositionController.execute(serviceMap);
        String response = String.format("Your IPA would open this app: [%s] and execute this action: [%s]. " +
                        "Is that what you would want your IPA to do (Y/N)?",
                Utils.splitByCapitalizedWord(result[0].replace("Service", ""),
                        false),
                Utils.splitByCapitalizedWord(result[1], true));
        Log4J.info(this, String.format("%s%s\t%s", DemoConstants.IPA, DemoConstants.LEVEL0, response));
        stage = DemoConstants.ASK_FOR_APP_CONFIRMATION_STAGE;
        postToBlackboard(DemoConstants.MSG_SEND_TO_CLIENT, response);
    }

    /***
     * Post to Blackboard
     * @param MSG_ID
     * @param response
     */
    private void postToBlackboard(String MSG_ID, String response)
    {
        SessionMessage clientSessionMessage = new SessionMessage();
        clientSessionMessage.setMessageId(MSG_ID);
        clientSessionMessage.setRequestType(MSG_ID);
        clientSessionMessage.setPayload(response);
        blackboard.post(this, MSG_ID,
                clientSessionMessage);
    }

    /***
     * Service methods results confirmed and accepted as correct by user
     */
    private void continueWithCurrentScenario()
    {
        compositionController.fireRulesGS();
        String response = "Great, let's continue. What is the next thing you would ask your IPA to do? " +
                "(type 'DONE' if you are done for this scenario -- but at least 7 actions/steps are required)";
        postToBlackboard(DemoConstants.MSG_SEND_TO_CLIENT , response);
        Log4J.info(this, String.format("%s%s\t%s", DemoConstants.IPA, DemoConstants.LEVEL1, response));
        stage = DemoConstants.REQUEST_ACTION_STAGE;
        actionCounter++;
    }

    /***
     * user did not confirm correctly (response!= yes/y/Y/Yes)
     */
    private void rephraseYourStep()
    {
        String response = "Ok, can you re-phrase your command? your IPA will try to do it better " +
                "this time...";
        postToBlackboard(DemoConstants.MSG_SEND_TO_CLIENT , response);
        Log4J.info(this, String.format("%s%s\t%s", DemoConstants.IPA,
                DemoConstants.LEVEL2, response));
        stage = DemoConstants.REQUEST_ACTION_STAGE;
    }

    /***
     * User sentence is too short. request another sentence
     */
    private void userSentenceIsTooShort()
    {
        String response = String.format("Your sentence is empty or too short (only %s characters). " +
                "Please re-enter a sentence with at least %s-characters length", userAction.length(),
                minLength);
        Log4J.info(this, String.format("%s%s\t%s", DemoConstants.IPA, DemoConstants.LEVEL5, response));
        postToBlackboard(DemoConstants.MSG_SEND_TO_CLIENT , response);
    }

    /***
     * User typed DONE but scenario is not yet complete unless user elicits 7 steps.
     */
    private void askUserToEnterNextStep()
    {
        String response = String.format("You have identified %s actions/steps so far, please identify %s " +
                "more....", actionCounter, (maxActions - actionCounter));
        postToBlackboard(DemoConstants.MSG_SEND_TO_CLIENT, response);
        Log4J.info(this, String.format("%s%s\t%s", DemoConstants.IPA,
                DemoConstants.LEVEL3, response));
    }

    private void resetAbstractServiceCandidates()
    {
        absServiceCandidates="";
    }
}
