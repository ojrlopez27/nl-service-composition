package edu.cmu.inmind.services.apis;

import edu.cmu.inmind.osgi.commons.markers.BundleAPI;
import edu.cmu.inmind.services.commons.GenericService;
import edu.cmu.inmind.services.markers.ArgDesc;
import edu.cmu.inmind.services.markers.Description;
import edu.cmu.inmind.services.pojos.FoodPOJO;
import edu.cmu.inmind.services.pojos.LocationPOJO;
import java.util.Date;

import static edu.cmu.inmind.services.apis.OrderFoodService.SERVICE;

@BundleAPI(id = SERVICE)
public interface OrderFoodService extends GenericService {

    /** this is the service id **/
    String SERVICE = "OrderFoodService";

    @Description(capabilities = {
            "This method allows to order food for deliver or pickup",
            "This method allows to get food given a restaurant or food ordering service",
            "This method orders food for delivery or pickup"
    })
    @ArgDesc(args = {
            "cuisine : what is your destination?",
            "location : what is your destination?",
            "items : what items of food selected?(pizza, coke, garlic bread)",
            "price : what kind of cost level ? ($, $$, $$$, five star, three star)",
            "dietary restrictions : what kind of restrictions ? (dairy, nuts, gluten, celiac, vegan, vegetarian) ",
            "type of service: delivery or pickup?",
            "date : when are you leaving (yyyy-mm-dd)?",
            "time : what time are you starting at (hh:mm)"
    })
    // TODO: add @Feature annotation
    FoodPOJO orderFood(String cuisine, LocationPOJO locationPOJO, String items,
                       int priceRange, String dietaryRestrictions,
                       String typeOfService, Date date, String time);
}
