package ca.bc.gov.open.pcsscriminalapplication.controller.crowncontroller;
import ca.bc.gov.open.pcsscriminalapplication.service.CrownValidator;
import ca.bc.gov.open.wsdl.pcss.one.CrownAssignment2;
import java.time.Instant;
import java.util.Collections;

import ca.bc.gov.open.pcsscriminalapplication.controller.CrownController;
import ca.bc.gov.open.pcsscriminalapplication.exception.BadDateException;
import ca.bc.gov.open.pcsscriminalapplication.exception.ORDSException;
import ca.bc.gov.open.pcsscriminalapplication.properties.PcssProperties;
import ca.bc.gov.open.pcsscriminalapplication.utils.LogBuilder;
import ca.bc.gov.open.wsdl.pcss.one.SetCrownAssignmentResponse;
import ca.bc.gov.open.wsdl.pcss.two.SetCrownAssignment;
import ca.bc.gov.open.wsdl.pcss.two.SetCrownAssignmentRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import javax.xml.ws.http.HTTPException;

import static org.mockito.ArgumentMatchers.any;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("SetCrownAssignment Test")
public class SetCrownAssignmentTest {
    @Mock
    private RestTemplate restTemplateMock;

    @Mock
    private PcssProperties pcssPropertiesMock;

    @Mock
    private ObjectMapper objectMapperMock;

    @Mock
    private CrownValidator crownValidatorMock;

    private CrownController sut;

    @BeforeAll
    public void beforeAll() {

        MockitoAnnotations.openMocks(this);

        Mockito.when(pcssPropertiesMock.getHost()).thenReturn("http://localhost/");

        sut = new CrownController(restTemplateMock, pcssPropertiesMock, new LogBuilder(objectMapperMock), crownValidatorMock);

    }

    @Test
    @DisplayName("Success: post returns expected object")
    public void successTestReturns() throws BadDateException, JsonProcessingException {

        SetCrownAssignmentResponse response = new SetCrownAssignmentResponse();
        response.setResponseCd("Test");
        response.setResponseMessageTxt("Test");

        Mockito.when(restTemplateMock.exchange(any(String.class), any(), any(), any(Class.class))).thenReturn(ResponseEntity.ok(response));

        ca.bc.gov.open.wsdl.pcss.two.SetCrownAssignmentResponse result = sut.setCrownAssignment(createTestRequest());

        SetCrownAssignmentResponse innerResult = result.getSetCrownAssignmentResponse().getSetCrownAssignmentResponse();
        Assertions.assertEquals("Test", innerResult.getResponseCd());
        Assertions.assertEquals("Test", innerResult.getResponseMessageTxt());

    }

    @Test
    @DisplayName("Error: with a bad date throw exception")
    public void errorBadDateException() {

        Assertions.assertThrows(BadDateException.class, ()-> sut.setCrownAssignment(new SetCrownAssignment()));

    }

    @Test
    @DisplayName("Error: ords throws exception")
    public void errorOrdsException() {

        Mockito.when(restTemplateMock.exchange(any(String.class), any(), any(), any(Class.class))).thenThrow(new HTTPException(400));
        Assertions.assertThrows(ORDSException.class, () -> sut.setCrownAssignment(createTestRequest()));

    }

    private SetCrownAssignment createTestRequest() {
        SetCrownAssignment setCrownAssignment = new SetCrownAssignment();
        SetCrownAssignmentRequest setCrownAssignmentRequest = new SetCrownAssignmentRequest();
        ca.bc.gov.open.wsdl.pcss.one.SetCrownAssignmentRequest setCrownAssignmentRequest1 = new ca.bc.gov.open.wsdl.pcss.one.SetCrownAssignmentRequest();
        setCrownAssignmentRequest1.setRequestAgencyIdentifierId("Test");
        setCrownAssignmentRequest1.setRequestPartId("Test");
        setCrownAssignmentRequest1.setRequestDtm(Instant.now());
        setCrownAssignmentRequest1.setJustinNo("Test");
        setCrownAssignmentRequest1.setCrownAssignment(Collections.singletonList(new CrownAssignment2()));

        setCrownAssignmentRequest.setSetCrownAssignmentRequest(setCrownAssignmentRequest1);
        setCrownAssignment.setSetCrownAssignmentRequest(setCrownAssignmentRequest);
        return setCrownAssignment;
    }
}
