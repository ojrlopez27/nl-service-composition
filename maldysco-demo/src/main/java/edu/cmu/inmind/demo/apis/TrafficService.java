package edu.cmu.inmind.demo.apis;

import edu.cmu.inmind.demo.markers.ArgDesc;
import edu.cmu.inmind.services.commons.GenericService;
import edu.cmu.inmind.demo.markers.Description;
import edu.cmu.inmind.demo.pojos.LocationPOJO;

import java.util.Date;

/**
 * Created by oscarr on 8/17/18.
 */
public interface TrafficService extends GenericService{

    @Description(capabilities = {
            "This method allows check traffic from source location to destination location",
            "This method allows to get traffic given a destination place, source",
            "This method gives a traffic estimation"
    })
    @ArgDesc(args = {
            "from : where are you travelling from?",
            "destination : what is your destination?",
            "date : when are you leaving (yyyy-mm-dd)?",
            "time : what time are you starting at (hh:mm)"
    })
    void checkTracffic(LocationPOJO sourceLocationPojo, LocationPOJO destinationLocationPojo,
                       Date date, String timeFormat);

    @Description(capabilities = {
            "This method allows to get shortest route given a source place, destination place, departure date, and return date",
            "This method allows to get shortest route by road given a source place, destination place, departure date, and time",
            "This method gets a shortest route with optimal traffic conditions",
    })
    @ArgDesc(args = {
            "source : where are you starting from?",
            "destination : what is your destination?",
            "date : when are you leaving (yyyy-mm-dd)?",
            "time : what time are you starting at (hh:mm)"
    })
    void calculateShortestRoute(LocationPOJO sourceLocationPojo,
                                LocationPOJO destinationLocationPojo, Date date, String time);
}
