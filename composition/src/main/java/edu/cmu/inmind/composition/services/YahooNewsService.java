package edu.cmu.inmind.composition.services;

import edu.cmu.inmind.composition.annotations.BatteryQoS;
import edu.cmu.inmind.composition.annotations.ConnectivityQoS;
import edu.cmu.inmind.composition.apis.NewsService;
import edu.cmu.inmind.composition.common.Constants;
import edu.cmu.inmind.composition.pojos.NewsPOJO;

/**
 * Created by oscarr on 8/10/18.
 */
public class YahooNewsService implements NewsService {
    @Override
    public void execute() {
        System.out.println("Executing YahooNewsService.execute...");
    }

    @Override
    @BatteryQoS( minBatteryLevel = Constants.LOW_BATTERY_SERVICE)
    @ConnectivityQoS( wifiStatus = Constants.NOT_REQUIRES_WIFI_CONNECTIVITY)
    public NewsPOJO getNewsFeed(String topic) {
        System.out.println("Executing YahooNewsService.execute...");
        return null;
    }
}
