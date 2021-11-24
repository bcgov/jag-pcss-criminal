package service.personnelvalidatorimpl;

import ca.bc.gov.open.pcsscriminalapplication.service.impl.PersonnelValidatorImpl;
import ca.bc.gov.open.pcsscriminalcommon.utils.InstantUtils;
import ca.bc.gov.open.wsdl.pcss.one.GetPersonnelSearchRequest;
import ca.bc.gov.open.wsdl.pcss.three.OfficerSearchType;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.*;

import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("ValidateGetPersonnelSearch Test")
public class ValidateGetPersonnelSearchTest {

    private static final String LONG_STRING = "VERYLONGSTRINGTOTESTTHELENGTHRESTRICTION";
    private static final String VALUE = "TEST";
    private static final String DATE = "2013-03-25 13:04:22.1";
    private static final String BAD_DATE = "2001-DEC-26";

    PersonnelValidatorImpl sut;

    @BeforeAll
    public void BeforeAll() {

        sut = new PersonnelValidatorImpl();

    }

    @Test
    @DisplayName("Success: all validations succeed")
    public void successTestReturns() {

        GetPersonnelSearchRequest getPersonnelSearchRequest = new GetPersonnelSearchRequest();
        getPersonnelSearchRequest.setRequestDtm(InstantUtils.parse(DATE));
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
        getPersonnelSearchRequest.setRequestDtm(InstantUtils.parse(BAD_DATE));
        getPersonnelSearchRequest.setRequestAgencyIdentifierId(LONG_STRING);
        getPersonnelSearchRequest.setRequestPartId(LONG_STRING);
        getPersonnelSearchRequest.setSearchTypeCd(OfficerSearchType.PIN);
        getPersonnelSearchRequest.setAgencyId(LONG_STRING);
        getPersonnelSearchRequest.setSearchTxt(LONG_STRING);

        List<String> result = sut.validateGetPersonnelSearch(getPersonnelSearchRequest);

        Assertions.assertEquals(4, result.size());
        Assertions.assertEquals("RequestAgencyIdentifierId is not valid,RequestPartId is not valid,RequestDtm is not valid,AgencyId is not valid", StringUtils.join(result, ","));

    }

}