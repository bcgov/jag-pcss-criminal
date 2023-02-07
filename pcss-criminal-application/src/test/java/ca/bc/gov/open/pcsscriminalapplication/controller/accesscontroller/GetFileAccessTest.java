package ca.bc.gov.open.pcsscriminalapplication.controller.accesscontroller;

import static org.mockito.ArgumentMatchers.any;

import ca.bc.gov.open.pcsscriminalapplication.controller.AccessController;
import ca.bc.gov.open.pcsscriminalapplication.exception.ORDSException;
import ca.bc.gov.open.pcsscriminalapplication.properties.PcssProperties;
import ca.bc.gov.open.pcsscriminalapplication.utils.LogBuilder;
import ca.bc.gov.open.wsdl.pcss.one.GetFileAccessResponse;
import ca.bc.gov.open.wsdl.pcss.one.JustinFileAccess;
import ca.bc.gov.open.wsdl.pcss.three.JustinFileAccessType;
import ca.bc.gov.open.wsdl.pcss.two.GetFileAccess;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import javax.xml.ws.http.HTTPException;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("GetFileAccess Test")
public class GetFileAccessTest {

    @Mock private PcssProperties pcssPropertiesMock;

    @Mock private RestTemplate restTemplateMock;

    @Mock private ObjectMapper objectMapperMock;

    private AccessController sut;

    @BeforeAll
    public void beforeAll() {
        MockitoAnnotations.openMocks(this);

        Mockito.when(pcssPropertiesMock.getHost()).thenReturn("http://localhost/");

        sut =
                new AccessController(
                        restTemplateMock, pcssPropertiesMock, new LogBuilder(objectMapperMock));
    }

    @Test
    @DisplayName("Success: get returns expected object")
    public void successTestReturns() throws JsonProcessingException {
        GetFileAccessResponse response = new GetFileAccessResponse();
        response.setResponseCd("TEST");
        List<JustinFileAccess> value = new ArrayList<>();
        JustinFileAccess justinFileAccess = new JustinFileAccess();
        justinFileAccess.setJustinNo("TEST");
        justinFileAccess.setAccessCd(JustinFileAccessType.ALLOWED);
        value.add(justinFileAccess);
        response.setFileAccess(value);

        Mockito.when(restTemplateMock.exchange(any(URI.class), any(), any(), any(Class.class)))
                .thenReturn(ResponseEntity.ok(response));

        GetFileAccessResponse result = sut.getFileAccess(new GetFileAccess());

        Assertions.assertNotNull(result);
        Assertions.assertEquals("TEST", result.getResponseCd());
        Assertions.assertEquals("TEST", result.getFileAccess());
    }

    @Test
    @DisplayName("Error: ords throws exception")
    public void errorOrdsException() {
        Mockito.when(restTemplateMock.exchange(any(URI.class), any(), any(), any(Class.class)))
                .thenThrow(new HTTPException(400));

        Assertions.assertThrows(ORDSException.class, () -> sut.getFileAccess(new GetFileAccess()));
    }
}
