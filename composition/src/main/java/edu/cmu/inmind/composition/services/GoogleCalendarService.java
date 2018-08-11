package edu.cmu.inmind.composition.services;

import edu.cmu.inmind.composition.annotations.BatteryQoS;
import edu.cmu.inmind.composition.annotations.ConnectivityQoS;
import edu.cmu.inmind.composition.apis.CalendarService;
import edu.cmu.inmind.composition.common.Constants;
import edu.cmu.inmind.composition.pojos.CalendarPOJO;

import java.util.Date;

/**
 * Created by oscarr on 8/8/18.
 */
public class GoogleCalendarService implements CalendarService{

    @Override
    @BatteryQoS( minBatteryLevel = Constants.WORKS_WITH_LOW_CHARGE)
    @ConnectivityQoS( wifiStatus = Constants.NOT_REQUIRES_WIFI_CONNECTIVITY)
    public Boolean checkAvailability(Date from, Date to){
        System.out.println("Executing GoogleCalendarService.checkAvailability....");
        return true;
    }

    @Override
    @BatteryQoS( minBatteryLevel = Constants.WORKS_WITH_LOW_CHARGE)
    @ConnectivityQoS( wifiStatus = Constants.NOT_REQUIRES_WIFI_CONNECTIVITY)
    public CalendarPOJO createEvent(String eventName, Date from, Date to){
        System.out.println("Executing GoogleCalendarService.createEvent....");
        return null;
    }

    @Override
    public void execute() {
        System.out.println("Executing GoogleCalendarService...");
    }
}
