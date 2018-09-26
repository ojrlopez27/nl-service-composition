package edu.cmu.experiments.apis;

import java.util.Date;
import java.util.List;

/**
 * Created by oscarr on 8/8/18.
 */
public interface SearchThingsToDoService {

    List<String> whatToDo(String place, Date when, String weatherConditions);
}
