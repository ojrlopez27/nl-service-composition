package edu.cmu.inmind.demo.apis;

import edu.cmu.inmind.demo.markers.ArgDesc; import edu.cmu.inmind.services.commons.GenericService;
import edu.cmu.inmind.demo.markers.Description;
import edu.cmu.inmind.demo.pojos.FloristPojo;
import edu.cmu.inmind.demo.pojos.LocationPOJO;

import java.util.Date;

/**
 * Created by oscarr on 8/17/18.
 */
public interface FloristService extends GenericService{

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
    FloristPojo orderFlowers(LocationPOJO locationPOJO,
                             FloristPojo floristPojo, Date date, String time);

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
    FloristPojo[] checkAvailableFlorists(LocationPOJO locationPOJO,
                                         FloristPojo floristPojo, Date date, String time);
}
