package edu.cmu.inmind.composition.launchers;

import edu.cmu.inmind.composition.common.ModuleLoader;
import edu.cmu.inmind.multiuser.controller.muf.MUFLifetimeManager;
import edu.cmu.inmind.multiuser.controller.muf.MultiuserController;
import edu.cmu.inmind.multiuser.controller.muf.ShutdownHook;

/**
 * Created by oscarr on 8/19/18.
 */
public class MainExecuter {

    public static void main(String args[]) throws Throwable {
//        System.setProperty("log.folder", CommonUtils.getProperty("logs.mkt.regular.path") );
        MultiuserController multiuserController = MUFLifetimeManager.startFramework(ModuleLoader.createComponents(),
                ModuleLoader.createConfig());
        multiuserController.addShutDownHook(new ShutdownHook() {
            @Override
            public void execute() {
                MUFLifetimeManager.stopFramework(multiuserController);
            }
        });
    }
}
