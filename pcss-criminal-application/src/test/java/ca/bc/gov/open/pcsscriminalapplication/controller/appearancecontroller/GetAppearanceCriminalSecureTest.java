package ca.bc.gov.open.pcsscriminalapplication.controller.appearancecontroller;

import static org.mockito.ArgumentMatchers.any;

import ca.bc.gov.open.pcsscriminalapplication.controller.AppearanceController;
import ca.bc.gov.open.pcsscriminalapplication.exception.ORDSException;
import ca.bc.gov.open.pcsscriminalapplication.properties.PcssProperties;
import ca.bc.gov.open.pcsscriminalapplication.utils.LogBuilder;
import ca.bc.gov.open.wsdl.pcss.secure.one.ApprDetail;
import ca.bc.gov.open.wsdl.pcss.secure.two.*;
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
@DisplayName("GetAppearanceCriminalSecure Test")
public class GetAppearanceCriminalSecureTest {

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
    @DisplayName("Success: get returns expected object")
    public void successTestReturns() throws JsonProcessingException {

        ca.bc.gov.open.wsdl.pcss.secure.one.GetAppearanceCriminalResponse response =
                new ca.bc.gov.open.wsdl.pcss.secure.one.GetAppearanceCriminalResponse();
        response.setResponseMessageTxt("TEST");
        response.setResponseCd("TEST");
        response.getApprDetail().add(new ApprDetail());

        Mockito.when(restTemplateMock.exchange(any(URI.class), any(), any(), any(Class.class)))
                .thenReturn(ResponseEntity.ok(response));

        GetAppearanceCriminalSecureResponse result =
                sut.getAppearanceCriminalSecure(createTestRequest());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(
                "TEST",
                result.getGetAppearanceCriminalResponse()
                        .getGetAppearanceCriminalResponse()
                        .getResponseMessageTxt());
        Assertions.assertEquals(
                "TEST",
                result.getGetAppearanceCriminalResponse()
                        .getGetAppearanceCriminalResponse()
                        .getResponseCd());
        Assertions.assertEquals(
                1,
                result.getGetAppearanceCriminalResponse()
                        .getGetAppearanceCriminalResponse()
                        .getApprDetail()
                        .size());
    }

    @Test
    @DisplayName("Error: ords throws exception")
    public void errorOrdsException() {

        Mockito.when(restTemplateMock.exchange(any(URI.class), any(), any(), any(Class.class)))
                .thenThrow(new HTTPException(400));

        Assertions.assertThrows(
                ORDSException.class, () -> sut.getAppearanceCriminalSecure(createTestRequest()));
    }

    private GetAppearanceCriminalSecure createTestRequest() {

        GetAppearanceCriminalSecure getAppearanceCriminalSecure = new GetAppearanceCriminalSecure();
        GetAppearanceCriminalSecureRequest getAppearanceCriminalSecureRequest =
                new GetAppearanceCriminalSecureRequest();
        ca.bc.gov.open.wsdl.pcss.secure.one.GetAppearanceCriminalSecureRequest
                getAppearanceCriminalSecureRequest1 =
                        new ca.bc.gov.open.wsdl.pcss.secure.one
                                .GetAppearanceCriminalSecureRequest();

        getAppearanceCriminalSecureRequest1.setAppearanceId("TEST");
        getAppearanceCriminalSecureRequest1.setRequestAgencyIdentifierId("TEST");
        getAppearanceCriminalSecureRequest1.setRequestDtm(Instant.now());
        getAppearanceCriminalSecureRequest1.setRequestPartId("TEST");
        getAppearanceCriminalSecureRequest1.setApplicationCd("TEST");

        getAppearanceCriminalSecureRequest.setGetAppearanceCriminalSecureRequest(
                getAppearanceCriminalSecureRequest1);

        getAppearanceCriminalSecure.setGetAppearanceCriminalSecureRequest(
                getAppearanceCriminalSecureRequest);

        return getAppearanceCriminalSecure;
    }
}
