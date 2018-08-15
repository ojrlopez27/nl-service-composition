package edu.cmu.inmind.composition.services;

import edu.cmu.inmind.composition.annotations.BatteryQoS;
import edu.cmu.inmind.composition.annotations.ConnectivityQoS;
import edu.cmu.inmind.composition.apis.SearchThingsToDoService;
import edu.cmu.inmind.composition.common.Constants;
import edu.cmu.inmind.composition.pojos.LocationPOJO;
import edu.cmu.inmind.composition.pojos.ThingToDoPOJO;
import edu.cmu.inmind.composition.pojos.WeatherPOJO;

import java.util.Date;
import java.util.List;

/**
 * Created by oscarr on 8/8/18.
 */
public class TripAdvisorService implements SearchThingsToDoService{

    @Override
    @BatteryQoS( minBatteryLevel = Constants.REQUIRES_FULLY_CHARGED)
    @ConnectivityQoS( wifiStatus = Constants.REQUIRES_WIFI_CONNECTIVITY)
    public List<ThingToDoPOJO> whatToDo(LocationPOJO place, Date when, WeatherPOJO weatherConditions){
        System.out.println(String.format("Executing TripAdvisorService.whatToDo for: [place: %s, " +
                "when: %s, conditions: %s]", place.getPlace(), when, weatherConditions));
        return null;
    }

    @Override
    public void execute() {
        System.out.println("Executing TripAdvisorService...");
    }
}
