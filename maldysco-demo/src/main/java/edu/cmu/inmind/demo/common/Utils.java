package edu.cmu.inmind.demo.common;

import java.net.InetAddress;
import java.net.UnknownHostException;
/**
 * Created for demo : sakoju 10/4/2018
 */
public class Utils {

    public static String getSystemIPAddress() {
        String ipAddress = "";
        try {
            ipAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return ipAddress;
    }

    public static String splitByCapitalizedWord(String word, boolean lowercase){
        String[] split = word.split("(?=\\p{Upper})");
        String newWord = "";
        for(String sp : split){
            newWord += (!newWord.isEmpty()? "-" + sp : sp);
        }
        return lowercase? newWord.toLowerCase() : newWord;
    }

}
