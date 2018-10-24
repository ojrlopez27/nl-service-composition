package edu.cmu.inmind.demo.services;

import edu.cmu.inmind.demo.common.DemoConstants;
import edu.cmu.inmind.demo.markers.BatteryQoS;
import edu.cmu.inmind.demo.markers.ConnectivityQoS;
import edu.cmu.inmind.demo.apis.CalendarService;
import edu.cmu.inmind.demo.pojos.CalendarPOJO;
import edu.cmu.inmind.multiuser.controller.log.Log4J;

import java.util.Date;

/**
 * Created by oscarr on 8/8/18.
 */
public class GoogleCalendarService implements CalendarService{

    @Override
    @BatteryQoS( minBatteryLevel = DemoConstants.WORKS_WITH_LOW_CHARGE)
    @ConnectivityQoS( wifiStatus = DemoConstants.NOT_REQUIRES_WIFI_CONNECTIVITY)
    public Boolean checkAvailability(Date from, Date to){
        Log4J.warn(this, String.format("Executing GoogleCalendarService.checkAvailability for: [from date: %s, " +
                "to date: %s]", from, to));
        return true;
    }

    @Override
    @BatteryQoS( minBatteryLevel = DemoConstants.WORKS_WITH_LOW_CHARGE)
    @ConnectivityQoS( wifiStatus = DemoConstants.NOT_REQUIRES_WIFI_CONNECTIVITY)
    public CalendarPOJO createEvent(String eventName, Date from, Date to){
        Log4J.warn(this, String.format("Executing GoogleCalendarService.createEvent for: [eventName: %s. " +
                "from date: %s, to date: %s]", eventName, from, to));
        return null;
    }

    @Override
    public void execute() {
        Log4J.warn(this, "Executing GoogleCalendarService...");
    }
}
