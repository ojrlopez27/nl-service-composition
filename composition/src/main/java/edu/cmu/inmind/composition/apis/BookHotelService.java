package edu.cmu.inmind.composition.apis;

import edu.cmu.inmind.composition.annotations.Description;
import edu.cmu.inmind.composition.pojos.HotelPOJO;
import edu.cmu.inmind.composition.pojos.LocationPOJO;

import java.util.Date;

/**
 * Created by oscarr on 8/8/18.
 */
public interface BookHotelService extends GenericService {

    @Description(capabilities = {
            "This method searches different hotel alternatives based on a destination place, check-in date, and check-out date",
            "This method searches for hotels on a given destination and range of dates",
            "This method looks for hotels on a given destination and range of dates"
    })
    HotelPOJO searchHotel(LocationPOJO destination, Date checkin, Date checkout, Double maxPrice);

    @Description(capabilities = {
            "This method allows hotel reservation given a destination place, check-in date, and check-out date",
            "This method allows to book a hotel given a destination place and a range of dates",
            "This method makes a hotel reservation given a destination place and a range of dates"
    })
    HotelPOJO bookHotel(LocationPOJO destination, Date checkin, Date checkout);
}
