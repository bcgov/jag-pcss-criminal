package ca.bc.gov.open.pcsscriminalapplication.controller.appearancecontroller;

import ca.bc.gov.open.pcsscriminalapplication.controller.AppearanceController;
import ca.bc.gov.open.pcsscriminalapplication.exception.BadDateException;
import ca.bc.gov.open.pcsscriminalapplication.exception.ORDSException;
import ca.bc.gov.open.pcsscriminalapplication.properties.PcssProperties;
import ca.bc.gov.open.pcsscriminalapplication.service.AppearanceValidator;
import ca.bc.gov.open.pcsscriminalapplication.utils.LogBuilder;
import ca.bc.gov.open.wsdl.pcss.secure.one.ApprCount;
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
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("GetAppearanceCriminalCountSecure Test")
public class GetAppearanceCriminalCountSecureTest {

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
    public void successTestReturns() throws BadDateException, JsonProcessingException {

        ca.bc.gov.open.wsdl.pcss.secure.one.GetAppearanceCriminalCountResponse response = new ca.bc.gov.open.wsdl.pcss.secure.one.GetAppearanceCriminalCountResponse();
        response.setResponseMessageTxt("TEST");
        response.setResponseCd("TEST");
        response.setApprCount(Collections.singletonList(new ApprCount()));

        Mockito.when(restTemplateMock.exchange(any(String.class), any(), any(), any(Class.class))).thenReturn(ResponseEntity.ok(response));

        GetAppearanceCriminalCountSecureResponse result = sut.getAppearanceCriminalCountSecure(createTestRequest());

        Assertions.assertNotNull(result);
        Assertions.assertEquals("TEST", result.getGetAppearanceCriminalCountResponse().getGetAppearanceCriminalCountResponse().getResponseMessageTxt());
        Assertions.assertEquals("TEST", result.getGetAppearanceCriminalCountResponse().getGetAppearanceCriminalCountResponse().getResponseCd());
        Assertions.assertEquals(1, result.getGetAppearanceCriminalCountResponse().getGetAppearanceCriminalCountResponse().getApprCount().size());

    }

    @Test
    @DisplayName("Error: ords throws exception")
    public void errorOrdsException() {

        Mockito.when(restTemplateMock.exchange(any(String.class), any(), any(), any(Class.class))).thenThrow(new HTTPException(400));

        Assertions.assertThrows(ORDSException.class, () -> sut.getAppearanceCriminalCountSecure(createTestRequest()));

    }

    @Test
    @DisplayName("Error: with a bad date throw exception")
    public void whenBadDateExceptionThrown() {

        Assertions.assertThrows(BadDateException.class, () -> sut.getAppearanceCriminalCountSecure(new GetAppearanceCriminalCountSecure()));

    }

    private GetAppearanceCriminalCountSecure createTestRequest() {

        GetAppearanceCriminalCountSecure getAppearanceCriminalCountSecure = new GetAppearanceCriminalCountSecure();
        GetAppearanceCriminalCountSecureRequest getAppearanceCriminalCountSecureRequest = new GetAppearanceCriminalCountSecureRequest();
        ca.bc.gov.open.wsdl.pcss.secure.one.GetAppearanceCriminalCountSecureRequest getAppearanceCriminalCountSecureRequest1 = new ca.bc.gov.open.wsdl.pcss.secure.one.GetAppearanceCriminalCountSecureRequest();

        getAppearanceCriminalCountSecureRequest1.setAppearanceId("TEST");
        getAppearanceCriminalCountSecureRequest1.setRequestAgencyIdentifierId("TEST");
        getAppearanceCriminalCountSecureRequest1.setRequestDtm(Instant.now());
        getAppearanceCriminalCountSecureRequest1.setRequestPartId("TEST");
        getAppearanceCriminalCountSecureRequest1.setApplicationCd("TEST");

        getAppearanceCriminalCountSecureRequest.setGetAppearanceCriminalCountSecureRequest(getAppearanceCriminalCountSecureRequest1);

        getAppearanceCriminalCountSecure.setGetAppearanceCriminalCountSecureRequest(getAppearanceCriminalCountSecureRequest);

        return getAppearanceCriminalCountSecure;

    }

}
