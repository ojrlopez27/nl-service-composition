package edu.cmu.experiments.services;

import java.util.Date;

import edu.cmu.experiments.apis.BookHotelService;
import edu.cmu.inmind.multiuser.controller.log.Log4J;

/**
 * Created by oscarr on 8/8/18.
 */
public class AirBnBService implements BookHotelService {

    @Override
    public String searchHotel(String destination, Date checkin, Date checkout, Double maxPrice){
        Log4J.warn(this, String.format("Executing AirBnBService.searchHotel for: [destination: %s, " +
                "checkin: %s, checkout: %s, maxPrice: %s]", destination, checkin, checkout, maxPrice));
        return null;
    }

    @Override
    public String bookHotel(String destination, Date checkin, Date checkout){
        Log4J.warn(this, String.format("Executing AirBnBService.bookHotel for: [destination: %s, " +
                "checkin: %s, checkout: %s]", destination, checkin, checkout));
        return null;
    }

}
