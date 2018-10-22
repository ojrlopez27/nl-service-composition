package edu.cmu.inmind.services.apis;

import edu.cmu.inmind.osgi.commons.markers.BundleAPI;
import edu.cmu.inmind.services.markers.ArgDesc;
import edu.cmu.inmind.services.markers.Description;
import edu.cmu.inmind.services.pojos.GroundTransportationPOJO;
import edu.cmu.inmind.services.pojos.LocationPOJO;
import java.util.Date;

import static edu.cmu.inmind.services.apis.GroundTransportationService.SERVICE;

/**
 * Created by oscarr on 8/17/18.
 */
@BundleAPI(id = SERVICE)
public interface GroundTransportationService extends GenericService {

    /** this is the service id **/
    String SERVICE = "GroundTransportationService";

    @Description(capabilities = {
            "This method allows to check availability of ground transportation from a pickup location to a destination location",
            "This method allows to look up availability of road transportation from a pickup location to a destination location",
            "This method checks available road transportation options near by"
    })
    @ArgDesc(args = {
            "pickup location : what is your pickup location?",
            "destination : what is your destination?",
            "type of location : what is the popular landmark? (airport, busstop)",
            "type of transportation:what type of transportation? (cab, bus, subway, train, metro)?",
            "date : when are you leaving (yyyy-mm-dd)?",
            "time : what time are you starting at (hh:mm)"
    })
    // TODO: add @Feature annotation
    GroundTransportationService checkTransportation(LocationPOJO pickupLocation, LocationPOJO dropoffLocation,
                                                    String locationLandmark, String transportationType,
                                                    Date date, String time);

    @Description(capabilities = {
            "This method allows to book ground transportation option from a pickup location to a destination location",
            "This method allows to schedule a transportation option from a pickup location to a destination location",
            "This method books ground transportation option"
    })
    @ArgDesc(args = {
            "pickup location : what is your pickup location?",
            "destination : what is your destination?",
            "type of transportation:what type of transportation? (cab, bus, subway, train, metro)?",
            "date : when are you leaving (yyyy-mm-dd)?",
            "time : what time are you starting at (hh:mm)"
    })
    // TODO: add @Feature annotation
    GroundTransportationPOJO scheduleTransportation(LocationPOJO pickupLocation, LocationPOJO dropoffLocation,
                                                    String transportationType, Date date, String time);
}
