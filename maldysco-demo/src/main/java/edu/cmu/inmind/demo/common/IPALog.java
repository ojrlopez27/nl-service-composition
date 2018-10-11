package edu.cmu.inmind.demo.common;

import edu.cmu.inmind.multiuser.controller.log.Log4J;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

public class IPALog extends Log4J {
    private static final Level IPA = Level.forName("IPA", 2);

    public static void log(Object caller, String message) {
        if(getInstance().isTurnedOn()) {
            getLogger(caller).log(IPA, message);
        }
    }

    public static void setFileName(String userId) {
        System.setProperty("log.name", userId);
        org.apache.logging.log4j.core.LoggerContext ctx =
                (org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(false);
        ctx.reconfigure();
    }
}