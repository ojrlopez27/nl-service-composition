package edu.cmu.inmind.messenger.comm;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/*import edu.cmu.inmind.multiuser.controller.common.CommonUtils;
import edu.cmu.inmind.services.muf.data.OSGiService;*/

/**
 * Created by sakoju on 10/12/18.
 */

public class OSGiServices {
    /*
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
    }*/

}
