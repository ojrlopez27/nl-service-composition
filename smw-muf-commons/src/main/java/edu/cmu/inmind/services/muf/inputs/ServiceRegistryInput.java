package edu.cmu.inmind.services.muf.inputs;

import edu.cmu.inmind.services.muf.commons.Command;
import edu.cmu.inmind.services.muf.data.OSGiService;

import static edu.cmu.inmind.services.muf.commons.Constants.MSG_SR_GET_SERVICE_BY_KEY;
import static edu.cmu.inmind.services.muf.commons.Constants.MSG_SR_REGISTER_SERVICE;

public class ServiceRegistryInput extends Command {
    private String serviceKey;
    // private GenericPOJO genericPOJO;
    private OSGiService osGiService;

    private ServiceRegistryInput(VanillaBuilder vanillaBuilder) {
        super(vanillaBuilder.command);
    }

    private ServiceRegistryInput(RegisterServiceBuilder registerServiceBuilder) {
        super(registerServiceBuilder.command);
        this.osGiService = registerServiceBuilder.osGiService;
    }

    private ServiceRegistryInput(GetServiceByKeyBuilder getServiceByKeyBuilder) {
        super(getServiceByKeyBuilder.command);
        this.serviceKey = getServiceByKeyBuilder.serviceKey;
    }

    /*
    private ServiceRegistryInput(GetServiceByPOJOBuilder getServiceByPOJOBuilder) {
        super(getServiceByPOJOBuilder.command);
        this.genericPOJO = getServiceByPOJOBuilder.genericPOJO;
    }
    */

    public OSGiService getOsGiService() {
        validateCommand(MSG_SR_REGISTER_SERVICE);
        return osGiService;
    }

    public String getServiceKey() {
        validateCommand(MSG_SR_GET_SERVICE_BY_KEY);
        return serviceKey;
    }

    /*
    public GenericPOJO getGenericPOJO() {
        validateCommand(MSG_SR_GET_SERVICE_BY_POJO);
        return genericPOJO;
    }
    */

    @Override
    public String toString() {
        return "ServiceRegistryInput{" +
                "command='" + this.getCommand() + '\'' +
                ", " + toStringHelper() +
                '}';
    }

    private String toStringHelper() {
        switch (command) {
            case MSG_SR_REGISTER_SERVICE:
                return "OSGiService=" + osGiService;
            case MSG_SR_GET_SERVICE_BY_KEY:
                return "serviceKey='" + serviceKey + '\'';
            /* case MSG_SR_GET_SERVICE_BY_POJO:
                return "genericPOJO=" + genericPOJO;
             */
        }
        return "";
    }

    public static class VanillaBuilder {
        private String command;

        public VanillaBuilder(String command) {
            this.command = command;
        }

        public ServiceRegistryInput build() {
            return new ServiceRegistryInput(this);
        }
    }

    public static class RegisterServiceBuilder {
        private String command;
        private OSGiService osGiService;

        public RegisterServiceBuilder(String command) {
            this.command = command;
        }

        public RegisterServiceBuilder setOSGiService(OSGiService osGiService) {
            this.osGiService = osGiService;
            return this;
        }

        public ServiceRegistryInput build() {
            return new ServiceRegistryInput(this);
        }
    }

    public static class GetServiceByKeyBuilder {
        private String command;
        private String serviceKey;

        public GetServiceByKeyBuilder(String command) {
            this.command = command;
        }

        public GetServiceByKeyBuilder setServiceKey(String serviceKey) {
            this.serviceKey = serviceKey;
            return this;
        }

        public ServiceRegistryInput build() {
            return new ServiceRegistryInput(this);
        }
    }

    /*
    public static class GetServiceByPOJOBuilder {
        private String command;
        private GenericPOJO genericPOJO;

        public GetServiceByPOJOBuilder(String command) {
            this.command = command;
        }

        public GetServiceByPOJOBuilder setGenericPOJO(GenericPOJO genericPOJO) {
            this.genericPOJO = genericPOJO;
            return this;
        }

        public ServiceRegistryInput build() {
            return new ServiceRegistryInput(this);
        }
    }
    */
}
