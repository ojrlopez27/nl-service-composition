package edu.cmu.inmind.services.apis;

import edu.cmu.inmind.osgi.commons.markers.BundleAPI;
import edu.cmu.inmind.services.commons.GenericService;

import static edu.cmu.inmind.services.apis.UndefinedService.SERVICE;

/**
 * Created by oscarr on 8/17/18.
 */
@BundleAPI(id = SERVICE)
public interface UndefinedService extends GenericService {

    /** this is the service id **/
    String SERVICE = "UndefinedService";

    // TODO: add @Description annotation
    // TODO: add @ArgDesc annotation
    // TODO: add @Feature annotation
    void undefinedOperation();
}
