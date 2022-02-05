package ca.bc.gov.open.pcsscriminalapplication.controller.synccontroller;

import static org.mockito.ArgumentMatchers.any;

import ca.bc.gov.open.pcsscriminalapplication.controller.SyncController;
import ca.bc.gov.open.pcsscriminalapplication.exception.ORDSException;
import ca.bc.gov.open.pcsscriminalapplication.properties.PcssProperties;
import ca.bc.gov.open.pcsscriminalapplication.service.SyncValidator;
import ca.bc.gov.open.pcsscriminalapplication.utils.LogBuilder;
import ca.bc.gov.open.wsdl.pcss.one.HearingRestriction;
import ca.bc.gov.open.wsdl.pcss.two.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Instant;
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
@DisplayName("GetSyncCriminalHearingRestriction Test")
public class GetSyncCriminalHearingRestrictionTest {

    @Mock private RestTemplate restTemplateMock;

    @Mock private PcssProperties pcssPropertiesMock;

    @Mock private ObjectMapper objectMapperMock;

    @Mock private SyncValidator syncValidatorMock;

    private SyncController sut;

    @BeforeAll
    public void beforeAll() {

        MockitoAnnotations.openMocks(this);

        Mockito.when(pcssPropertiesMock.getHost()).thenReturn("http://localhost/");

        sut =
                new SyncController(
                        restTemplateMock,
                        pcssPropertiesMock,
                        new LogBuilder(objectMapperMock),
                        syncValidatorMock);
    }

    @Test
    @DisplayName("Success: get returns expected object")
    public void successTestReturns() throws JsonProcessingException {

        ca.bc.gov.open.wsdl.pcss.one.GetSyncCriminalHearingRestrictionResponse response =
                new ca.bc.gov.open.wsdl.pcss.one.GetSyncCriminalHearingRestrictionResponse();
        response.setResponseMessageTxt("TEST");
        response.setResponseCd("TEST");
        response.setHearingRestriction(Collections.singletonList(new HearingRestriction()));

        Mockito.when(restTemplateMock.exchange(any(String.class), any(), any(), any(Class.class)))
                .thenReturn(ResponseEntity.ok(response));
        Mockito.when(syncValidatorMock.validateGetSyncCriminalHearingRestriction(any()))
                .thenReturn(new ArrayList<>());

        GetSyncCriminalHearingRestrictionResponse result =
                sut.getSyncCriminalHearingRestriction(createTestRequest());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(
                "TEST",
                result.getGetSyncCriminalHearingRestrictionResponse()
                        .getGetSyncCriminalHearingRestrictionResponse()
                        .getResponseMessageTxt());
        Assertions.assertEquals(
                "TEST",
                result.getGetSyncCriminalHearingRestrictionResponse()
                        .getGetSyncCriminalHearingRestrictionResponse()
                        .getResponseCd());
        Assertions.assertEquals(
                1,
                result.getGetSyncCriminalHearingRestrictionResponse()
                        .getGetSyncCriminalHearingRestrictionResponse()
                        .getHearingRestriction()
                        .size());
    }

    @Test
    @DisplayName("Fail: get returns validation failure object")
    public void failTestReturns() throws JsonProcessingException {

        Mockito.when(syncValidatorMock.validateGetSyncCriminalHearingRestriction(any()))
                .thenReturn(Collections.singletonList("BAD DATA"));

        GetSyncCriminalHearingRestrictionResponse result =
                sut.getSyncCriminalHearingRestriction(createTestRequest());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(
                "BAD DATA",
                result.getGetSyncCriminalHearingRestrictionResponse()
                        .getGetSyncCriminalHearingRestrictionResponse()
                        .getResponseMessageTxt());
        Assertions.assertEquals(
                "-2",
                result.getGetSyncCriminalHearingRestrictionResponse()
                        .getGetSyncCriminalHearingRestrictionResponse()
                        .getResponseCd());
    }

    @Test
    @DisplayName("Error: ords throws exception")
    public void errorOrdsException() {

        Mockito.when(restTemplateMock.exchange(any(String.class), any(), any(), any(Class.class)))
                .thenThrow(new HTTPException(400));
        Mockito.when(syncValidatorMock.validateGetSyncCriminalHearingRestriction(any()))
                .thenReturn(new ArrayList<>());

        Assertions.assertThrows(
                ORDSException.class,
                () -> sut.getSyncCriminalHearingRestriction(createTestRequest()));
    }

    private GetSyncCriminalHearingRestriction createTestRequest() {

        GetSyncCriminalHearingRestriction getSyncCriminalAppearance =
                new GetSyncCriminalHearingRestriction();
        GetSyncCriminalHearingRestrictionRequest getSyncCriminalAppearanceRequest =
                new GetSyncCriminalHearingRestrictionRequest();
        ca.bc.gov.open.wsdl.pcss.one.GetSyncCriminalHearingRestrictionRequest
                getSyncCriminalAppearanceRequest1 =
                        new ca.bc.gov.open.wsdl.pcss.one.GetSyncCriminalHearingRestrictionRequest();

        getSyncCriminalAppearanceRequest1.setProcessUpToDtm(Instant.now());
        getSyncCriminalAppearanceRequest1.setRequestAgencyIdentifierId("TEST");
        getSyncCriminalAppearanceRequest1.setRequestDtm(Instant.now());
        getSyncCriminalAppearanceRequest1.setRequestPartId("TEST");

        getSyncCriminalAppearanceRequest.setGetSyncCriminalHearingRestrictionRequest(
                getSyncCriminalAppearanceRequest1);

        getSyncCriminalAppearance.setGetSyncCriminalHearingRestrictionRequest(
                getSyncCriminalAppearanceRequest);

        return getSyncCriminalAppearance;
    }
}
