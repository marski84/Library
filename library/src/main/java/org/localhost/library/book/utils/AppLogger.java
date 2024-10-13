package org.localhost.library.book.utils;

import org.apache.logging.log4j.LogManager;
import org.localhost.library.book.BaseBookService;

import java.util.logging.Logger;


public final class AppLogger {
    private static final Logger logger = Logger.getLogger(BaseBookService.class.getName());

    public static void logInfo(String message) {
        logger.info(message);
    }

    public static void logError(String message) {
        logger.severe(message);
    }

    public static void logWarn(String message) {
        logger.warning(message);
    }




    private AppLogger() {}

}
