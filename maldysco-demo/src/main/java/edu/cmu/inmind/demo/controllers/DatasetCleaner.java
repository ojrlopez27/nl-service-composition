package edu.cmu.inmind.demo.controllers;

import edu.cmu.inmind.demo.common.Utils;
import edu.cmu.inmind.multiuser.controller.common.CommonUtils;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Created by oscarr on 8/17/18.
 */
public class DatasetCleaner {

    public static void main(String args[]){
        Utils.printToFile(CommonUtils.getProperty("sent2vec.corpora.methods.mt.temp.path"), getCleanDataset());
    }

    public static StringBuffer getCleanDataset(){
        try {
            List<String> blackList = Arrays.asList("flight", "flights", "hotel", "hotels", "calendar",
                    "restaurant", "restaurants", "taxi", "uber", "cab", "weather", "search", "searches", "book",
                    "books", "reservation", "reservations", "reserve", "reserves");
            Scanner scanner = new Scanner(new File(CommonUtils.getProperty("sent2vec.corpora.methods.mt.path")));
            StringBuffer buffer = new StringBuffer();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                boolean eliminate = false;
                for (String blackWord : blackList) {
                    String[] words = line.split(" ");
                    for (String word : words) {
                        if (word.equalsIgnoreCase(blackWord)) {
                            eliminate = true;
                            break;
                        }
                    }
                    if (eliminate) break;
                }
                if (!eliminate) buffer.append(buffer.length() > 0 ? "\n" + line : line);
            }
            return buffer;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
