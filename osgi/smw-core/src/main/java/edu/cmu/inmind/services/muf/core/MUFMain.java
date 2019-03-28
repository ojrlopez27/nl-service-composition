package edu.cmu.inmind.services.muf.core;

import edu.cmu.inmind.multiuser.controller.muf.MUFLifetimeManager;
import edu.cmu.inmind.multiuser.controller.muf.MultiuserController;
import edu.cmu.inmind.multiuser.controller.muf.ShutdownHook;

public class MUFMain {

    public static void main(String[] args) throws Exception {

        MultiuserController multiuserController =
                MUFLifetimeManager.startFramework(
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
