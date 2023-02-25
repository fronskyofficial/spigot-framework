package org.example.fronskyspigotframework.logic.logging;

import java.util.logging.Level;

import static org.bukkit.Bukkit.*;

public class Logger {

    private Logger() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Logs a message with the specified log level.
     *
     * @param level the log level.
     * @param message the message.
     */
    public static void log(Level level, String message) {
        getServer().getLogger().log(level, message);
    }

    /**
     * Logs an informational message.
     *
     * @param message the informational message.
     */
    public static void logInfo(String message) {
        getServer().getLogger().log(Level.INFO, message);
    }

    /**
     * Logs a warning message.
     *
     * @param message the warning message.
     */
    public static void logWarning(String message) {
        getServer().getLogger().log(Level.WARNING, message);
    }

    /**
     * Logs an error message.
     *
     * @param message the error message.
     */
    public static void logError(String message) {
        getServer().getLogger().log(Level.SEVERE, message);
    }

    /**
     * Logs a message with the specified level, prepending the name of the class.
     *
     * @param clazz the class to log.
     * @param level the level of the log message.
     * @param message the log message.
     */
    public static void logClass(Class<?> clazz, Level level, String message) {
        String className = clazz.getName();
        message = className + ": " + message;

        getServer().getLogger().log(level, message);
    }
}
