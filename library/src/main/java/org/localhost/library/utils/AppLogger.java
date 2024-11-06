package org.localhost.library.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class AppLogger {
    private static final Logger logger = LoggerFactory.getLogger(AppLogger.class);

    private AppLogger() {
    }

    public static void logInfo(String message) {
        logger.info(message);
    }

    public static void logError(String message) {
        logger.error(message);
    }

    public static void logError(String message, Object... args) {
        logger.error(message, args);
    }

    public static void logWarn(String message) {
        logger.warn(message);
    }

    public static void logWarn(String message, Object... args) {
        logger.warn(message, args);
    }

    public static void logDebug(String message) {
        logger.debug(message);
    }

    public static void logDebug(String message, Object... args) {
        logger.debug(message, args);
    }
}
