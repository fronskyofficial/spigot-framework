package org.example.fronskyspigotframework.test.logic.logger;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class TestLogger {

    private final Logger logger;

    /**
     * Initializes a new instance of the TestLogger class with the specified class.
     *
     * @param clazz The class for which the logger is being created.
     */
    public TestLogger(Class<?> clazz) {
        logger = Logger.getLogger(clazz.getName());
        try {
            FileHandler fileHandler = new FileHandler("logs/logger.txt");
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Logs an informational message.
     *
     * @param message The message to log.
     */
    public void logInfo(String message) {
        logger.info(message);
    }

    /**
     * Logs a warning message.
     *
     * @param message The message to log.
     */
    public void logWarning(String message) {
        logger.warning(message);
    }

    /**
     * Logs an error message.
     *
     * @param message The message to log.
     */
    public void logError(String message) {
        logger.severe(message);
    }
}
