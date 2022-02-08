package ca.bc.gov.open.pcsscriminalapplication.controller.appearancecontroller;

import static org.mockito.ArgumentMatchers.any;

import ca.bc.gov.open.pcsscriminalapplication.controller.AppearanceController;
import ca.bc.gov.open.pcsscriminalapplication.exception.ORDSException;
import ca.bc.gov.open.pcsscriminalapplication.properties.PcssProperties;
import ca.bc.gov.open.pcsscriminalapplication.utils.LogBuilder;
import ca.bc.gov.open.wsdl.pcss.one.Detail3;
import ca.bc.gov.open.wsdl.pcss.two.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.Collections;
import javax.xml.ws.http.HTTPException;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SetAppearanceMethodCriminalTest {

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

        ca.bc.gov.open.wsdl.pcss.one.SetAppearanceMethodCriminalResponse response =
                new ca.bc.gov.open.wsdl.pcss.one.SetAppearanceMethodCriminalResponse();
        response.setResponseCd("TEST");
        response.setResponseMessageTxt("TEST");

        Mockito.when(restTemplateMock.exchange(any(String.class), any(), any(), any(Class.class)))
                .thenReturn(ResponseEntity.ok(response));

        SetAppearanceMethodCriminalResponse result =
                sut.setAppearanceMethodCriminal(createTestRequest());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(
                "TEST",
                result.getSetAppearanceMethodCriminalResponse()
                        .getSetAppearanceMethodCriminalResponse()
                        .getResponseMessageTxt());
        Assertions.assertEquals(
                "TEST",
                result.getSetAppearanceMethodCriminalResponse()
                        .getSetAppearanceMethodCriminalResponse()
                        .getResponseCd());
    }

    @Test
    @DisplayName("Error: ords throws exception")
    public void errorOrdsException() {

        Mockito.when(restTemplateMock.exchange(any(String.class), any(), any(), any(Class.class)))
                .thenThrow(new HTTPException(400));

        Assertions.assertThrows(
                ORDSException.class, () -> sut.setAppearanceMethodCriminal(createTestRequest()));
    }

    private SetAppearanceMethodCriminal createTestRequest() {

        SetAppearanceMethodCriminal setAppearanceMethodCriminal = new SetAppearanceMethodCriminal();
        SetAppearanceMethodCriminalRequest setAppearanceMethodCriminalRequest =
                new SetAppearanceMethodCriminalRequest();
        ca.bc.gov.open.wsdl.pcss.one.SetAppearanceMethodCriminalRequest
                setAppearanceMethodCriminalRequest1 =
                        new ca.bc.gov.open.wsdl.pcss.one.SetAppearanceMethodCriminalRequest();

        setAppearanceMethodCriminalRequest1.setRequestAgencyIdentifierId("TEST");
        setAppearanceMethodCriminalRequest1.setRequestDtm(Instant.now());
        setAppearanceMethodCriminalRequest1.setRequestPartId("TEST");
        setAppearanceMethodCriminalRequest1.setDetail(Collections.singletonList(new Detail3()));

        setAppearanceMethodCriminalRequest.setSetAppearanceMethodCriminalRequest(
                setAppearanceMethodCriminalRequest1);

        setAppearanceMethodCriminal.setSetAppearanceMethodCriminalRequest(
                setAppearanceMethodCriminalRequest);

        return setAppearanceMethodCriminal;
    }
}
