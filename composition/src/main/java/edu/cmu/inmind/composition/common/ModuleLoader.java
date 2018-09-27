package edu.cmu.inmind.composition.common;

import edu.cmu.inmind.composition.components.MKTController;
import edu.cmu.inmind.composition.orchestrators.MKTExperimentOrchestrator;
import edu.cmu.inmind.multiuser.controller.common.CommonUtils;
import edu.cmu.inmind.multiuser.controller.common.Constants;
import edu.cmu.inmind.multiuser.controller.plugin.PluginModule;
import edu.cmu.inmind.multiuser.controller.resources.Config;

import java.util.concurrent.TimeUnit;

/**
 * Created by oscarr on 3/23/18.
 */
public class ModuleLoader {

    public static Config createConfig() {
        return new Config.Builder()
                .setExceptionTraceLevel( Constants.SHOW_ALL_EXCEPTIONS)
                .setSessionManagerPort( Integer.parseInt(CommonUtils.getProperty("server.composition")))
                .setDefaultNumOfPoolInstances(10)
                .setPathLogs(CommonUtils.getProperty("logs.mkt.regular.path"))
                .setSessionTimeout(5, TimeUnit.MINUTES)
                .setServerAddress("tcp://128.237.123.214") //use IP instead of 'localhost'
//                .setServerAddress("tcp://sogoranmac.ddns.net") //use IP instead of 'localhost'
                // if using FileLogger, just specify the path to store the logs
                //.setPathExceptionLogger(Utils.getProperty("pathLogs"))
                .build();
    }

    public static PluginModule[] createComponents() {
        return new PluginModule[]{
                new PluginModule.Builder(MKTExperimentOrchestrator.class,
                        MKTController.class, MKTController.class.getSimpleName())
                        .build()
        };
    }
}
