package edu.cmu.inmind.composition.components.launchpad.muf;

import edu.cmu.inmind.multiuser.controller.muf.MUFLifetimeManager;
import edu.cmu.inmind.multiuser.controller.muf.MultiuserController;
import edu.cmu.inmind.multiuser.controller.muf.ShutdownHook;

public class MUFLaunchpadLauncher {

    public static void main(String args[]) throws Throwable {
        System.setProperty("log4j.configurationFile", "muf-launchpad-log4j2.xml,log4j2.xml");

        MultiuserController multiuserController
                = MUFLifetimeManager.startFramework(
                        ModuleLoader.createComponents(),
                ModuleLoader.createConfig()
        );

        multiuserController.addShutDownHook(new ShutdownHook() {
            @Override
            public void execute() {
                MUFLifetimeManager.stopFramework(multiuserController);
            }
        });
    }
}
