package service.personnelvalidatorimpl;

import ca.bc.gov.open.pcsscriminalapplication.service.impl.PersonnelValidatorImpl;
import ca.bc.gov.open.wsdl.pcss.one.GetPersonnelAvailabilityRequest;
import ca.bc.gov.open.wsdl.pcss.three.AvailablePersonType;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("ValidateGetPersonnelAvailability Test")
public class ValidateGetPersonnelAvailabilityTest {

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
    @DisplayName("Success: null returns empty")
    public void nullTestReturnsEmpty() {

        List<String> result = sut.validateGetPersonnelAvailability(null);

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("Empty request is invalid", result.get(0));
    }

    @Test
    @DisplayName("Success: all validations succeed")
    public void successTestReturns() {

        GetPersonnelAvailabilityRequest getPersonnelAvailabilityRequest =
                new GetPersonnelAvailabilityRequest();
        getPersonnelAvailabilityRequest.setRequestDtm(DATE);
        getPersonnelAvailabilityRequest.setRequestAgencyIdentifierId(VALUE);
        getPersonnelAvailabilityRequest.setRequestPartId(VALUE);
        getPersonnelAvailabilityRequest.setPersonTypeCd(AvailablePersonType.C);
        getPersonnelAvailabilityRequest.setPartIdList(VALUE);
        getPersonnelAvailabilityRequest.setFromDt(DATE);
        getPersonnelAvailabilityRequest.setToDt(DATE);

        List<String> result = sut.validateGetPersonnelAvailability(getPersonnelAvailabilityRequest);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Fail: all validations fail")
    public void failTestReturns() {

        GetPersonnelAvailabilityRequest getPersonnelAvailabilityRequest =
                new GetPersonnelAvailabilityRequest();
        getPersonnelAvailabilityRequest.setRequestDtm(BAD_DATE);
        getPersonnelAvailabilityRequest.setRequestAgencyIdentifierId(LONG_STRING);
        getPersonnelAvailabilityRequest.setRequestPartId(LONG_STRING);
        getPersonnelAvailabilityRequest.setPartIdList(LONG_STRING);
        getPersonnelAvailabilityRequest.setFromDt(BAD_DATE);
        getPersonnelAvailabilityRequest.setToDt(BAD_DATE);

        List<String> result = sut.validateGetPersonnelAvailability(getPersonnelAvailabilityRequest);

        Assertions.assertEquals(6, result.size());
        Assertions.assertEquals(
                "RequestAgencyIdentifierId is not valid,RequestPartId is not valid,RequestDtm is not valid,ToDt is not valid,FromDt is not valid,PersonTypeCd is not valid",
                StringUtils.join(result, ","));
    }
}
