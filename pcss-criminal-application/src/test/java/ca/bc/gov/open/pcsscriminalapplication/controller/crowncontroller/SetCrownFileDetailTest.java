package ca.bc.gov.open.pcsscriminalapplication.controller.crowncontroller;
import ca.bc.gov.open.pcsscriminalapplication.exception.BadDateException;
import ca.bc.gov.open.pcsscriminalapplication.service.CrownValidator;
import ca.bc.gov.open.wsdl.pcss.two.SetCrownFileDetailResponse;
import ca.bc.gov.open.wsdl.pcss.three.AppearanceDurationType;
import ca.bc.gov.open.wsdl.pcss.three.FileComplexityType;

import ca.bc.gov.open.pcsscriminalapplication.controller.CrownController;
import ca.bc.gov.open.pcsscriminalapplication.exception.ORDSException;
import ca.bc.gov.open.pcsscriminalapplication.properties.PcssProperties;
import ca.bc.gov.open.pcsscriminalapplication.utils.LogBuilder;
import ca.bc.gov.open.wsdl.pcss.two.SetCrownFileDetail;
import ca.bc.gov.open.wsdl.pcss.two.SetCrownFileDetailRequest;
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

import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("SetCrownFileDetail Test")
public class SetCrownFileDetailTest {
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

        ca.bc.gov.open.wsdl.pcss.one.SetCrownFileDetailResponse response = new ca.bc.gov.open.wsdl.pcss.one.SetCrownFileDetailResponse();
        response.setResponseCd("Test");
        response.setMdocCcn("Test");
        response.setResponseMessageTxt("Test");

        Mockito.when(restTemplateMock.exchange(any(String.class), any(), any(), any(Class.class))).thenReturn(ResponseEntity.ok(response));

        SetCrownFileDetailResponse result = sut.setCrownFileDetail(createTestRequest());

        ca.bc.gov.open.wsdl.pcss.one.SetCrownFileDetailResponse innerResult = result.getSetCrownFileDetailResponse().getSetCrownFileDetailResponse();
        Assertions.assertEquals("Test", innerResult.getResponseCd());
        Assertions.assertEquals("Test", innerResult.getResponseMessageTxt());
        Assertions.assertEquals("Test", innerResult.getMdocCcn());

    }

    @Test
    @DisplayName("Error: with a bad date throw exception")
    public void errorBadDateException() {

        Assertions.assertThrows(BadDateException.class, ()-> sut.setCrownFileDetail(new SetCrownFileDetail()));

    }

    @Test
    @DisplayName("Error: ords throws exception")
    public void errorOrdsException() {

        Mockito.when(restTemplateMock.exchange(any(String.class), any(), any(), any(Class.class))).thenThrow(new HTTPException(400));
        Assertions.assertThrows(ORDSException.class, () -> sut.setCrownFileDetail(createTestRequest()));

    }

    private SetCrownFileDetail createTestRequest() {
        SetCrownFileDetail setCrownFileDetail = new SetCrownFileDetail();
        SetCrownFileDetailRequest setCrownFileDetailRequest = new SetCrownFileDetailRequest();
        ca.bc.gov.open.wsdl.pcss.one.SetCrownFileDetailRequest setCrownFileDetailRequest1 = new ca.bc.gov.open.wsdl.pcss.one.SetCrownFileDetailRequest();
        setCrownFileDetailRequest1.setRequestAgencyIdentifierId("Test");
        setCrownFileDetailRequest1.setRequestPartId("Test");
        setCrownFileDetailRequest1.setRequestDtm(Instant.now());
        setCrownFileDetailRequest1.setJustinNo("Test");
        setCrownFileDetailRequest1.setCrownEstimateLenQty("Test");
        setCrownFileDetailRequest1.setCrownEstimateLenUnit(AppearanceDurationType.HRS);
        setCrownFileDetailRequest1.setFileDesignationCd(FileComplexityType.SPC);
        setCrownFileDetailRequest1.setMdocCcn("Test");

        setCrownFileDetailRequest.setSetCrownFileDetailRequest(setCrownFileDetailRequest1);
        setCrownFileDetail.setSetCrownFileDetailRequest(setCrownFileDetailRequest);

        return setCrownFileDetail;
    }
}
