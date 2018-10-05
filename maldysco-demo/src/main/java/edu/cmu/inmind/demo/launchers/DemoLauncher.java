package edu.cmu.inmind.demo.launchers;

import edu.cmu.inmind.demo.common.DemoMUFController;
import edu.cmu.inmind.multiuser.controller.muf.MUFLifetimeManager;
import edu.cmu.inmind.multiuser.controller.muf.MultiuserController;
import edu.cmu.inmind.multiuser.controller.muf.ShutdownHook;

/**
 * Created for demo : sakoju 10/4/2018
 */
public class DemoLauncher {
    public static void main(String args[]) throws Throwable
    {
        MultiuserController multiuserController = MUFLifetimeManager
                .startFramework(DemoMUFController.createModules()
                ,DemoMUFController.createConfig());
        multiuserController.addShutDownHook(new ShutdownHook() {
            @Override
            public void execute() {
                MUFLifetimeManager.stopFramework(multiuserController);
            }
        });
    }
}
