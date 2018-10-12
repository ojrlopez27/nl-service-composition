package edu.cmu.inmind.services.muf.inputs;

import edu.cmu.inmind.services.muf.data.OSGiService;
import org.osgi.framework.ServiceReference;

import static edu.cmu.inmind.services.muf.commons.Constants.MSG_LP_GET_SERVICE_IMPL;
import static edu.cmu.inmind.services.muf.commons.Constants.MSG_LP_START_SERVICE;

public class LaunchpadInput extends CommandInput {
    private OSGiService osGiService;
    private ServiceReference serviceRef;

    private LaunchpadInput(VanillaBuilder vanillaBuilder) {
        super(vanillaBuilder.command);
    }

    private LaunchpadInput(StartServiceBuilder startServiceBuilder) {
        super(startServiceBuilder.command);
        this.osGiService = startServiceBuilder.osGiService;
    }

    private LaunchpadInput(GetServiceImplementationBuilder getServiceImplementationBuilder) {
        super(getServiceImplementationBuilder.command);
        this.serviceRef = getServiceImplementationBuilder.serviceRef;
    }

    public OSGiService getOSGiService() {
        validateCommand(MSG_LP_START_SERVICE);
        return osGiService;
    }

    public ServiceReference getServiceReference() {
        validateCommand(MSG_LP_GET_SERVICE_IMPL);
        return serviceRef;
    }

    @Override
    public String toString() {
        return "LaunchpadInput{" +
                "command='" + this.getCommand() + '\'' +
                ", " + toStringHelper() +
                '}';
    }

    private String toStringHelper() {
        switch (command) {
            case MSG_LP_START_SERVICE:
                return "osGiService=" + osGiService;
            case MSG_LP_GET_SERVICE_IMPL:
                return "serviceRef=" + serviceRef;
        }
        return "";
    }

    public static class VanillaBuilder {
        private String command;

        public VanillaBuilder(String command) {
            this.command = command;
        }

        public LaunchpadInput build() {
            return new LaunchpadInput(this);
        }
    }

    public static class StartServiceBuilder {
        private String command;
        private OSGiService osGiService;

        public StartServiceBuilder(String command) {
            this.command = command;
        }

        public StartServiceBuilder setOSGiService(OSGiService osGiService) {
            this.osGiService = osGiService;
            return this;
        }

        public LaunchpadInput build() {
            return new LaunchpadInput(this);
        }
    }

    public static class GetServiceImplementationBuilder {
        private String command;
        private ServiceReference serviceRef;

        public GetServiceImplementationBuilder(String command) {
            this.command = command;
        }

        public GetServiceImplementationBuilder setServiceRef(ServiceReference serviceRef) {
            this.serviceRef = serviceRef;
            return this;
        }

        public LaunchpadInput build() {
            return new LaunchpadInput(this);
        }
    }
}
