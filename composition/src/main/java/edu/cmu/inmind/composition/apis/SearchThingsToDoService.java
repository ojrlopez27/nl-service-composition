package edu.cmu.inmind.composition.apis;

import edu.cmu.inmind.composition.annotations.Description;
import edu.cmu.inmind.composition.pojos.LocationPOJO;
import edu.cmu.inmind.composition.pojos.ThingToDoPOJO;
import edu.cmu.inmind.composition.pojos.WeatherPOJO;

import java.util.Date;
import java.util.List;

/**
 * Created by oscarr on 8/8/18.
 */
public interface SearchThingsToDoService extends GenericService {

    @Description(capabilities = {
            "This method returns a list of activities to do in a given city or place, date and weather conditions",
            "This method returns a list of indoor and outdoor activities to do in a new place",
            "It recommends things to do or visit in a city or place"
    })
    List<ThingToDoPOJO> whatToDo(LocationPOJO place, Date when, WeatherPOJO weatherConditions);
}
