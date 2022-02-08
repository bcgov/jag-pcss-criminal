package ca.bc.gov.open.pcsscriminalapplication.controller.crowncontroller;

import static org.mockito.ArgumentMatchers.any;

import ca.bc.gov.open.pcsscriminalapplication.controller.CrownController;
import ca.bc.gov.open.pcsscriminalapplication.exception.ORDSException;
import ca.bc.gov.open.pcsscriminalapplication.properties.PcssProperties;
import ca.bc.gov.open.pcsscriminalapplication.utils.LogBuilder;
import ca.bc.gov.open.wsdl.pcss.one.Detail4;
import ca.bc.gov.open.wsdl.pcss.one.SetCounselDetailCriminalResponse;
import ca.bc.gov.open.wsdl.pcss.two.SetCounselDetailCriminal;
import ca.bc.gov.open.wsdl.pcss.two.SetCounselDetailCriminalRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.time.Instant;
import java.util.Collections;
import javax.xml.ws.http.HTTPException;
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
@DisplayName("SetCounselDetailCriminal Test")
public class SetCounselDetailCriminalTest {
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
    @DisplayName("Success: post returns expected object")
    public void successTestReturns() throws JsonProcessingException {

        SetCounselDetailCriminalResponse response = new SetCounselDetailCriminalResponse();
        response.setResponseCd("Test");
        response.setResponseMessageTxt("Test");

        Mockito.when(restTemplateMock.exchange(any(URI.class), any(), any(), any(Class.class)))
                .thenReturn(ResponseEntity.ok(response));

        ca.bc.gov.open.wsdl.pcss.two.SetCounselDetailCriminalResponse result =
                sut.setCounselDetailCriminal(createTestRequest());

        SetCounselDetailCriminalResponse innerResult =
                result.getSetCounselDetailCriminalResponse().getSetCounselDetailCriminalResponse();
        Assertions.assertEquals("Test", innerResult.getResponseCd());
        Assertions.assertEquals("Test", innerResult.getResponseMessageTxt());
    }

    @Test
    @DisplayName("Error: ords throws exception")
    public void errorOrdsException() {
        Mockito.when(restTemplateMock.exchange(any(URI.class), any(), any(), any(Class.class)))
                .thenThrow(new HTTPException(400));
        Assertions.assertThrows(
                ORDSException.class, () -> sut.setCounselDetailCriminal(createTestRequest()));
    }

    private SetCounselDetailCriminal createTestRequest() {
        SetCounselDetailCriminal setCounselDetailCriminal = new SetCounselDetailCriminal();
        SetCounselDetailCriminalRequest setCounselDetailCriminalRequest =
                new SetCounselDetailCriminalRequest();
        ca.bc.gov.open.wsdl.pcss.one.SetCounselDetailCriminalRequest
                setCounselDetailCriminalRequest1 =
                        new ca.bc.gov.open.wsdl.pcss.one.SetCounselDetailCriminalRequest();
        setCounselDetailCriminalRequest1.setRequestAgencyIdentifierId("Test");
        setCounselDetailCriminalRequest1.setRequestPartId("Test");
        setCounselDetailCriminalRequest1.setRequestDtm(Instant.now());
        setCounselDetailCriminalRequest1.setProfPartId("Test");
        setCounselDetailCriminalRequest1.setProfSeqNo("Test");
        setCounselDetailCriminalRequest1.setDetail(Collections.singletonList(new Detail4()));

        setCounselDetailCriminalRequest.setSetCounselDetailCriminalRequest(
                setCounselDetailCriminalRequest1);
        setCounselDetailCriminal.setSetCounselDetailCriminalRequest(
                setCounselDetailCriminalRequest);
        return setCounselDetailCriminal;
    }
}
