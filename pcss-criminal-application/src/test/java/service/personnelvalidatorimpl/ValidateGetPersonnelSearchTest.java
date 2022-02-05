package service.personnelvalidatorimpl;

import ca.bc.gov.open.pcsscriminalapplication.service.impl.PersonnelValidatorImpl;
import ca.bc.gov.open.wsdl.pcss.one.GetPersonnelSearchRequest;
import ca.bc.gov.open.wsdl.pcss.three.OfficerSearchType;

import java.time.Instant;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("ValidateGetPersonnelSearch Test")
public class ValidateGetPersonnelSearchTest {

    private static final String LONG_STRING = "VERYLONGSTRINGTOTESTTHELENGTHRESTRICTION";
    private static final String VALUE = "TEST";
    private static final Instant DATE = Instant.now();
    private static final Instant BAD_DATE = null;

    PersonnelValidatorImpl sut;

    @BeforeAll
    public void BeforeAll() {

        sut = new PersonnelValidatorImpl();
    }

    @Test
    @DisplayName("Success: null returns empty")
    public void nullTestReturnsEmpty() {

        List<String> result = sut.validateGetPersonnelSearch(null);

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("Empty request is invalid", result.get(0));
    }

    @Test
    @DisplayName("Success: all validations succeed")
    public void successTestReturns() {

        GetPersonnelSearchRequest getPersonnelSearchRequest = new GetPersonnelSearchRequest();
        getPersonnelSearchRequest.setRequestDtm(DATE);
        getPersonnelSearchRequest.setRequestAgencyIdentifierId(VALUE);
        getPersonnelSearchRequest.setRequestPartId(VALUE);
        getPersonnelSearchRequest.setSearchTypeCd(OfficerSearchType.PIN);
        getPersonnelSearchRequest.setAgencyId(VALUE);
        getPersonnelSearchRequest.setSearchTxt(VALUE);

        List<String> result = sut.validateGetPersonnelSearch(getPersonnelSearchRequest);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Fail: all validations fail")
    public void failTestReturns() {

        GetPersonnelSearchRequest getPersonnelSearchRequest = new GetPersonnelSearchRequest();
        getPersonnelSearchRequest.setRequestDtm(BAD_DATE);
        getPersonnelSearchRequest.setRequestAgencyIdentifierId(LONG_STRING);
        getPersonnelSearchRequest.setRequestPartId(LONG_STRING);
        getPersonnelSearchRequest.setAgencyId(LONG_STRING);

        List<String> result = sut.validateGetPersonnelSearch(getPersonnelSearchRequest);

        Assertions.assertEquals(6, result.size());
        Assertions.assertEquals(
                "RequestAgencyIdentifierId is not valid,RequestPartId is not valid,RequestDtm is not valid,AgencyId is not valid,SearchTypeCd is not valid,SearchTxt is not valid",
                StringUtils.join(result, ","));
    }
}
