package ca.bc.gov.open.pcsscriminalapplication.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Slf4j
public class DateUtils {

    private DateUtils() {
        //empty constructor
    }

    public static String formatDate(String inDate) {

        if (StringUtils.isBlank(inDate)) return null;

        SimpleDateFormat dt = new SimpleDateFormat("dd-MMM-yy hh.mm.ss.SSSSSS a", Locale.US);
        try {
            Date date = dt.parse(inDate);
            SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
            return dt1.format(date);
        } catch (Exception e) {
            log.error("Invalid date returned from ords");
        }

        return inDate;

    }

    public static String formatORDSDate(String inDate) {

        if (StringUtils.isBlank(inDate)) return null;

        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
        try {
            Date date = dt.parse(inDate);
            SimpleDateFormat dt1 = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
            return dt1.format(date);
        } catch (Exception e) {
            log.error("Invalid date returned from ords");
        }

        return inDate;

    }

}
