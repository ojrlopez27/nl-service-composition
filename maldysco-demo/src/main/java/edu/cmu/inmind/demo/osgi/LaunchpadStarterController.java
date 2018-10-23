package edu.cmu.inmind.demo.osgi;

import edu.cmu.inmind.multiuser.controller.log.Log4J;
import edu.cmu.inmind.services.muf.data.OSGiService;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

import java.util.Map;

import static edu.cmu.inmind.demo.osgi.LaunchpadConstants.FELIX_CMD_PS;

/***
 * Merging Ankit's changes
 */
public class LaunchpadStarterController {
    LaunchpadStarter launchpadStarter;

    public void initFramework() {
        Log4J.info(this, "Inside LaunchpadStarter.initFramework()");

        // initialize launchpad starter
        launchpadStarter = LaunchpadStarter.getInstance();
    }

    public void stopFramework() {
        Log4J.info(this, "Inside LaunchpadStarter.stopFramework()");

        // if there's an exception that can be caught, stop the felix framework
        LaunchpadStarter.getInstance().stopFelixFramework();
    }

    public void startService(OSGiService osGiService) {
        Log4J.info(this, "Inside LaunchpadStarter.startService() for "
                + osGiService.getServiceName() + " at "
                + osGiService.getServiceLevel());

        // ask launchpad starter to start the service
        launchpadStarter.startService(osGiService.getServiceName(), Integer.parseInt(osGiService.getServiceLevel()));
    }

    public void startService(String service, Number level) {
        Log4J.info(this, "Inside LaunchpadStarter.startService() for "
                + service + " at " + level);

        // ask launchpad starter to start the service
        launchpadStarter.startService(service, level.intValue());
    }

    public void listServices() {
        Log4J.info(this, "Inside LaunchpadStarter.listServices()");

        // ask launchpad to list the bundles
        launchpadStarter.executeCommand(FELIX_CMD_PS);
    }

    public ServiceReference[] getServices(OSGiService osGiService) throws InvalidSyntaxException {
        return launchpadStarter.getServices(osGiService.getServiceName(), Boolean.FALSE);
    }

    public Object getImplementation(ServiceReference serviceReference) {
        Log4J.info(this, "Inside LaunchpadStarter.getImplementation() for " + serviceReference);
        return launchpadStarter.getImplementation(serviceReference);
    }

    public Map<ServiceReference, Object> getAllServiceImplementations() {
        Log4J.info(this, "Inside LaunchpadStarter.getAllServiceImplementations()");

        // retrieve all service references and implementations from launchpad
        return launchpadStarter.getAllServiceImplementations();
    }
}
