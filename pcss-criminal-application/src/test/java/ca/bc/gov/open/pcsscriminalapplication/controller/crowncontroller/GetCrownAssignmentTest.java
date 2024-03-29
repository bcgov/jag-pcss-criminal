package ca.bc.gov.open.pcsscriminalapplication.controller.crowncontroller;

import static org.mockito.ArgumentMatchers.any;

import ca.bc.gov.open.pcsscriminalapplication.controller.CrownController;
import ca.bc.gov.open.pcsscriminalapplication.exception.ORDSException;
import ca.bc.gov.open.pcsscriminalapplication.properties.PcssProperties;
import ca.bc.gov.open.pcsscriminalapplication.utils.LogBuilder;
import ca.bc.gov.open.wsdl.pcss.one.CrownAssignment;
import ca.bc.gov.open.wsdl.pcss.two.GetCrownAssignment;
import ca.bc.gov.open.wsdl.pcss.two.GetCrownAssignmentRequest;
import ca.bc.gov.open.wsdl.pcss.two.GetCrownAssignmentResponse;
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
@DisplayName("GetCrownAssignment Test")
public class GetCrownAssignmentTest {
    @Mock private RestTemplate restTemplateMock;

    @Mock private PcssProperties pcssPropertiesMock;

    @Mock private ObjectMapper objectMapperMock;

    private CrownController sut;

    @BeforeAll
    public void beforeAll() {

        MockitoAnnotations.openMocks(this);

        Mockito.when(pcssPropertiesMock.getHost()).thenReturn("http://localhost/");

        sut =
                new CrownController(
                        restTemplateMock, pcssPropertiesMock, new LogBuilder(objectMapperMock));
    }

    @Test
    @DisplayName("Success: get returns expected object")
    public void successTestReturns() throws JsonProcessingException {

        ca.bc.gov.open.wsdl.pcss.one.GetCrownAssignmentResponse response =
                new ca.bc.gov.open.wsdl.pcss.one.GetCrownAssignmentResponse();
        response.setResponseCd("Test");
        response.setResponseMessageTxt("Test");
        response.getCrownAssignment().add(new CrownAssignment());

        Mockito.when(restTemplateMock.exchange(any(URI.class), any(), any(), any(Class.class)))
                .thenReturn(ResponseEntity.ok(response));

        GetCrownAssignmentResponse result = sut.getCrownAssignment(createTestRequest());

        ca.bc.gov.open.wsdl.pcss.one.GetCrownAssignmentResponse innerResult =
                result.getGetCrownAssignmentResponse().getGetCrownAssignmentResponse();
        Assertions.assertEquals("Test", innerResult.getResponseCd());
        Assertions.assertEquals("Test", innerResult.getResponseMessageTxt());
        Assertions.assertEquals(response.getCrownAssignment(), innerResult.getCrownAssignment());
    }

    @Test
    @DisplayName("Error: ords throws exception")
    public void errorOrdsException() {
        Mockito.when(restTemplateMock.exchange(any(URI.class), any(), any(), any(Class.class)))
                .thenThrow(new HTTPException(400));
        Assertions.assertThrows(
                ORDSException.class, () -> sut.getCrownAssignment(createTestRequest()));
    }

    private GetCrownAssignment createTestRequest() {

        GetCrownAssignment getCrownAssignment = new GetCrownAssignment();
        GetCrownAssignmentRequest getCrownAssignmentRequest = new GetCrownAssignmentRequest();
        ca.bc.gov.open.wsdl.pcss.one.GetCrownAssignmentRequest getCrownAssignmentRequest1 =
                new ca.bc.gov.open.wsdl.pcss.one.GetCrownAssignmentRequest();
        getCrownAssignmentRequest1.setRequestAgencyIdentifierId("Test");
        getCrownAssignmentRequest1.setRequestPartId("Test");
        getCrownAssignmentRequest1.setRequestDtm(Instant.now());
        getCrownAssignmentRequest1.setJustinNo("Test");
        getCrownAssignmentRequest1.setSinceDt(Instant.now());

        getCrownAssignmentRequest.setGetCrownAssignmentRequest(getCrownAssignmentRequest1);
        getCrownAssignment.setGetCrownAssignmentRequest(getCrownAssignmentRequest);
        return getCrownAssignment;
    }
}
