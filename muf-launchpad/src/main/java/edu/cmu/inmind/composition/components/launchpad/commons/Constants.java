package edu.cmu.inmind.composition.components.launchpad.commons;

public class Constants {

    // OBR URLs
    private static final String OBR_PREFIX              = "obr/";
    public static final String URL_OBR_INMIND           = OBR_PREFIX + "inmind/repository.xml";
    public static final String URL_OBR_CALCULATOR       = OBR_PREFIX + "calculator/repository.xml";
    public static final String URL_OBR_INMIND_OSGI_CORE = OBR_PREFIX + "merge-Apr25/repository.xml";

    // Felix shell commands
    public static final String FELIX_CMD_BUNDLE_LEVEL = "bundlelevel";
    public static final String FELIX_CMD_START_LEVEL  = "startlevel";
    public static final String FELIX_CMD_PS           = "ps";

    public static final String POJO = "POJO";
    public static final String TOKEN = ":";
    public static final String OSGI_EE = "osgi.ee";

    // MUF Configurations
    public static final String MUF_SERVER_COMPOSITION       = "server.composition";
    public static final String MUF_SERVER_COMMUNICATION     = "launchpad.server.port";
    public static final String MUF_LOGS_REGULAR             = "logs.regular.path";
    public static final String MUF_LOGS_EXCEPTION           = "logs.exception.path";
}