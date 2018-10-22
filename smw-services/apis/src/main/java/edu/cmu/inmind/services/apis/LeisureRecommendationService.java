package edu.cmu.inmind.services.apis;

import edu.cmu.inmind.osgi.commons.markers.BundleAPI;
import edu.cmu.inmind.services.markers.ArgDesc;
import edu.cmu.inmind.services.markers.Description;
import edu.cmu.inmind.services.pojos.LocationPOJO;
import edu.cmu.inmind.services.pojos.ThingToDoPOJO;
import edu.cmu.inmind.services.pojos.WeatherPOJO;
import java.util.Date;
import java.util.List;

import static edu.cmu.inmind.services.apis.LeisureRecommendationService.SERVICE;

/**
 * Created by oscarr on 8/8/18.
 */
@BundleAPI(id = SERVICE)
public interface LeisureRecommendationService extends GenericService {

    /** this is the service id **/
    String SERVICE = "LeisureRecommendationService";

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
    // TODO: add @Feature annotation
    List<ThingToDoPOJO> whatToDo(LocationPOJO place, Date when, WeatherPOJO weatherConditions);
}
