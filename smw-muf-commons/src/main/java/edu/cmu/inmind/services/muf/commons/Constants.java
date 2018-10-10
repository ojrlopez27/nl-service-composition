package edu.cmu.inmind.services.muf.commons;

public class Constants {

    // MUF Pluggable Component IDs
    public static final String ID_COMPOSITION       = "ID_COMPOSITION";
    public static final String ID_MIDDLEWARE        = "ID_MIDDLEWARE";
    public static final String ID_NER               = "ID_NER";
    public static final String ID_OSGI_DEPLOYER     = "ID_OSGI_DEPLOYER";
    public static final String ID_OSGI_LAUNCHPAD    = "ID_OSGI_LAUNCHPAD";
    public static final String ID_SENT2VEC          = "ID_SENT2VEC";

    // Request Type Messages
    public static final String MSG_LAUNCHPAD        = "MSG_LAUNCHPAD";
    public static final String MSG_SENT2VEC         = "MSG_SENT2VEC";
    public static final String MSG_COMPOSITION      = "MSG_COMPOSITION";
    public static final String MSG_MIDDLEWARE       = "MSG_MIDDLEWARE";
    public static final String MSG_NER              = "MSG_NER";
    public static final String MSG_DEPLOYER         = "MSG_DEPLOYER";

    // Launchpad Commands/Messages
    public static final String MSG_LP_INPUT_CMD     = "MSG_LP_INPUT_CMD";
    public static final String MSG_LP_OUTPUT_CMD    = "MSG_LP_OUTPUT_CMD";
    public static final String MSG_LP_START_SERVICE = "MSG_LP_START_SERVICE";
    public static final String MSG_LP_LIST_SERVICES = "MSG_LP_LIST_SERVICES";

    // OSGi Service Messages
    public static final String MSG_OSGI_SERVICE_DEPLOYED = "MSG_OSGI_SERVICE_DEPLOYED";
}
