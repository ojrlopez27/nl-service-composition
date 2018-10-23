package edu.cmu.inmind.services.apis;

import edu.cmu.inmind.osgi.commons.markers.BundleAPI;
import edu.cmu.inmind.services.commons.GenericService;
import edu.cmu.inmind.services.markers.ArgDesc;
import edu.cmu.inmind.services.markers.Description;
import edu.cmu.inmind.services.pojos.LocationPOJO;
import edu.cmu.inmind.services.pojos.WeatherPOJO;
import java.util.Date;

import static edu.cmu.inmind.services.apis.WeatherService.SERVICE;

/**
 * Created by oscarr on 8/8/18.
 */
@BundleAPI(id = SERVICE)
public interface WeatherService extends GenericService {

    /** this is the service id **/
    String SERVICE = "WeatherService";

    @Description(capabilities = {
            "This method validates the weather conditions of a specific place given a range of dates",
            "This method checks the weather conditions and forecast for a given place and range of dates",
            "This method determines good and bad weather conditions for a given place and range of dates"
    })
    @ArgDesc(args = {
            "place : which location do you want me to check the weather for?",
            "from : from date (yyyy-mm-dd)...?",
            "to : to date (yyyy-mm-dd)...?"
    })
    // TODO: add @Feature annotation
    WeatherPOJO checkWeatherConditions(LocationPOJO place, Date from, Date to);
}
