package ca.bc.gov.open.pcsscriminalcommon.utils;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

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

    public static Instant parse(String value) {
        try {
            Date d;
            try {
                d = new SimpleDateFormat("dd-MMM-yy hh.mm.ss.SSSSSS a", Locale.US).parse(value);
            } catch (ParseException ex) {
                d = new SimpleDateFormat("dd-MMM-yy", Locale.US).parse(value);
            }
            return d.toInstant();

        } catch (Exception ex) {
            return null;
        }
    }

    public static Instant parseSoap(String value) {
        try {
            Date d;
            // Try to parse a datetime first then try date only if both fail return null
            try {
                // Date time parser
                var sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSSSSS", Locale.US);
                sdf.setTimeZone(TimeZone.getTimeZone("GMT-7"));
                d = sdf.parse(value);
            } catch (ParseException ex) {
                // Date only parser
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
