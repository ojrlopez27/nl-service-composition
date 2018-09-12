package edu.cmu.yahoo.inmind.launchpad.java.binders;

import edu.cmu.yahoo.inmind.launchpad.utils.LoggerContext;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by dangiankit on 2/14/18.
 */
public class LoggerContextJavaBinder extends LoggerContext {
    private static final Logger LOGGER = LogManager.getLogger(LoggerContextJavaBinder.class);
    private static LoggerContextJavaBinder loggerContext;

    // This creates the "VERBOSE" level if it does not exist yet.
    private static final Level VERBOSE = Level.forName("VERBOSE", 550);
    private static final int LOG_SUCCESS = 1;

    public static LoggerContext getInstance() {
        if (loggerContext == null) {
            loggerContext = new LoggerContextJavaBinder();
        }
        return loggerContext;
    }

    @Override
    public int d(String tag, String msg) {
        LOGGER.debug(format(tag, msg));
        return LOG_SUCCESS;
    }

    @Override
    public int d(String tag, String msg, Throwable tr) {
        LOGGER.debug(format(tag, msg), tr);
        return LOG_SUCCESS;
    }

    @Override
    public int e(String tag, Throwable tr) {
        LOGGER.error(format(tag, tr.getMessage()), tr);
        return LOG_SUCCESS;
    }

    @Override
    public int e(String tag, String msg) {
        LOGGER.error(format(tag, msg));
        return LOG_SUCCESS;
    }

    @Override
    public int e(String tag, String msg, Throwable tr) {
        LOGGER.error(format(tag, msg), tr);
        return LOG_SUCCESS;
    }

    @Override
    public int i(String tag, String msg, Throwable tr) {
        LOGGER.info(format(tag, msg), tr);
        return LOG_SUCCESS;
    }

    @Override
    public int v(String tag, String msg, Throwable tr) {
        LOGGER.log(VERBOSE, format(tag, msg), tr);
        return LOG_SUCCESS;
    }

    @Override
    public int w(String tag, String msg, Throwable tr) {
        LOGGER.warn(format(tag, msg), tr);
        return LOG_SUCCESS;
    }

    @Override
    public String getStackTraceString(Throwable tr) {
        return tr.getStackTrace().toString();
    }

    private String format(String tag, String msg) {
        return (String.format("%s: %s", tag, msg));
    }
}
