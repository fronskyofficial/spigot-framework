package org.example.fronskyframework.logic.logging;

import org.bukkit.Bukkit;

import java.util.logging.Level;

public class Logger {

    /**
     * Logs a message with the specified log level.
     *
     * @param level the log level.
     * @param message the message.
     */
    public static void log(Level level, String message) {
        Bukkit.getServer().getLogger().log(level, message);
    }

    /**
     * Logs an informational message.
     *
     * @param message the informational message.
     */
    public static void logInfo(String message) {
        Bukkit.getServer().getLogger().log(Level.INFO, message);
    }

    /**
     * Logs a warning message.
     *
     * @param message the warning message.
     */
    public static void logWarning(String message) {
        Bukkit.getServer().getLogger().log(Level.WARNING, message);
    }

    /**
     * Logs an error message.
     *
     * @param message the error message.
     */
    public static void logError(String message) {
        Bukkit.getServer().getLogger().log(Level.SEVERE, message);
    }

    /**
     * Logs a message with the specified level, prepending the name of the class.
     *
     * @param clazz the class to log.
     * @param level the level of the log message.
     * @param message the log message.
     */
    public static void logClass(Class<?> clazz, Level level, String message) {
        Bukkit.getServer().getLogger().log(level, clazz.getName() + ": " + message);
    }
}
