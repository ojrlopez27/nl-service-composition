package edu.cmu.inmind.demo.services;

import edu.cmu.inmind.demo.common.DemoConstants;
import edu.cmu.inmind.demo.markers.BatteryQoS;
import edu.cmu.inmind.demo.markers.ConnectivityQoS;
import edu.cmu.inmind.demo.pojos.ThingToDoPOJO;
import edu.cmu.inmind.demo.pojos.WeatherPOJO;
import edu.cmu.inmind.multiuser.controller.log.Log4J;
import edu.cmu.inmind.demo.pojos.LocationPOJO;
import edu.cmu.inmind.demo.apis.LeisureRecommendationService;

import java.util.Date;
import java.util.List;

/**
 * Created by oscarr on 8/8/18.
 */
public class TripAdvisorService implements LeisureRecommendationService {

    @Override
    @BatteryQoS( minBatteryLevel = DemoConstants.REQUIRES_FULLY_CHARGED)
    @ConnectivityQoS( wifiStatus = DemoConstants.REQUIRES_WIFI_CONNECTIVITY)
    public List<ThingToDoPOJO> whatToDo(LocationPOJO place, Date when, WeatherPOJO weatherConditions){
        Log4J.warn(this, String.format("Executing TripAdvisorService.whatToDo for: [place: %s, " +
                "when: %s, conditions: %s]", place.getPlace(), when, weatherConditions));
        return null;
    }

    @Override
    public void execute() {
        Log4J.warn(this, "Executing TripAdvisorService...");
    }
}
