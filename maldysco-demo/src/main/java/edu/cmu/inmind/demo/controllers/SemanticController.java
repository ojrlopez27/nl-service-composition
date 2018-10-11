package edu.cmu.inmind.demo.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by oscarr on 8/16/18.
 */
//TODO: we need to use a proper semantic mechanism (e.g., WordNet?, Word2Vec, etc.) that provides related words by similarity
public class SemanticController {
    private static List<List<String>> synonyms = new ArrayList<>();

    static{
        synonyms.add( Arrays.asList(new String[]{"from", "departureDate", "checkin", "when"}) );
        synonyms.add( Arrays.asList(new String[]{"to", "returnDate", "checkout", "when"}) );
        synonyms.add( Arrays.asList(new String[]{"place", "destination"}) );
    }

    public static List<String> getSynonyms(String word){
        for(List<String> group : synonyms){
            if(group.contains(word)){
                List<String> temp = new ArrayList<>(group);
                temp.remove(word);
                return temp;
            }
        }
        return null;
    }
}
