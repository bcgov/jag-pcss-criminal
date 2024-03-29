package ca.bc.gov.open.pcsscriminalapplication.controller.appearancecontroller;

import static org.mockito.ArgumentMatchers.any;

import ca.bc.gov.open.pcsscriminalapplication.controller.AppearanceController;
import ca.bc.gov.open.pcsscriminalapplication.exception.ORDSException;
import ca.bc.gov.open.pcsscriminalapplication.properties.PcssProperties;
import ca.bc.gov.open.pcsscriminalapplication.utils.LogBuilder;
import ca.bc.gov.open.wsdl.pcss.one.Detail;
import ca.bc.gov.open.wsdl.pcss.one.Detail2;
import ca.bc.gov.open.wsdl.pcss.two.SetAppearanceCriminal;
import ca.bc.gov.open.wsdl.pcss.two.SetAppearanceCriminalRequest;
import ca.bc.gov.open.wsdl.pcss.two.SetAppearanceCriminalResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.xml.ws.http.HTTPException;
import java.net.URI;
import java.time.Instant;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("SetAppearanceCriminal Test")
public class SetAppearanceCriminalTest {

    @Mock private RestTemplate restTemplateMock;

    @Mock private PcssProperties pcssPropertiesMock;

    @Mock private ObjectMapper objectMapperMock;

    private AppearanceController sut;

    @BeforeAll
    public void beforeAll() {

        MockitoAnnotations.openMocks(this);

        Mockito.when(pcssPropertiesMock.getHost()).thenReturn("http://localhost/");

        sut =
                new AppearanceController(
                        restTemplateMock, pcssPropertiesMock, new LogBuilder(objectMapperMock));
    }

    @Test
    @DisplayName("Success: post returns expected object")
    public void successTestReturns() throws JsonProcessingException {

        ca.bc.gov.open.wsdl.pcss.one.SetAppearanceCriminalResponse response =
                new ca.bc.gov.open.wsdl.pcss.one.SetAppearanceCriminalResponse();
        response.setResponseCd("TEST");
        response.setResponseMessageTxt("TEST");
        response.getDetail().add(new Detail2());

        Mockito.when(restTemplateMock.exchange(any(URI.class), any(), any(), any(Class.class)))
                .thenReturn(ResponseEntity.ok(response));

        SetAppearanceCriminalResponse result = sut.setAppearanceCriminal(createTestRequest());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(
                "TEST",
                result.getSetAppearanceCriminalResponse()
                        .getSetAppearanceCriminalResponse()
                        .getResponseMessageTxt());
        Assertions.assertEquals(
                "TEST",
                result.getSetAppearanceCriminalResponse()
                        .getSetAppearanceCriminalResponse()
                        .getResponseCd());
        Assertions.assertEquals(
                1,
                result.getSetAppearanceCriminalResponse()
                        .getSetAppearanceCriminalResponse()
                        .getDetail()
                        .size());
    }

    @Test
    @DisplayName("Error: ords throws exception")
    public void errorOrdsException() {

        Mockito.when(restTemplateMock.exchange(any(URI.class), any(), any(), any(Class.class)))
                .thenThrow(new HTTPException(400));

        Assertions.assertThrows(
                ORDSException.class, () -> sut.setAppearanceCriminal(createTestRequest()));
    }

    private SetAppearanceCriminal createTestRequest() {

        SetAppearanceCriminal setAppearanceCriminal = new SetAppearanceCriminal();
        SetAppearanceCriminalRequest setAppearanceCriminalRequest =
                new SetAppearanceCriminalRequest();
        ca.bc.gov.open.wsdl.pcss.one.SetAppearanceCriminalRequest setAppearanceCriminalRequest1 =
                new ca.bc.gov.open.wsdl.pcss.one.SetAppearanceCriminalRequest();

        setAppearanceCriminalRequest1.setRequestAgencyIdentifierId("TEST");
        setAppearanceCriminalRequest1.setRequestDtm(Instant.now());
        setAppearanceCriminalRequest1.setRequestPartId("TEST");
        setAppearanceCriminalRequest1.getDetail().add(new Detail());

        setAppearanceCriminalRequest.setSetAppearanceCriminalRequest(setAppearanceCriminalRequest1);

        setAppearanceCriminal.setSetAppearanceCriminalRequest(setAppearanceCriminalRequest);

        return setAppearanceCriminal;
    }
}
