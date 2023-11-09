package ca.bc.gov.open.pcsscriminalapplication.controller.hearingcontroller;

import static org.mockito.ArgumentMatchers.any;

import ca.bc.gov.open.pcsscriminalapplication.controller.HearingController;
import ca.bc.gov.open.pcsscriminalapplication.exception.ORDSException;
import ca.bc.gov.open.pcsscriminalapplication.properties.PcssProperties;
import ca.bc.gov.open.pcsscriminalapplication.utils.LogBuilder;
import ca.bc.gov.open.wsdl.pcss.one.SetHearingRestrictionCriminalResponse;
import ca.bc.gov.open.wsdl.pcss.three.HearingRestrictionType;
import ca.bc.gov.open.wsdl.pcss.three.OperationModeType;
import ca.bc.gov.open.wsdl.pcss.two.SetHearingRestrictionCriminal;
import ca.bc.gov.open.wsdl.pcss.two.SetHearingRestrictionCriminalRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.xml.ws.http.HTTPException;
import java.net.URI;
import java.time.Instant;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("SetHearingRestrictionCriminal Test")
public class SetHearingRestrictionCriminalTest {
    @Mock private RestTemplate restTemplateMock;

    @Mock private PcssProperties pcssPropertiesMock;

    @Mock private ObjectMapper objectMapperMock;

    private HearingController sut;

    @BeforeAll
    public void beforeAll() {

        MockitoAnnotations.openMocks(this);

        Mockito.when(pcssPropertiesMock.getHost()).thenReturn("http://localhost/");

        sut =
                new HearingController(
                        restTemplateMock, pcssPropertiesMock, new LogBuilder(objectMapperMock));
    }

    @Test
    @DisplayName("Success: post returns expected object")
    public void successTestReturns() throws JsonProcessingException {

        SetHearingRestrictionCriminalResponse response =
                new SetHearingRestrictionCriminalResponse();
        response.setHearingRestrictionCcn("Test");
        response.setHearingRestrictionId("Test");
        response.setResponseCd("Test");
        response.setResponseMessageTxt("Test");

        Mockito.when(restTemplateMock.exchange(any(URI.class), any(), any(), any(Class.class)))
                .thenReturn(ResponseEntity.ok(response));

        ca.bc.gov.open.wsdl.pcss.two.SetHearingRestrictionCriminalResponse result =
                sut.setHearingRestrictionCriminal(createTestRequest());

        SetHearingRestrictionCriminalResponse innerResponse =
                result.getSetHearingRestrictionCriminalResponse()
                        .getSetHearingRestrictionCriminalResponse();
        Assertions.assertEquals("Test", innerResponse.getResponseCd());
        Assertions.assertEquals("Test", innerResponse.getHearingRestrictionCcn());
        Assertions.assertEquals("Test", innerResponse.getHearingRestrictionId());
        Assertions.assertEquals("Test", innerResponse.getResponseMessageTxt());
    }

    @Test
    @DisplayName("Error: ords throws exception")
    public void errorOrdsException() {

        Mockito.when(restTemplateMock.exchange(any(URI.class), any(), any(), any(Class.class)))
                .thenThrow(new HTTPException(400));

        Assertions.assertThrows(
                ORDSException.class, () -> sut.setHearingRestrictionCriminal(createTestRequest()));
    }

    public SetHearingRestrictionCriminal createTestRequest() {
        SetHearingRestrictionCriminal setHearingRestrictionCriminal =
                new SetHearingRestrictionCriminal();
        SetHearingRestrictionCriminalRequest setHearingRestrictionCriminalRequest =
                new SetHearingRestrictionCriminalRequest();
        ca.bc.gov.open.wsdl.pcss.one.SetHearingRestrictionCriminalRequest
                setHearingRestrictionCriminalRequest1 =
                        new ca.bc.gov.open.wsdl.pcss.one.SetHearingRestrictionCriminalRequest();

        setHearingRestrictionCriminalRequest1.setHearingRestrictionCcn("Test");
        setHearingRestrictionCriminalRequest1.setHearingRestrictionCd(HearingRestrictionType.A);
        setHearingRestrictionCriminalRequest1.setHearingRestrictionId("Test");
        setHearingRestrictionCriminalRequest1.setRequestDtm(Instant.now());
        setHearingRestrictionCriminalRequest1.setRequestPartId("Test");
        setHearingRestrictionCriminalRequest1.setAdjudicatorPartId("Test");
        setHearingRestrictionCriminalRequest1.setJustinNo("Test");
        setHearingRestrictionCriminalRequest1.setProfSeqNo("Test");
        setHearingRestrictionCriminalRequest1.setRequestAgencyIdentifierId("Test");
        setHearingRestrictionCriminalRequest1.setPartId("Test");
        setHearingRestrictionCriminalRequest1.setOperationModeCd(OperationModeType.ADD);

        setHearingRestrictionCriminalRequest.setSetHearingRestrictionCriminalRequest(
                setHearingRestrictionCriminalRequest1);
        setHearingRestrictionCriminal.setSetHearingRestrictionCriminalRequest(
                setHearingRestrictionCriminalRequest);

        return setHearingRestrictionCriminal;
    }
}
