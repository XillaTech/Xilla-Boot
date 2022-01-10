package net.xilla.boot.log;

import lombok.Getter;
import lombok.Setter;

import java.util.TimeZone;

public interface Logger {

    static void debug(String message) {
        log(LogLevel.DEBUG, message);
    }

    static void info(String message) {
        log(LogLevel.INFO, message);
    }

    static void warn(String message) {
        log(LogLevel.WARN, message);
    }

    static void error(String message) {
        log(LogLevel.ERROR, message);
    }

    static void fatal(String message) {
        log(LogLevel.FATAL, message);
    }

    static void debug(String message, Class clazz) {
        log(LogLevel.DEBUG, message, clazz);
    }

    static void info(String message, Class clazz) {
        log(LogLevel.INFO, message, clazz);
    }

    static void warn(String message, Class clazz) {
        log(LogLevel.WARN, message, clazz);
    }

    static void error(String message, Class clazz) {
        log(LogLevel.ERROR, message, clazz);
    }

    static void fatal(String message, Class clazz) {
        log(LogLevel.FATAL, message, clazz);
    }

    static void log(LogLevel level, String message) {
        Log log = new Log(level, message);
        log(log);
    }

    static void log(LogLevel level, String message, Class clazz) {
        Log log = new Log(level, message, clazz);
        log(log);
    }

    static void log(Log log) {
        if(log.getLevel().equals(LogLevel.DEBUG)) {
            LogManager.getLogger().debug(log);
        } else if(log.getLevel().equals(LogLevel.INFO)) {
            LogManager.getLogger().info(log);
        } else if(log.getLevel().equals(LogLevel.WARN)) {
            LogManager.getLogger().warn(log);
        } else if(log.getLevel().equals(LogLevel.ERROR)) {
            LogManager.getLogger().error(log);
        } else if(log.getLevel().equals(LogLevel.FATAL)) {
            LogManager.getLogger().fatal(log);
        }
    }

    void debug(Log log);

    void info(Log log);

    void warn(Log log);

    void error(Log log);

    void fatal(Log log);

}
