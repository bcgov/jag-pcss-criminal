package service.personnelvalidatorimpl;

import ca.bc.gov.open.pcsscriminalapplication.service.impl.PersonnelValidatorImpl;
import ca.bc.gov.open.wsdl.pcss.one.GetPersonnelAvailDetailRequest;
import ca.bc.gov.open.wsdl.pcss.three.AvailablePersonType;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("ValidateGetPersonnelAvailDetail Test")
public class ValidateGetPersonnelAvailDetailTest {

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

        List<String> result = sut.validateGetPersonnelAvailDetail(null);

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("Empty request is invalid", result.get(0));
    }

    @Test
    @DisplayName("Success: all validations succeed")
    public void successTestReturns() {

        GetPersonnelAvailDetailRequest getPersonnelAvailDetailRequest =
                new GetPersonnelAvailDetailRequest();
        getPersonnelAvailDetailRequest.setRequestDtm(DATE);
        getPersonnelAvailDetailRequest.setRequestAgencyIdentifierId(VALUE);
        getPersonnelAvailDetailRequest.setRequestPartId(VALUE);
        getPersonnelAvailDetailRequest.setPersonTypeCd(AvailablePersonType.C);
        getPersonnelAvailDetailRequest.setAvailabilityDt(DATE);
        getPersonnelAvailDetailRequest.setPaasPartId(VALUE);

        List<String> result = sut.validateGetPersonnelAvailDetail(getPersonnelAvailDetailRequest);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Fail: all validations fail")
    public void failTestReturns() {

        GetPersonnelAvailDetailRequest getPersonnelAvailDetailRequest =
                new GetPersonnelAvailDetailRequest();
        getPersonnelAvailDetailRequest.setRequestDtm(BAD_DATE);
        getPersonnelAvailDetailRequest.setRequestAgencyIdentifierId(LONG_STRING);
        getPersonnelAvailDetailRequest.setRequestPartId(LONG_STRING);
        getPersonnelAvailDetailRequest.setAvailabilityDt(BAD_DATE);
        getPersonnelAvailDetailRequest.setPaasPartId(LONG_STRING);

        List<String> result = sut.validateGetPersonnelAvailDetail(getPersonnelAvailDetailRequest);

        Assertions.assertEquals(5, result.size());
        Assertions.assertEquals(
                "RequestAgencyIdentifierId is not valid,RequestPartId is not valid,RequestDtm is not valid,AvailabilityDt is not valid,PersonTypeCd is not valid",
                StringUtils.join(result, ","));
    }
}
