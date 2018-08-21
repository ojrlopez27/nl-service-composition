package edu.cmu.inmind.composition.apis;

import edu.cmu.inmind.composition.annotations.ArgDesc;
import edu.cmu.inmind.composition.annotations.Description;
import edu.cmu.inmind.composition.annotations.Provided;
import edu.cmu.inmind.composition.pojos.FlightPOJO;
import edu.cmu.inmind.composition.pojos.LocationPOJO;

import java.util.Date;

/**
 * Created by oscarr on 8/8/18.
 */
public interface BookFlightService extends GenericService {

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
    FlightPOJO bookFlight(LocationPOJO from, LocationPOJO destination, Date departureDate, Date returnDate);
}
