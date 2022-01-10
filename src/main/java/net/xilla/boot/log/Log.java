package net.xilla.boot.log;

import lombok.Getter;

public class Log {

    @Getter
    private String message;

    @Getter
    private String className = null;

    @Getter
    private LogLevel level;

    public Log(LogLevel level, String message) {
        this.level = level;
        this.message = message;
    }

    public Log(LogLevel level, String message, Class clazz)  {
        this(level, message);

        this.className = clazz.getName();
    }

}
