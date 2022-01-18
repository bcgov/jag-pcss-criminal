package ca.bc.gov.open.pcsscriminalapplication.controller.filecontroller;

import static org.mockito.ArgumentMatchers.any;

import ca.bc.gov.open.pcsscriminalapplication.controller.FileController;
import ca.bc.gov.open.pcsscriminalapplication.exception.ORDSException;
import ca.bc.gov.open.pcsscriminalapplication.properties.PcssProperties;
import ca.bc.gov.open.pcsscriminalapplication.service.FileValidator;
import ca.bc.gov.open.pcsscriminalapplication.utils.LogBuilder;
import ca.bc.gov.open.wsdl.pcss.three.FileNoteType;
import ca.bc.gov.open.wsdl.pcss.two.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Collections;
import javax.xml.ws.http.HTTPException;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("SetFileNoteTest Test")
public class SetFileNoteTest {

    @Mock private RestTemplate restTemplateMock;

    @Mock private PcssProperties pcssPropertiesMock;

    @Mock private ObjectMapper objectMapperMock;

    @Mock private FileValidator fileValidatorMock;

    private FileController sut;

    @BeforeAll
    public void beforeAll() {

        MockitoAnnotations.openMocks(this);

        Mockito.when(pcssPropertiesMock.getHost()).thenReturn("http://localhost/");

        sut =
                new FileController(
                        restTemplateMock,
                        pcssPropertiesMock,
                        new LogBuilder(objectMapperMock),
                        fileValidatorMock);
    }

    @Test
    @DisplayName("Success: post returns expected object")
    public void successTestReturns() throws JsonProcessingException {

        ca.bc.gov.open.wsdl.pcss.one.SetFileNoteResponse response =
                new ca.bc.gov.open.wsdl.pcss.one.SetFileNoteResponse();
        response.setResponseCd("TEST");
        response.setResponseMessageTxt("TEST");

        Mockito.when(fileValidatorMock.validateSetFileNote(any())).thenReturn(new ArrayList<>());

        Mockito.when(restTemplateMock.exchange(any(String.class), any(), any(), any(Class.class)))
                .thenReturn(ResponseEntity.ok(response));

        SetFileNoteResponse result = sut.setFileNote(createTestRequest());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(
                "TEST",
                result.getSetFileNoteResponse().getSetFileNoteResponse().getResponseMessageTxt());
        Assertions.assertEquals(
                "TEST", result.getSetFileNoteResponse().getSetFileNoteResponse().getResponseCd());
    }

    @Test
    @DisplayName("Fail: post returns validation failure object")
    public void failTestReturns() throws JsonProcessingException {

        Mockito.when(fileValidatorMock.validateSetFileNote(any()))
                .thenReturn(Collections.singletonList("BAD DATA"));

        SetFileNoteResponse result = sut.setFileNote(createTestRequest());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(
                "BAD DATA",
                result.getSetFileNoteResponse().getSetFileNoteResponse().getResponseMessageTxt());
        Assertions.assertEquals(
                "-2", result.getSetFileNoteResponse().getSetFileNoteResponse().getResponseCd());
    }

    @Test
    @DisplayName("Error: ords throws exception")
    public void errorOrdsException() {

        Mockito.when(fileValidatorMock.validateSetFileNote(any())).thenReturn(new ArrayList<>());

        Mockito.when(restTemplateMock.exchange(any(String.class), any(), any(), any(Class.class)))
                .thenThrow(new HTTPException(400));

        Assertions.assertThrows(ORDSException.class, () -> sut.setFileNote(createTestRequest()));
    }

    private SetFileNote createTestRequest() {

        SetFileNote setAppearanceCriminal = new SetFileNote();
        SetFileNoteRequest setAppearanceCriminalRequest = new SetFileNoteRequest();
        ca.bc.gov.open.wsdl.pcss.one.SetFileNoteRequest setAppearanceCriminalRequest1 =
                new ca.bc.gov.open.wsdl.pcss.one.SetFileNoteRequest();

        setAppearanceCriminalRequest1.setRequestAgencyIdentifierId("TEST");
        setAppearanceCriminalRequest1.setRequestDtm("2013-03-25 13:04:22.1");
        setAppearanceCriminalRequest1.setRequestPartId("TEST");
        setAppearanceCriminalRequest1.setJustinNo("TEST");
        setAppearanceCriminalRequest1.setNoteTxt("TEST");
        setAppearanceCriminalRequest1.setFileNoteTypeCd(FileNoteType.FILE);

        setAppearanceCriminalRequest.setSetFileNoteRequest(setAppearanceCriminalRequest1);

        setAppearanceCriminal.setSetFileNoteRequest(setAppearanceCriminalRequest);

        return setAppearanceCriminal;
    }
}
