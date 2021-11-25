package ca.bc.gov.open.pcsscriminalapplication.controller.synccontroller;

import ca.bc.gov.open.pcsscriminalapplication.controller.SyncController;
import ca.bc.gov.open.pcsscriminalapplication.exception.BadDateException;
import ca.bc.gov.open.pcsscriminalapplication.exception.ORDSException;
import ca.bc.gov.open.pcsscriminalapplication.properties.PcssProperties;
import ca.bc.gov.open.pcsscriminalapplication.service.SyncValidator;
import ca.bc.gov.open.pcsscriminalapplication.utils.LogBuilder;
import ca.bc.gov.open.wsdl.pcss.one.Appearance;
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
@DisplayName("GetSyncCriminalAppearance Test")
public class GetSyncCriminalAppearanceTest {

    @Mock
    private RestTemplate restTemplateMock;

    @Mock
    private PcssProperties pcssPropertiesMock;

    @Mock
    private ObjectMapper objectMapperMock;

    @Mock
    private SyncValidator syncValidatorMock;

    private SyncController sut;

    @BeforeAll
    public void beforeAll() {

        MockitoAnnotations.openMocks(this);

        Mockito.when(pcssPropertiesMock.getHost()).thenReturn("http://localhost/");

        sut = new SyncController(restTemplateMock, pcssPropertiesMock, new LogBuilder(objectMapperMock), syncValidatorMock);

    }

    @Test
    @DisplayName("Success: get returns expected object")
    public void successTestReturns() throws BadDateException, JsonProcessingException {

        ca.bc.gov.open.wsdl.pcss.one.GetSyncCriminalAppearanceResponse response = new ca.bc.gov.open.wsdl.pcss.one.GetSyncCriminalAppearanceResponse();
        response.setResponseMessageTxt("TEST");
        response.setResponseCd("TEST");
        response.setAppearance(Collections.singletonList(new Appearance()));

        Mockito.when(restTemplateMock.exchange(any(String.class), any(), any(), any(Class.class))).thenReturn(ResponseEntity.ok(response));
        Mockito.when(syncValidatorMock.validateGetSyncCriminalAppearance(any())).thenReturn(new ArrayList<>());

        GetSyncCriminalAppearanceResponse result = sut.getSyncCriminalAppearance(createTestRequest());

        Assertions.assertNotNull(result);
        Assertions.assertEquals("TEST", result.getGetSyncCriminalAppearanceResponse().getGetSyncCriminalAppearanceResponse().getResponseMessageTxt());
        Assertions.assertEquals("TEST", result.getGetSyncCriminalAppearanceResponse().getGetSyncCriminalAppearanceResponse().getResponseCd());
        Assertions.assertEquals(1, result.getGetSyncCriminalAppearanceResponse().getGetSyncCriminalAppearanceResponse().getAppearance().size());

    }

    @Test
    @DisplayName("Fail: get returns validation failure object")
    public void failTestReturns() throws BadDateException, JsonProcessingException {

        Mockito.when(syncValidatorMock.validateGetSyncCriminalAppearance(any())).thenReturn(Collections.singletonList("BAD DATA"));

        GetSyncCriminalAppearanceResponse result = sut.getSyncCriminalAppearance(createTestRequest());

        Assertions.assertNotNull(result);
        Assertions.assertEquals("BAD DATA", result.getGetSyncCriminalAppearanceResponse().getGetSyncCriminalAppearanceResponse().getResponseMessageTxt());
        Assertions.assertEquals("-2", result.getGetSyncCriminalAppearanceResponse().getGetSyncCriminalAppearanceResponse().getResponseCd());

    }


    @Test
    @DisplayName("Error: ords throws exception")
    public void errorOrdsException() {

        Mockito.when(restTemplateMock.exchange(any(String.class), any(), any(), any(Class.class))).thenThrow(new HTTPException(400));
        Mockito.when(syncValidatorMock.validateGetSyncCriminalAppearance(any())).thenReturn(new ArrayList<>());

        Assertions.assertThrows(ORDSException.class, () -> sut.getSyncCriminalAppearance(createTestRequest()));

    }

    private GetSyncCriminalAppearance createTestRequest() {

        GetSyncCriminalAppearance getSyncCriminalAppearance = new GetSyncCriminalAppearance();
        GetSyncCriminalAppearanceRequest getSyncCriminalAppearanceRequest = new GetSyncCriminalAppearanceRequest();
        ca.bc.gov.open.wsdl.pcss.one.GetSyncCriminalAppearanceRequest getSyncCriminalAppearanceRequest1 = new ca.bc.gov.open.wsdl.pcss.one.GetSyncCriminalAppearanceRequest();

        getSyncCriminalAppearanceRequest1.setProcessUpToDtm("2013-03-25 13:04:22.1");
        getSyncCriminalAppearanceRequest1.setRequestAgencyIdentifierId("TEST");
        getSyncCriminalAppearanceRequest1.setRequestDtm("2013-03-25 13:04:22.1");
        getSyncCriminalAppearanceRequest1.setRequestPartId("TEST");

        getSyncCriminalAppearanceRequest.setGetSyncCriminalAppearanceRequest(getSyncCriminalAppearanceRequest1);

        getSyncCriminalAppearance.setGetSyncCriminalAppearanceRequest(getSyncCriminalAppearanceRequest);

        return getSyncCriminalAppearance;

    }

}
