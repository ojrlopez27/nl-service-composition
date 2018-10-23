package edu.cmu.inmind.services.apis;

import edu.cmu.inmind.osgi.commons.markers.BundleAPI;
import edu.cmu.inmind.services.commons.GenericService;
import edu.cmu.inmind.services.markers.ArgDesc;
import edu.cmu.inmind.services.markers.Description;
import edu.cmu.inmind.services.pojos.CalendarPOJO;
import java.util.Date;

import static edu.cmu.inmind.services.apis.CalendarService.SERVICE;

/**
 * Created by oscarr on 8/8/18.
 */
@BundleAPI(id = SERVICE)
public interface CalendarService extends GenericService {

    /** this is the service id **/
    String SERVICE = "CalendarService";

    @Description(capabilities = {
            "This method validates whether there is availability on calendar given a time slot",
            "This method checks whether there is a conflict on the calendar given a range of dates",
            "This method checks calendar availability on a range of dates"
    })
    @ArgDesc(args = {
            "from : from date (yyyy-mm-dd)...?",
            "to : to date (yyyy-mm-dd)...?"
    })
    // TODO: add @Feature annotation
    Boolean checkAvailability(Date from, Date to);



    @Description(capabilities = {
            "This method creates a new calendar event given a time slot and an event name",
            "This method generates a new calendar event given an event name and a range of dates",
    })
    @ArgDesc(args = {
            "eventName : what is the name of your event?",
            "from : from date (yyyy-mm-dd)...?",
            "to : to date (yyyy-mm-dd)...?"
    })
    // TODO: add @Feature annotation
    CalendarPOJO createEvent(String eventName, Date from, Date to);
}
