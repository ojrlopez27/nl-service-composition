package edu.cmu.inmind.composition.services;

import edu.cmu.inmind.composition.annotations.BatteryQoS;
import edu.cmu.inmind.composition.annotations.ConnectivityQoS;
import edu.cmu.inmind.composition.apis.WeatherService;
import edu.cmu.inmind.composition.common.Constants;
import edu.cmu.inmind.composition.pojos.LocationPOJO;
import edu.cmu.inmind.composition.pojos.WeatherPOJO;

import java.util.Date;

/**
 * Created by oscarr on 8/8/18.
 */
public class WeatherChannelService implements WeatherService{

    @Override
    @BatteryQoS( minBatteryLevel = Constants.WORKS_WITH_LOW_CHARGE)
    @ConnectivityQoS( wifiStatus = Constants.NOT_REQUIRES_WIFI_CONNECTIVITY)
    public WeatherPOJO checkWeatherConditions(LocationPOJO place, Date from, Date to){
        System.out.println("Executing WeatherChannelService.checkWeatherConditions....");
        return null;
    }

    @Override
    public void execute() {
        System.out.println("Executing WeatherChannelService...");
    }
}
