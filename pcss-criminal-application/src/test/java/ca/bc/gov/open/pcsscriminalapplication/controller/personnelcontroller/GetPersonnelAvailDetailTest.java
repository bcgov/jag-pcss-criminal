package ca.bc.gov.open.pcsscriminalapplication.controller.personnelcontroller;

import static org.mockito.ArgumentMatchers.any;

import ca.bc.gov.open.pcsscriminalapplication.controller.PersonnelController;
import ca.bc.gov.open.pcsscriminalapplication.exception.ORDSException;
import ca.bc.gov.open.pcsscriminalapplication.properties.PcssProperties;
import ca.bc.gov.open.pcsscriminalapplication.utils.LogBuilder;
import ca.bc.gov.open.wsdl.pcss.one.Assignment;
import ca.bc.gov.open.wsdl.pcss.one.Commitment;
import ca.bc.gov.open.wsdl.pcss.three.AvailablePersonType;
import ca.bc.gov.open.wsdl.pcss.two.*;
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
@DisplayName("GetPersonnelAvailDetail Test")
public class GetPersonnelAvailDetailTest {

    @Mock private RestTemplate restTemplateMock;

    @Mock private PcssProperties pcssPropertiesMock;

    @Mock private ObjectMapper objectMapperMock;

    private PersonnelController sut;

    @BeforeAll
    public void beforeAll() {

        MockitoAnnotations.openMocks(this);

        Mockito.when(pcssPropertiesMock.getHost()).thenReturn("http://localhost/");

        sut =
                new PersonnelController(
                        restTemplateMock, pcssPropertiesMock, new LogBuilder(objectMapperMock));
    }

    @Test
    @DisplayName("Success: get returns expected object")
    public void successTestReturns() throws JsonProcessingException {

        ca.bc.gov.open.wsdl.pcss.one.GetPersonnelAvailDetailResponse response =
                new ca.bc.gov.open.wsdl.pcss.one.GetPersonnelAvailDetailResponse();
        response.setResponseMessageTxt("TEST");
        response.setResponseCd("TEST");
        response.setDutyDsc("TEST");
        response.setShiftLadderDsc("TEST");
        response.getAssignment().add(new Assignment());
        response.getCommitment().add(new Commitment());

        Mockito.when(restTemplateMock.exchange(any(URI.class), any(), any(), any(Class.class)))
                .thenReturn(ResponseEntity.ok(response));

        GetPersonnelAvailDetailResponse result = sut.getPersonnelAvailDetail(createTestRequest());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(
                "TEST",
                result.getGetPersonnelAvailDetailResponse()
                        .getGetPersonnelAvailDetailResponse()
                        .getResponseMessageTxt());
        Assertions.assertEquals(
                "TEST",
                result.getGetPersonnelAvailDetailResponse()
                        .getGetPersonnelAvailDetailResponse()
                        .getResponseCd());
        Assertions.assertEquals(
                "TEST",
                result.getGetPersonnelAvailDetailResponse()
                        .getGetPersonnelAvailDetailResponse()
                        .getDutyDsc());
        Assertions.assertEquals(
                "TEST",
                result.getGetPersonnelAvailDetailResponse()
                        .getGetPersonnelAvailDetailResponse()
                        .getShiftLadderDsc());
        Assertions.assertEquals(
                1,
                result.getGetPersonnelAvailDetailResponse()
                        .getGetPersonnelAvailDetailResponse()
                        .getAssignment()
                        .size());
        Assertions.assertEquals(
                1,
                result.getGetPersonnelAvailDetailResponse()
                        .getGetPersonnelAvailDetailResponse()
                        .getCommitment()
                        .size());
    }

    @Test
    @DisplayName("Error: ords throws exception")
    public void errorOrdsException() {

        Mockito.when(restTemplateMock.exchange(any(URI.class), any(), any(), any(Class.class)))
                .thenThrow(new HTTPException(400));

        Assertions.assertThrows(
                ORDSException.class, () -> sut.getPersonnelAvailDetail(createTestRequest()));
    }

    private GetPersonnelAvailDetail createTestRequest() {

        GetPersonnelAvailDetail getPersonnelAvailDetail = new GetPersonnelAvailDetail();
        GetPersonnelAvailDetailRequest getPersonnelAvailDetailRequest =
                new GetPersonnelAvailDetailRequest();
        ca.bc.gov.open.wsdl.pcss.one.GetPersonnelAvailDetailRequest
                getPersonnelAvailDetailRequest1 =
                        new ca.bc.gov.open.wsdl.pcss.one.GetPersonnelAvailDetailRequest();

        getPersonnelAvailDetailRequest1.setPaasPartId("TEST");
        getPersonnelAvailDetailRequest1.setRequestAgencyIdentifierId("TEST");
        getPersonnelAvailDetailRequest1.setRequestDtm(Instant.now());
        getPersonnelAvailDetailRequest1.setRequestPartId("TEST");
        getPersonnelAvailDetailRequest1.setAvailabilityDt(Instant.now());
        getPersonnelAvailDetailRequest1.setPersonTypeCd(AvailablePersonType.C);

        getPersonnelAvailDetailRequest.setGetPersonnelAvailDetailRequest(
                getPersonnelAvailDetailRequest1);

        getPersonnelAvailDetail.setGetPersonnelAvailDetailRequest(getPersonnelAvailDetailRequest);

        return getPersonnelAvailDetail;
    }
}
