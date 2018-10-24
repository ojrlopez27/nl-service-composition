package edu.cmu.inmind.demo.apis;

import edu.cmu.inmind.demo.markers.ArgDesc;
import edu.cmu.inmind.demo.markers.Description;
import edu.cmu.inmind.demo.pojos.FoodPOJO;
import edu.cmu.inmind.demo.pojos.LocationPOJO;

import java.util.Date;

public interface OrderFoodService extends GenericService{

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
    FoodPOJO orderFood(String cuisine, LocationPOJO locationPOJO, String items,
                       int priceRange, String dietaryRestrictions,
                       String typeOfService, Date date, String time);
}
