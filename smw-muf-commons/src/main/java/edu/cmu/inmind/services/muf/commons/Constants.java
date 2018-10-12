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
    public static final String MSG_LP_INPUT_CMD             = "MSG_LP_INPUT_CMD";                   // process launchpad commands assuming it is from client or other component
    public static final String MSG_LP_OUTPUT_CMD            = "MSG_LP_OUTPUT_CMD";

    // Launchpad Messages: processed by Launchpad as method invocations
    public static final String MSG_LP_START_SERVICE         = "MSG_LP_START_SERVICE";               // start a service by loading the bundle via launchpad
    public static final String MSG_LP_LIST_SERVICES         = "MSG_LP_LIST_SERVICES";               // list all services that have been loaded in Launchpad
    public static final String MSG_LP_GET_ALL_SERVICES      = "MSG_LP_GET_ALL_SERVICES";            // retrieve all service references from launchpad for service registration
    public static final String MSG_LP_GET_SERVICE_IMPL      = "MSG_LP_GET_SERVICE_IMPL";            // retrieve a service object from the Felix framework via launchpad

    // Launchpad Messages: return objects after Launchpad method invocations
    public static final String MSG_LP_RESP_GET_ALL_SERVICES = "MSG_LP_RESP_GET_ALL_SERVICES";       // response by returning all services in launchpad
    public static final String MSG_LP_RESP_GET_SERVICE_IMPL = "MSG_LP_RESP_GET_SERVICE_IMPL";       // response by returning the service implementation obj from launchpad

    // Service Registry Commands/Messages
    public static final String MSG_SR_INPUT_CMD             = "MSG_SR_INPUT_CMD";                   // process the service registry inputs from other components
    public static final String MSG_SR_OUTPUT_CMD            = "MSG_SR_OUTPUT_CMD";

    // Service Registry Messages: processed by service registry as method invocations
    public static final String MSG_SR_INITIALIZE            = "MSG_SR_INITIALIZE";                  // register services after launchpad has started
    public static final String MSG_SR_REGISTER_SERVICE      = "MSG_SR_REGISTER_SERVICE";            // register a service once it is updated by launchpad
    public static final String MSG_SR_GET_SERVICE_BY_KEY    = "MSG_SR_GET_SERVICE_BY_KEY";          // retrieve the service pair from the registry by service key
    public static final String MSG_SR_GET_SERVICE_BY_POJO   = "MSG_SR_GET_SERVICE_BY_POJO";         // retrieve the service pair from the registry by service POJO

    // Service Registry Messages: return object after service registry method invocations
    public static final String MSG_SR_RESP_GET_SERVICE_BY_KEY   = "MSG_SR_RESP_GET_SERVICE_BY_KEY";     // response after the service pair has been found in registry
    public static final String MSG_SR_RESP_GET_SERVICE_BY_POJO  = "MSG_SR_RESP_GET_SERVICE_BY_POJO";    // response after the service pair has been found in registry

    // OSGi Service Messages
    public static final String MSG_OSGI_SERVICE_DEPLOYED = "MSG_OSGI_SERVICE_DEPLOYED";
}
