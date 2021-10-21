package ca.bc.gov.open.pcsscriminalcommon.serializer;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.time.Instant;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Create Account Test Suite")
public class InstantDeserializerTest {

    @Mock
    JsonParser jsonParserMock;

    InstantDeserializer sut;

    @BeforeAll
    public void beforeAll() {

        MockitoAnnotations.openMocks(this);

        sut = new InstantDeserializer();
    }

    @Test
    @DisplayName("Success object deserialized")
    public void objectDeserialized() throws IOException {

        Mockito.when(jsonParserMock.getText()).thenReturn("26-NOV-01 12.00.00.0000000 PM -08:00");

        Instant result = sut.deserialize(jsonParserMock, null);

        Assertions.assertEquals("2001-11-26T20:00:00Z", result.toString());

    }

    @Test
    @DisplayName("Error object not deserialized")
    public void objecNotDeserialized() throws IOException {

        Mockito.when(jsonParserMock.getText()).thenReturn("GARBAGE");

        Instant result = sut.deserialize(jsonParserMock, null);

        Assertions.assertNull(result);

    }

}
