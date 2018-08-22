package edu.cmu.inmind.composition.aserv;

import java.util.Date;

import edu.cmu.inmind.multiuser.controller.log.Log4J;

public class ABookingsService {

	/**
	 * This method searches for hotels on a given destination and range of dates
	 * 
	 * @param destination the location of the hotel
	 * @param checkin the check-in date of the hotel reservation
	 * @param checkout the check-out date of the hotel reservation
	 * @param maxPrice the maximum price the user is willing to pay
	 * 
	 * @return return the hotel found
	 */
    public String searchHotel(String destination, Date checkin, Date checkout, Double maxPrice){
        Log4J.warn(this, String.format("Executing ABookingsService.searchHotel for: [destination: %s, " +
                "checkin: %s, checkout: %s, maxPrice: %s]", destination, checkin, checkout, maxPrice));
        return null;
    }
    
    /**
     * This method searches for hotels provided we have the hotel identifier.
     * 
     * @param hotelID the unique hotel identifier
     * @return return the hotel found
     */
    public String searchHotelByID(String hotelID) {
        Log4J.warn(this, String.format("Executing ABookingsService.searchHotelByID for: "
        		+ "[hotelID: %s ]", hotelID));
        return null;
    }
    
    /**
     * This method allows to book a hotel given a destination place and a range of dates.
     * 
     * @param destination the location of the hotel
     * @param checkin the check-in date of the hotel reservation
     * @param checkout the check-out date of the hotel reservation
     * 
     * @return the name of the hotel booked
     */
    public String bookHotel(String destination, Date checkin, Date checkout){
        Log4J.warn(this, String.format("Executing ABookingsService.bookHotel for: [destination: %s, " +
                "checkin: %s, checkout: %s]", destination, checkin, checkout));
        return null;
    }

}
