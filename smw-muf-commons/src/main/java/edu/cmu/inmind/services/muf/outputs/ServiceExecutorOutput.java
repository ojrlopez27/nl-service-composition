package edu.cmu.inmind.services.muf.outputs;

import edu.cmu.inmind.services.commons.GenericPOJO;
import edu.cmu.inmind.services.muf.commons.Command;

import static edu.cmu.inmind.services.muf.commons.Constants.MSG_SE_RESP_EXECUTE_OSGI_SERVICE;

public class ServiceExecutorOutput extends Command {
    private GenericPOJO resultPOJO;

    private ServiceExecutorOutput(VanillaBuilder vanillaBuilder) {
        super(vanillaBuilder.command);
    }

    private ServiceExecutorOutput(ExecuteOSGIServiceRespBuilder executeOSGIServiceRespBuilder) {
        super(executeOSGIServiceRespBuilder.command);
        this.resultPOJO = executeOSGIServiceRespBuilder.resultPOJO;
    }

    public GenericPOJO getResultPOJO() {
        validateCommand(MSG_SE_RESP_EXECUTE_OSGI_SERVICE);
        return resultPOJO;
    }

    @Override
    public String toString() {
        return "ServiceExecutorOutput{" +
                "command='" + this.getCommand() + '\'' +
                ", " + toStringHelper() +
                '}';
    }

    private String toStringHelper() {
        switch (command) {
            case MSG_SE_RESP_EXECUTE_OSGI_SERVICE:
                return "resultPOJO=" + resultPOJO;
        }
        return "";
    }

    public static class VanillaBuilder {
        private String command;

        public VanillaBuilder(String command) {
            this.command = command;
        }

        public ServiceExecutorOutput build() {
            return new ServiceExecutorOutput(this);
        }
    }

    public static class ExecuteOSGIServiceRespBuilder {
        private String command;
        private GenericPOJO resultPOJO;

        public ExecuteOSGIServiceRespBuilder(String command) {
            this.command = command;
        }

        public ExecuteOSGIServiceRespBuilder setResult(GenericPOJO resultPOJO) {
            this.resultPOJO = resultPOJO;
            return this;
        }

        public ServiceExecutorOutput build() {
            return new ServiceExecutorOutput(this);
        }
    }
}

