package edu.cmu.inmind.services.apis;

import edu.cmu.inmind.osgi.commons.markers.BundleAPI;
import edu.cmu.inmind.osgi.commons.markers.Feature;
import edu.cmu.inmind.services.markers.ArgDesc;
import edu.cmu.inmind.services.markers.Description;
import edu.cmu.inmind.services.pojos.FlightPOJO;
import edu.cmu.inmind.services.pojos.LocationPOJO;
import java.util.Date;

import static edu.cmu.inmind.services.apis.BookFlightService.SERVICE;

/**
 * Created by oscarr on 8/8/18.
 */
@BundleAPI(id = SERVICE)
public interface BookFlightService extends GenericService {

    /** this is the service id **/
    String SERVICE = "BookFlightService";

    /** Constants we will need when extracting features from GenericPOJO **/
    String BFS_SEARCH_FLIGHT    = "search flight";
    String BFS_BOOK_FLIGHT      = "book flight";

    @Description(capabilities = {
            "This method searches different flight options based on a destination place, departure date, and return date",
            "This method searches multiple flights",
            "This method looks for different flights"
    })
    @ArgDesc(args = {
            "from : where are you flying from?",
            "destination : what is your destination?",
            "departureDate : when are you leaving (yyyy-mm-dd)?",
            "returnDate : when are you returning (yyyy-mm-dd)?",
            "what is the maximum price you are willing to pay for your flight?"
    })
    @Feature(id = BFS_SEARCH_FLIGHT,
            description = "This method searches for flights",
            keywords = {"search flight", "locate flight", "find a flight", "which flight"})
    FlightPOJO searchFlight(LocationPOJO from, LocationPOJO destination, Date departureDate, Date returnDate,
                            Double maxPrice);

    @Description(capabilities = {
            "This method allows flight reservation given a destination place, departure date, and return date",
            "This method allows to book a flight given a destination place, departure date, and return date",
            "This method makes a flight reservation",
    })
    @ArgDesc(args = {
            "from : where are you flying from?",
            "destination : what is your destination?",
            "departureDate : when are you leaving (yyyy-mm-dd)?",
            "returnDate : when are you returning (yyyy-mm-dd)?"
    })
    @Feature(id = BFS_BOOK_FLIGHT,
            description = "This method reserves a flight",
            keywords = {"book flight", "reserve flight"})
    FlightPOJO bookFlight(LocationPOJO from, LocationPOJO destination, Date departureDate, Date returnDate);
}
