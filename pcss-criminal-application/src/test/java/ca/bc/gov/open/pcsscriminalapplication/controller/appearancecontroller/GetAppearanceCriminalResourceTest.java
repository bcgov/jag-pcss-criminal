package ca.bc.gov.open.pcsscriminalapplication.controller.appearancecontroller;

import static org.mockito.ArgumentMatchers.any;

import ca.bc.gov.open.pcsscriminalapplication.controller.AppearanceController;
import ca.bc.gov.open.pcsscriminalapplication.exception.ORDSException;
import ca.bc.gov.open.pcsscriminalapplication.properties.PcssProperties;
import ca.bc.gov.open.pcsscriminalapplication.utils.LogBuilder;
import ca.bc.gov.open.wsdl.pcss.one.Resource;
import ca.bc.gov.open.wsdl.pcss.two.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.Collections;
import javax.xml.ws.http.HTTPException;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("GetAppearanceCriminalResource Test")
public class GetAppearanceCriminalResourceTest {

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

        ca.bc.gov.open.wsdl.pcss.one.GetAppearanceCriminalResourceResponse response =
                new ca.bc.gov.open.wsdl.pcss.one.GetAppearanceCriminalResourceResponse();
        response.setResponseMessageTxt("TEST");
        response.setResponseCd("TEST");
        response.setResource(Collections.singletonList(new Resource()));

        Mockito.when(restTemplateMock.exchange(any(String.class), any(), any(), any(Class.class)))
                .thenReturn(ResponseEntity.ok(response));

        GetAppearanceCriminalResourceResponse result =
                sut.getAppearanceCriminalResource(createTestRequest());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(
                "TEST",
                result.getGetAppearanceCriminalResourceResponse()
                        .getGetAppearanceCriminalResourceResponse()
                        .getResponseMessageTxt());
        Assertions.assertEquals(
                "TEST",
                result.getGetAppearanceCriminalResourceResponse()
                        .getGetAppearanceCriminalResourceResponse()
                        .getResponseCd());
        Assertions.assertEquals(
                1,
                result.getGetAppearanceCriminalResourceResponse()
                        .getGetAppearanceCriminalResourceResponse()
                        .getResource()
                        .size());
    }

    @Test
    @DisplayName("Error: ords throws exception")
    public void errorOrdsException() {

        Mockito.when(restTemplateMock.exchange(any(String.class), any(), any(), any(Class.class)))
                .thenThrow(new HTTPException(400));

        Assertions.assertThrows(
                ORDSException.class, () -> sut.getAppearanceCriminalResource(createTestRequest()));
    }

    private GetAppearanceCriminalResource createTestRequest() {

        GetAppearanceCriminalResource getAppearanceCriminalResource =
                new GetAppearanceCriminalResource();
        GetAppearanceCriminalResourceRequest getAppearanceCriminalResourceRequest =
                new GetAppearanceCriminalResourceRequest();
        ca.bc.gov.open.wsdl.pcss.one.GetAppearanceCriminalResourceRequest
                getAppearanceCriminalResourceRequest1 =
                        new ca.bc.gov.open.wsdl.pcss.one.GetAppearanceCriminalResourceRequest();

        getAppearanceCriminalResourceRequest1.setAppearanceId("TEST");
        getAppearanceCriminalResourceRequest1.setRequestAgencyIdentifierId("TEST");
        getAppearanceCriminalResourceRequest1.setRequestDtm(Instant.now());
        getAppearanceCriminalResourceRequest1.setRequestPartId("TEST");

        getAppearanceCriminalResourceRequest.setGetAppearanceCriminalResourceRequest(
                getAppearanceCriminalResourceRequest1);

        getAppearanceCriminalResource.setGetAppearanceCriminalResourceRequest(
                getAppearanceCriminalResourceRequest);

        return getAppearanceCriminalResource;
    }
}
