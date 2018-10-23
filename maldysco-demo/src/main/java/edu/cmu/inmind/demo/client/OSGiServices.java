package edu.cmu.inmind.demo.client;

import edu.cmu.inmind.multiuser.controller.common.CommonUtils;
import edu.cmu.inmind.services.muf.data.OSGiService;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
// Merging Ankit's changes *****
public class OSGiServices {

    private static final String SERVICES_METADATA = "services.json";

    private Map<String, OSGiService> map = new HashMap<>();
    private volatile static OSGiServices osGiServices;

    static {
        try {
            load();
        }
        catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public static void load() throws Throwable {
        osGiServices = CommonUtils.fromJsonFile(SERVICES_METADATA, OSGiServices.class);
    }

    public static Set<String> getServiceIDs() {
        return osGiServices.map.keySet();
    }

    public static OSGiService getService(String serviceId) {
        return osGiServices.map.get(serviceId);
    }

}
