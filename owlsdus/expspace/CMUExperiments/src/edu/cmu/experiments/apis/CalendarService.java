package edu.cmu.experiments.apis;

import java.util.Date;

/**
 * Created by oscarr on 8/8/18.
 */
public interface CalendarService {

    Boolean checkAvailability(Date from, Date to);

    String createEvent(String eventName, Date from, Date to);
}
