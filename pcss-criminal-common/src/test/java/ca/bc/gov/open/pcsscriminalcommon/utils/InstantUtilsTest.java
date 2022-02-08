package ca.bc.gov.open.pcsscriminalcommon.utils;

import com.fasterxml.jackson.core.JsonParser;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.TimeZone;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Instant Utils Test Suite")
public class InstantUtilsTest {

    @Mock JsonParser jsonParserMock;

    @BeforeAll
    public void beforeAll() {

        MockitoAnnotations.openMocks(this);

        TimeZone.setDefault(TimeZone.getTimeZone("GMT-7"));
    }

    @Test
    @DisplayName("Success object converted")
    public void objectConverted() throws IOException {

        LocalDate testDate = LocalDate.parse("2021-04-17");
        LocalDateTime testDateTime = testDate.atStartOfDay();

        String result = InstantSoapConverter.convert(testDateTime.toInstant(ZoneOffset.UTC));

        Assertions.assertEquals("16-Apr-2021", result);
    }

    @Test
    @DisplayName("Null object not converted")
    public void objecNotConverted() throws IOException {

        Mockito.when(jsonParserMock.getText()).thenReturn("GARBAGE");

        Assertions.assertNull(InstantSoapConverter.convert(null));
    }

    @Test
    @DisplayName("Success object printed")
    public void objectPrinted() throws IOException {

        LocalDate testDate = LocalDate.parse("2021-04-17");
        LocalDateTime testDateTime = testDate.atStartOfDay();

        String result = InstantSoapConverter.print(testDateTime.toInstant(ZoneOffset.UTC));

        Assertions.assertEquals("2021-04-17 00:00:00.0", result);
    }
}
