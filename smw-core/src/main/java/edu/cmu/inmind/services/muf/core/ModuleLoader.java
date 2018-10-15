package edu.cmu.inmind.services.muf.core;

import edu.cmu.inmind.multiuser.controller.common.CommonUtils;
import edu.cmu.inmind.multiuser.controller.common.Constants;
import edu.cmu.inmind.multiuser.controller.plugin.PluginModule;
import edu.cmu.inmind.multiuser.controller.resources.Config;
import edu.cmu.inmind.services.muf.components.CompositionComponent;
import edu.cmu.inmind.services.muf.components.NERComponent;
import edu.cmu.inmind.services.muf.components.OSGiDeployerComponent;
import edu.cmu.inmind.services.muf.components.OSGiLaunchpadComponent;
import edu.cmu.inmind.services.muf.components.Sent2VecComponent;
import edu.cmu.inmind.services.muf.components.services.ServiceRegistryComponent;
import java.util.concurrent.TimeUnit;

import static edu.cmu.inmind.services.muf.commons.Constants.ID_COMPOSITION;
import static edu.cmu.inmind.services.muf.commons.Constants.ID_NER;
import static edu.cmu.inmind.services.muf.commons.Constants.ID_OSGI_DEPLOYER;
import static edu.cmu.inmind.services.muf.commons.Constants.ID_OSGI_LAUNCHPAD;
import static edu.cmu.inmind.services.muf.commons.Constants.ID_SENT2VEC;
import static edu.cmu.inmind.services.muf.commons.Constants.ID_SERVICE_REGISTRY;
import static edu.cmu.inmind.services.muf.core.Constants.MUF_LOGS_EXCEPTION;
import static edu.cmu.inmind.services.muf.core.Constants.MUF_LOGS_REGULAR;
import static edu.cmu.inmind.services.muf.core.Constants.MUF_SERVER_PORT;

public class ModuleLoader {

    private static final int NUM_POOL_INSTANCES = 10;
    private static final int SESSION_TIMEOUT_THRESHOLD = 5;     // minutes
    private static final String URL_SERVER_ADDRESS = "tcp://localhost";

    public static PluginModule[] createComponents() {
        return new PluginModule[]{
                new PluginModule.Builder(MUFOrchestrator.class, OSGiLaunchpadComponent.class, ID_OSGI_LAUNCHPAD)
                    .addPlugin(CompositionComponent.class, ID_COMPOSITION)
                    //.addPlugin(MiddlewareComponent.class, ID_MIDDLEWARE)
                    .addPlugin(ServiceRegistryComponent.class, ID_SERVICE_REGISTRY)
                    .addPlugin(NERComponent.class, ID_NER)
                    .addPlugin(OSGiDeployerComponent.class, ID_OSGI_DEPLOYER)
                    .addPlugin(Sent2VecComponent.class, ID_SENT2VEC)
                    .build()
        };
    }

    public static Config createConfig() {
        return new Config.Builder()
                .setServerAddress(URL_SERVER_ADDRESS)
                .setSessionManagerPort(Integer.parseInt(CommonUtils.getProperty(MUF_SERVER_PORT)))
                .setSessionTimeout(SESSION_TIMEOUT_THRESHOLD, TimeUnit.MINUTES)
                .setDefaultNumOfPoolInstances(NUM_POOL_INSTANCES)
                .setPathLogs(CommonUtils.getProperty(MUF_LOGS_REGULAR))
                .setPathExceptionLogger(CommonUtils.getProperty(MUF_LOGS_EXCEPTION))
                .setExceptionTraceLevel(Constants.SHOW_ALL_EXCEPTIONS)
                .build();
    }
}
