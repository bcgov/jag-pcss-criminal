package ca.bc.gov.open.pcsscriminalapplication.utils;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Locale;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class DateUtils {

    private DateUtils() {
        // empty constructor
    }

    public static String formatDate(String inDate) {

        if (StringUtils.isBlank(inDate)) return null;

        try {
            SimpleDateFormat dt = new SimpleDateFormat("dd-MMM-yy hh.mm.ss.SSSSSS a", Locale.US);
            Date date = dt.parse(inDate);
            SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
            return dt1.format(date);
        } catch (Exception e) {
            try {
                SimpleDateFormat dt = new SimpleDateFormat("dd-MMM-yy", Locale.US);
                Date date1 = dt.parse(inDate);
                SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
                return dt1.format(date1);
            } catch (Exception e1) {
                log.error("Invalid date returned from ords");
            }
        }

        return inDate;
    }

    public static String formatORDSDate(Instant inDate) {


        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
        try {
            SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
            return dt1.format(inDate);
        } catch (Exception e) {
            log.error("Invalid date returned from ords");
        }

        return inDate.toString();
    }
}
