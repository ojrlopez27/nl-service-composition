package edu.cmu.inmind.composition.aserv;

import edu.cmu.inmind.multiuser.controller.log.Log4J;

public class AExpediaPriceService {

    /**
     * This method allows to get the price of the hotel from Expedia.com 
     * given the name of the hotel.
     * 
     * @param hotelName the name of the hotel for which price is sought
     * 
     * @return the price of the specified hotel
     */
    public Long hotelPrice(String hotelName) {
        Log4J.warn(this, String.format("Executing AExpediaPriceService.hotelPrice for: "
        		+ "[hotelName: %s]", hotelName));
        return null;
    }
    
    
}
