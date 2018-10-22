package edu.cmu.inmind.services.apis;

import edu.cmu.inmind.osgi.commons.markers.BundleAPI;
import edu.cmu.inmind.services.markers.ArgDesc;
import edu.cmu.inmind.services.markers.Description;
import edu.cmu.inmind.services.pojos.FloristPOJO;
import edu.cmu.inmind.services.pojos.LocationPOJO;
import java.util.Date;

import static edu.cmu.inmind.services.apis.FloristService.SERVICE;

/**
 * Created by oscarr on 8/17/18.
 */
@BundleAPI(id = SERVICE)
public interface FloristService extends GenericService{

    /** this is the service id **/
    String SERVICE = "FloristService";

    @Description(capabilities = {
            "This method allows to order type of flowers from a florist to deliver or pickup",
            "This method allows to get flowers given a destination place, florist details",
            "This method allows to order flowers from a florist"
    })
    @ArgDesc(args = {
            "destination : what is your destination?",
            "flower type : what type of flowers(orchids, roses)",
            "service type: type of service(pickup, delivery)?",
            "date : when are you leaving (yyyy-mm-dd)?",
            "time : what time are you starting at (hh:mm)"
    })
    // TODO: add @Feature annotation
    FloristPOJO orderFlowers(LocationPOJO locationPOJO,
                             FloristPOJO floristPOJO, Date date, String time);

    @Description(capabilities = {
            "This method allows to check availability of florists, type of flowers at a destination location",
            "This method allows to look up florists given a destination location",
            "This method checks available florists near by"
    })
    @ArgDesc(args = {
            "destination : what is your destination?",
            "flower type : what type of flowers(orchids, roses)",
            "service type: type of service(pickup, delivery)?",
            "date : when are you leaving (yyyy-mm-dd)?",
            "time : what time are you starting at (hh:mm)"
    })
    // TODO: add @Feature annotation
    FloristPOJO[] checkAvailableFlorists(LocationPOJO locationPOJO,
                                         FloristPOJO floristPOJO, Date date, String time);
}
