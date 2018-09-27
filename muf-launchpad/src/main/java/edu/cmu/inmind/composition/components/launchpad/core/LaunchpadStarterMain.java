package edu.cmu.inmind.composition.components.launchpad.core;

import static edu.cmu.inmind.composition.components.launchpad.commons.Constants.FELIX_CMD_PS;

public class LaunchpadStarterMain {

    private static final String TAG = LaunchpadStarterMain.class.getSimpleName();
    private static LaunchpadStarter launchpadStarter;

    public static void init() {

        System.out.println("in LaunchpadStarterMain.init()..");
        try {
            // initialize launchpad starter
            launchpadStarter = LaunchpadStarter.getInstance();

            // ask launchpad starter to start services
            launchpadStarter.startServices();

            // ask launchpad to list the bundles
            launchpadStarter.executeCommand(FELIX_CMD_PS);

        } catch (Exception e) {

            // if there's an exception that can be caught, stop the felix framework
            LaunchpadStarter.getInstance().stopFelixFramework();

            // then, throw the exception
            throw new RuntimeException(e);
        }
    }
}
