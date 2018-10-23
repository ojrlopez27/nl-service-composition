package edu.cmu.inmind.services.muf.inputs;

import edu.cmu.inmind.services.commons.GenericPOJO;
import edu.cmu.inmind.services.muf.commons.Command;

import static edu.cmu.inmind.services.muf.commons.Constants.MSG_SE_EXECUTE_OSGI_SERVICE;

public class ServiceExecutorInput extends Command {
    private GenericPOJO genericPOJO;

    private ServiceExecutorInput(VanillaBuilder vanillaBuilder) {
        super(vanillaBuilder.command);
    }

    private ServiceExecutorInput(ExecuteOSGIServiceBuilder executeOSGIServiceBuilder) {
        super(executeOSGIServiceBuilder.command);
        this.genericPOJO = executeOSGIServiceBuilder.genericPOJO;
    }

    public GenericPOJO getGenericPOJO() {
        validateCommand(MSG_SE_EXECUTE_OSGI_SERVICE);
        return genericPOJO;
    }

    @Override
    public String toString() {
        return "ServiceExecutorInput{" +
                "command='" + this.getCommand() + '\'' +
                ", " + toStringHelper() +
                '}';
    }

    private String toStringHelper() {
        switch (command) {
            case MSG_SE_EXECUTE_OSGI_SERVICE:
                return "genericPOJO=" + genericPOJO;
        }
        return "";
    }

    public static class VanillaBuilder {
        private String command;

        public VanillaBuilder(String command) {
            this.command = command;
        }

        public ServiceExecutorInput build() {
            return new ServiceExecutorInput(this);
        }
    }

    public static class ExecuteOSGIServiceBuilder {
        private String command;
        private GenericPOJO genericPOJO;

        public ExecuteOSGIServiceBuilder(String command) {
            this.command = command;
        }

        public ExecuteOSGIServiceBuilder setGenericPOJO(GenericPOJO genericPOJO) {
            this.genericPOJO = genericPOJO;
            return this;
        }

        public ServiceExecutorInput build() {
            return new ServiceExecutorInput(this);
        }
    }
}
