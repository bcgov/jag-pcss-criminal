package ca.bc.gov.open.pcsscriminalcommon.utils;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InstantUtils {
    public static String convert(Instant value) {
        if (value == null) {
            return null;
        }

        return DateTimeFormatter.ofPattern("dd-MMM-yyyy")
                .withZone(ZoneId.of("GMT-7"))
                .withLocale(Locale.US)
                .format(value);
    }

    public static String print(Instant xml) {
        String first = xml.toString();
        return first.substring(0, first.length() - 1);
    }
}
