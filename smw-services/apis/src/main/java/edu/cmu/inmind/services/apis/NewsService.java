package edu.cmu.inmind.services.apis;

import edu.cmu.inmind.osgi.commons.markers.BundleAPI;
import edu.cmu.inmind.services.commons.GenericService;
import edu.cmu.inmind.services.markers.ArgDesc;
import edu.cmu.inmind.services.markers.Description;
import edu.cmu.inmind.services.pojos.NewsPOJO;

import static edu.cmu.inmind.services.apis.NewsService.SERVICE;

/**
 * Created by oscarr on 8/10/18.
 */
@BundleAPI(id = SERVICE)
public interface NewsService extends GenericService {

    /** this is the service id **/
    String SERVICE = "NewsService";

    @Description( capabilities = {
            "It returns the news feeds about a given topic",
            "It retrieves news about people",
            "It retrieves news about places, countries, and cities"
    })
    @ArgDesc(args = {
            "topic : which topic do you want me to use to filter your news feeds?"
    })
    // TODO: add @Feature annotation
    NewsPOJO getNewsFeed(String topic);
}
