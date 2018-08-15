package edu.cmu.inmind.composition.services;

import edu.cmu.inmind.composition.annotations.BatteryQoS;
import edu.cmu.inmind.composition.annotations.ConnectivityQoS;
import edu.cmu.inmind.composition.apis.BookHotelService;
import edu.cmu.inmind.composition.common.Constants;
import edu.cmu.inmind.composition.pojos.HotelPOJO;
import edu.cmu.inmind.composition.pojos.LocationPOJO;

import java.util.Date;

/**
 * Created by oscarr on 8/8/18.
 */
public class AirBnBService implements BookHotelService{

    @Override
    @BatteryQoS( minBatteryLevel = Constants.REQUIRES_FULLY_CHARGED)
    @ConnectivityQoS( wifiStatus = Constants.REQUIRES_WIFI_CONNECTIVITY)
    public HotelPOJO searchHotel(LocationPOJO destination, Date checkin, Date checkout, Double maxPrice){
        System.out.println(String.format("Executing AirBnBService.searchHotel for: [destination: %s, " +
                "checkin: %s, checkout: %s, maxPrice: %s]", destination.getPlace(), checkin, checkout, maxPrice));
        return null;
    }

    @Override
    @BatteryQoS( minBatteryLevel = Constants.REQUIRES_FULLY_CHARGED)
    @ConnectivityQoS( wifiStatus = Constants.REQUIRES_WIFI_CONNECTIVITY)
    public HotelPOJO bookHotel(LocationPOJO destination, Date checkin, Date checkout){
        System.out.println(String.format("Executing AirBnBService.bookHotel for: [destination: %s, " +
                "checkin: %s, checkout: %s]", destination.getPlace(), checkin, checkout));
        return null;
    }

    @Override
    public void execute() {
        System.out.println("Executing AirBnBService...");
    }

}
