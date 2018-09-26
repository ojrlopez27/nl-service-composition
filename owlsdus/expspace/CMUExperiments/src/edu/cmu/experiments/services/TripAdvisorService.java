package edu.cmu.experiments.services;

import java.util.Date;
import java.util.List;

import edu.cmu.experiments.apis.SearchThingsToDoService;
import edu.cmu.inmind.multiuser.controller.log.Log4J;

/**
 * Created by oscarr on 8/8/18.
 */
public class TripAdvisorService implements SearchThingsToDoService {

    @Override
    public List<String> whatToDo(String place, Date when, String weatherConditions){
        Log4J.warn(this, String.format("Executing TripAdvisorService.whatToDo for: [place: %s, " +
                "when: %s, conditions: %s]", place, when, weatherConditions));
        return null;
    }
}
