package ca.bc.gov.open.pcsscriminalapplication.controller.personnelcontroller;

import ca.bc.gov.open.pcsscriminalapplication.controller.PersonnelController;
import ca.bc.gov.open.pcsscriminalapplication.exception.BadDateException;
import ca.bc.gov.open.pcsscriminalapplication.exception.ORDSException;
import ca.bc.gov.open.pcsscriminalapplication.properties.PcssProperties;
import ca.bc.gov.open.pcsscriminalapplication.utils.LogBuilder;
import ca.bc.gov.open.wsdl.pcss.one.Personnel;
import ca.bc.gov.open.wsdl.pcss.three.AvailablePersonType;
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
@DisplayName("GetPersonnelAvailability Test")
public class GetPersonnelAvailabilityTest {

    @Mock
    private RestTemplate restTemplateMock;

    @Mock
    private PcssProperties pcssPropertiesMock;

    @Mock
    private ObjectMapper objectMapperMock;

    private PersonnelController sut;

    @BeforeAll
    public void beforeAll() {

        MockitoAnnotations.openMocks(this);

        Mockito.when(pcssPropertiesMock.getHost()).thenReturn("http://localhost/");

        sut = new PersonnelController(restTemplateMock, pcssPropertiesMock, new LogBuilder(objectMapperMock));

    }

    @Test
    @DisplayName("Success: get returns expected object")
    public void successTestReturns() throws BadDateException, JsonProcessingException {

        ca.bc.gov.open.wsdl.pcss.one.GetPersonnelAvailabilityResponse response = new ca.bc.gov.open.wsdl.pcss.one.GetPersonnelAvailabilityResponse();
        response.setResponseMessageTxt("TEST");
        response.setResponseCd("TEST");
        response.setPersonnel(Collections.singletonList(new Personnel()));

        Mockito.when(restTemplateMock.exchange(any(String.class), any(), any(), any(Class.class))).thenReturn(ResponseEntity.ok(response));

        GetPersonnelAvailabilityResponse result = sut.getPersonnelAvailability(createTestRequest());

        Assertions.assertNotNull(result);
        Assertions.assertEquals("TEST", result.getGetPersonnelAvailabilityResponse().getGetPersonnelAvailabilityResponse().getResponseMessageTxt());
        Assertions.assertEquals("TEST", result.getGetPersonnelAvailabilityResponse().getGetPersonnelAvailabilityResponse().getResponseCd());
        Assertions.assertEquals(1, result.getGetPersonnelAvailabilityResponse().getGetPersonnelAvailabilityResponse().getPersonnel().size());

    }

    @Test
    @DisplayName("Error: ords throws exception")
    public void errorOrdsException() {

        Mockito.when(restTemplateMock.exchange(any(String.class), any(), any(), any(Class.class))).thenThrow(new HTTPException(400));

        Assertions.assertThrows(ORDSException.class, () -> sut.getPersonnelAvailability(createTestRequest()));

    }

    @Test
    @DisplayName("Error: with a bad date throw exception")
    public void whenBadDateExceptionThrown() {

        Assertions.assertThrows(BadDateException.class, () -> sut.getPersonnelAvailability(new GetPersonnelAvailability()));

    }

    private GetPersonnelAvailability createTestRequest() {

        GetPersonnelAvailability getPersonnelAvailability = new GetPersonnelAvailability();
        GetPersonnelAvailabilityRequest getPersonnelAvailabilityRequest = new GetPersonnelAvailabilityRequest();
        ca.bc.gov.open.wsdl.pcss.one.GetPersonnelAvailabilityRequest getPersonnelAvailabilityRequest1 = new ca.bc.gov.open.wsdl.pcss.one.GetPersonnelAvailabilityRequest();

        getPersonnelAvailabilityRequest1.setPartIdList("TEST");
        getPersonnelAvailabilityRequest1.setRequestAgencyIdentifierId("TEST");
        getPersonnelAvailabilityRequest1.setRequestDtm(Instant.now());
        getPersonnelAvailabilityRequest1.setRequestPartId("TEST");
        getPersonnelAvailabilityRequest1.setFromDt(Instant.now());
        getPersonnelAvailabilityRequest1.setPersonTypeCd(AvailablePersonType.C);

        getPersonnelAvailabilityRequest.setGetPersonnelAvailabilityRequest(getPersonnelAvailabilityRequest1);

        getPersonnelAvailability.setGetPersonnelAvailabilityRequest(getPersonnelAvailabilityRequest);

        return getPersonnelAvailability;

    }

}
