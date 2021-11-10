package ca.bc.gov.open.pcsscriminalapplication.controller.appearancecontroller;

import ca.bc.gov.open.pcsscriminalapplication.controller.AppearanceController;
import ca.bc.gov.open.pcsscriminalapplication.exception.BadDateException;
import ca.bc.gov.open.pcsscriminalapplication.exception.ORDSException;
import ca.bc.gov.open.pcsscriminalapplication.properties.PcssProperties;
import ca.bc.gov.open.pcsscriminalapplication.service.AppearanceValidator;
import ca.bc.gov.open.pcsscriminalapplication.utils.LogBuilder;
import ca.bc.gov.open.wsdl.pcss.one.ApprCount;
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
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("GetAppearanceCriminalCount Test")
public class GetAppearanceCriminalCountTest {

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

        ca.bc.gov.open.wsdl.pcss.one.GetAppearanceCriminalCountResponse response = new ca.bc.gov.open.wsdl.pcss.one.GetAppearanceCriminalCountResponse();
        response.setResponseMessageTxt("TEST");
        response.setResponseCd("TEST");
        response.setApprCount(Collections.singletonList(new ApprCount()));

        Mockito.when(restTemplateMock.exchange(any(String.class), any(), any(), any(Class.class))).thenReturn(ResponseEntity.ok(response));

        GetAppearanceCriminalCountResponse result = sut.getAppearanceCriminalCount(createTestRequest());

        Assertions.assertNotNull(result);
        Assertions.assertEquals("TEST", result.getGetAppearanceCriminalCountResponse().getGetAppearanceCriminalCountResponse().getResponseMessageTxt());
        Assertions.assertEquals("TEST", result.getGetAppearanceCriminalCountResponse().getGetAppearanceCriminalCountResponse().getResponseCd());
        Assertions.assertEquals(1, result.getGetAppearanceCriminalCountResponse().getGetAppearanceCriminalCountResponse().getApprCount().size());

    }

    @Test
    @DisplayName("Error: ords throws exception")
    public void errorOrdsException() {

        Mockito.when(restTemplateMock.exchange(any(String.class), any(), any(), any(Class.class))).thenThrow(new HTTPException(400));

        Assertions.assertThrows(ORDSException.class, () -> sut.getAppearanceCriminalCount(createTestRequest()));

    }

    @Test
    @DisplayName("Error: with a bad date throw exception")
    public void whenBadDateExceptionThrown() {

        Assertions.assertThrows(BadDateException.class, () -> sut.getAppearanceCriminalCount(new GetAppearanceCriminalCount()));

    }

    private GetAppearanceCriminalCount createTestRequest() {

        GetAppearanceCriminalCount getAppearanceCriminalCountRequest = new GetAppearanceCriminalCount();
        GetAppearanceCriminalCountRequest getAppearanceCriminalCountRequest1 = new GetAppearanceCriminalCountRequest();
        ca.bc.gov.open.wsdl.pcss.one.GetAppearanceCriminalCountRequest getAppearanceCriminalCountRequest2 = new ca.bc.gov.open.wsdl.pcss.one.GetAppearanceCriminalCountRequest();

        getAppearanceCriminalCountRequest2.setAppearanceId("TEST");
        getAppearanceCriminalCountRequest2.setRequestAgencyIdentifierId("TEST");
        getAppearanceCriminalCountRequest2.setRequestDtm(Instant.now());
        getAppearanceCriminalCountRequest2.setRequestPartId("TEST");

        getAppearanceCriminalCountRequest1.setGetAppearanceCriminalCountRequest(getAppearanceCriminalCountRequest2);

        getAppearanceCriminalCountRequest.setGetAppearanceCriminalCountRequest(getAppearanceCriminalCountRequest1);

        return getAppearanceCriminalCountRequest;

    }

}
