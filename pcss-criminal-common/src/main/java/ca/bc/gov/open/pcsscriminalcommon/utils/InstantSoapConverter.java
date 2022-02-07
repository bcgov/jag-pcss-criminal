package ca.bc.gov.open.pcsscriminalcommon.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InstantSoapConverter {
    public static String convert(Instant value) {
        if (value == null) {
            return null;
        }

        return DateTimeFormatter.ofPattern("dd-MMM-yyyy")
                .withZone(ZoneId.of("GMT-7"))
                .withLocale(Locale.US)
                .format(value);
    }

    public static String print(Instant value) {
        String out =
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                        .withZone(ZoneId.systemDefault())
                        .format(value);
        return out + ".0";
    }

    public static Instant parse(String value) {
        try {
            Date d;
            // Try to parse a datetime first then try date only if both fail return null
            try {
                // Date time parser
                var sdf = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss.SSSSSS", Locale.US);
                sdf.setTimeZone(TimeZone.getTimeZone("GMT-7"));
                d = sdf.parse(value);
            } catch (ParseException ex) {
                try {
                    var sdf = new SimpleDateFormat("dd-MMM-yy", Locale.US);
                    sdf.setTimeZone(TimeZone.getTimeZone("GMT-7"));
                    d = sdf.parse(value);
                } catch (ParseException ex2) {
                    return Instant.parse(value + "Z");
                }
            }
            return d.toInstant();
        } catch (Exception ex) {
            log.warn("Bad date received from soap request");
            return null;
        }
    }
}
