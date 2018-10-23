package edu.cmu.inmind.services.apis;

import edu.cmu.inmind.osgi.commons.markers.BundleAPI;
import edu.cmu.inmind.osgi.commons.markers.Feature;
import edu.cmu.inmind.services.commons.GenericService;
import edu.cmu.inmind.services.markers.ArgDesc;
import edu.cmu.inmind.services.markers.Description;
import edu.cmu.inmind.services.pojos.HotelPOJO;
import edu.cmu.inmind.services.pojos.LocationPOJO;
import java.util.Date;

import static edu.cmu.inmind.services.apis.BookHotelService.SERVICE;

/**
 * Created by oscarr on 8/8/18.
 */
@BundleAPI(id = SERVICE)
public interface BookHotelService extends GenericService {

    /** this is the service id **/
    String SERVICE = "BookHotelService";

    /** Constants we will need when extracting features from GenericPOJO **/
    String BHS_SEARCH_HOTEL    = "search hotel";
    String BHS_BOOK_HOTEL      = "book hotel";

    @Description(capabilities = {
            "This method searches different hotel alternatives based on a destination place, check-in date, and check-out date",
            "This method searches for hotels on a given destination and range of dates",
            "This method looks for hotels on a given destination and range of dates",
            "This method searches for accomodations"
    })
    @ArgDesc(args = {
            "destination : what is your destination?",
            "departureDate : when are you checking-in (yyyy-mm-dd)?",
            "returnDate : when are you checking-out (yyyy-mm-dd)?",
            "maxPrice : what is the maximum price p/night you are willing to pay?"
    })
    @Feature(id = BHS_SEARCH_HOTEL,
            description = "This method searches for hotels",
            keywords = {"search hotel", "find a hotel", "which hotel"})
    HotelPOJO searchHotel(LocationPOJO destination, Date checkin, Date checkout, Double maxPrice);

    @Description(capabilities = {
            "This method allows hotel reservation given a destination place, check-in date, and check-out date",
            "This method allows to book a hotel given a destination place and a range of dates",
            "This method makes a hotel reservation given a destination place and a range of dates",
            "This method book an accomodation"
    })
    @ArgDesc(args = {
            "destination : what is your destination?",
            "departureDate : when are you checking-in (yyyy-mm-dd)?",
            "returnDate : when are you checking-out (yyyy-mm-dd)?"
    })
    @Feature(id = BHS_SEARCH_HOTEL,
            description = "This method reserves a hotel",
            keywords = {"book hotel", "reserve hotel"})
    HotelPOJO bookHotel(LocationPOJO destination, Date checkin, Date checkout);
}
