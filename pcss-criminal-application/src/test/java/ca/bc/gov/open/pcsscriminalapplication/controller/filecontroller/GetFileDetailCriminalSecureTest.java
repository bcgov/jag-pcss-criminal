package ca.bc.gov.open.pcsscriminalapplication.controller.filecontroller;

import ca.bc.gov.open.pcsscriminalapplication.controller.FileController;
import ca.bc.gov.open.pcsscriminalapplication.exception.BadDateException;
import ca.bc.gov.open.pcsscriminalapplication.exception.ORDSException;
import ca.bc.gov.open.pcsscriminalapplication.properties.PcssProperties;
import ca.bc.gov.open.pcsscriminalapplication.utils.LogBuilder;
import ca.bc.gov.open.wsdl.pcss.secure.two.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.xml.ws.http.HTTPException;
import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("GetFileDetailCriminalSecureTest Test")
public class GetFileDetailCriminalSecureTest {

    @Mock
    private RestTemplate restTemplateMock;

    @Mock
    private PcssProperties pcssPropertiesMock;

    @Mock
    private ObjectMapper objectMapperMock;

    private FileController sut;

    @BeforeAll
    public void beforeAll() {

        MockitoAnnotations.openMocks(this);


        Mockito.when(pcssPropertiesMock.getHost()).thenReturn("http://localhost/");

        sut = new FileController(restTemplateMock, pcssPropertiesMock, new LogBuilder(objectMapperMock));

    }

    @Test
    @DisplayName("Success: get returns expected object")
    public void successTestReturns() throws BadDateException, JsonProcessingException {

        ca.bc.gov.open.wsdl.pcss.secure.one.GetFileDetailCriminalResponse response = new ca.bc.gov.open.wsdl.pcss.secure.one.GetFileDetailCriminalResponse();
        response.setResponseMessageTxt("TEST");
        response.setResponseCd("TEST");

        Mockito.when(restTemplateMock.exchange(any(String.class), any(), any(), any(Class.class))).thenReturn(ResponseEntity.ok(response));

        GetFileDetailCriminalSecureResponse result = sut.getFileDetailCriminalSecure(createTestRequest());

        Assertions.assertNotNull(result);
        Assertions.assertEquals("TEST", result.getGetFileDetailCriminalResponse().getGetFileDetailCriminalResponse().getResponseMessageTxt());
        Assertions.assertEquals("TEST", result.getGetFileDetailCriminalResponse().getGetFileDetailCriminalResponse().getResponseCd());

    }

    @Test
    @DisplayName("Error: ords throws exception")
    public void errorOrdsException() {

        Mockito.when(restTemplateMock.exchange(any(String.class), any(), any(), any(Class.class))).thenThrow(new HTTPException(400));

        Assertions.assertThrows(ORDSException.class, () -> sut.getFileDetailCriminalSecure(createTestRequest()));

    }

    @Test
    @DisplayName("Error: with a bad date throw exception")
    public void whenBadDateExceptionThrown() {

        Assertions.assertThrows(BadDateException.class, () -> sut.getFileDetailCriminalSecure(new GetFileDetailCriminalSecure()));

    }

    private GetFileDetailCriminalSecure createTestRequest() {

        GetFileDetailCriminalSecure getFileDetailCriminalSecure = new GetFileDetailCriminalSecure();
        ca.bc.gov.open.wsdl.pcss.secure.two.GetFileDetailCriminalRequest fileDetailCriminalRequest = new ca.bc.gov.open.wsdl.pcss.secure.two.GetFileDetailCriminalRequest();
        ca.bc.gov.open.wsdl.pcss.secure.one.GetFileDetailCriminalRequest getFileDetailCriminalRequest = new ca.bc.gov.open.wsdl.pcss.secure.one.GetFileDetailCriminalRequest();

        getFileDetailCriminalRequest.setApplicationCd("TEST");
        getFileDetailCriminalRequest.setRequestAgencyIdentifierId("TEST");
        getFileDetailCriminalRequest.setRequestDtm(Instant.now());
        getFileDetailCriminalRequest.setRequestPartId("TEST");
        getFileDetailCriminalRequest.setJustinNo("TEST");

        fileDetailCriminalRequest.setGetFileDetailCriminalRequest(getFileDetailCriminalRequest);

        getFileDetailCriminalSecure.setGetFileDetailCriminalSecureRequest(fileDetailCriminalRequest);

        return getFileDetailCriminalSecure;

    }

}
