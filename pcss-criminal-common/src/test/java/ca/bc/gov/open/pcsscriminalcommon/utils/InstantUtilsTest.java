package ca.bc.gov.open.pcsscriminalcommon.utils;

import com.fasterxml.jackson.core.JsonParser;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Instant Utils Test Suite")
public class InstantUtilsTest {

    @Mock
    JsonParser jsonParserMock;

    @BeforeAll
    public void beforeAll() {

        MockitoAnnotations.openMocks(this);

    }

    @Test
    @DisplayName("Success object converted")
    public void objectConverted() throws IOException {

        LocalDate testDate = LocalDate.parse("2021-04-17");
        LocalDateTime testDateTime = testDate.atStartOfDay();

        String result = InstantUtils.convert(testDateTime.toInstant(ZoneOffset.UTC));

        Assertions.assertEquals("16-Apr-2021", result);

    }

    @Test
    @DisplayName("Null object not converted")
    public void objecNotConverted() throws IOException {

        Mockito.when(jsonParserMock.getText()).thenReturn("GARBAGE");

        Assertions.assertNull(InstantUtils.convert(null));

    }

    @Test
    @DisplayName("Success object printed")
    public void objectPrinted() throws IOException {

        LocalDate testDate = LocalDate.parse("2021-04-17");
        LocalDateTime testDateTime = testDate.atStartOfDay();

        String result = InstantUtils.print(testDateTime.toInstant(ZoneOffset.UTC));

        Assertions.assertEquals("2021-04-17T00:00:00", result);

    }

    @Test
    @DisplayName("Success object parsed")
    public void objectParsed() throws IOException {

        Instant result = InstantUtils.parse("26-NOV-01 12.00.00.0000000 PM -08:00");

        Assertions.assertEquals("2001-11-26T20:00:00Z", result.toString());

    }

    @Test
    @DisplayName("Success object parsed no time")
    public void objectParsedNoTime() throws IOException {

        Instant result = InstantUtils.parse("16-Apr-21");

        Assertions.assertEquals("2021-04-16T07:00:00Z", result.toString());

    }


    @Test
    @DisplayName("Error object not parsed")
    public void objecNotParsed() throws IOException {

        Instant result = InstantUtils.parse(null);

        Assertions.assertNull(result);

    }

    @Test
    @DisplayName("Success object soap parsed")
    public void objectParsedSoap() throws IOException {

        Instant result = InstantUtils.parseSoap("26-NOV-01 12.00.00.0000000 PM -08:00");

        Assertions.assertEquals("2001-11-26T07:00:00Z", result.toString());

    }

    @Test
    @DisplayName("Success object soap parsed no time")
    public void objectParsedNoTimeSoap() throws IOException {

        Instant result = InstantUtils.parseSoap("16-Apr-21");

        Assertions.assertEquals("2021-04-16T07:00:00Z", result.toString());

    }

    @Test
    @DisplayName("Error object not soap parsed")
    public void objecNotParsedSoap() throws IOException {

        Instant result = InstantUtils.parseSoap(null);

        Assertions.assertNull(result);


    }

}
