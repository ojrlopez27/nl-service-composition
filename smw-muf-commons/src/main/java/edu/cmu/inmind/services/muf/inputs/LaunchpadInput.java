package edu.cmu.inmind.services.muf.inputs;

import edu.cmu.inmind.services.muf.commons.Command;
import edu.cmu.inmind.services.muf.data.OSGiService;
import java.util.ArrayList;

import static edu.cmu.inmind.services.muf.commons.Constants.MSG_LP_GET_SERVICE_IMPL;
import static edu.cmu.inmind.services.muf.commons.Constants.MSG_LP_START_SERVICE;

public class LaunchpadInput extends Command {
    private OSGiService osGiService;

    private LaunchpadInput(VanillaBuilder vanillaBuilder) {
        super(vanillaBuilder.command);
    }

    private LaunchpadInput(StartServiceBuilder startServiceBuilder) {
        super(startServiceBuilder.command);
        this.osGiService = startServiceBuilder.osGiService;
    }

    private LaunchpadInput(GetServiceImplementationBuilder getServiceImplementationBuilder) {
        super(getServiceImplementationBuilder.command);
        this.osGiService = getServiceImplementationBuilder.osGiService;
    }

    public OSGiService getOSGiService() {
        validateIfAnyCommand(new String[]{MSG_LP_START_SERVICE, MSG_LP_GET_SERVICE_IMPL});
        return osGiService;
    }

    public OSGiService getServiceReference() {
        validateCommand(MSG_LP_GET_SERVICE_IMPL);
        return osGiService;
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
            case MSG_LP_GET_SERVICE_IMPL:
                return "osGiService=" + osGiService;
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
        private OSGiService osGiService;

        public GetServiceImplementationBuilder(String command) {
            this.command = command;
        }

        public GetServiceImplementationBuilder setOSGiService(OSGiService osGiService) {
            this.osGiService = osGiService;
            return this;
        }

        public LaunchpadInput build() {
            return new LaunchpadInput(this);
        }
    }
}
