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

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by oscarr on 8/14/18.
 */
public class NERController {
    public static final String ISO_8601_24H_FULL_FORMAT = "yyyy-MM-dd'T'HH:mm";
    public static final String ISO_8601_24H_SHORT_FORMAT = "yyyy-MM-dd";

    private static AbstractSequenceClassifier<CoreLabel> classifier;
    static{
        try {
            String serializedClassifier = "classifiers/english.all.3class.distsim.crf.ser.gz";
            classifier = new NERClassifierCombiner(serializedClassifier);//CRFClassifier.getClassifier(serializedClassifier);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static List<NERPojo> annotate(String sentence){
        List<NERPojo> annotations = new ArrayList<>();
        List<List<CoreLabel>> labels = classifier.classify(sentence);
        for(List<CoreLabel> innerLabels : labels){
            for(CoreLabel coreLabel : innerLabels){
                System.out.println(coreLabel);
                if(!coreLabel.ner().equals("O")){
                    NERPojo nerPojo = new NERPojo();
                    nerPojo.setWord(coreLabel.word());
                    nerPojo.setPrevious(annotations.size() > 1? annotations.get(annotations.size()-1) : null);
                    if( checkDate(coreLabel, nerPojo) ) {
                        nerPojo.setBegin(coreLabel.beginPosition());
                        nerPojo.setEnd(coreLabel.endPosition());
                        nerPojo.setAnnotation(coreLabel.ner());
                        nerPojo.setNormalizedAnnotation(
                                coreLabel.getString(CoreAnnotations.NormalizedNamedEntityTagAnnotation.class));
                        annotations.add(nerPojo);
                    }
                }
            }
        }
        return annotations;
    }

    private static boolean checkDate(CoreLabel coreLabel, NERPojo nerPojo) {
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
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }

    public static void main(String args[]){
        NERController.annotate("First, Check ten times (1, 2, 3, four) the availability on calendar from August 3 to August 10 at 10:30am");
    }
}
