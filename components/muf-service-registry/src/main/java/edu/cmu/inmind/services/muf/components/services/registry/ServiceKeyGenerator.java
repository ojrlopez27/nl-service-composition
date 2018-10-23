package edu.cmu.inmind.services.muf.components.services.registry;

import edu.cmu.inmind.osgi.commons.core.BundleApiInfo;
import edu.cmu.inmind.osgi.commons.core.BundleImplInfo;
import edu.cmu.inmind.services.commons.GenericPOJO;

/**
 * Created by adangi.
 */
public class ServiceKeyGenerator {

    private static final String PARAMETER_CANNOT_BE_NULL     = "The parameter %s cannot be null or empty.";
    private static final String FAILURE_SERVICE_KEY_GENERATION = "Failed to generate service key for service: %s";
    private static final String TOKEN = ":";

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
            return generateMapKey(serviceId, alias, methodName);
        }
        catch (Exception e) {
            throw new ServiceRegistryException(FAILURE_SERVICE_KEY_GENERATION, serviceId);
        }
    }

    /**
     * this method generates a key that will be used to map services (bundles)
     * using the service name, the method name and the type (alias)
     * @return
     */
    private static String generateMapKey(String bundleId, String bundleAlias, String methodName) throws Exception {
        if( bundleId == null || bundleId.isEmpty() )
            throw new ServiceRegistryException(PARAMETER_CANNOT_BE_NULL, "serviceName");
        if( methodName == null || methodName.isEmpty() )
            throw new ServiceRegistryException(PARAMETER_CANNOT_BE_NULL, "methodName");
        if( bundleAlias == null || bundleAlias.isEmpty() )
            throw new ServiceRegistryException(PARAMETER_CANNOT_BE_NULL, "alias");
        return bundleId + TOKEN + bundleAlias + TOKEN + methodName;
    }
}
