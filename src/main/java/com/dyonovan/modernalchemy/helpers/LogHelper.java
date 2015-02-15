package com.dyonovan.modernalchemy.helpers;

import com.dyonovan.modernalchemy.lib.Constants;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogHelper {
    private static final Logger logger = LogManager.getLogger(Constants.MODID);

    public static void log(Level level, Object object) {

        logger.log(level, object.toString());
    }

    public static void severe(Object object) {

        log(Level.FATAL, object.toString());
    }

    public static void debug(Object object) {

        log(Level.DEBUG, "[DEBUG] " + object.toString());
    }

    public static void warning(Object object) {

        log(Level.WARN, object.toString());
    }

    public static void info(Object object) {

        log(Level.INFO, object.toString());
    }

    public static void config(Object object) {

        log(Level.INFO, object.toString());
    }
}