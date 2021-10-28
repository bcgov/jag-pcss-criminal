package ca.bc.gov.open.pcsscriminalapplication.controller.filecontroller;

import ca.bc.gov.open.pcsscriminalapplication.controller.FileController;
import ca.bc.gov.open.pcsscriminalapplication.exception.ORDSException;
import ca.bc.gov.open.pcsscriminalapplication.properties.PcssProperties;
import ca.bc.gov.open.pcsscriminalapplication.utils.LogBuilder;
import ca.bc.gov.open.wsdl.pcss.three.FileNoteType;
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

import static org.mockito.ArgumentMatchers.any;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("SetFileNoteTest Test")
public class SetFileNoteTest {

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
    @DisplayName("Success: post returns expected object")
    public void successTestReturns() throws JsonProcessingException {

        ca.bc.gov.open.wsdl.pcss.one.SetFileNoteResponse response = new ca.bc.gov.open.wsdl.pcss.one.SetFileNoteResponse();
        response.setResponseCd("TEST");
        response.setResponseMessageTxt("TEST");


        Mockito.when(restTemplateMock.exchange(any(String.class), any(), any(), any(Class.class))).thenReturn(ResponseEntity.ok(response));

        SetFileNoteResponse result = sut.setFileNote(createTestRequest());

        Assertions.assertNotNull(result);
        Assertions.assertEquals("TEST", result.getSetFileNoteResponse().getSetFileNoteResponse().getResponseMessageTxt());
        Assertions.assertEquals("TEST", result.getSetFileNoteResponse().getSetFileNoteResponse().getResponseCd());

    }

    @Test
    @DisplayName("Error: ords throws exception")
    public void errorOrdsException() {

        Mockito.when(restTemplateMock.exchange(any(String.class), any(), any(), any(Class.class))).thenThrow(new HTTPException(400));

        Assertions.assertThrows(ORDSException.class, () -> sut.setFileNote(createTestRequest()));

    }

    private SetFileNote createTestRequest() {

        SetFileNote setAppearanceCriminal = new SetFileNote();
        SetFileNoteRequest setAppearanceCriminalRequest = new SetFileNoteRequest();
        ca.bc.gov.open.wsdl.pcss.one.SetFileNoteRequest setAppearanceCriminalRequest1 = new ca.bc.gov.open.wsdl.pcss.one.SetFileNoteRequest();

        setAppearanceCriminalRequest1.setRequestAgencyIdentifierId("TEST");
        setAppearanceCriminalRequest1.setRequestDtm(Instant.now());
        setAppearanceCriminalRequest1.setRequestPartId("TEST");
        setAppearanceCriminalRequest1.setJustinNo("TEST");
        setAppearanceCriminalRequest1.setNoteTxt("TEST");
        setAppearanceCriminalRequest1.setFileNoteTypeCd(FileNoteType.FILE);

        setAppearanceCriminalRequest.setSetFileNoteRequest(setAppearanceCriminalRequest1);

        setAppearanceCriminal.setSetFileNoteRequest(setAppearanceCriminalRequest);

        return setAppearanceCriminal;

    }

}
