package edu.cmu.inmind.composition.services;

import edu.cmu.inmind.composition.annotations.BatteryQoS;
import edu.cmu.inmind.composition.annotations.ConnectivityQoS;
import edu.cmu.inmind.composition.apis.CalendarService;
import edu.cmu.inmind.composition.common.Constants;
import edu.cmu.inmind.composition.pojos.CalendarPOJO;
import edu.cmu.inmind.multiuser.controller.log.Log4J;

import java.util.Date;

/**
 * Created by oscarr on 8/8/18.
 */
public class YahooCalendarService implements CalendarService{

    @Override
    @BatteryQoS( minBatteryLevel = Constants.REQUIRES_FULLY_CHARGED)
    @ConnectivityQoS( wifiStatus = Constants.REQUIRES_WIFI_CONNECTIVITY)
    public Boolean checkAvailability(Date from, Date to){
        Log4J.warn(this, String.format("Executing YahooCalendarService.checkAvailability for: [from date: %s, " +
                "to date: %s]", from, to));
        return true;
    }

    @Override
    @BatteryQoS( minBatteryLevel = Constants.REQUIRES_FULLY_CHARGED)
    @ConnectivityQoS( wifiStatus = Constants.REQUIRES_WIFI_CONNECTIVITY)
    public CalendarPOJO createEvent(String eventName, Date from, Date to){
        Log4J.warn(this, String.format("Executing YahooCalendarService.createEvent for: [eventName: %s. " +
                "from date: %s, to date: %s]", eventName, from, to));
        return null;
    }

    @Override
    public void execute() {
        Log4J.warn(this, "Executing YahooCalendarService...");
    }
}
