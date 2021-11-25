package ca.bc.gov.open.pcsscriminalapplication.controller.filecontroller;

import ca.bc.gov.open.pcsscriminalapplication.controller.FileController;
import ca.bc.gov.open.pcsscriminalapplication.exception.BadDateException;
import ca.bc.gov.open.pcsscriminalapplication.exception.ORDSException;
import ca.bc.gov.open.pcsscriminalapplication.properties.PcssProperties;
import ca.bc.gov.open.pcsscriminalapplication.service.FileValidator;
import ca.bc.gov.open.pcsscriminalapplication.utils.LogBuilder;
import ca.bc.gov.open.wsdl.pcss.secure.two.GetFileDetailCriminalSecureResponse;
import ca.bc.gov.open.wsdl.pcss.two.*;
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
import java.util.ArrayList;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("GetFileDetailCriminalTest Test")
public class GetFileDetailCriminalTest {

    @Mock
    private RestTemplate restTemplateMock;

    @Mock
    private PcssProperties pcssPropertiesMock;

    @Mock
    private ObjectMapper objectMapperMock;

    @Mock
    private FileValidator fileValidatorMock;

    private FileController sut;

    @BeforeAll
    public void beforeAll() {

        MockitoAnnotations.openMocks(this);

        Mockito.when(pcssPropertiesMock.getHost()).thenReturn("http://localhost/");

        sut = new FileController(restTemplateMock, pcssPropertiesMock, new LogBuilder(objectMapperMock), fileValidatorMock);

    }

    @Test
    @DisplayName("Success: get returns expected object")
    public void successTestReturns() throws BadDateException, JsonProcessingException {

        ca.bc.gov.open.wsdl.pcss.one.GetFileDetailCriminalResponse response = new ca.bc.gov.open.wsdl.pcss.one.GetFileDetailCriminalResponse();
        response.setResponseMessageTxt("TEST");
        response.setResponseCd("TEST");

        Mockito.when(fileValidatorMock.validateGetFileDetailCriminal(any())).thenReturn(new ArrayList<>());

        Mockito.when(restTemplateMock.exchange(any(String.class), any(), any(), any(Class.class))).thenReturn(ResponseEntity.ok(response));

        GetFileDetailCriminalResponse result = sut.getFileDetailCriminal(createTestRequest());

        Assertions.assertNotNull(result);
        Assertions.assertEquals("TEST", result.getGetFileDetailCriminalResponse().getGetFileDetailCriminalResponse().getResponseMessageTxt());
        Assertions.assertEquals("TEST", result.getGetFileDetailCriminalResponse().getGetFileDetailCriminalResponse().getResponseCd());

    }

    @Test
    @DisplayName("Error: ords throws exception")
    public void errorOrdsException() {

        Mockito.when(fileValidatorMock.validateGetFileDetailCriminal(any())).thenReturn(new ArrayList<>());

        Mockito.when(restTemplateMock.exchange(any(String.class), any(), any(), any(Class.class))).thenThrow(new HTTPException(400));

        Assertions.assertThrows(ORDSException.class, () -> sut.getFileDetailCriminal(createTestRequest()));

    }

    @Test
    @DisplayName("Fail: post returns validation failure object")
    public void failTestReturns() throws JsonProcessingException {

        Mockito.when(fileValidatorMock.validateGetFileDetailCriminal(any())).thenReturn(Collections.singletonList("BAD DATA"));

        GetFileDetailCriminalResponse result = sut.getFileDetailCriminal(createTestRequest());

        Assertions.assertNotNull(result);
        Assertions.assertEquals("BAD DATA", result.getGetFileDetailCriminalResponse().getGetFileDetailCriminalResponse().getResponseMessageTxt());
        Assertions.assertEquals("-2", result.getGetFileDetailCriminalResponse().getGetFileDetailCriminalResponse().getResponseCd());

    }

    private GetFileDetailCriminal createTestRequest() {

        GetFileDetailCriminal getFileDetailCriminal = new GetFileDetailCriminal();
        GetFileDetailCriminalRequest getFileDetailCriminalRequest = new GetFileDetailCriminalRequest();
        ca.bc.gov.open.wsdl.pcss.one.GetFileDetailCriminalRequest getClosedFileRequest1 = new ca.bc.gov.open.wsdl.pcss.one.GetFileDetailCriminalRequest();

        getClosedFileRequest1.setApplicationCd("TEST");
        getClosedFileRequest1.setRequestAgencyIdentifierId("TEST");
        getClosedFileRequest1.setRequestDtm("2013-03-25 13:04:22.1");
        getClosedFileRequest1.setRequestPartId("TEST");
        getClosedFileRequest1.setJustinNo("TEST");

        getFileDetailCriminalRequest.setGetFileDetailCriminalRequest(getClosedFileRequest1);

        getFileDetailCriminal.setGetFileDetailCriminalRequest(getFileDetailCriminalRequest);

        return getFileDetailCriminal;

    }

}
