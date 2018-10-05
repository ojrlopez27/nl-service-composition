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
}
