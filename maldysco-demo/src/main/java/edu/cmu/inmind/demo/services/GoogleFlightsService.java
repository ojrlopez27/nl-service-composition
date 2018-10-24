package edu.cmu.inmind.demo.services;

import edu.cmu.inmind.demo.apis.BookFlightService;
import edu.cmu.inmind.demo.common.DemoConstants;
import edu.cmu.inmind.demo.markers.BatteryQoS;
import edu.cmu.inmind.demo.markers.ConnectivityQoS;
import edu.cmu.inmind.demo.markers.Provided;
import edu.cmu.inmind.demo.pojos.FlightPOJO;
import edu.cmu.inmind.demo.pojos.LocationPOJO;
import edu.cmu.inmind.multiuser.controller.log.Log4J;

import java.util.Date;

/**
 * Created by oscarr on 8/8/18.
 */
public class GoogleFlightsService implements BookFlightService {

    @Override
    @BatteryQoS( minBatteryLevel = DemoConstants.WORKS_WITH_LOW_CHARGE)
    @ConnectivityQoS( wifiStatus = DemoConstants.NOT_REQUIRES_WIFI_CONNECTIVITY)
    public FlightPOJO searchFlight(@Provided(value = "<Your Current Place>") LocationPOJO from, LocationPOJO destination,
                                   Date departureDate, Date returnDate, Double maxPrice){
        Log4J.warn(this, String.format("Executing GoogleFlightsService.searchFlight for: [from: %s, destination: %s, " +
                "departure date: %s, return date: %s, maxPrice: %s]", from.getPlace(), destination.getPlace(),
                departureDate, returnDate, maxPrice));
        return null;
    }

    @Override
    @BatteryQoS( minBatteryLevel = DemoConstants.WORKS_WITH_LOW_CHARGE)
    @ConnectivityQoS( wifiStatus = DemoConstants.NOT_REQUIRES_WIFI_CONNECTIVITY)
    public FlightPOJO bookFlight(@Provided(value = "<Your Current Place>") LocationPOJO from, LocationPOJO destination,
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
