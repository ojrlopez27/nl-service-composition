package edu.cmu.inmind.services.muf.components.services.registry;

import edu.cmu.inmind.framework.middleware.data.generic.GenericPOJO;
import edu.cmu.inmind.multiuser.controller.log.Log4J;
import edu.cmu.inmind.osgi.commons.core.BundleApiInfo;
import edu.cmu.inmind.osgi.commons.core.BundleImplInfo;
import edu.cmu.inmind.osgi.commons.utils.CommonUtils;
import edu.cmu.inmind.osgi.commons.utils.Pair;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.osgi.framework.ServiceReference;

/**
 * Created by adangi.
 *
 * TODO: eventually this class will go away
 */
public class ServiceMapper {

    private static final String TAG = ServiceMapper.class.getSimpleName();
    private static Map<String, Pair<BundleImplInfo, BundleApiInfo>> serviceMap = new HashMap<>();

    private static final List<String> ignoreableServices = CommonUtils.IGNORABLE_SERVICES;

    public static void map (ServiceReference[] serviceReferences) throws Exception {
        for (ServiceReference serviceReference : serviceReferences) {

            // instead of taking service references only from launchpad and
            // then querying each implementation in the below method,
            // I guess, launchpad could provide a map of service references and service implementation objects
            // TODO: add a method for the above scenario, and then modify the below method's signature

            map(serviceReference);
        }
    }

    public static void map(ServiceReference serviceReference, Object serviceImplementation) throws Exception {

        // obtain the service name
        String serviceName = serviceReference.toString(); //serviceReference.getBundle().getSymbolicName();
        Log4J.debug(TAG,"*** Service: " + serviceName);
        String serviceBundleName = serviceReference.getBundle().getSymbolicName();

        // do not map if the service belongs to the services to be ignored
        if (CommonUtils.serviceIgnoreable(serviceBundleName, ignoreableServices)) return;

        // extract the bundle implementation info
        BundleImplInfo bundleImplInfo = BundleMethodExtractor.extract(serviceImplementation, ignoreableServices);

        // we only want to map bundle implementations, not bundle apis
        if (isInterface(bundleImplInfo)) return;

        for (List<BundleApiInfo> bundleApiInfos : bundleImplInfo.getApis().values()) {
            
            for (BundleApiInfo bundleApiInfo : bundleApiInfos) {

                //TODO: if bundleInfo.getAlias() is empty, then we should extract keywords
                //TODO: from apiInfo.getKeywords()
                serviceMap.put(ServiceKeyGenerator.generate(bundleApiInfo, bundleImplInfo),
                        new Pair<>(bundleImplInfo, bundleApiInfo));
            }
        }
    }

    public static Map<String, Pair<BundleImplInfo, BundleApiInfo>> getServiceMap() {
        return serviceMap;
    }

    public static Pair<BundleImplInfo, BundleApiInfo> getServicePair(GenericPOJO pojo) {
        return getServicePair(ServiceKeyGenerator.generate(pojo));
    }

    private static Pair<BundleImplInfo, BundleApiInfo> getServicePair(String serviceKey) {
        return serviceMap.get(serviceKey);
    }

    private static boolean isInterface(BundleImplInfo bundleImplInfo) {
        return bundleImplInfo.getClassType().isInterface();
    }

    /*
    Override
    public String toString() {
        if (serviceMap == null) {
            return "ServiceMapper [0 Services]";
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ServiceMapper [")
                .append(serviceMap.size())
                .append(" Services]")
                .append(System.lineSeparator());
        for (String serviceKey : serviceMap.keySet()) {
            stringBuilder.append("\t [Key: ")
                    .append(serviceKey)
                    .append(", Obj: ")
                    .append(serviceMap.get(serviceKey))
                    .append("]")
                    .append(System.lineSeparator());
        }
        return stringBuilder.toString();
    }
    */
}
