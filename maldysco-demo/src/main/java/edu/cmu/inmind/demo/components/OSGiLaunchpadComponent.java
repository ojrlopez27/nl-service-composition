package edu.cmu.inmind.demo.components;

import edu.cmu.inmind.demo.common.DemoConstants;
import edu.cmu.inmind.demo.data.LaunchpadInput;
import edu.cmu.inmind.demo.osgi.LaunchpadStarterController;
import edu.cmu.inmind.multiuser.controller.blackboard.Blackboard;
import edu.cmu.inmind.multiuser.controller.blackboard.BlackboardEvent;
import edu.cmu.inmind.multiuser.controller.blackboard.BlackboardSubscription;
import edu.cmu.inmind.multiuser.controller.common.Constants;
import edu.cmu.inmind.multiuser.controller.log.Log4J;
import edu.cmu.inmind.multiuser.controller.plugin.PluggableComponent;
import edu.cmu.inmind.multiuser.controller.plugin.StateType;
/***
 * Merging Ankit's changes
 */
@StateType(state = Constants.STATEFULL)
@BlackboardSubscription(messages = {DemoConstants.MSG_LP_INPUT_CMD}) // MSG_LP_START_SERVICE, MSG_LP_LIST_SERVICES})
public class OSGiLaunchpadComponent extends PluggableComponent {

    LaunchpadStarterController launchpadStarterController;

    @Override
    public void startUp(){
        super.startUp();
        Log4J.info(this, "Inside OSGiLaunchpadComponent.startUp");
        launchpadStarterController = new LaunchpadStarterController();
        launchpadStarterController.initFramework();
    }

    @Override
    public void shutDown() {
        super.shutDown();
        Log4J.info(this, "Inside OSGiLaunchpadComponent.shutDwon");
        launchpadStarterController.stopFramework();
    }

    /*
    @Override
    public void execute() {
        Log4J.info(this, "Inside OSGiLaunchpadComponent.execute: " + hashCode());
        handleService(getBlackBoard(getSessionId()));
    }
    */

    @Override
    public void onEvent(Blackboard blackboard, BlackboardEvent blackboardEvent) throws Throwable {
        Log4J.info(this, "Inside OSGiLaunchpadComponent.onEvent");
        try {
            // read the input command
            LaunchpadInput launchpadInput = (LaunchpadInput) blackboardEvent.getElement();
            String launchpadInputCommand = launchpadInput.getCommand();

            // process the input command
            switch (launchpadInputCommand) {
                case DemoConstants.MSG_LP_START_SERVICE:
                    launchpadStarterController.startService(launchpadInput.getOsGiService());
                    break;
                case DemoConstants.MSG_LP_LIST_SERVICES:
                    launchpadStarterController.listServices();
                    break;
                default:
                    Log4J.info(this, "Inside OSGiLaunchpadComponent.handleService " + " -- not sure what to do.");
            }

            // identify the output:
            // May be, a custom LaunchpadOutput object?
            // ... do something interesting here ...
            Object launchpadOutput = new Object();

            // update the blackboard
            if (launchpadInput.getOsGiService() != null) {
                blackboard.post(this, DemoConstants.MSG_LP_OUTPUT_CMD, launchpadInput.getOsGiService()); // launchpadOutput);
            }

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

}
