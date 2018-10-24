package edu.cmu.inmind.demo.services;

import edu.cmu.inmind.demo.common.DemoConstants;
import edu.cmu.inmind.demo.markers.BatteryQoS;
import edu.cmu.inmind.demo.markers.ConnectivityQoS;
import edu.cmu.inmind.multiuser.controller.log.Log4J;
import edu.cmu.inmind.demo.pojos.NewsPOJO;
import edu.cmu.inmind.demo.apis.NewsService;

/**
 * Created by oscarr on 8/10/18.
 */
public class YahooNewsService implements NewsService {
    @Override
    public void execute() {
        Log4J.warn(this, "Executing YahooNewsService.execute...");
    }

    @Override
    @BatteryQoS( minBatteryLevel = DemoConstants.LOW_BATTERY_SERVICE)
    @ConnectivityQoS( wifiStatus = DemoConstants.NOT_REQUIRES_WIFI_CONNECTIVITY)
    public NewsPOJO getNewsFeed(String topic) {
        Log4J.warn(this, String.format("Executing YahooNewsService.getNewsFeed for: [topic: %s]", topic));
        return null;
    }
}
