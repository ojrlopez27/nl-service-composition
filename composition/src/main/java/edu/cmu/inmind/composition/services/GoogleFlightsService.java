package edu.cmu.inmind.composition.services;

import edu.cmu.inmind.composition.annotations.BatteryQoS;
import edu.cmu.inmind.composition.annotations.ConnectivityQoS;
import edu.cmu.inmind.composition.annotations.Provided;
import edu.cmu.inmind.composition.apis.BookFlightService;
import edu.cmu.inmind.composition.common.Constants;
import edu.cmu.inmind.composition.pojos.FlightPOJO;
import edu.cmu.inmind.composition.pojos.LocationPOJO;
import edu.cmu.inmind.multiuser.controller.log.Log4J;

import java.util.Date;

/**
 * Created by oscarr on 8/8/18.
 */
public class GoogleFlightsService implements BookFlightService {

    @Override
    @BatteryQoS( minBatteryLevel = Constants.WORKS_WITH_LOW_CHARGE)
    @ConnectivityQoS( wifiStatus = Constants.NOT_REQUIRES_WIFI_CONNECTIVITY)
    public FlightPOJO searchFlight(@Provided(value = "Pittsburgh") LocationPOJO from, LocationPOJO destination,
                                   Date departureDate, Date returnDate, Double maxPrice){
        Log4J.warn(this, String.format("Executing GoogleFlightsService.searchFlight for: [from: %s, destination: %s, " +
                "departure date: %s, return date: %s, maxPrice: %s]", from.getPlace(), destination.getPlace(),
                departureDate, returnDate, maxPrice));
        return null;
    }

    @Override
    @BatteryQoS( minBatteryLevel = Constants.WORKS_WITH_LOW_CHARGE)
    @ConnectivityQoS( wifiStatus = Constants.NOT_REQUIRES_WIFI_CONNECTIVITY)
    public FlightPOJO bookFlight(@Provided(value = "Pittsburgh") LocationPOJO from, LocationPOJO destination,
                                 Date departureDate, Date returnDate){
        Log4J.warn(this, String.format("Executing GoogleFlightsService.bookFlight for: [from: %s, destination: %s, " +
                "departure date: %s, return date: %s]", from.getPlace(), destination.getPlace(), departureDate, returnDate));
        return null;
    }

    @Override
    public void execute() {
        Log4J.warn(this, "Executing GoogleFlightsService...");
    }
}
