package ca.bc.gov.open.pcsscriminalapplication.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("DateUtils Test")
public class DateUtilsTest {

    @Test
    @DisplayName("Test date format valid")
    public void testValidFormat() {

        Assertions.assertEquals(
                "2001-11-26 12:00:00.0",
                DateUtils.formatDate("26-NOV-01 12.00.00.0000000 PM -08:00"));
    }

    @Test
    @DisplayName("Test invalid date format ")
    public void testInvalidFormat() {

        Assertions.assertEquals("2013-03-25 00:00:00.0", DateUtils.formatDate("25-Mar-2013"));
    }

    @Test
    @DisplayName("Test ORDs date format valid")
    public void testOrdsValidFormat() {

        LocalDate testDate = LocalDate.parse("2013-03-25");
        LocalDateTime testDateTime = testDate.atTime(13, 4, 22);
        Assertions.assertEquals(
                "2013-03-25 13:04:22",
                DateUtils.formatORDSDate(testDateTime.toInstant(ZoneOffset.of("-7"))));
    }
}
