package edu.cmu.inmind.services.apis;

import edu.cmu.inmind.osgi.commons.markers.BundleAPI;
import edu.cmu.inmind.services.markers.ArgDesc;
import edu.cmu.inmind.services.markers.Description;
import edu.cmu.inmind.services.pojos.LocationPOJO;
import edu.cmu.inmind.services.pojos.MapsPOJO;
import java.util.Date;

import static edu.cmu.inmind.services.apis.MapsService.SERVICE;

/**
 * Created by oscarr on 8/17/18.
 */
@BundleAPI(id = SERVICE)
public interface MapsService extends GenericService {

    /** this is the service id **/
    String SERVICE = "MapsService";

    @Description(capabilities = {
            "This method allows to calculate distance from one location to destination location",
            "This method allows to estimate a distance from one location to destination location",
            "This method gets a distance between two locations",
            "This method calculates distance between two locations"
    })
    @ArgDesc(args = {
            "from : what is your from location?",
            "to : what is your to location?",
            "mode of travel : what mode of travel(by walk, by car, by bus, by subway, by drive)",
            "date : when are you leaving (yyyy-mm-dd)?",
            "time : what time are you starting at (hh:mm)"
    })
    // TODO: add @Feature annotation
    MapsPOJO calculateDistance(LocationPOJO from, LocationPOJO to,
                               String modeOfTravel, Date date, String time);

    @Description(capabilities = {
            "This method allows to get directions from one location to destination location",
            "This method allows to look up directions from one location to destination location",
            "This method gets directions between two locations, given a mode of transport",
    })
    @ArgDesc(args = {
            "from : what is your from location?",
            "to : what is your to location?",
            "mode of travel : what mode of travel(by walk, by car, by bus, by subway, by drive)",
            "date : when are you leaving (yyyy-mm-dd)?",
            "time : what time are you starting at (hh:mm)"
    })
    // TODO: add @Feature annotation
    MapsPOJO getDirections(LocationPOJO from, LocationPOJO to,
                           String modeOfTravel, Date date, String time);
}
