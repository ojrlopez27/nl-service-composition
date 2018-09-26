package edu.cmu.experiments.services;

import java.util.Date;

import edu.cmu.experiments.apis.WeatherService;
import edu.cmu.inmind.multiuser.controller.log.Log4J;

/**
 * Created by oscarr on 8/8/18.
 */
public class WundergroundService implements WeatherService {

    @Override
    public String checkWeatherConditions(String place, Date from, Date to){
        Log4J.warn(this, String.format("Executing WundergroundService.checkWeatherConditions for: [place: %s, " +
                "from: %s, to: %s]", place, from, to));
        return null;
    }
}
