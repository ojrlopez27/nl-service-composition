package edu.cmu.inmind.services.apis;

import edu.cmu.inmind.osgi.commons.markers.BundleAPI;

import static edu.cmu.inmind.services.apis.TextMessageService.SERVICE;

/**
 * Created by oscarr on 8/17/18.
 */
@BundleAPI(id = SERVICE)
public interface TextMessageService extends GenericService {

    /** this is the service id **/
    String SERVICE = "TextMessageService";

    // TODO: add @Description annotation
    // TODO: add @ArgDesc annotation
    // TODO: add @Feature annotation
    void sendMessage();
}
