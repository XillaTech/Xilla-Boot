package net.xilla.boot.log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DefaultLogger implements Logger {

    @Override
    public void debug(Log log) {
        if(log.getLevel().ordinal() < LogManager.getLevel().ordinal()) {
            return;
        }
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        df.setTimeZone(LogManager.getTimeZone());

        String nowAsISO = df.format(new Date());
        System.out.printf("[%s %s : DEBUG] (%s) %s%n\n", nowAsISO, LogManager.getTimeZone().getID(), log.getClassName(), log.getMessage());
    }

    @Override
    public void info(Log log) {
        if(log.getLevel().ordinal() < LogManager.getLevel().ordinal()) {
            return;
        }
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        df.setTimeZone(LogManager.getTimeZone());

        String nowAsISO = df.format(new Date());
        System.out.printf("[%s %s : INFO] %s%n\n", nowAsISO, LogManager.getTimeZone().getID(), log.getMessage());
    }

    @Override
    public void warn(Log log) {
        if(log.getLevel().ordinal() < LogManager.getLevel().ordinal()) {
            return;
        }
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        df.setTimeZone(LogManager.getTimeZone());

        String nowAsISO = df.format(new Date());
        System.out.printf("[%s %s : WARN] (%s) %s%n\n", nowAsISO, LogManager.getTimeZone().getID(), log.getClassName(), log.getMessage());
    }

    @Override
    public void error(Log log) {
        if(log.getLevel().ordinal() < LogManager.getLevel().ordinal()) {
            return;
        }
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        df.setTimeZone(LogManager.getTimeZone());

        String nowAsISO = df.format(new Date());
        System.out.printf("[%s %s : ERROR] (%s) %s%n\n", nowAsISO, LogManager.getTimeZone().getID(), log.getClassName(), log.getMessage());
    }

    @Override
    public void fatal(Log log) {
        if(log.getLevel().ordinal() < LogManager.getLevel().ordinal()) {
            return;
        }
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        df.setTimeZone(LogManager.getTimeZone());

        String nowAsISO = df.format(new Date());
        System.out.printf("[%s %s : FATAL] (%s) %s%n\n", nowAsISO, LogManager.getTimeZone().getID(), log.getClassName(), log.getMessage());
    }

}
