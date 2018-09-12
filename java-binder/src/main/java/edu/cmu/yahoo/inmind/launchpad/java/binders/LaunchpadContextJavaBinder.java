package edu.cmu.yahoo.inmind.launchpad.java.binders;

import edu.cmu.yahoo.inmind.launchpad.utils.LaunchpadContext;

/**
 * Created by dangiankit on 2/14/18.
 */

public class LaunchpadContextJavaBinder extends LaunchpadContext {

    private static LaunchpadContextJavaBinder instance;

    private LaunchpadContextJavaBinder() {
        init();
    }

    public static void bind() {
        if (instance == null) {
            instance = new LaunchpadContextJavaBinder();
        }
    }

    @Override
    protected void init() {
        register(LoggerContextJavaBinder.getInstance());
        register(StorageContextJavaBinder.getInstance());
        register(ExecutorContextJavaBinder.getInstance());
        register(PropertiesContextJavaBinder.getInstance());
    }
}
