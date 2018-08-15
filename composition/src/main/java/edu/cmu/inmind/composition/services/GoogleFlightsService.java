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
public class GoogleFlightsService implements BookFlightService {

    @Override
    @BatteryQoS( minBatteryLevel = Constants.WORKS_WITH_LOW_CHARGE)
    @ConnectivityQoS( wifiStatus = Constants.NOT_REQUIRES_WIFI_CONNECTIVITY)
    public FlightPOJO searchFlight(LocationPOJO from, LocationPOJO destination, Date departureDate, Date returnDate){
        System.out.println("Executing GoogleFlightsService.searchFlight....");
        return null;
    }

    @Override
    @BatteryQoS( minBatteryLevel = Constants.WORKS_WITH_LOW_CHARGE)
    @ConnectivityQoS( wifiStatus = Constants.NOT_REQUIRES_WIFI_CONNECTIVITY)
    public FlightPOJO bookFlight(LocationPOJO from, LocationPOJO destination, Date departureDate, Date returnDate){
        System.out.println("Executing GoogleFlightsService.bookFlight....");
        return null;
    }

    @Override
    public void execute() {
        System.out.println("Executing GoogleFlightsService...");
    }
}
