package edu.cmu.inmind.composition.services;

import edu.cmu.inmind.composition.annotations.BatteryQoS;
import edu.cmu.inmind.composition.annotations.ConnectivityQoS;
import edu.cmu.inmind.composition.apis.WeatherService;
import edu.cmu.inmind.composition.common.Constants;
import edu.cmu.inmind.composition.pojos.LocationPOJO;
import edu.cmu.inmind.composition.pojos.WeatherPOJO;
import edu.cmu.inmind.multiuser.controller.log.Log4J;

import java.util.Date;

/**
 * Created by oscarr on 8/8/18.
 */
public class WundergroundService implements WeatherService{

    @Override
    @BatteryQoS( minBatteryLevel = Constants.REQUIRES_FULLY_CHARGED)
    @ConnectivityQoS( wifiStatus = Constants.REQUIRES_WIFI_CONNECTIVITY)
    public WeatherPOJO checkWeatherConditions(LocationPOJO place, Date from, Date to){
        Log4J.warn(this, String.format("Executing WundergroundService.checkWeatherConditions for: [place: %s, " +
                "from: %s, to: %s]", place.getPlace(), from, to));
        return null;
    }

    @Override
    public void execute() {
        Log4J.warn(this, "Executing WundergroundService...");
    }
}
