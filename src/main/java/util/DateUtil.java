package util;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    private static final DateTimeFormatter ORDER_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final DateTimeFormatter DETAIL_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String formatOrderDate(Timestamp timestamp) {
        if (timestamp == null) {
            return "";
        }
        return timestamp.toLocalDateTime().format(ORDER_DATE_FORMATTER);
    }

    public static String formatDetailDate(Timestamp timestamp) {
        if (timestamp == null) {
            return "";
        }
        return timestamp.toLocalDateTime().format(DETAIL_DATE_FORMATTER);
    }
}
