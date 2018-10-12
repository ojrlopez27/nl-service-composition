package edu.cmu.inmind.demo.common;

import edu.cmu.inmind.demo.components.*;
import edu.cmu.inmind.demo.orchestrator.DemoOrchestrator;
import edu.cmu.inmind.multiuser.controller.common.CommonUtils;
import edu.cmu.inmind.multiuser.controller.common.Constants;
import edu.cmu.inmind.multiuser.controller.plugin.PluginModule;
import edu.cmu.inmind.multiuser.controller.resources.Config;

import java.util.concurrent.TimeUnit;
/**
 * Created for demo : sakoju 10/4/2018
 */
public class DemoMUFController {
    public static Config createConfig()
    {
        String serverIpAddress = Utils.getSystemIPAddress();
        System.out.println(serverIpAddress);
        return new Config.Builder()
                .setExceptionTraceLevel(Constants.SHOW_ALL_EXCEPTIONS)
                .setSessionManagerPort
                        (Integer.parseInt(CommonUtils.
                                getProperty("server.composition")))
                .setPathLogs(
                        CommonUtils.getProperty("logs.mkt.regular.path"))
                .setDefaultNumOfPoolInstances(10)
                .setSessionTimeout(5, TimeUnit.MINUTES)
                .setServerAddress("tcp://"+"inmind-maldysco.ddns.net")
                .build();
    }

    public static PluginModule[] createModules()
    {
        return new PluginModule[]{
                new PluginModule.Builder(DemoOrchestrator.class,
                        NERComponent.class, DemoConstants.ID_NER)
                .addPlugin(S2VComponent.class, DemoConstants.ID_S2V)
                .addPlugin(UserInteractionComponent.class, DemoConstants.ID_USERLOGIN)
                        .addPlugin(MiddlewareComponent.class, DemoConstants.ID_MIDDLEWARE)
                        .addPlugin(OSGiDeployerComponent.class, DemoConstants.ID_OSGI_DEPLOYER)
                        .addPlugin(OSGiLaunchpadComponent.class, DemoConstants.ID_OSGI_LAUNCHPAD)
                .build()
        };
    }
}
