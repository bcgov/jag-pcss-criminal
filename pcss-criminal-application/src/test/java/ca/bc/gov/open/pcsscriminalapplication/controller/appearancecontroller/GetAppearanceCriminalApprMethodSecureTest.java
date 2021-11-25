package ca.bc.gov.open.pcsscriminalapplication.controller.appearancecontroller;

import ca.bc.gov.open.pcsscriminalapplication.controller.AppearanceController;
import ca.bc.gov.open.pcsscriminalapplication.exception.BadDateException;
import ca.bc.gov.open.pcsscriminalapplication.exception.ORDSException;
import ca.bc.gov.open.pcsscriminalapplication.properties.PcssProperties;
import ca.bc.gov.open.pcsscriminalapplication.service.AppearanceValidator;
import ca.bc.gov.open.pcsscriminalapplication.utils.LogBuilder;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

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

    @Mock
    private AppearanceValidator appearanceValidatorMock;

    private AppearanceController sut;

    @BeforeAll
    public void beforeAll() {

        MockitoAnnotations.openMocks(this);

        Mockito.when(pcssPropertiesMock.getHost()).thenReturn("http://localhost/");

        sut = new AppearanceController(restTemplateMock, pcssPropertiesMock, new LogBuilder(objectMapperMock), appearanceValidatorMock);

    }

    @Test
    @DisplayName("Success: get returns expected object")
    public void successTestReturns() throws JsonProcessingException {

        Mockito.when(appearanceValidatorMock.validateGetAppearanceCriminalApprMethodSecure(any())).thenReturn(new ArrayList<>());

        ca.bc.gov.open.wsdl.pcss.secure.one.GetAppearanceCriminalApprMethodResponse response = new ca.bc.gov.open.wsdl.pcss.secure.one.GetAppearanceCriminalApprMethodResponse();
        response.setResponseMessageTxt("TEST");
        response.setResponseCd("TEST");
        response.setAppearanceMethod(Collections.singletonList(new AppearanceMethod()));

        Mockito.when(restTemplateMock.exchange(any(String.class), any(), any(), any(Class.class))).thenReturn(ResponseEntity.ok(response));

        GetAppearanceCriminalApprMethodSecureResponse result = sut.getAppearanceCriminalApprMethodSecure(createTestRequest());

        Assertions.assertNotNull(result);
        Assertions.assertEquals("TEST", result.getGetAppearanceCriminalApprMethodResponse().getGetAppearanceCriminalApprMethodResponse().getResponseMessageTxt());
        Assertions.assertEquals("TEST", result.getGetAppearanceCriminalApprMethodResponse().getGetAppearanceCriminalApprMethodResponse().getResponseCd());
        Assertions.assertEquals(1, result.getGetAppearanceCriminalApprMethodResponse().getGetAppearanceCriminalApprMethodResponse().getAppearanceMethod().size());

    }

    @Test
    @DisplayName("Fail: post returns validation failure object")
    public void failTestReturns() throws JsonProcessingException {

        Mockito.when(appearanceValidatorMock.validateGetAppearanceCriminalApprMethodSecure(any())).thenReturn(Collections.singletonList("BAD DATA"));

        GetAppearanceCriminalApprMethodSecureResponse result = sut.getAppearanceCriminalApprMethodSecure(createTestRequest());

        Assertions.assertNotNull(result);
        Assertions.assertEquals("BAD DATA", result.getGetAppearanceCriminalApprMethodResponse().getGetAppearanceCriminalApprMethodResponse().getResponseMessageTxt());
        Assertions.assertEquals("-2", result.getGetAppearanceCriminalApprMethodResponse().getGetAppearanceCriminalApprMethodResponse().getResponseCd());

    }

    @Test
    @DisplayName("Error: ords throws exception")
    public void errorOrdsException() {

        Mockito.when(appearanceValidatorMock.validateGetAppearanceCriminalApprMethodSecure(any())).thenReturn(new ArrayList<>());

        Mockito.when(restTemplateMock.exchange(any(String.class), any(), any(), any(Class.class))).thenThrow(new HTTPException(400));

        Assertions.assertThrows(ORDSException.class, () -> sut.getAppearanceCriminalApprMethodSecure(createTestRequest()));

    }

    private GetAppearanceCriminalApprMethodSecure createTestRequest() {

        GetAppearanceCriminalApprMethodSecure getAppearanceCriminalApprMethodSecureRequest = new GetAppearanceCriminalApprMethodSecure();
        GetAppearanceCriminalApprMethodSecureRequest getAppearanceCriminalApprMethodRequest1 = new GetAppearanceCriminalApprMethodSecureRequest();
        ca.bc.gov.open.wsdl.pcss.secure.one.GetAppearanceCriminalApprMethodSecureRequest getAppearanceCriminalApprMethodSecureRequest2 = new ca.bc.gov.open.wsdl.pcss.secure.one.GetAppearanceCriminalApprMethodSecureRequest();

        getAppearanceCriminalApprMethodSecureRequest2.setAppearanceId("TEST");
        getAppearanceCriminalApprMethodSecureRequest2.setRequestAgencyIdentifierId("TEST");
        getAppearanceCriminalApprMethodSecureRequest2.setRequestDtm("2013-03-25 13:04:22.1");
        getAppearanceCriminalApprMethodSecureRequest2.setRequestPartId("TEST");
        getAppearanceCriminalApprMethodSecureRequest2.setApplicationCd("TEST");

        getAppearanceCriminalApprMethodRequest1.setGetAppearanceCriminalApprMethodSecureRequest(getAppearanceCriminalApprMethodSecureRequest2);

        getAppearanceCriminalApprMethodSecureRequest.setGetAppearanceCriminalApprMethodSecureRequest(getAppearanceCriminalApprMethodRequest1);

        return getAppearanceCriminalApprMethodSecureRequest;

    }

}
