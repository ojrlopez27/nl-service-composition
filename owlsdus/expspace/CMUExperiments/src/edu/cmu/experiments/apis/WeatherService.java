package edu.cmu.experiments.apis;

import java.util.Date;

/**
 * Created by oscarr on 8/8/18.
 */
public interface WeatherService {

    String checkWeatherConditions(String place, Date from, Date to);
}
