package edu.cmu.inmind.composition.apis;

import edu.cmu.inmind.composition.annotations.ArgDesc;
import edu.cmu.inmind.composition.annotations.Description;
import edu.cmu.inmind.composition.pojos.MoviePOJO;

/**
 * Created by oscarr on 8/17/18.
 */
public interface MovieService {

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
    MoviePOJO searchCinemas();
}
