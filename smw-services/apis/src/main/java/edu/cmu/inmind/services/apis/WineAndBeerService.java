package edu.cmu.inmind.services.apis;

import edu.cmu.inmind.osgi.commons.markers.BundleAPI;
import edu.cmu.inmind.services.markers.ArgDesc;
import edu.cmu.inmind.services.markers.Description;

import static edu.cmu.inmind.services.apis.WineAndBeerService.SERVICE;

/**
 * Created by oscarr on 8/17/18.
 */
@BundleAPI(id = SERVICE)
public interface WineAndBeerService extends GenericService {

    /** this is the service id **/
    String SERVICE = "WineAndBeerService";

    @Description( capabilities = {
            "This method allows to search a list of Wine and Spirit shops near to a location for a given time",
            "This method allows to lookup for Wine and spirit shops near to a location",
            "This method searches a list of Wine shops given a location, time"
    })
    @ArgDesc(args = {
            "location : which location are you looking for?",
            "date : which date are you looking for?",
            "time : which time are you looking for?"
    })
    // TODO: add @Feature annotation
    void searchWineAndSpiritsShop();

    @Description( capabilities = {
            "This method allows to search a list of beer shops near to a location",
            "This method allows to lookup for beer shops near to a location",
            "This method searches a list of beer shops near to a location"
    })
    @ArgDesc(args = {
            "location : which location are you looking for?",
            "date : which date are you looking for?",
            "time : which time are you looking for?"
    })
    // TODO: add @Feature annotation
    void searchBeerShop();
}
