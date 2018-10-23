package edu.cmu.inmind.services.muf.components.services.executor;

import edu.cmu.inmind.osgi.commons.core.BundleApiInfo;
import edu.cmu.inmind.osgi.commons.core.BundleImplInfo;
import edu.cmu.inmind.osgi.commons.utils.Pair;
import edu.cmu.inmind.services.commons.GenericPOJO;

/**
 * Created by oscarr on 2/8/18.
 */
public class ServiceExecutor {

    private static final String TAG = ServiceExecutor.class.getSimpleName();

    public ServiceExecutor() {
        // by design, do nothing, instantiation using event bus only
    }

    /**
     * This method executes a specific method from a specific bundle id + alias.
     * // @param request
     */
    public static <T> T execute(GenericPOJO servicePOJO, Pair<BundleImplInfo, BundleApiInfo> bundleServicePair) {

        try {
            if (servicePOJO == null) {
                throw new ServiceExecutorException("Invalid POJO: " + servicePOJO);
            }
            System.out.println("Service POJO: " + servicePOJO);
            System.out.println("Service Pair: " + bundleServicePair);

            Object bundleInstance = bundleServicePair.fst.getInstance();
            String bundleMethodName = bundleServicePair.snd.getMethod().getName();

            return SEUtils.executeMethod (
                    bundleInstance,
                    bundleMethodName,
                    Boolean.FALSE,
                    servicePOJO.transform().toArray()
            );

        } catch (Exception exception) {
            throw new ServiceExecutorException(exception);
        }
    }
}
