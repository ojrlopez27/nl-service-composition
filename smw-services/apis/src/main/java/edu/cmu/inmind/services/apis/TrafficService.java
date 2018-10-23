package edu.cmu.inmind.services.apis;

import edu.cmu.inmind.osgi.commons.markers.BundleAPI;
import edu.cmu.inmind.services.commons.GenericService;
import edu.cmu.inmind.services.markers.ArgDesc;
import edu.cmu.inmind.services.markers.Description;
import edu.cmu.inmind.services.pojos.LocationPOJO;
import java.util.Date;

import static edu.cmu.inmind.services.apis.TrafficService.SERVICE;

/**
 * Created by oscarr on 8/17/18.
 */
@BundleAPI(id = SERVICE)
public interface TrafficService extends GenericService {

    /** this is the service id **/
    String SERVICE = "TrafficService";

    @Description(capabilities = {
            "This method allows check traffic from source location to destination location",
            "This method allows to get traffic given a destination place, source",
            "This method gives a traffic estimation"
    })
    @ArgDesc(args = {
            "from : where are you travelling from?",
            "destination : what is your destination?",
            "date : when are you leaving (yyyy-mm-dd)?",
            "time : what time are you starting at (hh:mm)"
    })
    // TODO: add @Feature annotation
    void checkTracffic(LocationPOJO sourceLocationPojo, LocationPOJO destinationLocationPojo,
                       Date date, String timeFormat);

    @Description(capabilities = {
            "This method allows to get shortest route given a source place, destination place, departure date, and return date",
            "This method allows to get shortest route by road given a source place, destination place, departure date, and time",
            "This method gets a shortest route with optimal traffic conditions",
    })
    @ArgDesc(args = {
            "source : where are you starting from?",
            "destination : what is your destination?",
            "date : when are you leaving (yyyy-mm-dd)?",
            "time : what time are you starting at (hh:mm)"
    })
    // TODO: add @Feature annotation
    void calculateShortestRoute(LocationPOJO sourceLocationPojo,
                                LocationPOJO destinationLocationPojo, Date date, String time);
}
