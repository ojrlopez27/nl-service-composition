package edu.cmu.experiments.services;

import edu.cmu.experiments.apis.NewsService;
import edu.cmu.inmind.multiuser.controller.log.Log4J;

/**
 * Created by oscarr on 8/10/18.
 */
public class YahooNewsService implements NewsService {
	
    @Override
    public String getNewsFeed(String topic) {
        Log4J.warn(this, String.format("Executing YahooNewsService.getNewsFeed for: [topic: %s]", topic));
        return null;
    }
}
