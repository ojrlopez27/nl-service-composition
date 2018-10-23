package edu.cmu.inmind.demo.common;

import java.util.*;

/**
 * Created for demo : sakoju 10/4/2018
 */
public class DemoConstants {
    public static final String ID_NER = "ID_NER";
    public static final String ID_S2V = "ID_S2V";
    public static final String ID_RULE_ENGINE = "ID_RULE_ENGINE";
    public static final String ID_USERLOGIN = "ID_USERLOGIN";
    public static final String ID_SERVICE_MAP = "ID_SERVICE_MAP";
    public static final String ID_SERVICE_EXECUTION = "ID_SERVICE_EXECUTION";

    public static final String MSG_SEND_TO_CLIENT = "MSG_SEND_TO_CLIENT";
    public static final String MSG_SEND_TO_S2V = "MSG_SEND_TO_S2V";
    public static final String MSG_RECEIVE_S2V = "MSG_RECEIVE_S2V";
    public static final String MSG_SEND_TO_NER = "MSG_SEND_TO_NER";
    public static final String EMPTY_S2V = "EMPTY_S2V";
    public static String STEP_END ="STEP_END";
    public static final String MSG_GET_SERVICE_MAP ="MSG_GET_SERVICE_MAP";
    public static final String MSG_SERVICE_EXECUTION ="MSG_SERVICE_EXECUTION";
    public static final String MSG_SEND_SERVICE_MAP ="MSG_SEND_SERVICE_MAP";


    /***
     * Merging Ankit's changes
     */
    // MUF Pluggable Component IDs
    public static final String ID_COMPOSITION       = "ID_COMPOSITION";
    public static final String ID_MIDDLEWARE        = "ID_MIDDLEWARE";
    public static final String ID_OSGI_DEPLOYER     = "ID_OSGI_DEPLOYER";
    public static final String ID_OSGI_LAUNCHPAD    = "ID_OSGI_LAUNCHPAD";

    // Request Type Messages
    public static final String MSG_LAUNCHPAD        = "MSG_LAUNCHPAD";
    public static final String MSG_COMPOSITION      = "MSG_COMPOSITION";
    public static final String MSG_MIDDLEWARE       = "MSG_MIDDLEWARE";
    public static final String MSG_DEPLOYER         = "MSG_DEPLOYER";

    // Launchpad Commands/Messages
    public static final String MSG_LP_INPUT_CMD     = "MSG_LP_INPUT_CMD";
    public static final String MSG_LP_OUTPUT_CMD    = "MSG_LP_OUTPUT_CMD";
    public static final String MSG_LP_START_SERVICE = "MSG_LP_START_SERVICE";
    public static final String MSG_LP_LIST_SERVICES = "MSG_LP_LIST_SERVICES";

    // OSGi Service Messages
    public static final String MSG_OSGI_SERVICE_DEPLOYED = "MSG_OSGI_SERVICE_DEPLOYED";
    // Service Registry Messages: return object after service registry method invocations
    public static final String MSG_SR_RESP_INITIALIZE           = "MSG_SR_RESP_INITIALIZE";             // response when the service registry has been initialized
    public static final String MSG_SR_RESP_REGISTER_SERVICE     = "MSG_SR_RESP_REGISTER_SERVICE";       // response when a service has been registered
    public static final String MSG_SR_RESP_GET_SERVICE_BY_KEY   = "MSG_SR_RESP_GET_SERVICE_BY_KEY";     // response after the service pair has been found in registry
    public static final String MSG_SR_RESP_GET_SERVICE_BY_POJO  = "MSG_SR_RESP_GET_SERVICE_BY_POJO";    // response after the service pair has been found in registry
    public static final String MSG_LP_GET_ALL_SERVICES      = "MSG_LP_GET_ALL_SERVICES";            // retrieve all service references from launchpad for service registration
    public static final String MSG_LP_GET_SERVICE_IMPL      = "MSG_LP_GET_SERVICE_IMPL";            // retrieve a service object from the Felix framework via launchpad

    // Merging Ankit's changes ***** END


    /***
     * following constants from Oscar's composition module.
     */
    public static final String REQUIRES_FULLY_CHARGED = "REQUIRES_FULLY_CHARGED";
    public static final String WORKS_WITH_LOW_CHARGE = "WORKS_WITH_LOW_CHARGE";
    public static final String PHONE_LOW_BATTERY = "PHONE_LOW_BATTERY";
    public static final String PHONE_HIGH_BATTERY = "PHONE_HIGH_BATTERY";
    public static final String CHECK_BATTERY = "CHECK_BATTERY";

    public static final String REQUIRES_WIFI_CONNECTIVITY = "REQUIRES_WIFI_CONNECTIVITY";
    public static final String NOT_REQUIRES_WIFI_CONNECTIVITY = "NOT_REQUIRES_WIFI_CONNECTIVITY";
    public static final String CHECK_WIFI = "CHECK_WIFI";
    public static final String WIFI_ON = "WIFI_ON";
    public static final String WIFI_OFF = "WIFI_OFF";

    public static final String PICK_REMOTE_SERVICE = "PICK_REMOTE_SERVICE";
    public static final String PICK_LOCAL_SERVICE = "PICK_LOCAL_SERVICE";
    public static final String LOW_BATTERY_SERVICE = "LOW_BATTERY_SERVICE";
    public static final String HIGH_BATTERY_SERVICE = "HIGH_BATTERY_SERVICE";

    public static final String EOS = "<eos>";
    public static final String DONE = "DONE";

    public static final String MSG_CHECK_USER_ID = "MSG_CHECK_USER_ID";
    public static final String MSG_USER_VALIDATION_SUCCCES = "MSG_USER_VALIDATION_SUCCCES";
    public static final String MSG_USER_VALIDATION_ERROR = "MSG_USER_VALIDATION_ERROR";
    public static final String MSG_PROCESS_USER_ACTION = "MSG_PROCESS_USER_ACTION";
    public static final String MSG_GROUP_CHAT_READY="MSG_GROUP_CHAT_READY";


    // stages:
    public static final String REQUEST_ACTION_STAGE = "REQUEST_ACTION_STAGE";
    public static final String ASK_FOR_APP_CONFIRMATION_STAGE = "ASK_FOR_APP_CONFIRMATION_STAGE";
    public static final String DONE_STAGE = "DONE_STAGE";


    public static final String USER = "[USER]";
    public static final String IPA = "[IPA]";
    public static final String S2V = "[S2V]";

    public static final String LEVEL0 = "[0]:";
    public static final String LEVEL1 = "[1]:";
    public static final String LEVEL2 = "[2]:";
    public static final String LEVEL3 = "[3]:";
    public static final String LEVEL4 = "[4]:";
    public static final String LEVEL5 = "[5]:";
}
