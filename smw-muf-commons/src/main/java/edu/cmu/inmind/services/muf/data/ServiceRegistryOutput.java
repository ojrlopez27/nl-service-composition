package edu.cmu.inmind.services.muf.data;

import edu.cmu.inmind.osgi.commons.core.BundleApiInfo;
import edu.cmu.inmind.osgi.commons.core.BundleImplInfo;
import edu.cmu.inmind.osgi.commons.utils.Pair;

public class ServiceRegistryOutput {

    Pair<BundleImplInfo, BundleApiInfo> servicePair;

    public ServiceRegistryOutput(Pair<BundleImplInfo, BundleApiInfo> servicePair) {
        this.servicePair = servicePair;
    }

    public Pair<BundleImplInfo, BundleApiInfo> getServicePair() {
        return servicePair;
    }
}
