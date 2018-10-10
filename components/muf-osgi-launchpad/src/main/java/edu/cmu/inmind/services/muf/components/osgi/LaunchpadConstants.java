package edu.cmu.inmind.services.muf.components.osgi;

public class LaunchpadConstants {

    public static final String FRAMEWORK_ALREADY_INIT       = "Felix framework already initialized by Launchpad.";
    public static final String FRAMEWORK_NOT_INITIALIZED    = "Felix framework not initialized by Launchpad.";

    // OBR URLs
    private static final String OBR_PREFIX              = "obr/";
    public static final String URL_OBR_INMIND           = OBR_PREFIX + "inmind/repository.xml";
    public static final String URL_OBR_CALCULATOR       = OBR_PREFIX + "calculator/repository.xml";
    public static final String URL_OBR_INMIND_OSGI_CORE = OBR_PREFIX + "merge-Apr25/repository.xml";

    // Felix shell commands
    public static final String FELIX_CMD_BUNDLE_LEVEL = "bundlelevel";
    public static final String FELIX_CMD_START_LEVEL  = "startlevel";
    public static final String FELIX_CMD_PS           = "ps";


    public static final String OSGI_EE = "osgi.ee";
}
