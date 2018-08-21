package edu.cmu.inmind.composition.apis;

import edu.cmu.inmind.composition.annotations.ArgDesc;
import edu.cmu.inmind.composition.annotations.Description;
import edu.cmu.inmind.composition.pojos.LocationPOJO;
import edu.cmu.inmind.composition.pojos.RestaurantPojo;

import java.util.Date;

/**
 * Created by oscarr on 8/17/18.
 */
public interface BookRestaurantService extends GenericService{

    @Description(capabilities = {
            "This method searches different restaurants based on a destination place, date, time, type of options",
            "This method searches for restaurants on a given destination, date, time, type of options",
            "This method looks for restaurants given a destination, date, time, type of options",
            "This method searches for restaurants with options"
    })
    @ArgDesc(args = {
            "destination : what is your destination?",
            "date : which day are you looking for (yyyy-mm-dd)?",
            "time : what time of the day (hh:mm)?",
            "type of restaurant : what type of restaurant are you looking for? (dinner, dinner with drinks, dinner with BYOB)",
            "cuisine: what type of cuisine are you looking for? (italian, french, asian, mexican, south american, spanish)",
            "price : what kind of cost level ? ($, $$, $$$, five star, three star)",
            "number of people : how many people ? (2)",
            "ambience : what kind of ambience ? (closed cabin, good view, rooftop, lake view, seaside view) ",
            "dietary restrictions : what kind of restrictions ? (dairy, nuts, gluten, celiac, vegan, vegetarian) "
    })
    RestaurantPojo[] searchRestaurant(LocationPOJO locationPOJO, Date date,String time, String restaurantType,
                          String cuisineType, String priceRange, int numOfPeople, String ambience,
                          String dietaryRestrictions);

    @Description(capabilities = {
            "This method makes a reservation at a given restaurant, date, time, number of people, dietary restrictions, special instructions",
            "This method books a reservation at a restaurant on a given address, date, time, type of options",
            "This method reserves a restaurant given a destination, date, time, type of options",
            "This method makes a reservation at a restaurant given options"
    })
    @ArgDesc(args = {
            "restaurant : what is the name of restaurant ?",
            "restaurant address : what is your restaurant address?",
            "date : which day are you looking for (yyyy-mm-dd)?",
            "time : what time of the day (hh:mm)?",
            "number of people : how many people ? (2)",
            "dietary restrictions : what kind of restrictions ? (dairy, nuts, gluten, celiac, vegan, vegetarian) ",
            "special instructions : what would you like additionally ? (music, specific wine, special decors) "
    })
    RestaurantPojo makeResearvation(String restaurantName, String address, Date date, String time,
                                    int numOfPeople, String dietaryRestrictions, String specialInstructions);
}
