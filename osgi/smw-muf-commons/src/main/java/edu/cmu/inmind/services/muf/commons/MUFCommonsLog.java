package edu.cmu.inmind.services.muf.commons;

import edu.cmu.inmind.multiuser.controller.log.Log4J;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;

public class MUFCommonsLog extends Log4J {

    private static final Level IPA = Level.forName("IPA", 2);

    public static void log(Object caller, String message) {
        if(getInstance().isTurnedOn()) {
            getLogger(caller).log(IPA, message);
        }
        else {
            System.out.println("IPA logger is not turned on..");
        }
    }

    public static void setFileName(String userId) {
        System.setProperty("log.name", userId);
        LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        ctx.reconfigure();
    }
}