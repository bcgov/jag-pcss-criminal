package ca.bc.gov.open.pcsscriminalapplication.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("LogHelper Test")
public class LogBuilderTest {

    LogBuilder sut;

    @BeforeAll
    public void beforeAll() {

        sut = new LogBuilder(new ObjectMapper());
    }

    @Test
    @DisplayName("Test data elements converted to string")
    public void testElementsConvertedToString() throws JsonProcessingException {

        String result = sut.writeLogMessage("TEST", "TEST", null, "TEST");

        Assertions.assertEquals(
                "{\"message\":\"TEST\",\"method\":\"TEST\",\"exception\":\"TEST\",\"request\":null}",
                result);
    }
}
