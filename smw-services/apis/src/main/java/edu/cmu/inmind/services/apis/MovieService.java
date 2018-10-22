package edu.cmu.inmind.services.apis;

import edu.cmu.inmind.osgi.commons.markers.BundleAPI;
import edu.cmu.inmind.services.markers.ArgDesc;
import edu.cmu.inmind.services.markers.Description;
import edu.cmu.inmind.services.pojos.MoviePOJO;

import static edu.cmu.inmind.services.apis.MovieService.SERVICE;

/**
 * Created by oscarr on 8/17/18.
 */
@BundleAPI(id = SERVICE)
public interface MovieService {

    /** this is the service id **/
    String SERVICE = "MovieService";

    @Description(capabilities = {
            "This method allows to get recommendations for movies based on genre, actors, directors, popularity, reviews, language",
            "This method allows to look up for movie recommendations given a genre, actors, directors, popularity, reviews, language",
            "This method gets movie recommendations given a genre, actors, directors, popularity, reviews, language",
            "This method recommends movies given genre, actors, directors, popularity, reviews and language"
    })
    @ArgDesc(args = {
            "genre : what type of genre?(romantic, thriller, horror, sci-fi, drama, anime, documentaries)",
            "preferences : what type of preferences? (actors, directors, oscar nominated, in theaters, popularity, reviews)",
            "language : which language? (Spanish, Brazilian, Indian, American, Italian, Chinese)",
    })
    // TODO: add @Feature annotation
    MoviePOJO recommendMovie(String genre, String preferencesString, String language);

    @Description(capabilities = {
            "This method allows to search for cinemas near to location",
            "This method allows to look up for cinemas at a given location, date, showtime",
            "This method gets movie theatres near a location, for a given time",
            "This method searches cinema theatres near a location"
    })
    @ArgDesc(args = {
            "location : what is your from location?",
            "showtimes: what show time are you looking for?(morning, aternoon, evening, night)",
            "date : which day are you planning to watch (yyyy-mm-dd)?"
    })
    // TODO: add @Feature annotation
    MoviePOJO searchCinemas();
}
