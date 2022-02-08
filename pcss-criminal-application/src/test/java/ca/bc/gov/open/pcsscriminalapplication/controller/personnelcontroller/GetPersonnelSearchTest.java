package ca.bc.gov.open.pcsscriminalapplication.controller.personnelcontroller;

import static org.mockito.ArgumentMatchers.any;

import ca.bc.gov.open.pcsscriminalapplication.controller.PersonnelController;
import ca.bc.gov.open.pcsscriminalapplication.exception.ORDSException;
import ca.bc.gov.open.pcsscriminalapplication.properties.PcssProperties;
import ca.bc.gov.open.pcsscriminalapplication.utils.LogBuilder;
import ca.bc.gov.open.wsdl.pcss.one.Personnel2;
import ca.bc.gov.open.wsdl.pcss.three.OfficerSearchType;
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
@DisplayName("GetPersonnelSearch Test")
public class GetPersonnelSearchTest {

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

        ca.bc.gov.open.wsdl.pcss.one.GetPersonnelSearchResponse response =
                new ca.bc.gov.open.wsdl.pcss.one.GetPersonnelSearchResponse();
        response.setResponseMessageTxt("TEST");
        response.setResponseCd("TEST");
        response.setPersonnel(Collections.singletonList(new Personnel2()));

        Mockito.when(restTemplateMock.exchange(any(String.class), any(), any(), any(Class.class)))
                .thenReturn(ResponseEntity.ok(response));

        GetPersonnelSearchResponse result = sut.getPersonnelSearch(createTestRequest());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(
                "TEST",
                result.getGetPersonnelSearchResponse()
                        .getGetPersonnelSearchResponse()
                        .getResponseMessageTxt());
        Assertions.assertEquals(
                "TEST",
                result.getGetPersonnelSearchResponse()
                        .getGetPersonnelSearchResponse()
                        .getResponseCd());
        Assertions.assertEquals(
                1,
                result.getGetPersonnelSearchResponse()
                        .getGetPersonnelSearchResponse()
                        .getPersonnel()
                        .size());
    }

    @Test
    @DisplayName("Error: ords throws exception")
    public void errorOrdsException() {

        Mockito.when(restTemplateMock.exchange(any(String.class), any(), any(), any(Class.class)))
                .thenThrow(new HTTPException(400));

        Assertions.assertThrows(
                ORDSException.class, () -> sut.getPersonnelSearch(createTestRequest()));
    }

    private GetPersonnelSearch createTestRequest() {

        GetPersonnelSearch getPersonnelSearch = new GetPersonnelSearch();
        GetPersonnelSearchRequest getPersonnelSearchRequest = new GetPersonnelSearchRequest();
        ca.bc.gov.open.wsdl.pcss.one.GetPersonnelSearchRequest getPersonnelSearchRequest1 =
                new ca.bc.gov.open.wsdl.pcss.one.GetPersonnelSearchRequest();

        getPersonnelSearchRequest1.setAgencyId("TEST");
        getPersonnelSearchRequest1.setRequestAgencyIdentifierId("TEST");
        getPersonnelSearchRequest1.setRequestDtm(Instant.now());
        getPersonnelSearchRequest1.setRequestPartId("TEST");
        getPersonnelSearchRequest1.setSearchTxt("TEST");
        getPersonnelSearchRequest1.setSearchTypeCd(OfficerSearchType.PIN);

        getPersonnelSearchRequest.setGetPersonnelSearchRequest(getPersonnelSearchRequest1);

        getPersonnelSearch.setGetPersonnelSearchRequest(getPersonnelSearchRequest);

        return getPersonnelSearch;
    }
}
