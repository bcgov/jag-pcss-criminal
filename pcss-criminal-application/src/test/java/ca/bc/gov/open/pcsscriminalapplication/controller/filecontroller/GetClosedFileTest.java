package ca.bc.gov.open.pcsscriminalapplication.controller.filecontroller;

import ca.bc.gov.open.pcsscriminalapplication.controller.FileController;
import ca.bc.gov.open.pcsscriminalapplication.exception.BadDateException;
import ca.bc.gov.open.pcsscriminalapplication.exception.ORDSException;
import ca.bc.gov.open.pcsscriminalapplication.properties.PcssProperties;
import ca.bc.gov.open.pcsscriminalapplication.utils.LogBuilder;
import ca.bc.gov.open.wsdl.pcss.one.CourtFile;
import ca.bc.gov.open.wsdl.pcss.secure.one.AppearanceMethod;
import ca.bc.gov.open.wsdl.pcss.secure.two.GetAppearanceCriminalApprMethodSecure;
import ca.bc.gov.open.wsdl.pcss.secure.two.GetAppearanceCriminalApprMethodSecureRequest;
import ca.bc.gov.open.wsdl.pcss.secure.two.GetAppearanceCriminalApprMethodSecureResponse;
import ca.bc.gov.open.wsdl.pcss.two.GetClosedFile;
import ca.bc.gov.open.wsdl.pcss.two.GetClosedFileRequest;
import ca.bc.gov.open.wsdl.pcss.two.GetClosedFileResponse;
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
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("GetClosedFileTest Test")
public class GetClosedFileTest {

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

        ca.bc.gov.open.wsdl.pcss.one.GetClosedFileResponse response = new ca.bc.gov.open.wsdl.pcss.one.GetClosedFileResponse();
        response.setResponseMessageTxt("TEST");
        response.setResponseCd("TEST");
        response.setCourtFile(Collections.singletonList(new CourtFile()));

        Mockito.when(restTemplateMock.exchange(any(String.class), any(), any(), any(Class.class))).thenReturn(ResponseEntity.ok(response));

        GetClosedFileResponse result = sut.getClosedFile(createTestRequest());

        Assertions.assertNotNull(result);
        Assertions.assertEquals("TEST", result.getGetClosedFileResponce().getGetClosedFileResponse().getResponseMessageTxt());
        Assertions.assertEquals("TEST", result.getGetClosedFileResponce().getGetClosedFileResponse().getResponseCd());
        Assertions.assertEquals(1, result.getGetClosedFileResponce().getGetClosedFileResponse().getCourtFile().size());

    }

    @Test
    @DisplayName("Error: ords throws exception")
    public void errorOrdsException() {

        Mockito.when(restTemplateMock.exchange(any(String.class), any(), any(), any(Class.class))).thenThrow(new HTTPException(400));

        Assertions.assertThrows(ORDSException.class, () -> sut.getClosedFile(createTestRequest()));

    }

    @Test
    @DisplayName("Error: with a bad date throw exception")
    public void whenBadDateExceptionThrown() {

        Assertions.assertThrows(BadDateException.class, () -> sut.getClosedFile(new GetClosedFile()));

    }

    private GetClosedFile createTestRequest() {

        GetClosedFile getClosedFile = new GetClosedFile();
        GetClosedFileRequest getClosedFileRequest = new GetClosedFileRequest();
        ca.bc.gov.open.wsdl.pcss.one.GetClosedFileRequest getClosedFileRequest1 = new ca.bc.gov.open.wsdl.pcss.one.GetClosedFileRequest();

        getClosedFileRequest1.setAgencyId("TEST");
        getClosedFileRequest1.setRequestAgencyIdentifierId("TEST");
        getClosedFileRequest1.setRequestDtm(Instant.now());
        getClosedFileRequest1.setRequestPartId("TEST");
        getClosedFileRequest1.setFromApprDt(Instant.now());
        getClosedFileRequest1.setToApprDt(Instant.now());

        getClosedFileRequest.setGetClosedFileRequest(getClosedFileRequest1);

        getClosedFile.setGetClosedFileRequest(getClosedFileRequest);

        return getClosedFile;

    }

}
