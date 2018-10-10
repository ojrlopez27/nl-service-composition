package edu.cmu.inmind.services.muf.components.middleware;

import edu.cmu.inmind.framework.middleware.data.generic.GenericPOJO;
import edu.cmu.inmind.osgi.commons.core.BundleApiInfo;
import edu.cmu.inmind.osgi.commons.core.BundleImplInfo;
import edu.cmu.inmind.services.muf.components.middleware.commons.MiddlewareException;
import edu.cmu.inmind.services.muf.components.middleware.commons.Utils;

import static edu.cmu.inmind.services.muf.components.middleware.commons.ErrorMessages.FAILURE_SERVICE_KEY_GENERATION;

/**
 * Created by adangi.
 */
public class ServiceKeyGenerator {

    public static String generate(GenericPOJO pojo) {
        return generate(pojo.getServiceId(),
                pojo.getAlias(),
                pojo.getMethodName());
    }

    public static String generate(BundleApiInfo bundleApiInfo, BundleImplInfo bundleImplInfo) {
        return generate(bundleImplInfo.getId(),
                bundleImplInfo.getAlias(),
                bundleApiInfo.getMethod().getName());
    }

    private static String generate(String serviceId, String alias, String methodName) {
        try {
            return Utils.generateMapKey(serviceId, alias, methodName);
        }
        catch (Exception e) {
            throw new MiddlewareException(FAILURE_SERVICE_KEY_GENERATION, serviceId);
        }
    }
}
