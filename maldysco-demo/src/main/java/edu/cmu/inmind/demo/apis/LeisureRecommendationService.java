package edu.cmu.inmind.demo.apis;

import edu.cmu.inmind.demo.markers.ArgDesc;
import edu.cmu.inmind.demo.markers.Description;
import edu.cmu.inmind.demo.pojos.LocationPOJO;
import edu.cmu.inmind.demo.pojos.ThingToDoPOJO;
import edu.cmu.inmind.demo.pojos.WeatherPOJO;

import java.util.Date;
import java.util.List;

/**
 * Created by oscarr on 8/8/18.
 */
public interface LeisureRecommendationService extends GenericService {

    @Description(capabilities = {
            "This method returns a list of activities to do in a given city or place, date and weather conditions",
            "This method returns a list of indoor and outdoor activities to do in a new place",
            "It recommends things to do or visit in a city or place"
    })
    @ArgDesc(args = {
            "place : which place do you want me to show you activities to do?",
            "when : when are you planning to do these activities?",
            "weatherConditions : which weather conditions do you expect (good, bad)?"
    })
    List<ThingToDoPOJO> whatToDo(LocationPOJO place, Date when, WeatherPOJO weatherConditions);
}
