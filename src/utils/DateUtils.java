package utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public final class DateUtils {
    private DateUtils() {
    }

    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}
