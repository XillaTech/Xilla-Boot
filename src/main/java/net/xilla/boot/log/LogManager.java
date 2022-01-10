package net.xilla.boot.log;

import lombok.Getter;
import lombok.Setter;

import java.util.TimeZone;

public class LogManager {

    @Getter
    @Setter
    private static LogLevel level = LogLevel.INFO;

    @Getter
    @Setter
    private static Logger logger = new DefaultLogger();

    @Getter
    @Setter
    private static TimeZone timeZone = TimeZone.getTimeZone("UTC");

}
