package ca.bc.gov.open.pcsscriminalapplication.controller.appearancecontroller;

import ca.bc.gov.open.pcsscriminalapplication.controller.AppearanceController;
import ca.bc.gov.open.pcsscriminalapplication.exception.BadDateException;
import ca.bc.gov.open.pcsscriminalapplication.exception.ORDSException;
import ca.bc.gov.open.pcsscriminalapplication.properties.PcssProperties;
import ca.bc.gov.open.wsdl.pcss.secure.one.AppearanceMethod;
import ca.bc.gov.open.wsdl.pcss.secure.two.GetAppearanceCriminalApprMethodSecure;
import ca.bc.gov.open.wsdl.pcss.secure.two.GetAppearanceCriminalApprMethodSecureRequest;
import ca.bc.gov.open.wsdl.pcss.secure.two.GetAppearanceCriminalApprMethodSecureResponse;
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
@DisplayName("GetAppearanceCriminalApprMethodSecure Test")
public class GetAppearanceCriminalApprMethodSecureTest {

    @Mock
    private RestTemplate restTemplateMock;

    @Mock
    private PcssProperties pcssPropertiesMock;

    @Mock
    private ObjectMapper objectMapperMock;

    private AppearanceController sut;

    @BeforeAll
    public void beforeAll() {

        MockitoAnnotations.openMocks(this);

        Mockito.when(pcssPropertiesMock.getHost()).thenReturn("http://localhost/");

        sut = new AppearanceController(restTemplateMock, pcssPropertiesMock, objectMapperMock);

    }

    @Test
    @DisplayName("Success: get returns expected object")
    public void successTestReturns() throws BadDateException, JsonProcessingException {

        GetAppearanceCriminalApprMethodSecure getAppearanceCriminalApprMethodSecureRequest = new GetAppearanceCriminalApprMethodSecure();
        GetAppearanceCriminalApprMethodSecureRequest getAppearanceCriminalApprMethodRequest1 = new GetAppearanceCriminalApprMethodSecureRequest();
        ca.bc.gov.open.wsdl.pcss.secure.one.GetAppearanceCriminalApprMethodSecureRequest getAppearanceCriminalApprMethodSecureRequest2 = new ca.bc.gov.open.wsdl.pcss.secure.one.GetAppearanceCriminalApprMethodSecureRequest();

        getAppearanceCriminalApprMethodSecureRequest2.setAppearanceId("TEST");
        getAppearanceCriminalApprMethodSecureRequest2.setRequestAgencyIdentifierId("TEST");
        getAppearanceCriminalApprMethodSecureRequest2.setRequestDtm(Instant.now());
        getAppearanceCriminalApprMethodSecureRequest2.setRequestPartId("TEST");
        getAppearanceCriminalApprMethodSecureRequest2.setApplicationCd("TEST");

        getAppearanceCriminalApprMethodRequest1.setGetAppearanceCriminalApprMethodSecureRequest(getAppearanceCriminalApprMethodSecureRequest2);

        getAppearanceCriminalApprMethodSecureRequest.setGetAppearanceCriminalApprMethodSecureRequest(getAppearanceCriminalApprMethodRequest1);

        ca.bc.gov.open.wsdl.pcss.secure.one.GetAppearanceCriminalApprMethodResponse response = new ca.bc.gov.open.wsdl.pcss.secure.one.GetAppearanceCriminalApprMethodResponse();
        response.setResponseMessageTxt("TEST");
        response.setResponseCd("TEST");
        response.setAppearanceMethod(Collections.singletonList(new AppearanceMethod()));

        Mockito.when(restTemplateMock.exchange(any(String.class), any(), any(), any(Class.class))).thenReturn(ResponseEntity.ok(response));

        GetAppearanceCriminalApprMethodSecureResponse result = sut.getAppearanceCriminalApprMethodSecure(getAppearanceCriminalApprMethodSecureRequest);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("TEST", result.getGetAppearanceCriminalApprMethodResponse().getGetAppearanceCriminalApprMethodResponse().getResponseMessageTxt());
        Assertions.assertEquals("TEST", result.getGetAppearanceCriminalApprMethodResponse().getGetAppearanceCriminalApprMethodResponse().getResponseCd());
        Assertions.assertEquals(1, result.getGetAppearanceCriminalApprMethodResponse().getGetAppearanceCriminalApprMethodResponse().getAppearanceMethod().size());

    }

    @Test
    @DisplayName("Error: ords throws exception")
    public void errorOrdsException() {

        GetAppearanceCriminalApprMethodSecure getAppearanceCriminalApprMethodSecureRequest = new GetAppearanceCriminalApprMethodSecure();
        GetAppearanceCriminalApprMethodSecureRequest getAppearanceCriminalApprMethodRequest1 = new GetAppearanceCriminalApprMethodSecureRequest();
        ca.bc.gov.open.wsdl.pcss.secure.one.GetAppearanceCriminalApprMethodSecureRequest getAppearanceCriminalApprMethodSecureRequest2 = new ca.bc.gov.open.wsdl.pcss.secure.one.GetAppearanceCriminalApprMethodSecureRequest();

        getAppearanceCriminalApprMethodSecureRequest2.setAppearanceId("TEST");
        getAppearanceCriminalApprMethodSecureRequest2.setRequestAgencyIdentifierId("TEST");
        getAppearanceCriminalApprMethodSecureRequest2.setRequestDtm(Instant.now());
        getAppearanceCriminalApprMethodSecureRequest2.setRequestPartId("TEST");
        getAppearanceCriminalApprMethodSecureRequest2.setApplicationCd("TEST");

        getAppearanceCriminalApprMethodRequest1.setGetAppearanceCriminalApprMethodSecureRequest(getAppearanceCriminalApprMethodSecureRequest2);

        getAppearanceCriminalApprMethodSecureRequest.setGetAppearanceCriminalApprMethodSecureRequest(getAppearanceCriminalApprMethodRequest1);

        Mockito.when(restTemplateMock.exchange(any(String.class), any(), any(), any(Class.class))).thenThrow(new HTTPException(400));

        Assertions.assertThrows(ORDSException.class, () -> sut.getAppearanceCriminalApprMethodSecure(getAppearanceCriminalApprMethodSecureRequest));

    }

    @Test
    @DisplayName("Error: with a bad date throw exception")
    public void whenBadDateExceptionThrown() throws BadDateException, JsonProcessingException {

        Assertions.assertThrows(BadDateException.class, () -> sut.getAppearanceCriminalApprMethodSecure(new GetAppearanceCriminalApprMethodSecure()));

    }

}
