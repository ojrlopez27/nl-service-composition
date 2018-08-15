package edu.cmu.inmind.composition.services;

import edu.cmu.inmind.composition.annotations.BatteryQoS;
import edu.cmu.inmind.composition.annotations.ConnectivityQoS;
import edu.cmu.inmind.composition.apis.BookFlightService;
import edu.cmu.inmind.composition.common.Constants;
import edu.cmu.inmind.composition.pojos.FlightPOJO;
import edu.cmu.inmind.composition.pojos.LocationPOJO;

import java.util.Date;

/**
 * Created by oscarr on 8/8/18.
 */
public class SkyscannerService implements BookFlightService {

    @Override
    @BatteryQoS( minBatteryLevel = Constants.REQUIRES_FULLY_CHARGED)
    @ConnectivityQoS( wifiStatus = Constants.REQUIRES_WIFI_CONNECTIVITY)
    public FlightPOJO searchFlight(LocationPOJO from, LocationPOJO destination, Date departureDate, Date returnDate, Double maxPrice){
        System.out.println(String.format("Executing SkyscannerService.searchFlight for: [from: %s, destination: %s, " +
                "departure date: %s, return date: %s, maxPrice: %s]", from.getPlace(), destination.getPlace(),
                departureDate, returnDate, maxPrice));
        return new FlightPOJO();
    }

    @Override
    @BatteryQoS( minBatteryLevel = Constants.REQUIRES_FULLY_CHARGED)
    @ConnectivityQoS( wifiStatus = Constants.REQUIRES_WIFI_CONNECTIVITY)
    public FlightPOJO bookFlight(LocationPOJO from, LocationPOJO destination, Date departureDate, Date returnDate){
        System.out.println(String.format("Executing SkyscannerService.bookFlight for: [from: %s, destination: %s, " +
                "departure date: %s, return date: %s]", from.getPlace(), destination.getPlace(), departureDate, returnDate));
        return new FlightPOJO();
    }

    @Override
    public void execute() {
        System.out.println("Executing SkyscannerService...");
    }
}
