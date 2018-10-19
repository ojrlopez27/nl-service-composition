package edu.cmu.inmind.services.muf.outputs;

import edu.cmu.inmind.osgi.commons.core.BundleApiInfo;
import edu.cmu.inmind.osgi.commons.core.BundleImplInfo;
import edu.cmu.inmind.osgi.commons.utils.Pair;
import edu.cmu.inmind.services.muf.commons.Command;

import static edu.cmu.inmind.services.muf.commons.Constants.MSG_SR_RESP_GET_SERVICE_BY_KEY;
import static edu.cmu.inmind.services.muf.commons.Constants.MSG_SR_RESP_GET_SERVICE_BY_POJO;

public class ServiceRegistryOutput extends Command {

    private Pair<BundleImplInfo, BundleApiInfo> servicePair;

    public ServiceRegistryOutput(VanillaBuilder vanillaBuilder) {
        super(vanillaBuilder.command);
    }

    public ServiceRegistryOutput(GetServicePairRespBuilder getServicePairRespBuilder) {
        super(getServicePairRespBuilder.command);
        this.servicePair = getServicePairRespBuilder.servicePair;
    }

    public Pair<BundleImplInfo, BundleApiInfo> getServicePair() {
        validateIfAnyCommand(new String[]{MSG_SR_RESP_GET_SERVICE_BY_KEY, MSG_SR_RESP_GET_SERVICE_BY_POJO});
        return servicePair;
    }

    public static class VanillaBuilder {
        private String command;

        public VanillaBuilder(String command) {
            this.command = command;
        }

        public ServiceRegistryOutput build() {
            return new ServiceRegistryOutput(this);
        }
    }

    public static class GetServicePairRespBuilder {
        private String command;
        private Pair<BundleImplInfo, BundleApiInfo> servicePair;

        public GetServicePairRespBuilder(String command) {
            this.command = command;
        }

        public GetServicePairRespBuilder setServicePair(Pair<BundleImplInfo, BundleApiInfo> servicePair) {
            this.servicePair = servicePair;
            return this;
        }

        public ServiceRegistryOutput build() {
            return new ServiceRegistryOutput(this);
        }
    }
}
