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
    public FlightPOJO searchFlight(LocationPOJO destination, Date departureDate, Date returnDate){
        System.out.println("Executing SkyscannerService.searchFlight....");
        return null;
    }

    @Override
    @BatteryQoS( minBatteryLevel = Constants.REQUIRES_FULLY_CHARGED)
    @ConnectivityQoS( wifiStatus = Constants.REQUIRES_WIFI_CONNECTIVITY)
    public FlightPOJO bookFlight(LocationPOJO destination, Date departureDate, Date returnDate){
        System.out.println("Executing SkyscannerService.bookFlight....");
        return null;
    }

    @Override
    public void execute() {
        System.out.println("Executing SkyscannerService...");
    }
}
