package edu.cmu.inmind.composition.apis;

import edu.cmu.inmind.composition.annotations.Description;
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
    FlightPOJO searchFlight(LocationPOJO destination, Date departureDate, Date returnDate);

    @Description(capabilities = {
            "This method allows flight reservation given a destination place, departure date, and return date",
            "This method allows to book a flight given a destination place, departure date, and return date",
            "This method makes a flight reservation",
    })
    FlightPOJO bookFlight(LocationPOJO destination, Date departureDate, Date returnDate);
}
