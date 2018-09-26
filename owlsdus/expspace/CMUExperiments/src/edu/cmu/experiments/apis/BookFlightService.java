package edu.cmu.experiments.apis;

import java.util.Date;

/**
 * Created by oscarr on 8/8/18.
 */
public interface BookFlightService {

    String searchFlight(String from, String destination, Date departureDate, Date returnDate, Double maxPrice);

    String bookFlight(String from, String destination, Date departureDate, Date returnDate);
}
