package edu.cmu.inmind.composition.controllers;

import edu.cmu.inmind.composition.pojos.NERPojo;
import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.NERClassifierCombiner;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.time.TimeAnnotations;
import edu.stanford.nlp.time.Timex;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.*;

/**
 * Created by oscarr on 8/14/18.
 */
public class NERController {
    public static final String ISO_8601_24H_FULL_FORMAT = "yyyy-MM-dd'T'HH:mm";
    public static final String ISO_8601_24H_SHORT_FORMAT = "yyyy-MM-dd";

    private static AbstractSequenceClassifier<CoreLabel> classifier;
    public static void init(){
        try {
            String serializedClassifier = "classifiers/english.all.3class.distsim.crf.ser.gz";
            classifier = new NERClassifierCombiner(serializedClassifier);//CRFClassifier.getClassifier(serializedClassifier);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static List<NERPojo> extractEntities(String sentence){
        List<NERPojo> annotations = new ArrayList<>();
        List<List<CoreLabel>> labels = classifier.classify(sentence);
        for(List<CoreLabel> innerLabels : labels){
            for(CoreLabel coreLabel : innerLabels){
                if(!coreLabel.ner().equals("O")){
                    NERPojo nerPojo = new NERPojo();
                    nerPojo.setWord(coreLabel.word());
                    nerPojo.setNormalizedAnnotation(
                            coreLabel.getString(CoreAnnotations.NormalizedNamedEntityTagAnnotation.class));
                    nerPojo.setPrevious(annotations.size() > 0? annotations.get(annotations.size()-1) : null);
                    if( checkValues(coreLabel, nerPojo) ) {
                        nerPojo.setBegin(coreLabel.beginPosition());
                        nerPojo.setEnd(coreLabel.endPosition());
                        nerPojo.setAnnotation(coreLabel.ner());
                        annotations.add(nerPojo);
                    }
                }
            }
        }
        return annotations;
    }

    private static boolean checkValues(CoreLabel coreLabel, NERPojo nerPojo) {
        try {
            Timex timex = coreLabel.get(TimeAnnotations.TimexAnnotation.class);
            if (timex != null) {
                String stringDate = timex.value().replace("XXXX", ""
                        + Calendar.getInstance().get(Calendar.YEAR));
                DateTimeFormatter dtf = DateTimeFormat.forPattern(stringDate.contains("T")?
                        ISO_8601_24H_FULL_FORMAT : ISO_8601_24H_SHORT_FORMAT);
                DateTime dateTime = dtf.parseDateTime(stringDate);
                nerPojo.setDate(dateTime.toDate());
                nerPojo.setWord(timex.text());
                // if date has been already added, then discard this NERPojo
                if( nerPojo.getPrevious() != null && nerPojo.getPrevious().getDate() != null
                        && nerPojo.getPrevious().getDate().compareTo(nerPojo.getDate()) == 0 ){
                    return false;
                }
            }else{
                if(coreLabel.ner().equals("MONEY")){
                    try {
                        Double.parseDouble(nerPojo.getWord());
                        nerPojo.setNormalizedAnnotation(nerPojo.getWord());
                    }catch (Exception e){
                        return false;
                    }
                }
            }
        }catch (Exception e){
            return false;
        }
        return true;
    }

    private static void print(List<NERPojo> annotations){
        for(NERPojo annotation : annotations){
            System.out.println(annotation);
        }
    }

    public static void main(String args[]){
        init();
        //extractEntities("First, Check ten times (1, 2, 3, four) the availability on calendar from August 3 to August 10 at 10:30am");
        print(extractEntities("look for flights to Paris August 10 at 10:30am"));
        System.out.println("===============");
        print(extractEntities("look  for  flights  to  Paris  for  less than  $700"));
        System.out.println("===============");
        print(extractEntities("flying from a specific origin place"));
        System.out.println("===============");
        print(extractEntities("flying to a specific destination place"));
        System.out.println("===============");
        print(extractEntities("maximum price per flight ticket"));
        System.out.println("===============");
        print(extractEntities("cabin class"));
        System.out.println("===============");
        print(extractEntities("number of passengers or travelers"));
    }
}
