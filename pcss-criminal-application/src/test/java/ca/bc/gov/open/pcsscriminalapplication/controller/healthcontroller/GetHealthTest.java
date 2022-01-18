package ca.bc.gov.open.pcsscriminalapplication.controller.healthcontroller;

import static org.mockito.ArgumentMatchers.any;

import ca.bc.gov.open.pcsscriminalapplication.controller.HealthController;
import ca.bc.gov.open.pcsscriminalapplication.exception.ORDSException;
import ca.bc.gov.open.pcsscriminalapplication.properties.PcssProperties;
import ca.bc.gov.open.pcsscriminalapplication.utils.LogBuilder;
import ca.bc.gov.open.wsdl.pcss.two.GetHealth;
import ca.bc.gov.open.wsdl.pcss.two.GetHealthResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.xml.ws.http.HTTPException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("GetHealth Test")
public class GetHealthTest {

    @Mock private PcssProperties pcssPropertiesMock;

    @Mock private RestTemplate restTemplateMock;

    @Mock private ObjectMapper objectMapperMock;

    private HealthController sut;

    @BeforeAll
    public void beforeAll() {
        MockitoAnnotations.openMocks(this);

        Mockito.when(pcssPropertiesMock.getHost()).thenReturn("http://localhost/");

        sut =
                new HealthController(
                        restTemplateMock, pcssPropertiesMock, new LogBuilder(objectMapperMock));
    }

    @Test
    @DisplayName("Success: get returns expected object")
    public void successTestReturns() throws JsonProcessingException {
        GetHealthResponse response = new GetHealthResponse();
        response.setAppid("TEST");
        response.setStatus("TEST");
        response.setCompatibility("TEST");
        response.setHost("TEST");
        response.setInstance("TEST");
        response.setMethod("TEST");
        response.setVersion("TEST");

        Mockito.when(restTemplateMock.exchange(any(String.class), any(), any(), any(Class.class)))
                .thenReturn(ResponseEntity.ok(response));

        GetHealthResponse result = sut.getHealth(new GetHealth());

        Assertions.assertNotNull(result);
        Assertions.assertEquals("TEST", result.getAppid());
        Assertions.assertEquals("TEST", result.getStatus());
        Assertions.assertEquals("TEST", result.getCompatibility());
        Assertions.assertEquals("TEST", result.getHost());
        Assertions.assertEquals("TEST", result.getInstance());
        Assertions.assertEquals("TEST", result.getMethod());
        Assertions.assertEquals("TEST", result.getVersion());
    }

    @Test
    @DisplayName("Error: ords throws exception")
    public void errorOrdsException() {
        Mockito.when(restTemplateMock.exchange(any(String.class), any(), any(), any(Class.class)))
                .thenThrow(new HTTPException(400));

        Assertions.assertThrows(ORDSException.class, () -> sut.getHealth(new GetHealth()));
    }
}
