package edu.cmu.inmind.services.muf.components.osgi;

import edu.cmu.inmind.services.muf.data.OSGiService;
import edu.cmu.inmind.multiuser.controller.log.Log4J;
import java.util.ArrayList;
import java.util.List;

public class LaunchpadStarterController {
    //LaunchpadStarter launchpadStarter;
    List<OSGiService> osGiServiceList;
    boolean debug = Boolean.TRUE;

    public void initFramework() {
        Log4J.info(this, "Inside LaunchpadStarter.initFramework()");

        if (debug) {

            // initialize launchpad starter
            //launchpadStarter = LaunchpadStarter.getInstance();
        }

        osGiServiceList = new ArrayList<>();
    }

    public void stopFramework() {
        Log4J.info(this, "Inside LaunchpadStarter.stopFramework()");

        if (debug) {

            // if there's an exception that can be caught, stop the felix framework
            //LaunchpadStarter.getInstance().stopFelixFramework();
        }

        osGiServiceList = null;
    }

    public void startService(OSGiService osGiService) {
        Log4J.info(this, "Inside LaunchpadStarter.startService() for "
                + osGiService.getServiceName() + " at "
                + osGiService.getServiceLevel());

        if (debug) {

            // ask launchpad starter to start the service
            //launchpadStarter.startService(osGiService.getServiceName(), Integer.parseInt(osGiService.getServiceLevel()));
        }

        osGiServiceList.add(osGiService);
    }

    public void startService(String service, Number level) {
        Log4J.info(this, "Inside LaunchpadStarter.startService() for "
                + service + " at " + level);

        if (debug) {

            // ask launchpad starter to start the service
            //launchpadStarter.startService(service, level.intValue());
        }

        // osGiServiceList.add(new OSGiService(service, level));
    }

    public void listServices() {
        Log4J.info(this, "Inside LaunchpadStarter.listServices()");

        if (debug) {

            // ask launchpad to list the bundles
            //launchpadStarter.executeCommand(FELIX_CMD_PS);
        }

        System.out.println(osGiServiceList);

    }
}
