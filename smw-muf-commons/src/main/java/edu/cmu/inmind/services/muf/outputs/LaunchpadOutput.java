package edu.cmu.inmind.services.muf.outputs;

import edu.cmu.inmind.services.muf.commons.Command;
import java.util.Map;
import org.osgi.framework.ServiceReference;

import static edu.cmu.inmind.services.muf.commons.Constants.MSG_LP_RESP_GET_ALL_SERVICES;
import static edu.cmu.inmind.services.muf.commons.Constants.MSG_LP_RESP_GET_SERVICE_IMPL;

public class LaunchpadOutput extends Command {
    private Map<ServiceReference, Object> allServicesMap;
    private ServiceReference serviceReference;
    private Object serviceImplObj;

    private LaunchpadOutput(LaunchpadOutput.VanillaBuilder vanillaBuilder) {
        super(vanillaBuilder.command);
    }

    public LaunchpadOutput(GetAllServicesRespBuilder getAllServicesRespBuilder) {
        super(getAllServicesRespBuilder.command);
        this.allServicesMap = getAllServicesRespBuilder.allServicesMap;
    }

    public LaunchpadOutput(GetServiceImplementationRespBuilder getServiceImplementationRespBuilder) {
        super(getServiceImplementationRespBuilder.command);
        this.serviceReference = getServiceImplementationRespBuilder.serviceReference;
        this.serviceImplObj = getServiceImplementationRespBuilder.serviceImplObj;
    }

    public Map<ServiceReference, Object> getAllServices() {
        validateCommand(MSG_LP_RESP_GET_ALL_SERVICES);
        return allServicesMap;
    }

    public ServiceReference getServiceReference() {
        validateCommand(MSG_LP_RESP_GET_SERVICE_IMPL);
        return serviceReference;
    }

    public Object getServiceImpl() {
        validateCommand(MSG_LP_RESP_GET_SERVICE_IMPL);
        return serviceImplObj;
    }

    public static class VanillaBuilder {
        private String command;

        public VanillaBuilder(String command) {
            this.command = command;
        }

        public LaunchpadOutput build() {
            return new LaunchpadOutput(this);
        }
    }

    public static class GetAllServicesRespBuilder {
        private String command;
        private Map<ServiceReference, Object> allServicesMap;

        public GetAllServicesRespBuilder(String command) {
            this.command = command;
        }

        public GetAllServicesRespBuilder setAllServices(Map<ServiceReference, Object> allServicesMap) {
            this.allServicesMap = allServicesMap;
            return this;
        }

        public LaunchpadOutput build() {
            return new LaunchpadOutput(this);
        }
    }

    public static class GetServiceImplementationRespBuilder {
        private String command;
        private ServiceReference serviceReference;
        private Object serviceImplObj;

        public GetServiceImplementationRespBuilder(String command) {
            this.command = command;
        }

        public GetServiceImplementationRespBuilder setServiceReference(ServiceReference serviceReference) {
            this.serviceReference = serviceReference;
            return this;
        }

        public GetServiceImplementationRespBuilder setServiceImpl(Object serviceImplObj) {
            this.serviceImplObj = serviceImplObj;
            return this;
        }

        public LaunchpadOutput build() {
            return new LaunchpadOutput(this);
        }
    }

}
