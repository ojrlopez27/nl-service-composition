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
public class BookingsService implements BookHotelService{

    @Override
    @BatteryQoS( minBatteryLevel = Constants.WORKS_WITH_LOW_CHARGE)
    @ConnectivityQoS( wifiStatus = Constants.NOT_REQUIRES_WIFI_CONNECTIVITY)
    public HotelPOJO searchHotel(LocationPOJO destination, Date checkin, Date checkout, Double maxPrice){
        System.out.println("Executing BookingsService.searchHotel....");
        return null;
    }

    @Override
    @BatteryQoS( minBatteryLevel = Constants.WORKS_WITH_LOW_CHARGE)
    @ConnectivityQoS( wifiStatus = Constants.NOT_REQUIRES_WIFI_CONNECTIVITY)
    public HotelPOJO bookHotel(LocationPOJO destination, Date checkin, Date checkout){
        System.out.println("Executing BookingsService.bookHotel....");
        return null;
    }

    @Override
    public void execute() {
        System.out.println("Executing BookingsService...");
    }

}
