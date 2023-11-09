package ca.bc.gov.open.pcsscriminalapplication.controller.filecontroller.demsCasesController;

import static org.mockito.ArgumentMatchers.any;

import ca.bc.gov.open.pcsscriminalapplication.controller.DemsCasesController;
import ca.bc.gov.open.pcsscriminalapplication.model.CaseHyperLinkerLookupResponse;
import ca.bc.gov.open.pcsscriminalapplication.model.JustinRCCs;
import ca.bc.gov.open.pcsscriminalapplication.model.JustinRcc;
import ca.bc.gov.open.pcsscriminalapplication.properties.CaseLookupProperties;
import ca.bc.gov.open.pcsscriminalapplication.properties.DemsProperties;
import ca.bc.gov.open.pcsscriminalapplication.utils.LogBuilder;
import ca.bc.gov.open.wsdl.pcss.demsCaseUrl.GetDemsCasesRequest;
import ca.bc.gov.open.wsdl.pcss.demsCaseUrl.GetDemsCasesResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GetDemsCaseMappingTest {
    @Mock private RestTemplate restTemplateMock;
    @Mock private RestTemplate restTemplateCaseLookupMock;
    @Mock private DemsProperties demsPropertiesMock;
    @Mock private CaseLookupProperties caseLookupProperties;
    @Mock private ObjectMapper objectMapperMock;

    private DemsCasesController sut;

    @BeforeAll
    public void beforeAll() throws JsonProcessingException {

        MockitoAnnotations.openMocks(this);

        Mockito.when(demsPropertiesMock.getHost()).thenReturn("http://localhost/");
        Mockito.when(caseLookupProperties.getHost()).thenReturn("http://localhost/");

        sut =
                new DemsCasesController(
                        restTemplateMock,
                        restTemplateCaseLookupMock,
                        demsPropertiesMock,
                        caseLookupProperties,
                        new LogBuilder(objectMapperMock));
    }

    @Test
    public void successTestReturns() throws JsonProcessingException {

        GetDemsCasesRequest getDemsCasesRequest = new GetDemsCasesRequest();
        getDemsCasesRequest.setApplicationCd("A");
        getDemsCasesRequest.getJustinNo().add("A");
        getDemsCasesRequest.setRequestAgencyIdentifierId("A");
        getDemsCasesRequest.setRequestDtm(Instant.now());
        getDemsCasesRequest.setRequestPartId("A");

        JustinRCCs justinRccsResponse = new JustinRCCs();
        JustinRcc justinRcc = new JustinRcc();
        justinRcc.setJustinNo("137489");
        justinRcc.setRccId("174050.0877");
        justinRccsResponse.getJustins().add(justinRcc);
        Mockito.when(restTemplateMock.exchange(any(URI.class), any(), any(), any(Class.class)))
                .thenReturn(ResponseEntity.ok(justinRccsResponse));

        CaseHyperLinkerLookupResponse caseHyperLinkerLookupResponse =
                new CaseHyperLinkerLookupResponse();
        CaseHyperLinkerLookupResponse.CaseHyperlinks caseHyperlinks =
                new CaseHyperLinkerLookupResponse.CaseHyperlinks();
        caseHyperlinks.setHyperlink(
                "https://dems.test.jag.gov.bc.ca/Edt.aspx#/review/345?tab=3&workspace=4");
        caseHyperlinks.setMessage("Case found.");
        caseHyperlinks.setRcc_id("174050.0877");
        List<CaseHyperLinkerLookupResponse.CaseHyperlinks> list = new ArrayList<>();
        list.add(caseHyperlinks);
        caseHyperLinkerLookupResponse.setCase_hyperlinks(list);
        caseHyperLinkerLookupResponse.setMessage("A");

        Mockito.when(
                        restTemplateCaseLookupMock.exchange(
                                any(URI.class), any(), any(), any(Class.class)))
                .thenReturn(ResponseEntity.ok(caseHyperLinkerLookupResponse));

        GetDemsCasesResponse result = sut.getDemsCaseMapping(getDemsCasesRequest);

        Assertions.assertEquals("137489", result.getDemsCase().get(0).getJustinNo());
    }
}
