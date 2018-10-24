package edu.cmu.inmind.demo.services;

import edu.cmu.inmind.demo.common.DemoConstants;
import edu.cmu.inmind.demo.markers.BatteryQoS;
import edu.cmu.inmind.demo.markers.ConnectivityQoS;
import edu.cmu.inmind.demo.apis.WeatherService;
import edu.cmu.inmind.demo.pojos.LocationPOJO;
import edu.cmu.inmind.demo.pojos.WeatherPOJO;
import edu.cmu.inmind.multiuser.controller.log.Log4J;

import java.util.Date;

/**
 * Created by oscarr on 8/8/18.
 */
public class WundergroundService implements WeatherService{

    @Override
    @BatteryQoS( minBatteryLevel = DemoConstants.REQUIRES_FULLY_CHARGED)
    @ConnectivityQoS( wifiStatus = DemoConstants.REQUIRES_WIFI_CONNECTIVITY)
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
