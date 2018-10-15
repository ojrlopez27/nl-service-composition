package edu.cmu.inmind.demo.common;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

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

    public static String removeEOS(String original){
        return original.replace(DemoConstants.EOS, "")
                .replace("\\u003ceos\\git au003e", "");
    }


    public static String removeExtraQuotes(String payload){
        payload = payload.trim();
        if( payload.startsWith("\"") ) payload = payload.substring(1);
        if( payload.endsWith("\"") ) payload = payload.substring(0, payload.length() - 1);
        return payload;
    }

    private static long time;
    public static void startChrono(){
        time = System.currentTimeMillis();
    }

    public static long stopChrono(){
        return (System.currentTimeMillis() - time);
    }
    private static int ruleCont = 1;
    public static String getRuleName(String step) {
        String[] words = step.split(" ");
        if(words.length > 0){
            String name = "";
            for(String word : words){
                name += name.isEmpty()? word : "-" + word;
            }
            return String.format("Rule%s-%s", (ruleCont++), name);
        }
        return String.format("Rule%s-%s", (ruleCont++), step);
    }


    public static <A> Map<String, A> asMap(Object... keysAndValues) {
        return new LinkedHashMap<String, A>() {{
            for (int i = 0; i < keysAndValues.length - 1; i++) {
                put(keysAndValues[i].toString(), (A) keysAndValues[++i]);
            }
        }};
    }


}
