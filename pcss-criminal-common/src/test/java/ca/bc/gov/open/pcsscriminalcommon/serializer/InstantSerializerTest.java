package ca.bc.gov.open.pcsscriminalcommon.serializer;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Instant Serializer Test Suite")
public class InstantSerializerTest {

    @Mock
    JsonParser jsonParserMock;

    InstantSerializer sut;

    @BeforeAll
    public void beforeAll() {

        MockitoAnnotations.openMocks(this);

        sut = new InstantSerializer();

    }

    @Test
    @DisplayName("Success object serialized")
    public void objectSerialized() throws IOException {

        Mockito.when(jsonParserMock.getText()).thenReturn("2021-10-21T14:48:50+00:00");

        JsonFactory factory = new JsonFactory();
        StringWriter jsonObjectWriter = new StringWriter();
        JsonGenerator generator = factory.createGenerator(jsonObjectWriter);

        LocalDate testDate = LocalDate.parse("2021-04-17");
        LocalDateTime testDateTime = testDate.atStartOfDay();

        Assertions.assertDoesNotThrow(() -> sut.serialize(testDateTime.toInstant(ZoneOffset.UTC), generator, null));

    }

}
